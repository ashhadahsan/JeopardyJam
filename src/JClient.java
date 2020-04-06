/*
 * JeopardyAssignment Submission 3 Question and Answer
 * JClient.java
 *
 * This program implements a simple multithreaded jeopardy client.  
 * Uses JClientListener
 *
 */
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

public class JClient
{
	public static void main(String[] args)
	{
		try
		{
			Scanner keyboard = new Scanner(System.in);
			System.out.println("Enter your name: ");
			String name = keyboard.nextLine();

			String hostname = "localhost";//127.0.0.1
			int port = 7654;

			System.out.println("Connecting to server on port " + port);
			Socket connectionSock = new Socket(hostname, port);

			DataOutputStream serverOutput = new DataOutputStream(connectionSock.getOutputStream());

			System.out.println("Connection made.");

			// Start a thread to listen and display data sent by the server
			JClientListener listener = new JClientListener(connectionSock);
			Thread theThread = new Thread(listener);
			theThread.start();
			serverOutput.writeBytes(name + "\n");
			// Read input from the keyboard and send it to everyone else.
			// The only way to quit is to hit control-c, but a quit command
			// could easily be added.
			
			while (true)
			{
				String data = keyboard.nextLine();
				serverOutput.writeBytes(data + "\n");
			}
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
} // JClient

