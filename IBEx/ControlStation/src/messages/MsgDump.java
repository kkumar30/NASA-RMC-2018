package messages;

/**
 * Created by Kushagra Kumar on 4/14/2018.
 */

import common.AbsMessage;
import common.MessageType;

public class MsgDump extends AbsMessage
{
    public MsgDump()
    {
        this(0.0);
    }

    public MsgDump(double direction)
    {
        super();
        setType(MessageType.MSG_DUMP);
        setSize(1);
        setInfo("Dumping the bucket \n");

        setDataByIndex(0, direction);
        setDataTagByIndex(0, "direction");

    }
}

