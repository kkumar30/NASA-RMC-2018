/**
 * Example HERO application can reads a serial port and echos the bytes back.
 * After deploying this application, the user can open a serial terminal and type while the HERO echoes the typed keys back.
 * Use a USB to UART (TTL) cable like the Adafruit Raspberry PI or FTDI-TTL cable.
 * Use device manager to figure out which serial port number to select in your PC terminal program.
 * HERO Gadgeteer Port 1 is used in this example, but can be changed at the top of Main().
 */
using System;
using System.Threading;
using Microsoft.SPOT;
using System.Collections;
using System.Text.RegularExpressions;

namespace Ibex_Motor_Control
{
    public class Program
    {
        //Enumeration for bools[] reseting encoder values
        private const int RESET_DRIVE_ENC = 0;
        private const int RESET_DEPTH_ENC = 1;
        private const int RESET_SCOOP_ENC = 2;
        private const int RESET_WINCH_ENC = 3;

        //Flags for Motor ID's
        private const int LDRIVE_ID = 0;
        private const int RDRIVE_ID = 1;
        private const int SCOOP_ID = 2;
        private const int DEPTH_ID = 3;
        private const int WINCH_ID = 4;

        static bool[] flags = { false, false, false, false };

        static System.IO.Ports.SerialPort uart;
        static byte[] rx = new byte[1024];

