package messages;

import common.AbsMessage;
import common.MessageType;

public class MsgDriveDistance extends AbsMessage 
{
	public MsgDriveDistance()
	{
		this(0.0, 0.0);
	}
	
	
	public MsgDriveDistance(double distance, double speed)
	{
		super();
		setType(MessageType.MSG_DRIVE_DISTANCE);
		setSize(2);
		setInfo("Driving for " + distance + "m at speed:" + speed + "\n");
		
		setDataByIndex(0, distance);
		setDataByIndex(1, speed);
		setDataTagByIndex(0, "Distance");
		setDataTagByIndex(1, "Speed");
		
	}
}
