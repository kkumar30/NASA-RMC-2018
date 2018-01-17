package messages;

import common.AbsMessage;
import common.MessageType;

public class MsgRatchetPosition extends AbsMessage
{
	public MsgRatchetPosition()
	{
		this(90.0);
	}
	
	public MsgRatchetPosition(double position)
	{
		super();
		setType(MessageType.MSG_RATCHET_POSITION);
		setSize(1);
		setInfo("Moving ratchet to position: " + position + "\n");
		
		setDataByIndex(0, position);
		setDataTagByIndex(0, "Position");
	}
}
