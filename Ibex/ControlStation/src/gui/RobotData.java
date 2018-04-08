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
	private Motor dumpMotor;

	private Sensor imu;
	private Sensor camServo1;
	private Sensor camServo2;
	private Sensor LEDSensor;
	private Sensor hb; //Heartbeat
	
	public RobotData()
	{
		leftMotor = new Motor();
		rightMotor = new Motor();
		scoopMotor = new Motor();
		depthMotor = new Motor();
		dumpMotor = new Motor();

		imu = new Sensor();
		camServo1 = new Sensor();
		camServo2 = new Sensor();
		LEDSensor = new Sensor();
		hb = new Sensor();
	}
	
	public Motor getLeftMotor() {return leftMotor;}
	public Motor getRightMotor() {return rightMotor;}
	public Motor getScoopMotor() {return scoopMotor;}
	public Motor getDepthMotor() {return depthMotor;}
	public Motor getDumpMotor() {return dumpMotor;}
	public Sensor getImu() {return imu;}
	public Sensor getCamServo1() {return camServo1;}
	public Sensor getCamServo2() {return camServo2;}
	public Sensor getLEDSensor() {return LEDSensor;}
	public Sensor getHb(){return hb;}
	


	public void updateRobotData(String motorMessage, String sensorMessage)
	{
		final String patternStr = "<(.+)><(.+)><(.+)><(.+)><(.+)>";
//		final String patternStr = "<(.+)><(.+)>"; //patterns for all motors only
//
		final String SensorPatternStr = "<(.+)><(.+)><(.+)><(.+)>"; //patterns for sensors and other status messages

		Pattern pattern = Pattern.compile(patternStr);
		Pattern sensorPattern = Pattern.compile(SensorPatternStr);

		Matcher motorMatch = pattern.matcher(motorMessage);

//		Matcher sensorMatch = pattern.matcher(sensorMessage);
		Matcher sensorMatch = sensorPattern.matcher(sensorMessage);
		if(motorMatch.matches())
		{
			String leftMotorData = motorMatch.group(1);
			String rightMotorData = motorMatch.group(2);
			String scoopMotorData = motorMatch.group(3);
			String depthMotorData = motorMatch.group(4);
			String dumpMotorData = motorMatch.group(5);
			
			leftMotor.updateMotorData(leftMotorData);
			rightMotor.updateMotorData(rightMotorData);
			scoopMotor.updateMotorData(scoopMotorData);
			depthMotor.updateMotorData(depthMotorData);
			dumpMotor.updateMotorData(dumpMotorData);
		}
		System.out.println(sensorMatch.matches());
		if(sensorMatch.matches())
		{
			String imuData = sensorMatch.group(1);
			String LEDSensorData = sensorMatch.group(2);
			String hbData = sensorMatch.group(3);
			String camServoData1 = sensorMatch.group(4);
//			String camServoData2 = sensorMatch.group(5);
			imu.updateSensorData(imuData);
			camServo1.updateSensorData(camServoData1);
//			camServo2.updateSensorData(camServoData2);
			hb.updateSensorData(hbData);
			LEDSensor.updateSensorData(LEDSensorData);
		}

	}
	
	
}
