package http.method.handlers;

import http.Header;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Headers
{
	private final Map<String, String> map;

	public Headers()
	{
		map = new HashMap<>();
	}

	public void addHeader(Header key, String value)
	{
		addHeader(key.getKey(), value);

	}

	public void addHeader(String key, String value)
	{
		map.put(key, value);
	}

	public String getHeader(Header key)
	{
		return getHeader(key.getKey());
	}

	public String getHeader(String key)
	{
		return map.getOrDefault(key, null);
	}

	public int getHeaderCount()
	{
		return map.size();
	}

	public void forEachHeader(BiConsumer<String, String> action)
	{
		map.forEach(action);
	}

	public void forEachHeaderIO(IOHeaderConsumer action) throws IOException
	{
		for (Map.Entry<String, String> entry : map.entrySet())
			action.accept(entry.getKey(), entry.getValue());
	}

	public interface IOHeaderConsumer
	{
		void accept(String key, String value) throws IOException;
	}
}
