package http.method.handlers;

import http.StatusCode;
import http.method.ResponseParameters;

public class MethodUnimplemented implements MethodHandler
{
	@Override
	public ResponseParameters handle(String path)
	{
		return new ResponseParameters(StatusCode.NOT_IMPLEMENTED, null, null);
	}
}
