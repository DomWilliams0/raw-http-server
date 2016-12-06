package http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer
{
	private ServerSocket socket;
	private boolean running;

	public HttpServer()
	{
		running = true;
	}


	private boolean serve(String iface, int port)
	{
		// open socket
		try
		{
			socket = new ServerSocket(port, 0, InetAddress.getByName(iface));
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}

		System.out.printf("Listening on %s:%d...\n", iface, port);
		while (running)
		{
			Socket client = null;
			try
			{
				client = socket.accept();
				Request req = new Request(client);
				req.handle();
			} catch (IOException e)
			{
				e.printStackTrace();
			} finally
			{
				if (client != null)
				{
					try
					{
						client.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}


		}

		// close socket
		try
		{
			if (socket != null)
				socket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static void main(String[] args)
	{
		String iface = "127.0.0.1";
		int port = 65040;

		HttpServer server = new HttpServer();
		boolean success = server.serve(iface, port);

		System.exit(success ? 0 : 1);
	}
}
