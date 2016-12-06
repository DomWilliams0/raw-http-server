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
		String staticResponse =
			"<html>" +
				"<h1>Welcome to " + req.getPath() + "</h1>" +

				// TODO display parsed parameters
				"<h3>GET parameters</h3>" +
				"<p>TODO</p>" +
				"<hr>" +

				// display parsed headers
				"<h3>Request Headers</h3>" + getRenderedHeaderList(req.getHeaders()) +
				"<hr>" +

				(req.getBody() == null ? "" :
				"<h3>Request Body</h3>" +
				"<h4>" + req.getBody().length() + " byte(s)</h4>" + req.getBody().toString() +
				"<hr>"
				) +

			"</html>";

		CharBuffer staticBuffer = CharBuffer.wrap(staticResponse.toCharArray());

		ResponseParameters response = new ResponseParameters(StatusCode.OK, staticBuffer);
		response.getHeaders().addHeader(Header.CONTENT_TYPE, "text/html");
		response.getHeaders().addHeader("X-One-Two-Three", "123");

		return response;

	}

	private String getRenderedHeaderList(Headers headers)
	{
		StringBuilder list = new StringBuilder("<ul>");
		headers.forEachHeader((k, v) ->
			list.append(String.format("<li><strong>%s</strong>: %s</li>", k, v)));
		list.append("</ul>");
		return list.toString();
	}
}
