package common;

public abstract class AbsMessage implements Message
{
	private static int messageCount = 0;
	
	private MessageType type;
	
	private final int N_DATA = 8;
	private int size;
	private int messageNumber;
	private String info;
	private double[] data = new double[N_DATA];
	private String[] dataTags = new String[N_DATA];
	
	
	public AbsMessage()
	{
		initializeMessage();
	}

	public MessageType getType() {return type;}
	public void setType(MessageType type) {this.type = type;}

	public int getSize() {return size;}
	public void setSize(int size) {this.size = size;}

	public String getInfo() {return info;}
	public void setInfo(String info) {this.info = info;}
	
	public int getMessageNumber()
	{
		return messageNumber;
	}
	
	public void setDataByIndex(int index, double data)
	{
		if(index >= 0 && index < N_DATA)
		{
			this.data[index] = data;
		}
	}
	
	public double getDataByIndex(int index)
	{
		if(index >= 0 && index < N_DATA)
		{
			return this.data[index];
		}
		return 0.0;
	}
	
	public void setDataTagByIndex(int index, String tag)
	{
		if(index >= 0 && index < N_DATA)
		{
			this.dataTags[index] = tag;
		}
	}
	
	public String getDataTagByIndex(int index)
	{
		if(index >= 0 && index < N_DATA)
		{
			return this.dataTags[index];
		}
		return "";
	}
	
	public void initializeMessage()
	{
		messageNumber = messageCount++;
		for(int i = 0; i < N_DATA; i++)
		{
			dataTags[i] = "N/A";
		}
	}
	
	public static void resetMessageCount()
	{
		messageCount = 0;
	}
	
	
	/*
	 * Message string format is as follows:
	 * <Message Type | Message Number | DATA0 : DATA 1 : ... : DATA 7>
	 */
	public String getMessageString()
	{
		String messageString = "<";
		messageString += type.ordinal() + "|";
		messageString += messageNumber + "";
		for(int i = 0; i < size; i++)
		{
			messageString += ":";
			messageString += data[i];
		}
		messageString += ">";
		return messageString;
	}
	
	
	
}
