package network;

import java.io.*;
import java.net.Socket;

import common.MessageQueue;
import gui.RobotData;
import gui.RobotData;

class RequestHandler extends Thread {
	private Socket socket;
	private MessageQueue queue;
	private RobotData robotData;

	RequestHandler(Socket socket, MessageQueue queue, RobotData robotData) {
		this.socket = socket;
		this.queue = queue;
		this.robotData = robotData;
	}

	@Override
	public void run() {
//		System.out.println("HERE! - RequestHandler.java");
		try {
//			System.out.println("HERE! - RequestHandler.java - In Try 1");
			// Get input and output streams
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream());

			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String inboundMessageStr;

			try {
//				System.out.println("HERE! - RequestHandler.java - In Try 2");
				inboundMessageStr = inFromClient.readLine();
				if (inboundMessageStr != null && inboundMessageStr.replace("\n\t ", "").equals("Finished")) {
					if (!queue.isEmpty()) {
						queue.pop();
//						System.out.println("Next action in Queue!");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				inboundMessageStr = "Errors";
			}

			System.out.println("Received: " + inboundMessageStr);
			robotData.updateRobotData(inboundMessageStr);

			if (!queue.isEmpty()) {
				queue.peek();
				out.println(queue.peek().getMessageString());
				System.out.println("Sent: " + queue.peek().getMessageString());
				out.flush();
			} else {
				out.println("<0|-1>");
				out.flush();
			}

			// Close our connection
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}