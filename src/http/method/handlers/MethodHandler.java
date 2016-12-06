package http.method.handlers;

import http.method.ResponseParameters;

public interface MethodHandler
{
	ResponseParameters handle(RequestParameters req);

	static String getRenderedHeaderList(Headers headers)
	{
		StringBuilder list = new StringBuilder("<ul>");
		headers.forEachHeader((k, v) ->
			list.append(String.format("<li><strong>%s</strong>: %s</li>", k, v)));
		list.append("</ul>");
		return list.toString();
	}
}
