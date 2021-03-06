package http;

/**
 * Represents a standard-defined header key
 */
public enum Header
{
	CONTENT_LENGTH("Content-Length"),
	CONTENT_TYPE("Content-Type");

	private final String key;

	Header(String key)
	{
		this.key = key.toLowerCase();
	}

	/**
	 * @return The header key to use
	 */
	public String getKey()
	{
		return key;
	}
}
