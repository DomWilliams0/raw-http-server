package http;

public enum Header
{
	CONTENT_LENGTH("Content-Length");

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
