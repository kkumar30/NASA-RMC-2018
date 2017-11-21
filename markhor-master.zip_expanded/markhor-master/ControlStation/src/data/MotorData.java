package data;

import common.MotorMode;

public class MotorData 
{
	private final int deviceID;
	private double current = 0.0;
	private double temperature = 0.0;
	private double voltageOutput = 0.0;
	private double speed = 0.0;
	private double position = 0.0;
	private double setpoint = 0.0;
	private MotorMode mode = MotorMode.K_PERCENT_VBUS;
	private boolean fowrardLimit = false;
	private boolean reverseLimit = false;
	
	public double getCurrent() {return current;}
	public void setCurrent(double current) {this.current = current;}
	public double getTemperature() {return temperature;}
	public void setTemperature(double temperature) {this.temperature = temperature;}
	public double getVoltageOutput() {return voltageOutput;}
	public void setVoltageOutput(double voltageOutput) {this.voltageOutput = voltageOutput;}
	public double getSpeed() {return speed;	}
	public void setSpeed(double speed) {this.speed = speed;	}
	public double getPosition() {return position;}
	public void setPosition(double position) {this.position = position;	}
	public double getSetpoint() {return setpoint;}
	public void setSetpoint(double setpoint) {this.setpoint = setpoint;}
	public MotorMode getMode() {return mode;}
	public void setMode(MotorMode mode) {this.mode = mode;}
	public boolean isFowrardLimit() {return fowrardLimit;}
	public void setFowrardLimit(boolean fowrardLimit) {this.fowrardLimit = fowrardLimit;}
	public boolean isReverseLimit() {return reverseLimit;}
	public void setReverseLimit(boolean reverseLimit) {this.reverseLimit = reverseLimit;}
	public int getDeviceID() {return deviceID;}

	
	public MotorData(int deviceID)
	{
		this.deviceID = deviceID;
	}
	
	
	
}
