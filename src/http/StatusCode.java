package http;

public enum StatusCode
{
	OK(200, "OK"),
	BAD_REQUEST(400, "Bad Request"),
	LENGTH_REQUIRED(411, "Length Required");


	private final int code;
	private final String reason;

	StatusCode(int code, String reason)
	{
		this.code = code;
		this.reason = reason;
	}

	public int getCode()
	{
		return code;
	}

	public String getReason()
	{
		return reason;
	}
}
