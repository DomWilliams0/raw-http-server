package http;

public class BadlyFormedHttpException extends Exception
{

	public BadlyFormedHttpException(String message)
	{
		super(message);
	}
}
