package common;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import messages.*;

public class QueueTest {
	
	private MessageQueue mq;
	
	@Before
	public void setup()
	{
		mq = new MessageQueue();
		AbsMessage.resetMessageCount();
	}

	@Test
	public void queueIsCreated()
	{
		assertNotNull(mq);
		assertEquals(0, mq.getSize());
		assertTrue(mq.isEmpty());
		mq.addAtBack(new MsgStop());
		assertFalse(mq.isEmpty());
	}
	
	@Test
	public void getFirstMessage()
	{
		Message m1 = new MsgStop();
		mq.addAtBack(m1);
		assertEquals(1, mq.getSize());
		assertEquals(m1, mq.peek());
		assertEquals("<0|0>", mq.peek().getMessageString());
	}
	
	@Test
	public void checkMultiMessageQueue()
	{
		Message m1 = new MsgStop();
		Message m2 = new MsgDriveTime(0.0, 0.0);
		Message m3 = new MsgDriveDistance(0.0, 0.0);
		mq.addAtBack(m1);
		mq.addAtBack(m2);
		mq.addAtBack(m3);
		assertEquals(3, mq.getSize());
		assertEquals(m1, mq.peek());
		assertEquals("<0|0>", mq.pop().getMessageString());
		assertEquals(m2, mq.peek());
		assertEquals("<1|1:0.0:0.0>", mq.pop().getMessageString());
		assertEquals(m3, mq.peek());
		assertEquals("<2|2:0.0:0.0>", mq.pop().getMessageString());
		assertEquals(0, mq.getSize());
	}

}
