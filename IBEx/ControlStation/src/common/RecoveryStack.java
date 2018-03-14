package common;

import java.util.ArrayList;


public class RecoveryStack {

    private ArrayList<Message> stack;

    public RecoveryStack()
    {
        stack = new ArrayList<Message>();
    }


    public int getSize() { return stack.size(); }
    public boolean isEmpty() { return getSize() == 0; }

    public Message peek()
    {
        return !isEmpty() ? stack.get(getSize()-1) : null;
    }

    public Message peekAtIndex(int i)
    {
        return !isEmpty() ? stack.get(i) : null;
    }

    public Message pop()
    {
        return !isEmpty() ? stack.remove(getSize()-1) : null;
    }

    public void addAtBack(Message msg)
    {
        stack.add(getSize(), msg);
    }

    public void addAtPosition(Message msg, int i)
    {
        stack.add(i, msg);
    }

    public void addAtFront(Message msg)
    {
        stack.add(0, msg);
    }

    public void removeAtIndex(int i)
    {
        stack.remove(i);
    }

    public void clear()
    {
        stack = new ArrayList<Message>();
    }

}