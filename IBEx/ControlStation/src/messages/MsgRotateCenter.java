package messages;

import common.AbsMessage;
import common.MessageType;

public class MsgRotateCenter extends AbsMessage
{
	public MsgRotateCenter()
	{
		this(0.0, 0.0);
	}
	
	public MsgRotateCenter(double time, double speed)
	{
		super();
		setType(MessageType.MSG_ROTATE_TO_CENTER);
		setSize(2);
		setInfo("Moving bucket for " + time + "s at speed:" + speed + "\n");
		
		setDataByIndex(0, time);
		setDataByIndex(1, speed);
		setDataTagByIndex(0, "Time");
		setDataTagByIndex(1, "Speed");
	}
}
