package http;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class HttpServer
{
	private ServerSocket socket;
	private boolean running;

	public HttpServer()
	{
		running = true;
	}

	private void handleClient(Socket client) throws IOException, BadlyFormedHttpException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

		// find protocol
		String[] firstLine = reader.readLine().split(" ");
		if (firstLine.length != 3)
			throw new BadlyFormedHttpException("Invalid first line");

		System.out.println("firstLine = " + Arrays.toString(firstLine));

		String method = firstLine[0];
		String path = firstLine[1];

		// TODO read headers
		// TODO dont just drop everything
		String line;
		while (true)
		{
			line = reader.readLine();

			if (line.isEmpty())
				break;

			System.out.printf("Ignoring header: %s\n", line);
		}

		// TODO match version
		writer.write("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n");
		writer.write("heres some lovely response\r\n");
		writer.flush();
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
				System.out.println("Client connected");

				handleClient(client);
			} catch (IOException | BadlyFormedHttpException e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (client != null)
				{
					try
					{
						System.out.println("Client disconnected");
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
