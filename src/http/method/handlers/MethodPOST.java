package http.method.handlers;

import http.Header;
import http.StatusCode;
import http.method.ResponseParameters;

import java.nio.CharBuffer;

public class MethodPOST implements MethodHandler
{
	@Override
	public ResponseParameters handle(RequestParameters req)
	{
		String staticResponse =
			"<html>" +
				"<h1>You just POSTed to " + req.getPath() + "</h1>" +

				// display parsed headers
				"<h3>Request Headers</h3>" + MethodHandler.getRenderedHeaderList(req.getHeaders()) +
				"<hr>" +

				"<h3>Request Body</h3>" +
				"<h4>" + req.getBody().length() + " byte(s)</h4>" + String.valueOf(req.getBody()) +
				"<hr>" +

				"</html>";

		CharBuffer staticBuffer = CharBuffer.wrap(staticResponse.toCharArray());


		ResponseParameters resp = new ResponseParameters(StatusCode.OK, staticBuffer);
		resp.getHeaders().addHeader(Header.CONTENT_TYPE, "text/html");

		return resp;
	}
}
