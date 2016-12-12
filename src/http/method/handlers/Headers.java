package http.method.handlers;

import http.Header;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Represents a collection of key-value headers
 */
public class Headers
{
	private final Map<String, String> map;

	public Headers()
	{
		map = new HashMap<>();
	}

	/**
	 * Adds a standard header with the corresponding key and value
	 *
	 * @param key   The key
	 * @param value The value
	 */
	public void addHeader(Header key, String value)
	{
		addHeader(key.getKey(), value);

	}

	/**
	 * Adds a custom header with the corresponding key and value
	 *
	 * @param key   The key
	 * @param value The value
	 */
	public void addHeader(String key, String value)
	{
		map.put(key, value);
	}

	/**
	 * @param key A standard header key to lookup
	 * @return The corresponding value, or null if not found
	 */
	public String getHeader(Header key)
	{
		return getHeader(key.getKey());
	}

	/**
	 * @param key A custom header key to lookup
	 * @return The corresponding value, or null if not found
	 */
	public String getHeader(String key)
	{
		return map.getOrDefault(key, null);
	}

	/**
	 * @return The number of headers added
	 */
	public int getHeaderCount()
	{
		return map.size();
	}

	/**
	 * Runs the given action on every key-value pair
	 *
	 * @param action The action to run
	 */
	public void forEachHeader(BiConsumer<String, String> action)
	{
		map.forEach(action);
	}

	/**
	 * Runs the given action on every key-value pair, which could throw an IOException
	 *
	 * @param action The action to run
	 */
	public void forEachHeaderIO(IOHeaderConsumer action) throws IOException
	{
		for (Map.Entry<String, String> entry : map.entrySet())
			action.accept(entry.getKey(), entry.getValue());
	}

	/**
	 * An action that could thrown an IOException
	 */
	public interface IOHeaderConsumer
	{
		void accept(String key, String value) throws IOException;
	}
}
