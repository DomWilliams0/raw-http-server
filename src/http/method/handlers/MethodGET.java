package http.method.handlers;

import http.Header;
import http.StatusCode;
import http.method.ResponseParameters;

import java.nio.CharBuffer;

public class MethodGET implements MethodHandler
{
	@Override
	public ResponseParameters handle(RequestParameters req)
	{
		if (req.getPath().equals("/favicon.ico"))
			return new ResponseParameters(StatusCode.NOT_FOUND, null);


		String staticResponse =
			"<html>" +
				"<h1>Welcome to " + req.getPath() + "</h1>" +

				// TODO display parsed parameters
				"<h3>GET parameters</h3>" +
				"<p>TODO</p>" +
				"<hr>" +

				// display parsed headers
				"<h3>Request Headers</h3>" + MethodHandler.getRenderedHeaderList(req.getHeaders()) +
				"<hr>" +

			"</html>";

		CharBuffer staticBuffer = CharBuffer.wrap(staticResponse.toCharArray());

		ResponseParameters response = new ResponseParameters(StatusCode.OK, staticBuffer);
		response.getHeaders().addHeader(Header.CONTENT_TYPE, "text/html");
		response.getHeaders().addHeader("X-One-Two-Three", "123");

		return response;

	}

}
