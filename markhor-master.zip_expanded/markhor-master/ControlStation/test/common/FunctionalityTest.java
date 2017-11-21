package common;

import java.io.IOException;

import gui.RobotData;
import messages.*;
import network.NetworkServer;

public class FunctionalityTest 
{
	public static void main(String [] args) throws IOException
	{
		final int port = 11000;
		
		MessageQueue mq = new MessageQueue();
		NetworkServer server = new NetworkServer(port, mq, new RobotData());

		mq.addAtBack(new MsgDriveTime(5.0, -0.50));
		//mq.add(new MsgDriveDistance(10.0, 5.0));
		//mq.add(new MsgMotorValues());
		//mq.add(new MsgDriveTime(5.0, -0.25));
		//mq.add(new MsgBucketTime(5.0, 0.5));
		//mq.add(new MsgDriveTime(10.0, 0.0));
		//mq.add(new MsgBucketPosition(250.0, 0.25));
		//mq.add(new MsgDriveTime(10.0, 0.0));
		//mq.add(new MsgRotateTime(10.0, 0.50));
		//mq.add(new MsgRotateTime(10.0, 0.00));
		//mq.add(new MsgRotateTime(10.0, -0.50));
		//mq.add(new MsgDriveTime(2.0, -0.50));
		//mq.add(new MsgDriveTime(2.0, 0.00));
		//mq.add(new MsgDriveTime(2.0, -0.50));
		//mq.add(new MsgRotateTime(1.0, -0.25));
		//mq.add(new MsgDriveTime(5.0, 0.00));
		//mq.add(new MsgDriveTime(10.0, 1.00));
		//mq.add(new MsgDriveTime(5.0, -0.50));
		//mq.add(new MsgScoopTime(4.0, 0.20));
		//mq.add(new MsgDepthTime(6.0, 0.05));
		//mq.add(new MsgBucketTime(10, 1.0));
		//mq.add(new MsgStop());
		//mq.add(new MsgMotorValues());
		
		
		server.startServer();
		try
        {
            Thread.sleep( 600000 );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        server.stopServer();
	}
}
