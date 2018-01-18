package common;

import static org.junit.Assert.*;

import org.junit.Test;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class JoystickTest {

	@Test
	public void test() throws InterruptedException 
	{
		Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for(int i =0;i<ca.length;i++){

            /* Get the name of the controller */
            System.out.println(ca[i].getName());
            System.out.println("Type: "+ca[i].getType().toString());
        }
		
		Gamepad g = new Gamepad();
		while(true)
		{
			g.update();
			//System.out.println(g.getButtonA() + ":" +  g.getButtonB()  + ":" + g.getButtonX() + ":" + g.getButtonY() +
			//			 ":" + g.getButtonLB() + ":" + g.getButtonRB() + ":" + g.getButtonBack() + ":" + g.getButtonStart() +
			//			 ":" + g.getButtonLJ() + ":" + g.getButtonRJ());
			
			System.out.println("Left X " + g.getAxisX1() + ": Left Y " + g.getAxisY1() + ": Right X " + g.getAxisX2() + ": Right Y" + g.getAxisY2() + ": Trigger R " + g.getAxisT());
			//System.out.println(g.getDPad());
			Thread.sleep(20);
		}
	}
}
