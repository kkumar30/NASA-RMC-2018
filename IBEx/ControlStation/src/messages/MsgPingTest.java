package messages;

import common.AbsMessage;
import common.MessageType;

public class MsgPingTest extends AbsMessage
{
    public MsgPingTest()
    {
        this(0.0);
    }

    public MsgPingTest(double val)
    {
        super();
        setType(MessageType.MSG_PING);
        setSize(1);
        setInfo("Pinging LED to light up " + val + "\n");

        setDataByIndex(0, val);
//        setDataByIndex(1, speed);
        setDataTagByIndex(0, "Value");
//        setDataTagByIndex(1, "Speed");
    }
}
