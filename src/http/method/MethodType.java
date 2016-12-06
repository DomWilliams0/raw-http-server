package http.method;

import http.method.handlers.MethodGET;
import http.method.handlers.MethodHandler;
import http.method.handlers.MethodPOST;
import http.method.handlers.MethodUnimplemented;

public enum MethodType
{
	GET,
	HEAD,
	POST,
	PUT,
	DELETE;

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

	public MethodHandler getHandler()
	{
		switch (this)
		{
			case GET:
				return new MethodGET();
			case POST:
				return new MethodPOST();
			case HEAD:
			case PUT:
			case DELETE:
			default:
				return new MethodUnimplemented();
		}
	}
}
