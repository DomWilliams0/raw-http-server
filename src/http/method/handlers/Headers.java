package http.method.handlers;

import http.Header;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

	public int getHeaderCount()
	{
		return map.size();
	}


	public void forEachHeader(IOHeaderConsumer action) throws IOException
	{
		for (Map.Entry<String, String> entry : map.entrySet())
			action.accept(entry.getKey(), entry.getValue());
	}

	public interface IOHeaderConsumer
	{
		void accept(String key, String value) throws IOException;
	}
}
