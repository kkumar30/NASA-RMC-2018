package messages;

/**
 * Created by Kushagra Kumar on 4/14/2018.
 */

import common.AbsMessage;
import common.MessageType;

public class MsgGoToDump extends AbsMessage
{

    public MsgGoToDump()
    {
        super();
        setType(MessageType.MSG_GO_TO_DUMP);
        setSize(0);
        setInfo("Going to Collection Bin \n");

    }
}

