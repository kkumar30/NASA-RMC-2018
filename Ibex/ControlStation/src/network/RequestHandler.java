package network;

import java.io.*;
import java.net.Socket;

import common.Message;
import common.MessageQueue;
import common.RecoveryStack;
import gui.RobotData;
import gui.GUI;

import static gui.GUI.list_1;
import static gui.GUI.messageList;

class RequestHandler extends Thread {
	private Socket socket;
	private MessageQueue queue;
	private RobotData robotData;
	private RecoveryStack recoveryStack;

	RequestHandler(Socket socket, MessageQueue queue, RobotData robotData, RecoveryStack recoveryStack) {
		this.socket = socket;
		this.queue = queue;
		this.robotData = robotData;
		this.recoveryStack = recoveryStack;
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
			String inboundCameraMessageStr = "";
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
//				if (MotorAndSensorStr.length> 2) {
//					inboundCameraMessageStr = MotorAndSensorStr[2];
//				}
//				inboundSensorMessageStr = inFromClient.readLine();

				//if (inboundSensorMessageStr != null && inboundSensorMessageStr.replace("\n\t ", "").equals("Finished")) {
				if (entireMessageStr==null){System.out.println("Is null");}
				if (entireMessageStr != null && entireMessageStr.replace("\n\t ", "").equals("Finished")) {
					if (!queue.isEmpty()) {
						Message popped;
						popped = queue.pop();
						if (popped != recoveryStack.peek())
						{
							recoveryStack.addAtBack(popped);
							System.out.print("********************Added to Recovery Stack!:***************");
							System.out.println(popped);
//							GUI.recoveryStack = recoveryStack;
							GUI.updateMessageQueueList(messageList);
//							GUI.updateAutonomyQueue(queue);
							GUI.updateRecoveryStackList(list_1);
						}
						System.out.println("Next action in Queue!");
						System.out.println(queue.peek());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				inboundMotorMessageStr = "Errors";
			}

//			System.out.println("Received Motors: " + inboundMotorMessageStr);
//			System.out.println("Received Sensors: "+ inboundSensorMessageStr);
//			System.out.println("Camera Servo Pos: "+ inboundCameraMessageStr);
			robotData.updateRobotData(inboundMotorMessageStr, inboundSensorMessageStr);

			if (!queue.isEmpty()) {
				queue.peek();
				out.println(queue.peek().getMessageString());
				System.out.println("Sent: " + queue.peek().getMessageString());
				out.flush();
			} else {
				out.println("<0|-1>");  //Sending a default stop message
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