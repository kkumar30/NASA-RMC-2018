package gui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.MotorMode;
import data.Motor;
import data.Sensor;

public class RobotData 
{	
	private Motor leftMotor;
	private Motor rightMotor;
	private Motor scoopMotor;
	private Motor depthMotor;
	private Motor winchMotor;

	private Sensor imu;
	private Sensor camServo;
	private Sensor LEDSensor;
	
	public RobotData()
	{
		leftMotor = new Motor();
		rightMotor = new Motor();
		scoopMotor = new Motor();
		depthMotor = new Motor();
		winchMotor = new Motor();

		imu = new Sensor();
		camServo = new Sensor();
		LEDSensor = new Sensor();
	}
	
	public Motor getLeftMotor() {return leftMotor;}
	public Motor getRightMotor() {return rightMotor;}
	public Motor getScoopMotor() {return scoopMotor;}
	public Motor getDepthMotor() {return depthMotor;}
	public Motor getWinchMotor() {return winchMotor;}
	public Sensor getImu() {return imu;}
	public Sensor getCamServo() {return camServo;}
	public Sensor getLEDSensor() {return LEDSensor;}


	public void updateRobotData(String motorMessage, String sensorMessage)
	{
//		final String patternStr = "<(.+)><(.+)><(.+)><(.+)><(.+)>";
		final String patternStr = "<(.+)><(.+)>";
		final String SensorPatternStr = "<(.+)><(.+)><(.+)>";

		Pattern pattern = Pattern.compile(patternStr);
		Pattern sensorPattern = Pattern.compile(SensorPatternStr);

		Matcher motorMatch = pattern.matcher(motorMessage);

//		Matcher sensorMatch = pattern.matcher(sensorMessage);
		Matcher sensorMatch = sensorPattern.matcher(sensorMessage);
		if(motorMatch.matches())
		{
			String leftMotorData = motorMatch.group(1);
			String rightMotorData = motorMatch.group(2);
//			String scoopMotorData = m.group(3);
//			String depthMotorData = m.group(4);
//			String winchMotorData = m.group(5);
			
			leftMotor.updateMotorData(leftMotorData);
			rightMotor.updateMotorData(rightMotorData);
//			scoopMotor.updateMotorData(scoopMotorData);
//			depthMotor.updateMotorData(depthMotorData);
//			winchMotor.updateMotorData(winchMotorData);
		}

		if(sensorMatch.matches())
		{
			String imuData = sensorMatch.group(1);
			String camServoData = sensorMatch.group(3);

			imu.updateSensorData(imuData);
			camServo.updateSensorData(camServoData);

//			For the LED ping test
			String LEDSensorData = sensorMatch.group(2);
			LEDSensor.updateSensorData(LEDSensorData);
		}

	}
	
	
}
