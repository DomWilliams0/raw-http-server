package http.method;

import http.method.handlers.MethodGET;
import http.method.handlers.MethodHandler;
import http.method.handlers.MethodPOST;
import http.method.handlers.MethodUnimplemented;

/**
 * Represents the supported standard-defined HTTP methods
 */
public enum MethodType
{
	GET,
	HEAD,
	POST,
	PUT,
	DELETE,
	OPTIONS;

	/**
	 * Parses the given string and returns the corresponding method, if any
	 *
	 * @param method String input, which may match a HTTP method
	 * @return The corresponding method, or null if invalid
	 */
	public static MethodType parse(String method)
	{
		try
		{
			return valueOf(method);
		} catch (IllegalArgumentException e)
		{
			return null;
		}
	}

	/**
	 * @return A new {@link MethodHandler} to represent this method
	 */
	public MethodHandler getHandler()
	{
		switch (this)
		{
			case GET:
				return new MethodGET();
			case POST:
				return new MethodPOST();
			default:
				return new MethodUnimplemented();
		}
	}
}
