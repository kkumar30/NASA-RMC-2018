package common;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class Gamepad
{
	private final int AXS_Y1 = 0,
			AXS_X1 = 1,
			AXS_Y2 = 2,
			AXS_X2 = 3,
			AXS_T = 4,
			BTN_A = 5,
			BTN_B = 6,
			BTN_X = 7,
			BTN_Y = 8,
			BTN_LB = 9,
			BTN_RB = 10,
			BTN_BACK = 11,
			BTN_START = 12, 
			BTN_LJ = 13,
			BTN_RJ = 14,
			DPAD = 15;
	
	private boolean[] button = new boolean[12];
	private double[] axis = new double[5];
	private boolean[] hat = new boolean[4];
	private Controller joystick;
	
	public Gamepad()
	{
		initialize();
		update();		
	}
	
	/*
	 * Expected to be run once at the creation of the joystick in order
	 * to scan available devices and select Logitech gamepad 
	 */
	private void initialize()
	{
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		
		joystick = null;
		
		for(int i = 0; i < controllers.length; i++)
		{
			if(controllers[i].getType()==Controller.Type.GAMEPAD)	
			{
				joystick = controllers[i];
				break;
			}
		}
		if(joystick == null)
		{
			System.out.println("Joystick not connected.");
			//System.exit(0);
		}
	}
	
	public double getAxisX1() { return joystick.getComponents()[AXS_X1].getPollData(); }
	public double getAxisY1() { return -joystick.getComponents()[AXS_Y1].getPollData(); }
	public double getAxisX2() { return joystick.getComponents()[AXS_X2].getPollData(); }
	public double getAxisY2() { return -joystick.getComponents()[AXS_Y2].getPollData(); }
	public double getAxisT() { return joystick.getComponents()[AXS_T].getPollData(); }
	
	public boolean getButtonA() { return joystick.getComponents()[BTN_A].getPollData() != 0.0; }
	public boolean getButtonB() { return joystick.getComponents()[BTN_B].getPollData() != 0.0; }
	public boolean getButtonX() { return joystick.getComponents()[BTN_X].getPollData() != 0.0; }
	public boolean getButtonY() { return joystick.getComponents()[BTN_Y].getPollData() != 0.0; }
	public boolean getButtonLB() { return joystick.getComponents()[BTN_LB].getPollData() != 0.0; }
	public boolean getButtonRB() { return joystick.getComponents()[BTN_RB].getPollData() != 0.0; }
	public boolean getButtonBack() { return joystick.getComponents()[BTN_BACK].getPollData() != 0.0; }
	public boolean getButtonStart() { return joystick.getComponents()[BTN_START].getPollData() != 0.0; }
	public boolean getButtonLJ() { return joystick.getComponents()[BTN_LJ].getPollData() != 0.0; }
	public boolean getButtonRJ() { return joystick.getComponents()[BTN_RJ].getPollData() != 0.0; }
	
	public double getDPad() { return joystick.getComponents()[DPAD].getPollData(); }
	
	public void update()
	{
		try
		{
			joystick.poll();
		}
		catch(Exception e){}
	}
	
	private void printGamepadComponents()
	{
		Component[] components = joystick.getComponents();
		System.out.println("Component Count: " + components.length);
		for(int i = 0; i < components.length; i++)
		{
			System.out.println("Component " + i + ": " + components[i].getName());
		}
	}
	
}
