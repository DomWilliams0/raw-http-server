package http.method.handlers;

import http.method.ResponseParameters;

/**
 * Interface for generating a response for a given request
 */
public interface MethodHandler
{
	/**
	 * Generates a response for the given request
	 *
	 * @param req The request to response to
	 * @return The response
	 */
	ResponseParameters handle(RequestParameters req);

	/**
	 * Helper method to generate an HTML unordered list from a list of headers
	 *
	 * @param headers The headers to list
	 * @return The generated HTML
	 */
	static String getRenderedHeaderList(Headers headers)
	{
		StringBuilder list = new StringBuilder("<ul>");
		headers.forEachHeader((k, v) ->
			list.append(String.format("<li><strong>%s</strong>: %s</li>", k, v)));
		list.append("</ul>");
		return list.toString();
	}
}
