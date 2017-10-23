package common;

public interface Message {

	String getMessageString();
	String getDataTagByIndex(int index);
	MessageType getType();
	void setDataByIndex(int index, double data);

}
