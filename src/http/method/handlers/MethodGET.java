package http.method.handlers;

import http.Header;
import http.StatusCode;
import http.method.ResponseParameters;

import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

public class MethodGET implements MethodHandler
{
	@Override
	public ResponseParameters handle(String path)
	{
		Map<String, String> testHeaders = new HashMap<>();
		testHeaders.put(Header.CONTENT_TYPE.getKey(), "text/html");
		testHeaders.put("X-One-Two-Three", "123");

		String staticResponse =
			"<html>" +
				"<h1>Welcome to " + path + "</h1>" +

				// TODO display parsed parameters
				"<h4>GET parameters</h4>" +
				"<p>TODO</p>" +

				// TODO display parsed headers
				"<h4>Request Headers</h4>" +
				"<p>TODO</p>" +

			"</html>";

		CharBuffer staticBuffer = CharBuffer.wrap(staticResponse.toCharArray());
		return new ResponseParameters(StatusCode.OK, testHeaders, staticBuffer);
	}
}
