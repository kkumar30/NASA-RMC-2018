package messages;

import common.AbsMessage;
import common.MessageType;

public class MsgDepthTime extends AbsMessage
{
	public MsgDepthTime()
	{
		this(0.0, 0.0);
	}
	
	public MsgDepthTime(double time, double speed)
	{
		super();
		setType(MessageType.MSG_DEPTH_TIME);
		setSize(2);
		setInfo("Moving depth motor for " + time + "s at speed:" + speed + "\n");
		
		setDataByIndex(0, time);
		setDataByIndex(1, speed);	
		setDataTagByIndex(0, "Time");
		setDataTagByIndex(1, "Speed");
	}
}
