package http.method.handlers;

import http.Header;
import http.StatusCode;
import http.method.ResponseParameters;

import java.nio.CharBuffer;
import java.util.Map;

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

				// display parsed parameters
				"<h3>GET parameters</h3>" +
				formatParams(req) +
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

	/**
	 * Formats the GET parameters of the request into HTML
	 *
	 * @param req The request parameters
	 * @return An HTML unordered list, or a simple div if there are no parameters
	 */
	private String formatParams(RequestParameters req)
	{
		Map<String, String> parameters = req.getParameters();

		if (parameters.isEmpty())
			return "<div>None provided</div>";

		StringBuilder list = new StringBuilder("<ul>");
		parameters.forEach((k, v) ->
			list.append(String.format("<li><strong>%s</strong>: %s</li>", k, v)));
		list.append("</ul>");
		return list.toString();
	}
}
