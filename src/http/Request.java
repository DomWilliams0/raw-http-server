package http;

import java.io.*;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request
{
	private static final String HTTP_VERSION;
	private static final String CR_LF;
	private static final Pattern REQUEST_LINE;

	static
	{
		REQUEST_LINE = Pattern.compile("^([A-Z]+) ([a-zA-Z0-9.-_~!$&'()*+,;=:@%]+) HTTP/(\\d\\.\\d)$");
		HTTP_VERSION = "1.1";
		CR_LF = "\r\n";
	}


	private BufferedReader reader;
	private BufferedWriter writer;

	private String path;
	private String method; // TODO enum

	public Request(Socket socket) throws IOException
	{
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		path = "";
		method = "";
	}

	public void handle() throws IOException
	{
		// request line
		if (!parseRequestLine())
		{
			sendResponseCode(StatusCode.BAD_REQUEST);
			return;
		}

		sendResponseCode(StatusCode.OK);
	}

	private boolean parseRequestLine() throws IOException
	{
		String requestLine = readLine();

		Matcher matcher = REQUEST_LINE.matcher(requestLine);
		if (!matcher.matches())
			return false;

		String method = matcher.group(1);
		path = matcher.group(2);
		String version = matcher.group(3);

		// TODO look at yourself, you can do better
		if (!method.equals("GET"))
			return false;

		if (path.isEmpty())
			return false;

		if (!version.equals(HTTP_VERSION))
			return false;

		return true;
	}

	/**
	 * Sends just the status code and reason
	 */
	private void sendResponseCode(StatusCode statusCode) throws IOException
	{
		writer.write(String.format("HTTP/%s %d %s%s",
			HTTP_VERSION, statusCode.getCode(), statusCode.getReason(), CR_LF));

		writer.flush();
	}

	private String readLine() throws IOException
	{
		String s = reader.readLine();
		if (s == null)
			throw new EOFException();

		return s;
	}
}