        public static void Main()
        {
            //Set up the motors
            CTRE.TalonSrx leftmotor = new CTRE.TalonSrx(1);
            CTRE.TalonSrx rightmotor = new CTRE.TalonSrx(2);
            
            CTRE.TalonSrx scoopMotor = new CTRE.TalonSrx(3);
            CTRE.TalonSrx depthMotor = new CTRE.TalonSrx(4);
            CTRE.TalonSrx winchMotor = new CTRE.TalonSrx(5);
            
            //Set encoders for each motor            
            leftmotor.SetFeedbackDevice(CTRE.TalonSrx.FeedbackDevice.QuadEncoder);
            rightmotor.SetFeedbackDevice(CTRE.TalonSrx.FeedbackDevice.QuadEncoder);
            
            scoopMotor.SetFeedbackDevice(CTRE.TalonSrx.FeedbackDevice.QuadEncoder);
            depthMotor.SetFeedbackDevice(CTRE.TalonSrx.FeedbackDevice.QuadEncoder);
            winchMotor.SetFeedbackDevice(CTRE.TalonSrx.FeedbackDevice.QuadEncoder);

            //Set direction of the encoders            
            leftmotor.SetSensorDirection(false);
            rightmotor.SetSensorDirection(false);
            scoopMotor.SetSensorDirection(false);
            depthMotor.SetSensorDirection(false);
            winchMotor.SetSensorDirection(false);

            //Set Ticks per Rev of the encoders
            leftmotor.ConfigEncoderCodesPerRev(80);
            rightmotor.ConfigEncoderCodesPerRev(80);
            scoopMotor.ConfigEncoderCodesPerRev(80);
            depthMotor.ConfigEncoderCodesPerRev(80);
            winchMotor.ConfigEncoderCodesPerRev(80);
           
            //Sets PIDF values for each motor//
            leftmotor.SetP(0, 0.35F);
            leftmotor.SetI(0, 0.0F);
            leftmotor.SetD(0, 0.0F);
            leftmotor.SetF(0, 0.0F);
            leftmotor.SelectProfileSlot(0);

            rightmotor.SetP(0, 0.35F);
            rightmotor.SetI(0, 0.0F);
            rightmotor.SetD(0, 0.0F);
            rightmotor.SetF(0, 0.0F);
            rightmotor.SelectProfileSlot(0);

            
                        scoopMotor.SetP(0, 0.6F);
                        scoopMotor.SetI(0, 0.0F);
                        scoopMotor.SetD(0, 0.0F);
                        scoopMotor.SetF(0, 0.0F);
                        scoopMotor.SelectProfileSlot(0);

                        depthMotor.SetP(0, 0.6F);
                        depthMotor.SetI(0, 0.0F);
                        depthMotor.SetD(0, 0.0F);
                        depthMotor.SetF(0, 0.0F);
                        depthMotor.SelectProfileSlot(0);

                        winchMotor.SetP(0, 0.6F);
                        winchMotor.SetI(0, 0.0F);
                        winchMotor.SetD(0, 0.0F);
                        winchMotor.SetF(0, 0.0F);
                        winchMotor.SelectProfileSlot(0);
                        //////////////////////////////////
            

            //Sets Nominal Output Voltage for each motor
            leftmotor.ConfigNominalOutputVoltage(+0.0F, -0.0F);
            rightmotor.ConfigNominalOutputVoltage(+0.0F, -0.0F);
            scoopMotor.ConfigNominalOutputVoltage(+0.0F, -0.0F);
            depthMotor.ConfigNominalOutputVoltage(+0.0F, -0.0F);
            winchMotor.ConfigNominalOutputVoltage(+0.0F, -0.0F);

            // Set allowed error for closed loop feedback
            leftmotor.SetAllowableClosedLoopErr(0, 0);
            rightmotor.SetAllowableClosedLoopErr(0, 0);

            scoopMotor.SetAllowableClosedLoopErr(0, 0);
            depthMotor.SetAllowableClosedLoopErr(0, 0);
            winchMotor.SetAllowableClosedLoopErr(0, 0);

            //Set Initial position of the motors
            leftmotor.SetPosition(0);
            rightmotor.SetPosition(0);
           
            scoopMotor.SetPosition(0);
            depthMotor.SetPosition(0);
            winchMotor.SetPosition(0);
           
            //Sets Voltage Ramp rate of each motor
            leftmotor.SetVoltageRampRate(0);
            rightmotor.SetVoltageRampRate(0);
            scoopMotor.SetVoltageRampRate(0);
            depthMotor.SetVoltageRampRate(0);
            winchMotor.SetVoltageRampRate(0);
         


            ArrayList motorSetpointData = new ArrayList();
            ArrayList motorStatusData = new ArrayList();
            ArrayList talons = new ArrayList();

            //Add talons to each motor
            talons.Add(leftmotor);
            talons.Add(rightmotor);
            talons.Add(scoopMotor);
            talons.Add(depthMotor);
            talons.Add(winchMotor);
           
            //Initializes inbound and outbound message strings to empty            
            String inboundMessageStr = "";
            String outboundMessageStr = "";

            //Initializes and adds the SetpointData and the StatusData for each motor (ID, mode, setpoint//
            SetpointData leftmotorSetpointData = new SetpointData(1, 0, 0.0F);
            SetpointData rightmotorSetpointData = new SetpointData(2, 0, 0.0F);
            
            SetpointData scoopMotorSetpointData = new SetpointData(3, 0, 0.0F);
            SetpointData depthMotorSetpointData = new SetpointData(4, 0, 0.0F);
            SetpointData winchMotorSetpointData = new SetpointData(5, 0, 0.0F);

            StatusData leftmotorStatusData = new StatusData(1, leftmotor);
            StatusData rightmotorStatusData = new StatusData(2, rightmotor);
            StatusData scoopMotorStatusData = new StatusData(3, scoopMotor);
            StatusData depthMotorStatusData = new StatusData(4, depthMotor);
            StatusData winchMotorStatusData = new StatusData(5, winchMotor);

            motorSetpointData.Add(leftmotorSetpointData);
            motorSetpointData.Add(rightmotorSetpointData);
            
            motorSetpointData.Add(scoopMotorSetpointData);
            motorSetpointData.Add(depthMotorSetpointData);
            motorSetpointData.Add(winchMotorSetpointData);

            motorStatusData.Add(leftmotorStatusData);
            motorStatusData.Add(rightmotorStatusData);
            motorStatusData.Add(scoopMotorStatusData);
            motorStatusData.Add(depthMotorStatusData);
            motorStatusData.Add(winchMotorStatusData);
            //////////////////////////////////////////

            CTRE.Watchdog.Feed();

            //Open up UART communication to the linux box
            uart = new System.IO.Ports.SerialPort(CTRE.HERO.IO.Port1.UART, 115200);
            //uart.DiscardInBuffer();
            uart.Open();
            //uart.DiscardInBuffer();

            //The loop
            while (true)
            {
                //read whatever is available from the UART into the inboundMessageStr
                motorSetpointData = readUART(ref inboundMessageStr);
                CTRE.Watchdog.Feed();

                if (motorSetpointData.Count > 0)
                {
                    //if any of the talon positions need to be reset, this will reset them
                    //resetEncoderPositions(talons);
                    //CTRE.Watchdog.Feed();

                    //attempt to process whatever was contained in the most recent message
                    processInboundData(motorSetpointData, talons);
                    CTRE.Watchdog.Feed();

                    //get a bunch of data from the motors in their current states
                    updateMotorStatusData(motorStatusData);
                    CTRE.Watchdog.Feed();
                }
                //package that motor data into a formatted message
                outboundMessageStr = makeOutboundMessage(motorStatusData);
                //Debug.Print(outboundMessageStr);
                CTRE.Watchdog.Feed();

                /*string[] teststring = outboundMessageStr.Split(':');
                Debug.Print(" Enc:" + teststring[5] + " Enc2:"+ teststring[14]);
                CTRE.Watchdog.Feed();*/

                //send that message back to the main CPU
                //Debug.Print(outboundMessageStr);
                //if (uart.IsOpen) { 
                writeUART(outboundMessageStr);
                CTRE.Watchdog.Feed();
                //}
                CTRE.Watchdog.Feed();

                //Debug.Print(testmotor.GetPosition().ToString());
                //CTRE.Watchdog.Feed();

            }
        }

