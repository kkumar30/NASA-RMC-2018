package network;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

import common.Gamepad;
import common.MessageQueue;
import common.RecoveryStack;
import gui.RobotData;

public class NetworkServer extends Thread
{
	private ServerSocket serverSocket;
	private MessageQueue queue;
	private RobotData robotData;
	private int port;
	private boolean running = false;
	private Gamepad g = new Gamepad();

	private RecoveryStack recoveryStack;
	
	public NetworkServer(int port, MessageQueue queue, RobotData robotData, RecoveryStack recoveryStack)
	{
		this.port = port;
		this.queue = queue;
		this.robotData = robotData;
		this.recoveryStack = recoveryStack;
	}
	
	public void startServer()
	{
		try
		{
			serverSocket = null;
//			if(serverSocket == null)
//			{
//				serverSocket = new ServerSocket(port);
//			}
			serverSocket = new ServerSocket(port);
			running = true;
			this.start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void stopServer() throws IOException
	{
		running = false;
		serverSocket.close();
		this.interrupt();
		//serverSocket.close();
		
	}
	
	@Override
	public void run()
	{
//		System.out.println("HERE! - NetworkServer.java");
		while(running)
		{
//			System.out.println("HERE! - NetworkServer.java - In While");
			try
			{
//				System.out.println("HERE! - NetworkServer.java - In Try");
				Socket socket = serverSocket.accept();
//				System.out.println(socket.isConnected());
				RequestHandler requestHandler = new RequestHandler(socket, queue, robotData, recoveryStack);
				requestHandler.start();
				
			}
			catch(IOException e)
			{
//				System.out.println("HERE! - NetworkServer.java - Caught Exception");
				e.printStackTrace();
			}
//			System.out.println("HERE! - NetworkServer.java - In While Not Trying");
		}
//		System.out.println("HERE! - NetworkServer.java - Out of While");
	}

}
