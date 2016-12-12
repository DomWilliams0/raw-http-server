package http.method;

import http.StatusCode;
import http.method.handlers.Headers;

import java.nio.CharBuffer;

/**
 * Represents a response from the server to a client
 */
public class ResponseParameters
{
	private final StatusCode code;
	private final Headers responseHeaders;
	private final CharBuffer body;

	/**
	 * @param code The response code
	 * @param body The response body - can be null
	 */
	public ResponseParameters(StatusCode code, CharBuffer body)
	{
		this.code = code;
		this.responseHeaders = new Headers();
		this.body = body;
	}

	/**
	 * @return The response code
	 */
	public StatusCode getCode()
	{
		return code;
	}

	/**
	 * @return The response body
	 */
	public CharBuffer getBody()
	{
		return body;
	}

	/**
	 * @return The response headers
	 */
	public Headers getHeaders()
	{
		return responseHeaders;
	}
}
