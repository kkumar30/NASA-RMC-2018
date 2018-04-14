package messages;

/**
 * Created by Kushagra Kumar on 4/14/2018.
 */

import common.AbsMessage;
import common.MessageType;

public class MsgDigTime extends AbsMessage
{
    public MsgDigTime()
    {
        this(0.0);
    }

    public MsgDigTime(double time)
    {
        super();
        setType(MessageType.MSG_DIG);
        setSize(1);
        setInfo("Excavating for " + time + "s \n");

        setDataByIndex(0, time);
        setDataTagByIndex(0, "Time");

    }
}

