package gui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.MotorMode;
import data.Motor;

public class RobotData 
{	
	private Motor leftMotor;
	private Motor rightMotor;
	private Motor scoopMotor;
	private Motor depthMotor;
	private Motor winchMotor;
	
	public RobotData()
	{
		leftMotor = new Motor();
		rightMotor = new Motor();
		scoopMotor = new Motor();
		depthMotor = new Motor();
		winchMotor = new Motor();
	}
	
	public Motor getLeftMotor() {return leftMotor;}
	public Motor getRightMotor() {return rightMotor;}
	public Motor getScoopMotor() {return scoopMotor;}
	public Motor getDepthMotor() {return depthMotor;}
	public Motor getWinchMotor() {return winchMotor;}
	
	public void updateRobotData(String message)
	{
		final String patternStr = "<(.+)><(.+)><(.+)><(.+)><(.+)>";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher m = pattern.matcher(message);
		if(m.matches())
		{
			String leftMotorData = m.group(1);
			String rightMotorData = m.group(2);
			String scoopMotorData = m.group(3);
			String depthMotorData = m.group(4);
			String winchMotorData = m.group(5);
			
			leftMotor.updateMotorData(leftMotorData);
			rightMotor.updateMotorData(rightMotorData);
			scoopMotor.updateMotorData(scoopMotorData);
			depthMotor.updateMotorData(depthMotorData);
			winchMotor.updateMotorData(winchMotorData);
		}
	}
	
	
}
