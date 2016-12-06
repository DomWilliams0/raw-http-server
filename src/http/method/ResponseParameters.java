package http.method;

import http.StatusCode;
import http.method.handlers.Headers;

import java.nio.CharBuffer;

public class ResponseParameters
{
	private final StatusCode code;
	private final Headers responseHeaders;
	private final CharBuffer body;

	public ResponseParameters(StatusCode code, CharBuffer body)
	{
		this.code = code;
		this.responseHeaders = new Headers();
		this.body = body;
	}

	public StatusCode getCode()
	{
		return code;
	}

	public CharBuffer getBody()
	{
		return body;
	}

	public Headers getHeaders()
	{
		return responseHeaders;
	}
}
