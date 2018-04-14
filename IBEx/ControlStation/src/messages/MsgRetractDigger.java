package messages;

/**
 * Created by Kushagra Kumar on 4/14/2018.
 */

import common.AbsMessage;
import common.MessageType;

public class MsgRetractDigger extends AbsMessage
{
    public MsgRetractDigger()
    {
        super();
        setType(MessageType.MSG_RETRACT_DIGGER);
        setSize(0);
        setInfo("Retracting digger \n");

    }
}

