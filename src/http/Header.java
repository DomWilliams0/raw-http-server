package http;

public enum Header
{
	CONTENT_LENGTH("Content-Length"),
	CONTENT_TYPE("Content-Type");

	private final String key;

	Header(String key)
	{
		this.key = key.toLowerCase();
	}

	public String getKey()
	{
		return key;
	}
}
