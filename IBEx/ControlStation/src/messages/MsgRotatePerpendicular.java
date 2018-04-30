package messages;

import common.AbsMessage;
import common.MessageType;

public class MsgRotatePerpendicular extends AbsMessage
{
	public MsgRotatePerpendicular()
	{
		this(90.0);
	}
	
	public MsgRotatePerpendicular(double position)
	{
		super();
		setType(MessageType.MSG_ROTATE_TO_PERPENDICULAR);
		setSize(1);
		setInfo("Moving ratchet to position: " + position + "\n");
		
		setDataByIndex(0, position);
		setDataTagByIndex(0, "Position");
	}
}
