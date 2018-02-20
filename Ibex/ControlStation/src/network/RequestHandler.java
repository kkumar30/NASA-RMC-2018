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
			String inboundMotorMessageStr = "";
			String inboundSensorMessageStr = "";
			String entireMessageStr = "";
			String[] MotorAndSensorStr;


			try {
//				System.out.println("HERE! - RequestHandler.java - In Try 2");
//				inboundSensorMessageStr = "None";
				inboundMotorMessageStr = "None";

				entireMessageStr= inFromClient.readLine();
				System.out.println("Entire MessageString: " + entireMessageStr);
				MotorAndSensorStr = entireMessageStr.split("\\$");
				inboundMotorMessageStr = MotorAndSensorStr[0];

				if (MotorAndSensorStr.length> 1) {
					inboundSensorMessageStr = MotorAndSensorStr[1];
				}
//				inboundSensorMessageStr = inFromClient.readLine();

				//if (inboundSensorMessageStr != null && inboundSensorMessageStr.replace("\n\t ", "").equals("Finished")) {
				if (entireMessageStr != null && entireMessageStr.replace("\n\t ", "").equals("Finished")) {
					if (!queue.isEmpty()) {
						queue.pop();
						System.out.println("Next action in Queue!");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				inboundMotorMessageStr = "Errors";
			}

			System.out.println("Received Motors: " + inboundMotorMessageStr);
			System.out.println("Received Sensors: "+ inboundSensorMessageStr);
			robotData.updateRobotData(inboundMotorMessageStr, inboundSensorMessageStr);

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