        private static ArrayList readUART(ref String messageStr)
        {
            ArrayList setpointData = new ArrayList();
            if (uart.BytesToRead > 0)
            {
                //Debug.Print("Here");
                int readCnt = uart.Read(rx, 0, 1024);
                for (int i = 0; i < readCnt; ++i)
                {
                    messageStr += (char)rx[i];
                    if ((char)rx[i] == '\n')
                    {
                        //      Debug.Print("Here2");
                        checkEncoderResetFlags(messageStr);
                        Debug.Print("Inbound Message String: " + messageStr);
                        setpointData = MessageParser.parseMessage(messageStr);
                        Debug.Print("Setpoint Data: " + setpointData[4]);
                        // int c = setpointData.Count;
                        //Console.WriteLine(c);
                        //Debug.Print(c.ToString());
                        messageStr = "";
                    }
                    CTRE.Watchdog.Feed();
                }
            }
            //Debug.Print("Value = ");
            //Debug.Print("Value = " + setpointData[0]);

            if (setpointData.Count > 1)
            {

                SetpointData leftsetdata = (SetpointData)setpointData[0];
                //Debug.Print("Value: " + testsetdata.getSetpoint() + "ID: " + testsetdata.getDeviceID());
                //Debug.Print("Value: " + testsetdata.getSetpoint() + " Device ID: " + testsetdata.getDeviceID());

                SetpointData rightsetdata = (SetpointData)setpointData[1];
                //Debug.Print("Value: " + testsetdata1.getSetpoint() + " Device ID: " + testsetdata1.getDeviceID());
                CTRE.Watchdog.Feed();
            }
            return setpointData;
        }

        private static void resetEncoderPositions(ArrayList talons)
        {
            //If any flags for encoder reset are true, reset the encoder 
            if (flags[RESET_DRIVE_ENC])
            {
                flags[RESET_DRIVE_ENC] = false;
                ((CTRE.TalonSrx)talons[LDRIVE_ID]).SetPosition(0.0F);
                ((CTRE.TalonSrx)talons[RDRIVE_ID]).SetPosition(0.0F);
                ((CTRE.TalonSrx)talons[LDRIVE_ID]).SetControlMode(CTRE.TalonSrx.
                    ControlMode.kPosition);
                ((CTRE.TalonSrx)talons[RDRIVE_ID]).SetControlMode(CTRE.TalonSrx.
                    ControlMode.kPosition);
                ((CTRE.TalonSrx)talons[LDRIVE_ID]).Set(0.0F);
                ((CTRE.TalonSrx)talons[RDRIVE_ID]).Set(0.0F);
            }
            if (flags[RESET_DEPTH_ENC])
            {
                flags[RESET_DEPTH_ENC] = false;
                ((CTRE.TalonSrx)talons[DEPTH_ID]).SetPosition(0.0F);
            }

            /*if (flags[RESET_TEST_ENC])
            {
                flags[RESET_TEST_ENC] = false;
                ((CTRE.TalonSrx)talons[0]).SetPosition(0.0F);
            }
            */
            if (flags[RESET_SCOOP_ENC])
            {
                flags[RESET_SCOOP_ENC] = false;
                ((CTRE.TalonSrx)talons[SCOOP_ID]).SetPosition(0.0F);
            }
            if (flags[RESET_WINCH_ENC])
            {
                flags[RESET_WINCH_ENC] = false;
                ((CTRE.TalonSrx)talons[WINCH_ID]).SetPosition(0.0F);
            }
        }

