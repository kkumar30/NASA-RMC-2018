using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SlimDX.DirectInput;

namespace GameControllerInterface
{
    class ControllerValues
    {
        //each button and joystick is a 1x2 array that records the current state
        //and the previous state.  This way, presses, holds, and releases can be captured
        //value[0] is the current value
        //value[1] is the previous value
        private int[] xAxisPrimary;
        private int[] yAxisPrimary;
        private int[] xAxisSecondary;
        private int[] yAxisSecondary;
        private int[] leftTrigger;
        private int[] rightTrigger;
        private int[] dPad;

        private bool[] buttonA;
        private bool[] buttonB;
        private bool[] buttonX;
        private bool[] buttonY;
        private bool[] buttonLB;
        private bool[] buttonRB;
        private bool[] buttonStart;
        private bool[] buttonSelect;
        private bool[] buttonLStick;
        private bool[] buttonRStick;


        public ControllerValues()
        {
            xAxisPrimary = new int[] { 0, 0 };
            yAxisPrimary = new int[] { 0, 0 };
            xAxisSecondary = new int[] { 0, 0 };
            yAxisSecondary = new int[] { 0, 0 };
            leftTrigger = new int[] { 0, 0 };
            rightTrigger = new int[] { 0, 0 };
            dPad = new int[] { 0, 0 };

            buttonA = new bool[] {false, false};
            buttonB = new bool[] { false, false };
            buttonX = new bool[] { false, false };
            buttonY = new bool[] { false, false };
            buttonLB = new bool[] { false, false };
            buttonRB = new bool[] { false, false };
            buttonStart = new bool[] { false, false };
            buttonSelect = new bool[] { false, false };
            buttonLStick = new bool[] { false, false };
            buttonRStick = new bool[] { false, false };
            
        }

        public bool buttonA_pressed() { return buttonA[0] && !buttonA[1]; }
        public bool buttonB_pressed() { return buttonB[0] && !buttonB[1]; }
        public bool buttonX_pressed() { return buttonX[0] && !buttonX[1]; }
        public bool buttonY_pressed() { return buttonY[0] && !buttonY[1]; }
        public bool buttonLB_pressed() { return buttonLB[0] && !buttonLB[1]; }
        public bool buttonRB_pressed() { return buttonRB[0] && !buttonRB[1]; }
        public bool buttonSelect_pressed() { return buttonSelect[0] && !buttonSelect[1]; }
        public bool buttonStart_pressed() { return buttonStart[0] && !buttonStart[1]; }
        public bool buttonLStick_pressed() { return buttonLStick[0] && !buttonLStick[1]; }
        public bool buttonRStick_pressed() { return buttonRStick[0] && !buttonRStick[1]; }

        public bool buttonA_released() { return !buttonA[0] && buttonA[1]; }
        public bool buttonB_released() { return !buttonB[0] && buttonB[1]; }
        public bool buttonX_released() { return !buttonX[0] && buttonX[1]; }
        public bool buttonY_released() { return !buttonY[0] && buttonY[1]; }
        public bool buttonLB_released() { return !buttonLB[0] && buttonLB[1]; }
        public bool buttonRB_released() { return !buttonRB[0] && buttonRB[1]; }
        public bool buttonSelect_released() { return !buttonSelect[0] && buttonSelect[1]; }
        public bool buttonStart_released() { return !buttonStart[0] && buttonStart[1]; }
        public bool buttonLStick_released() { return !buttonLStick[0] && buttonLStick[1]; }
        public bool buttonRStick_released() { return !buttonRStick[0] && buttonRStick[1]; }


