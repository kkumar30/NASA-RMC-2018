package data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.MotorMode;

/*
 * 	msg_deviceID = int(match[0])
	msg_current = float(match[1])
	msg_temperature = float(match[2])
	msg_voltageOutput = float(match[3])
	msg_speed = float(match[4])
	msg_position = float(match[5])
	msg_setpoint = float(match[6])
	msg_controlMode = int(match[7])
	msg_forward_limit = bool(int(match[8]))
	msg_reverse_limit = bool(int(match[9]))
 * 
 */

public class Motor 
{
	private Integer deviceID = -1;
	private Double current = Double.NaN;
	private Double temperature = Double.NaN;
	private Double voltage = Double.NaN;
	private Double speed = Double.NaN;
	private Double position = Double.NaN;
	private Double setpoint = Double.NaN;
	private MotorMode mode = MotorMode.K_PERCENT_VBUS;
	private Boolean forward_limit = false;
	private Boolean reverse_limit = false;
	
	public Motor(){}

	public Integer getDeviceID() {return deviceID;}
	public Double getCurrent() {return current;}
	public Double getTemperature() {return temperature;}
	public Double getVoltage() {return voltage;}
	public Double getSpeed() {return speed;}
	public Double getPosition() {return position;}
	public Double getSetpoint() {return setpoint;}
	public MotorMode getMode() {return mode;}
	public Boolean getForwardLimit() {return forward_limit;}
	public Boolean getReverseLimit() {return reverse_limit;}
	
	public void updateMotorData(String motorData)
	{
		final String patternStr = "(\\d+):(\\-?\\d+\\.\\d+):(\\-?\\d+\\.\\d+):(\\-?\\d+\\.\\d+):(\\-?\\d+\\.\\d+):(\\-?\\d+\\.\\d+):(\\-?\\d+\\.\\d+):(\\d+):([01]):([01])";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher m = pattern.matcher(motorData);
		if(m.matches())
		{
			try
			{
				deviceID = Integer.parseInt(m.group(1));
				current = Double.parseDouble(m.group(2));
				temperature = Double.parseDouble(m.group(3));
				voltage = Double.parseDouble(m.group(4));
				speed = Double.parseDouble(m.group(5));
				position = Double.parseDouble(m.group(6));
				setpoint = Double.parseDouble(m.group(7));
				mode = MotorMode.values()[Integer.parseInt(m.group(8))];
				forward_limit = (Integer.parseInt(m.group(9)) == 1) ? true : false;
				reverse_limit = (Integer.parseInt(m.group(10)) == 1) ? true : false;
			}
			catch(Exception e)
			{
				System.out.println("Update failed due to malformed message");
			}
		}
		else
		{
			System.out.println("Update Actuals Failed!");
		}
		
		
	}
}
