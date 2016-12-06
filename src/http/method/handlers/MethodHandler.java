package http.method.handlers;

import http.method.ResponseParameters;

public interface MethodHandler
{
	ResponseParameters handle(RequestParameters req);
}