        public void updateValues(JoystickState jsState)
        {
            //record old axis values
            xAxisPrimary[1] = xAxisPrimary[0];
            yAxisPrimary[1] = yAxisPrimary[0];
            xAxisSecondary[1] = xAxisSecondary[0];
            yAxisSecondary[1] = yAxisSecondary[0];
            leftTrigger[1] = leftTrigger[0];
            rightTrigger[1] = rightTrigger[0];
            dPad[1] = dPad[0];

            //record old button values
            buttonA[1] = buttonA[0];
            buttonB[1] = buttonB[0];
            buttonX[1] = buttonX[0];
            buttonY[1] = buttonY[0];
            buttonLB[1] = buttonLB[0];
            buttonRB[1] = buttonRB[0];
            buttonStart[1] = buttonStart[0];
            buttonSelect[1] = buttonSelect[0];
            buttonLStick[1] = buttonLStick[0];
            buttonRStick[1] = buttonRStick[0];

            //record new axis values
            xAxisPrimary[0] = jsState.X;
            yAxisPrimary[0] = 255 - jsState.Y;
            xAxisSecondary[0] = jsState.RotationX;
            yAxisSecondary[0] = 255 - jsState.RotationY;

            if(jsState.Z > 140)
            {
                leftTrigger[0] = jsState.Z;
                rightTrigger[0] = 127;
            }
            else if(jsState.Z < 116)
            {
                leftTrigger[0] = 127;
                rightTrigger[0] = jsState.Z;
            }
            else //deadband
            {
                leftTrigger[0] = 127;
                rightTrigger[0] = 127;
            }
            dPad[0] = jsState.GetPointOfViewControllers()[0];
            

            //record new button values
            buttonA[0] = jsState.GetButtons()[0];
            buttonB[0] = jsState.GetButtons()[1];
            buttonX[0] = jsState.GetButtons()[2];
            buttonY[0] = jsState.GetButtons()[3];
            buttonLB[0] = jsState.GetButtons()[4];
            buttonRB[0] = jsState.GetButtons()[5];
            buttonSelect[0] = jsState.GetButtons()[6];
            buttonStart[0] = jsState.GetButtons()[7];
            buttonLStick[0] = jsState.GetButtons()[8];
            buttonRStick[0] = jsState.GetButtons()[9];

            //Console.WriteLine(jsState.GetSliders()[1].ToString());
        }

        public void printValues()
        {
            //setup axis values string
            string axisStr = "";
            axisStr += "X: " + xAxisPrimary[0].ToString();
            axisStr += ", Y: " + yAxisPrimary[0].ToString();
            axisStr += ", X2: " + xAxisSecondary[0].ToString();
            axisStr += ", Y2: " + yAxisSecondary[0].ToString();
            axisStr += ", LT: " + leftTrigger[0].ToString();
            axisStr += ", RT: " + rightTrigger[0].ToString();
            axisStr += ", Hat: " + (dPad[0]).ToString();


            //setup button values string
            string buttonStr = "";
            buttonStr += "A: " + buttonA[0].ToString();
            buttonStr += ", B: " + buttonB[0].ToString();
            buttonStr += ", X: " + buttonX[0].ToString();
            buttonStr += ", Y: " + buttonY[0].ToString();
            buttonStr += ", LB: " + buttonLB[0].ToString();
            buttonStr += ", RB: " + buttonRB[0].ToString();
            buttonStr += ", Start: " + buttonStart[0].ToString();
            buttonStr += ", Select: " + buttonSelect[0].ToString();
            buttonStr += ", LStick: " + buttonLStick[0].ToString();
            buttonStr += ", RStick: " + buttonRStick[0].ToString();

            Console.WriteLine(axisStr);
            Console.WriteLine(buttonStr);
        }

        public String generateSimpleMessage()
        {
            String msg = "";
            msg += String.Format("{0}:{1}:{2}:{3}:{4}:{5}:", xAxisPrimary[0], yAxisPrimary[0], xAxisSecondary[0], yAxisSecondary[0], leftTrigger[0], rightTrigger[0]);
            msg += String.Format("{0}:{1}:{2}:{3}:{4}:{5}:{6}:{7}:{8};", Convert.ToInt16(buttonA[0]).ToString(),
                                                                             Convert.ToInt16(buttonB[0]).ToString(),
                                                                             Convert.ToInt16(buttonX[0]).ToString(),
                                                                             Convert.ToInt16(buttonY[0]).ToString(),
                                                                             Convert.ToInt16(buttonLB[0]).ToString(),
                                                                             Convert.ToInt16(buttonRB[0]).ToString(),
                                                                             Convert.ToInt16(buttonSelect[0]).ToString(), 
                                                                             Convert.ToInt16(buttonStart[0]).ToString(),
                                                                             Convert.ToInt16(buttonLStick[0]).ToString());//, 
                                                                             //Convert.ToInt16(buttonRStick[0]).ToString());
            return msg;
        }
    }
}