        private static void checkEncoderResetFlags(String msg)
        {
            //Check the message to see if any encoders need to be reset

            if (msg.Trim().Equals("<ResetDriveEncoders>"))
            {
                flags[RESET_DRIVE_ENC] = true;
            }
            if (msg.Trim().Equals("<ResetScoopEncoder>"))
            {
                flags[RESET_SCOOP_ENC] = true;
            }
            if (msg.Trim().Equals("<ResetDepthEncoder>"))
            {
                flags[RESET_DEPTH_ENC] = true;
            }
            if (msg.Trim().Equals("<ResetWinchEncoder>"))
            {
                flags[RESET_WINCH_ENC] = true;
            }
        }

        private static void writeUART(String messageStr)
        {
            //create the outbound message as a byte array
            //messageStr += '\n';
            byte[] outboundMessage = MakeByteArrayFromString(messageStr);
            //Debug.Print("SENDING THIS OVER UART: " + outboundMessage[0]+ " "+ outboundMessage.Length);
            //Send message if possible over uart
            if (uart.CanWrite)
            {
                uart.Write(outboundMessage, 0, outboundMessage.Length);
                Debug.Print("Wrote to UART");
            }
            CTRE.Watchdog.Feed();
        }


        //Creates byte array from string
        private static byte[] MakeByteArrayFromString(String msg)
        {
            byte[] retval = new byte[msg.Length];
            for (int i = 0; i < msg.Length; ++i)
                retval[i] = (byte)msg[i];
            CTRE.Watchdog.Feed();
            return retval;
        }

        //Takes in an arraylist of statusData and updates each motor's status based on the arraylist        
        private static void updateMotorStatusData(ArrayList statusData)
        {
            for (int i = 0; i < statusData.Count; i++)
            {
                CTRE.Watchdog.Feed();
                ((StatusData)statusData[i]).updateStatusData();
            }
        }

        //Goes through each motors' status data and creates a string with each status data 
        private static String makeOutboundMessage(ArrayList statusData)
        {
            String outboundMessage = "";
            for (int i = 0; i < statusData.Count; i++)
            {
                outboundMessage += ((StatusData)statusData[i]).getOutboundMessage();
                CTRE.Watchdog.Feed();
            }
            outboundMessage += "\r";
            return outboundMessage;
        }

        //I think this is where the magic happens and the motors move
        private static void processInboundData(ArrayList setpointDataList, ArrayList talons)
        {
            //For each setpointData, for the talon that matches it set each mode and setpoint based on the list information 
            for (int i = 0; i < setpointDataList.Count; i++)
            {
                try
                {
                    SetpointData setpointData = (SetpointData)setpointDataList[i];
                    float setpointVal = (float)(setpointData.getSetpoint());

                    CTRE.TalonSrx talon = (CTRE.TalonSrx)talons[i];
                    if (talon.GetControlMode() != setpointData.getMode())
                    {
                        talon.SetControlMode(setpointData.getMode());
                        //Debug.Print(setpointData.getMode().ToString());
                    }
                    if (talon.GetSetpoint() != setpointVal)
                    {
                        talon.Set(setpointVal);
                        Debug.Print("Setting it to value = " + setpointVal.ToString());
                        //Debug.Print(setpointVal.ToString());
                    }
                }
                catch (ArgumentOutOfRangeException ex)
                {
                    Debug.Print(ex.ToString());
                }
            }
        }
    }
}