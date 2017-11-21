package messages;

import common.AbsMessage;
import common.MessageType;

public class MsgStop extends AbsMessage
{
	public MsgStop()
	{
		super();
		setType(MessageType.MSG_STOP);
		setSize(0);
		setInfo("Ceasing all motor functions.\n");
	}
}
