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
        this(0.0);
    }

    public MsgRetractDigger(double time)
    {
        super();
        setType(MessageType.MSG_RETRACT_DIGGER);
        setSize(1);
        setInfo("Retracting digger for " + time + "s \n");
    }
}

