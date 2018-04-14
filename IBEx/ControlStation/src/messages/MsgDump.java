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
        super();
        setType(MessageType.MSG_DUMP);
        setSize(0);
        setInfo("Dumping the bucket \n");

    }
}

