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

	/**
	 * Spawns a new thread to handle the given client
	 *
	 * @param client The client's socket
	 */
	private void handleClient(Socket client)
	{
		Thread t = new Thread(() ->
		{
			try
			{
				Request r = new Request(client);
				r.handle();
			} catch (IOException e)
			{
				e.printStackTrace();
			} finally
			{
				if (!client.isClosed())
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
		});

		t.start();
	}

	/**
	 * Starts the server at the given address. Blocks until the boolean running is false
	 *
	 * @param iface The interface to listen on
	 * @param port  The port to listen on
	 * @return If the server started and shutdown cleanly
	 */
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
			try
			{
				handleClient(socket.accept());
			} catch (IOException e)
			{
				e.printStackTrace();
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
		// TODO as arguments
		String iface = "127.0.0.1";
		int port = 65040;

		HttpServer server = new HttpServer();
		boolean success = server.serve(iface, port);

		System.exit(success ? 0 : 1);
	}
}
