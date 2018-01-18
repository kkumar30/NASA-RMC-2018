package messages;

import common.AbsMessage;
import common.MessageType;

public class MsgBucketTime extends AbsMessage
{
	public MsgBucketTime()
	{
		this(0.0, 0.0);
	}
	
	public MsgBucketTime(double time, double speed)
	{
		super();
		setType(MessageType.MSG_BUCKET_TIME);
		setSize(2);
		setInfo("Moving bucket for " + time + "s at speed:" + speed + "\n");
		
		setDataByIndex(0, time);
		setDataByIndex(1, speed);
		setDataTagByIndex(0, "Time");
		setDataTagByIndex(1, "Speed");
	}
}
