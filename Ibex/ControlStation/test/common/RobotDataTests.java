package common;
import static org.junit.Assert.*;

import org.junit.Test;

import common.MotorMode;
import gui.RobotData;

public class RobotDataTests {

	@Test
	public void regexMatchesAndUpdates() 
	{
		RobotData robotData = new RobotData();
		robotData.updateRobotData("<1:0.0:28.065:0.0:0.0:0.0:0.0:0:0:0><2:0.0:27.419:0.0:0.0:0.0:0.0:0:0:0><3:0.0:27.419:0.0:0.0:0.0:0.0:0:0:0><4:0.0:24.839:0.0:0.0:0.0:0.0:0:0:0><5:0.0:27.419:0.0:0.0:-0.006:0.0:0:0:0>");
		//robotData.updateRobotData("<1><2><3><4><5>");
		assertEquals(Integer.valueOf(1), robotData.getLeftMotor().getDeviceID());
		assertEquals(Integer.valueOf(2), robotData.getRightMotor().getDeviceID());
		assertEquals(Integer.valueOf(3), robotData.getScoopMotor().getDeviceID());
		assertEquals(Integer.valueOf(4), robotData.getDepthMotor().getDeviceID());
		assertEquals(Integer.valueOf(5), robotData.getWinchMotor().getDeviceID());
	}

}
