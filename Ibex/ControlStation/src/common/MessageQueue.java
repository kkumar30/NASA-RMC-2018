package common;

import java.util.ArrayList;

public class MessageQueue 
{
	private ArrayList<Message> queue;
	
	public MessageQueue()
	{
		queue = new ArrayList<Message>();
	}
	
	
	public int getSize() { return queue.size(); }
	public boolean isEmpty() { return getSize() == 0; }
	
	public Message peek()
	{
		return !isEmpty() ? queue.get(0) : null;
	}
	
	public Message peekAtIndex(int i)
	{
		return !isEmpty() ? queue.get(i) : null;
	}
	
	public Message pop()
	{
		return !isEmpty() ? queue.remove(0) : null;
	}
	
	public void addAtBack(Message msg)
	{
		queue.add(msg);
	}
	
	public void addAtPosition(Message msg, int i)
	{
		queue.add(i, msg);
	}
	
	public void addAtFront(Message msg)
	{
		queue.add(0, msg);
	}
	
	public void removeAtIndex(int i)
	{
		queue.remove(i);
	}
	
	public void clear()
	{
		queue = new ArrayList<Message>();
	}
}
