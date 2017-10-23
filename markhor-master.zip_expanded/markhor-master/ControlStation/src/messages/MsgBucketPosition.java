package messages;

import common.AbsMessage;
import common.MessageType;

public class MsgBucketPosition extends AbsMessage 
{
	
	public MsgBucketPosition()
	{
		this(0.0, 0.0);
	}
	
	public MsgBucketPosition(double position, double speed)
	{
		super();
		setType(MessageType.MSG_BUCKET_POSITION);
		setSize(2);
		setInfo("Moving winch to position" + position + "m at speed:" + speed + "\n");
		
		setDataByIndex(0, position);
		setDataByIndex(1, speed);
		setDataTagByIndex(0, "Position");
		setDataTagByIndex(1, "Speed");
	}
	


}
