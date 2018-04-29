package messages;

import common.AbsMessage;
import common.MessageType;

public class MsgGetTarget extends AbsMessage
{
	
	public MsgGetTarget()
	{
		this(0.0, 0.0);
	}
	
	public MsgGetTarget(double position, double speed)
	{
		super();
		setType(MessageType.MSG_GET_TARGET);
		setSize(2);
		setInfo("Moving winch to position" + position + "m at speed:" + speed + "\n");
		
		setDataByIndex(0, position);
		setDataByIndex(1, speed);
		setDataTagByIndex(0, "Position");
		setDataTagByIndex(1, "Speed");
	}
	


}
