package http.method;

import http.StatusCode;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Map;

public class ResponseParameters
{
	private final StatusCode code;
	private final Map<String, String> responseHeaders;
	private final CharBuffer body;

	public ResponseParameters(StatusCode code, Map<String, String> responseHeaders, CharBuffer body)
	{
		this.code = code;
		this.responseHeaders = responseHeaders;
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

	public int getHeaderCount()
	{
		return responseHeaders == null ? 0 : responseHeaders.size();
	}

	public void forEachHeader(HeaderConsumer action) throws IOException
	{
		if (responseHeaders != null)
			for (Map.Entry<String, String> entry : responseHeaders.entrySet())
				action.accept(entry.getKey(), entry.getValue());
	}

	public interface HeaderConsumer
	{
		void accept(String key, String value) throws IOException;
	}
}
