using System;
using System.Threading;
using Microsoft.SPOT;
using Microsoft.SPOT.Hardware;
namespace Hero_Simple_Application4
{
 public class Program
 {
 public static void Main()
 {
 /* create a gamepad object */
 CTRE.Gamepad myGamepad = new CTRE.Gamepad(new CTRE.UsbHostDevice());
 /* create a talon, the Talon Device ID in HERO LifeBoat is zero */
 CTRE.TalonSrx myTalon = new CTRE.TalonSrx(0);
 /* simple counter to print and watch using the debugger */
 int counter = 0;
 /* loop forever */
 while (true)
 {
 /* added inside the while loop */
 if (myGamepad.GetConnectionStatus() == CTRE.UsbDeviceConnection.Connected)
 {
 /* print the axis value */
 Debug.Print("axis:" + myGamepad.GetAxis(1));
 /* pass axis value to talon */
myTalon.Set(myGamepad.GetAxis(1));
 /* allow motor control */
 CTRE.Watchdog.Feed();
 }
 /* increment counter */
 ++counter; /* try to land a breakpoint here */
 /* wait a bit */
 System.Threading.Thread.Sleep(10);
 }
 }
 }
}