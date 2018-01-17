package messages;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import common.AbsMessage;
import common.Message;

public class IndividualMessageFunctionalityTests {

	@Before
	public void setup()
	{
		AbsMessage.resetMessageCount();
	}
	
	@Test
	public void testMsgStop()
	{
		Message m = new MsgStop();
		assertEquals("<0|0>", m.getMessageString());
	}
	
	@Test
	public void testMsgDriveDistance() 
	{
		Message m = new MsgDriveTime(5.0, 0.75);
		assertEquals("<1|0:5.0:0.75>", m.getMessageString());
	}
	

	@Test
	public void multipleMessagesHaveCorrectCount()
	{
		Message m1 = new MsgStop();
		Message m2 = new MsgStop();
		Message m3 = new MsgStop();
		assertEquals("<0|0>", m1.getMessageString());
		assertEquals("<0|1>", m2.getMessageString());
		assertEquals("<0|2>", m3.getMessageString());
	}
	
}
