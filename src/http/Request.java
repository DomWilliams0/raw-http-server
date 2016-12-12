package http;

import http.method.MethodType;
import http.method.ResponseParameters;
import http.method.handlers.Headers;
import http.method.handlers.MethodHandler;
import http.method.handlers.RequestParameters;

import java.io.*;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a single request to the server
 */
public class Request
{
	private static final String HTTP_VERSION;
	private static final String CR_LF;

	private static final Pattern REQUEST_LINE;
	private static final Pattern HEADER;

	static
	{
		REQUEST_LINE = Pattern.compile("^([A-Z]+) ([a-zA-Z0-9.-_~!$&'()*+,;=:@%]+) HTTP/(\\d\\.\\d)$");
		HEADER = Pattern.compile("^([a-zA-Z-_]+): (.+)");
		HTTP_VERSION = "1.1";
		CR_LF = "\r\n";
	}

	/**
	 * A helper enum to indicate the result of reading the request's body
	 */
	private enum BodyResult
	{
		OK,
		BAD_CONTENT_LENGTH,
		NOT_READ_YET
	}


	private final BufferedReader reader;
	private final BufferedWriter writer;

	private RequestParameters.RequestParametersBuilder reqBuilder;

	private final String clientAddress;

	/**
	 * @param socket The client's socket
	 */
	public Request(Socket socket) throws IOException
	{
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		reqBuilder = new RequestParameters.RequestParametersBuilder();
		clientAddress = socket.getRemoteSocketAddress().toString();
	}

	/**
	 * Reads the full request and send the appropriate response
	 */
	public void handle() throws IOException
	{
		// read full request
		BodyResult bodyResult = BodyResult.NOT_READ_YET;
		if (!parseRequestLine() ||
			!parseHeaders() ||
			(bodyResult = parseBody()) != BodyResult.OK)
		{
			StatusCode code = StatusCode.BAD_REQUEST;
			if (bodyResult == BodyResult.BAD_CONTENT_LENGTH)
				code = StatusCode.LENGTH_REQUIRED;

			sendStatusLine(code);
			return;
		}

		// parse rawMethod
		MethodType methodType = MethodType.parse(reqBuilder.getMethod());
		if (methodType == null)
		{
			sendStatusLine(StatusCode.BAD_REQUEST);
			return;
		}

		// TODO replace only sending status line above ^ with ResponseParameters

		RequestParameters req = reqBuilder.build();

		System.out.printf("%s - %s %s\n", clientAddress, req.getMethod(), req.getPath());

		// handle rawMethod
		MethodHandler handler = methodType.getHandler();
		ResponseParameters response = handler.handle(req);

		// send response
		sendStatusLine(response.getCode());

		Headers headers = response.getHeaders();
		headers.forEachHeaderIO(this::sendHeader);
		if (headers.getHeaderCount() > 0)
			sendNewLine();

		sendBody(response.getBody());
	}

	/**
	 * Reads the request line and parses the method, requested path and HTTP version
	 *
	 * @return If all fields were well formed and successfully read and parsed
	 */
	private boolean parseRequestLine() throws IOException
	{
		String requestLine = readLine();

		Matcher matcher = REQUEST_LINE.matcher(requestLine);
		if (!matcher.matches())
			return false;

		String rawMethod = matcher.group(1);
		String rawPath = matcher.group(2);
		String version = matcher.group(3);

		if (rawPath.isEmpty())
			return false;

		if (!version.equals(HTTP_VERSION))
			return false;

		reqBuilder
			.setMethod(rawMethod)
			.setPath(rawPath);

		return true;
	}

	/**
	 * Reads all request headers and stores them in the headers map
	 *
	 * @return If all headers were well formed and successfully read
	 */
	private boolean parseHeaders() throws IOException
	{
		boolean success = true;

		String line;
		Matcher matcher;
		while (!(line = readLine()).isEmpty())
		{
			matcher = HEADER.matcher(line);
			if (!matcher.matches())
			{
				success = false;
				break;
			}

			String key = matcher.group(1).toLowerCase();
			String value = matcher.group(2);
			reqBuilder.addHeader(key, value);
		}

		return success;
	}

	/**
	 * Reads the request body, if any, obeying the Content-Length header.
	 * If not present, it will read until the end of the stream.
	 */
	private BodyResult parseBody() throws IOException
	{
		// TODO binary body! it won't always be text
		BodyResult result = BodyResult.NOT_READ_YET;

		if (!reader.ready())
		{
			// no body to read
			result = BodyResult.OK;
		} else
		{
			Integer contentLength = 0;

			// parse content length header, if given
			String contentLengthStr = reqBuilder.getHeaders().getHeader(Header.CONTENT_LENGTH);
			if (contentLengthStr != null)
			{
				try
				{
					contentLength = Integer.parseInt(contentLengthStr);
				} catch (NumberFormatException e)
				{
					contentLength = 0;
					result = BodyResult.BAD_CONTENT_LENGTH;
				}
			}

			// safe to continue
			if (result == BodyResult.NOT_READ_YET)
			{
				// specified content length
				if (contentLength > 0)
				{
					char[] buffer = new char[contentLength];
					int bytesRead = reader.read(buffer);

					if (reader.ready() || bytesRead != contentLength)
					{
						result = BodyResult.BAD_CONTENT_LENGTH;
					} else
					{
						result = BodyResult.OK;
						reqBuilder.setBody(CharBuffer.wrap(buffer));
					}
				}

				// read until the end
				else
				{
					StringBuilder buffer = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null)
					{
						buffer.append(line).append("\n");
					}
					result = BodyResult.OK;

					reqBuilder.setBody(CharBuffer.wrap(buffer));
				}
			}
		}

		return result;
	}

	/**
	 * Writes the status line
	 *
	 * @param statusCode The response code to send
	 */
	private void sendStatusLine(StatusCode statusCode) throws IOException
	{
		writer.write(String.format("HTTP/%s %d %s%s",
			HTTP_VERSION, statusCode.getCode(), statusCode.getReason(), CR_LF));

		writer.flush();
	}

	/**
	 * Writes the given header
	 *
	 * @param key   The header's key
	 * @param value The header's value
	 */
	private void sendHeader(String key, String value) throws IOException
	{
		writer.write(String.format("%s: %s%s", key, value, CR_LF));
		writer.flush();
	}

	/**
	 * Writes \r\n
	 */
	private void sendNewLine() throws IOException
	{
		writer.write(CR_LF);
		writer.flush();
	}

	/**
	 * Writes the given buffer to the client. Can be null
	 *
	 * @param buffer The buffer to write
	 */
	private void sendBody(CharBuffer buffer) throws IOException
	{
		if (buffer != null)
		{
			writer.write(buffer.array());
			writer.flush();
		}
	}

	/**
	 * Wrapper around reader.readLine() that throws {@link EOFException} instead of returning null
	 */
	private String readLine() throws IOException
	{
		String s = reader.readLine();
		if (s == null)
			throw new EOFException();

		return s;
	}
}
