package com.chris.amx.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * 
 */
@Component(name = "com.amx")
public class SimpleSocketServer extends Thread {
	private ServerSocket serverSocket;
	private int port = 8005;
	private boolean running = false;

	public SimpleSocketServer(int port) {
		this.port = port;
	}

	@Activate
	public void startServer() {
		try {
			serverSocket = new ServerSocket(port);
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@Deactivate
	public void stopServer() {
		running = false;
		this.interrupt();
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			try {
				System.out.println("Listening for a connection");

				// Call accept() to receive the next connection
				Socket socket = serverSocket.accept();

				// Pass the socket to the RequestHandler thread for processing
				RequestHandler requestHandler = new RequestHandler(socket);
				requestHandler.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

class RequestHandler extends Thread {
	private Socket socket;

	RequestHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			System.out.println("Received a connection");

			// Get input and output streams
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream());

			// Write out our header to the client
			out.println("Echo Server 1.0");
			out.flush();

			// Echo lines back to the client until the client closes the
			// connection or we receive an empty line
			String line = in.readLine();
			while (line != null && line.length() > 0) {
				out.println("Echo: " + line);
				out.flush();
				line = in.readLine();
			}

			// Close our connection
			in.close();
			out.close();
			socket.close();

			System.out.println("Connection closed");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}