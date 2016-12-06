package http.method.handlers;

import http.method.ResponseParameters;

public interface MethodHandler
{
	ResponseParameters handle(String path); // TODO body too
}
