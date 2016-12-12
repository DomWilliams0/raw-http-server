package http.method.handlers;

import http.Header;
import http.method.MethodType;

import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a request from a client to the server
 */
public class RequestParameters
{
	private final MethodType method;
	private final String path;
	private final Headers requestHeaders;
	private final Map<String, String> parameters;
	private final CharBuffer body;

	/**
	 * @param method         The HTTP method
	 * @param path           The requested path
	 * @param requestHeaders The request headers
	 * @param body           The request body - can be null
	 */
	private RequestParameters(MethodType method, String path, Headers requestHeaders, CharBuffer body)
	{
		this.method = method;
		this.requestHeaders = requestHeaders;
		this.body = body;
		this.parameters = new HashMap<>();

		// parse parameters
		int startIndex = path.indexOf('?');
		if (startIndex >= 0)
		{
			String[] params = path.substring(startIndex + 1).split("&");
			path = path.substring(0, startIndex);
			for (String param : params)
			{
				int splitIndex = param.indexOf('=');
				String key = param.substring(0, splitIndex);
				String value = param.substring(splitIndex + 1);
				parameters.put(key, value);
			}
		}

		this.path = path;
	}

	/**
	 * @return The method
	 */
	public MethodType getMethod()
	{
		return method;
	}

	/**
	 * @return The requested path
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * @return The request headers
	 */
	public Headers getHeaders()
	{
		return requestHeaders;
	}

	/**
	 * @return The request body, if any
	 */
	public CharBuffer getBody()
	{
		return body;
	}

	/**
	 * @return GET parameters, if any
	 */
	public Map<String, String> getParameters()
	{
		return parameters;
	}

	/**
	 * A helper class to build a Request, following the Builder pattern
	 */
	public static class RequestParametersBuilder
	{
		private String method;
		private String path;
		private Headers headers;
		private CharBuffer body;

		public RequestParametersBuilder()
		{
			headers = new Headers();
		}

		public RequestParametersBuilder setMethod(String method)
		{
			this.method = method;
			return this;
		}

		public RequestParametersBuilder setPath(String path)
		{
			this.path = path;
			return this;
		}

		public RequestParametersBuilder setBody(CharBuffer body)
		{
			this.body = body;
			return this;
		}

		public RequestParametersBuilder addHeader(Header key, String value)
		{
			return addHeader(key.getKey(), value);
		}

		public RequestParametersBuilder addHeader(String key, String value)
		{
			headers.addHeader(key, value);
			return this;
		}

		public RequestParameters build()
		{
			return new RequestParameters(MethodType.valueOf(method), path, headers, body);
		}

		public String getMethod()
		{
			return method;
		}

		public String getPath()
		{
			return path;
		}

		public Headers getHeaders()
		{
			return headers;
		}
	}
}
