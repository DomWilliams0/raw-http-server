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

		ResponseParameters response = new ResponseParameters(StatusCode.OK, staticBuffer);
		response.getHeaders().addHeader(Header.CONTENT_TYPE, "text/html");
		response.getHeaders().addHeader("X-One-Two-Three", "123");

		return response;

	}
}
