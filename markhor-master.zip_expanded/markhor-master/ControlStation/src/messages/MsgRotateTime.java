package messages;

import common.AbsMessage;
import common.MessageType;

public class MsgRotateTime extends AbsMessage 
{
	public MsgRotateTime()
	{
		this(0.0, 0.0);
	}
	
	public MsgRotateTime(double time, double speed)
	{
		super();
		setType(MessageType.MSG_ROTATE_TIME);
		setSize(2);
		setInfo("Rotating for " + time + "s at speed:" + speed + "\n");
		
		setDataByIndex(0, time);
		setDataByIndex(1, speed);
		setDataTagByIndex(0, "Time");
		setDataTagByIndex(1, "Speed");
	}
}
