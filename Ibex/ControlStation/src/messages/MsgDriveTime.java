package messages;

import common.AbsMessage;
import common.MessageType;

public class MsgDriveTime extends AbsMessage 
{
	public MsgDriveTime()
	{
		this(0.0, 0.0);
	}
	
	public MsgDriveTime(double time, double speed)
	{
		super();
		setType(MessageType.MSG_DRIVE_TIME);
		setSize(2);
		setInfo("Driving for " + time + "s at speed:" + speed + "\n");
		
		setDataByIndex(0, time);
		setDataByIndex(1, speed);
		setDataTagByIndex(0, "Time");
		setDataTagByIndex(1, "Speed");
	}
}
