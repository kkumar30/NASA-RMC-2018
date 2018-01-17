package messages;

import common.AbsMessage;
import common.Gamepad;
import common.MessageType;

public class MsgMotorValues extends AbsMessage
{
	private Gamepad g = new Gamepad();
	public MsgMotorValues()
	{
		super();
		setType(MessageType.MSG_MOTOR_VALUES);
		setSize(5);
		setInfo("Writing Values from Gamepad\n");
	}
	
	@Override
	public String getMessageString()
	{
		g.update();
		double leftMotorVal = -g.getAxisY1();
		double rightMotorVal = g.getAxisY2();
		double scoopMotorVal = g.getAxisT();
		double depthMotorVal = g.getButtonA() ? 1.0 : g.getButtonY() ? -1.0 : 0.0;
		double winchMotorVal = g.getButtonLB() ? 1.0 : g.getButtonRB() ? -1.0 : 0.0;
		setDataByIndex(0, leftMotorVal);
		setDataByIndex(1, rightMotorVal);	
		setDataByIndex(2, scoopMotorVal);
		setDataByIndex(3, depthMotorVal);
		setDataByIndex(4, winchMotorVal);
		setDataTagByIndex(0, "Left Motor");
		setDataTagByIndex(1, "Right Motor");
		setDataTagByIndex(2, "Scoop Motor");
		setDataTagByIndex(3, "Depth Motor");
		setDataTagByIndex(4, "Winch Motor");
		String messageString = "<";
		messageString += getType().ordinal() + "|";
		messageString += getMessageNumber();
		messageString += ":" + leftMotorVal;
		messageString += ":" + rightMotorVal;
		messageString += ":" + scoopMotorVal;
		messageString += ":" + depthMotorVal;
		messageString += ":" + winchMotorVal;
		messageString += ">";
		System.out.println("Sending MessageString");
		return messageString;
	}
}
