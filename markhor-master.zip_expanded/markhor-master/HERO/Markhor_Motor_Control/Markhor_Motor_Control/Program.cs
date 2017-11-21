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

namespace Markhor_Motor_Control
{
    public class Program
    {
        private const int RESET_DRIVE_ENC = 0;
        private const int RESET_DEPTH_ENC = 1;
        private const int RESET_SCOOP_ENC = 2;
        private const int RESET_WINCH_ENC = 3;

        private const int LDRIVE_ID = 0;
        private const int RDRIVE_ID = 1;
        private const int SCOOP_ID = 2;
        private const int DEPTH_ID = 3;
        private const int WINCH_ID = 4;

        static bool [] flags = { false, false, false, false };

        static System.IO.Ports.SerialPort uart;
        static byte[] rx = new byte[1024];

        public static void Main()
        {
            CTRE.TalonSrx leftMotor = new CTRE.TalonSrx(1);
            CTRE.TalonSrx rightMotor = new CTRE.TalonSrx(2);
            CTRE.TalonSrx scoopMotor = new CTRE.TalonSrx(3);
            CTRE.TalonSrx depthMotor = new CTRE.TalonSrx(4);
            CTRE.TalonSrx winchMotor = new CTRE.TalonSrx(5);

            leftMotor.SetFeedbackDevice(CTRE.TalonSrx.FeedbackDevice.QuadEncoder);
            rightMotor.SetFeedbackDevice(CTRE.TalonSrx.FeedbackDevice.QuadEncoder);
            scoopMotor.SetFeedbackDevice(CTRE.TalonSrx.FeedbackDevice.QuadEncoder);
            depthMotor.SetFeedbackDevice(CTRE.TalonSrx.FeedbackDevice.QuadEncoder);
            winchMotor.SetFeedbackDevice(CTRE.TalonSrx.FeedbackDevice.QuadEncoder);

            leftMotor.SetSensorDirection(false);
            rightMotor.SetSensorDirection(true);
            scoopMotor.SetSensorDirection(false);
            depthMotor.SetSensorDirection(false);
            winchMotor.SetSensorDirection(false);

            leftMotor.ConfigEncoderCodesPerRev(80);
            rightMotor.ConfigEncoderCodesPerRev(80);
            scoopMotor.ConfigEncoderCodesPerRev(80);
            depthMotor.ConfigEncoderCodesPerRev(80);
            winchMotor.ConfigEncoderCodesPerRev(80);

            leftMotor.SetP(0, 0.35F);
            leftMotor.SetI(0, 0.0F);
            leftMotor.SetD(0, 0.0F);
            leftMotor.SetF(0, 0.0F);
            leftMotor.SelectProfileSlot(0);

            rightMotor.SetP(0, 0.35F);
            rightMotor.SetI(0, 0.0F);
            rightMotor.SetD(0, 0.0F);
            rightMotor.SetF(0, 0.0F);
            rightMotor.SelectProfileSlot(0);

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

            leftMotor.ConfigNominalOutputVoltage(+0.0F, -0.0F);
            rightMotor.ConfigNominalOutputVoltage(+0.0F, -0.0F);
            scoopMotor.ConfigNominalOutputVoltage(+0.0F, -0.0F);
            depthMotor.ConfigNominalOutputVoltage(+0.0F, -0.0F);
            winchMotor.ConfigNominalOutputVoltage(+0.0F, -0.0F);

            leftMotor.SetAllowableClosedLoopErr(0, 0);
            rightMotor.SetAllowableClosedLoopErr(0, 0);
            scoopMotor.SetAllowableClosedLoopErr(0, 0);
            depthMotor.SetAllowableClosedLoopErr(0, 0);
            winchMotor.SetAllowableClosedLoopErr(0, 0);

            leftMotor.SetPosition(0);
            rightMotor.SetPosition(0);
            scoopMotor.SetPosition(0);
            depthMotor.SetPosition(0);
            winchMotor.SetPosition(0);

            leftMotor.SetVoltageRampRate(0);
            rightMotor.SetVoltageRampRate(0);
            scoopMotor.SetVoltageRampRate(0);
            depthMotor.SetVoltageRampRate(0);
            winchMotor.SetVoltageRampRate(0);

            ArrayList motorSetpointData = new ArrayList();
            ArrayList motorStatusData = new ArrayList();
            ArrayList talons = new ArrayList();

            talons.Add(leftMotor);
            talons.Add(rightMotor);
            talons.Add(scoopMotor);
            talons.Add(depthMotor);
            talons.Add(winchMotor);

            String inboundMessageStr = "";
            String outboundMessageStr = "";

            SetpointData leftMotorSetpointData = new SetpointData(1, 0, 0.0F);
            SetpointData rightMotorSetpointData = new SetpointData(2, 0, 0.0F);
            SetpointData scoopMotorSetpointData = new SetpointData(3, 0, 0.0F);
            SetpointData depthMotorSetpointData = new SetpointData(4, 0, 0.0F);
            SetpointData winchMotorSetpointData = new SetpointData(5, 0, 0.0F);

            StatusData leftMotorStatusData = new StatusData(1, leftMotor);
            StatusData rightMotorStatusData = new StatusData(2, rightMotor);
            StatusData scoopMotorStatusData = new StatusData(3, scoopMotor);
            StatusData depthMotorStatusData = new StatusData(4, depthMotor);
            StatusData winchMotorStatusData = new StatusData(5, winchMotor);

            motorSetpointData.Add(leftMotorSetpointData);
            motorSetpointData.Add(rightMotorSetpointData);
            motorSetpointData.Add(scoopMotorSetpointData);
            motorSetpointData.Add(depthMotorSetpointData);
            motorSetpointData.Add(winchMotorSetpointData);

            motorStatusData.Add(leftMotorStatusData);
            motorStatusData.Add(rightMotorStatusData);
            motorStatusData.Add(scoopMotorStatusData);
            motorStatusData.Add(depthMotorStatusData);
            motorStatusData.Add(winchMotorStatusData);

            CTRE.Watchdog.Feed();

            uart = new System.IO.Ports.SerialPort(CTRE.HERO.IO.Port1.UART, 115200);
            uart.Open();
            

            while (true)
            {
                //read whatever is available from the UART into the inboundMessageStr
                motorSetpointData = readUART(ref inboundMessageStr);
                CTRE.Watchdog.Feed();
                //if any of the talon positions need to be reset, this will reset them
                resetEncoderPositions(talons);
                CTRE.Watchdog.Feed();
                //attempt to process whatever was contained in the most recent message
                processInboundData(motorSetpointData, talons);
                CTRE.Watchdog.Feed();
                //get a bunch of data from the motors in their current states
                updateMotorStatusData(motorStatusData);
                CTRE.Watchdog.Feed();
                //package that motor data into a formatted message
                outboundMessageStr = makeOutboundMessage(motorStatusData);
                CTRE.Watchdog.Feed();
                //send that message back to the main CPU
                writeUART(outboundMessageStr);

                //Debug.Print("set=" + winchMotor.GetSetpoint().ToString() + " mod=" + winchMotor.GetControlMode().ToString() +
                //            "pos=" + winchMotor.GetPosition().ToString() + " vel=" + winchMotor.GetSpeed().ToString() + 
                //            "err=" + winchMotor.GetClosedLoopError().ToString() + "vlt=" + winchMotor.GetOutputVoltage());
                //Debug.Print(DateTime.Now.ToString());
                CTRE.Watchdog.Feed();
                //keep the loop timing consistent //TODO: evaluate if this is necessary
                //System.Threading.Thread.Sleep(10);
            }
        }

        private static ArrayList readUART(ref String messageStr)
        {
            ArrayList setpointData = new ArrayList();
            if (uart.BytesToRead > 0)
            {      
                int readCnt = uart.Read(rx, 0, 1024);
                for (int i = 0; i < readCnt; ++i)
                {
                    messageStr += (char)rx[i];
                    if ((char)rx[i] == '\n')
                    {
                        checkEncoderResetFlags(messageStr);
                        setpointData = MessageParser.parseMessage(messageStr);
                        messageStr = "";
                    }
                }
            }
            return setpointData;
        }

        private static void resetEncoderPositions(ArrayList talons)
        {
            if(flags[RESET_DRIVE_ENC])
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
            if(msg.Trim().Equals("<ResetDriveEncoders>"))
            {
                flags[RESET_DRIVE_ENC] = true;
            }
            if(msg.Trim().Equals("<ResetScoopEncoder>"))
            {
                flags[RESET_SCOOP_ENC] = true;
            }
            if(msg.Trim().Equals("<ResetDepthEncoder>"))
            {
                flags[RESET_DEPTH_ENC] = true;
            }
            if(msg.Trim().Equals("<ResetWinchEncoder>"))
            {
                flags[RESET_WINCH_ENC] = true;
            }
        }

        private static void writeUART(String messageStr)
        {
            byte[] outboundMessage = MakeByteArrayFromString(messageStr);
            if (uart.CanWrite)
            {
                uart.Write(outboundMessage, 0, outboundMessage.Length);
            }
        }

        private static byte[] MakeByteArrayFromString(String msg)
        {
            byte[] retval = new byte[msg.Length];
            for (int i = 0; i < msg.Length; ++i)
                retval[i] = (byte)msg[i];
            return retval;
        }

        private static void updateMotorStatusData(ArrayList statusData)
        {
            for(int i = 0; i < statusData.Count; i++)
            {
                ((StatusData)statusData[i]).updateStatusData();
            }
        }

        private static String makeOutboundMessage(ArrayList statusData)
        {
            String outboundMessage = "";
            for(int i = 0; i < statusData.Count; i++)
            {
                outboundMessage += ((StatusData)statusData[i]).getOutboundMessage();
            }
            outboundMessage += "\n\r";
            return outboundMessage;
        }

        private static void processInboundData(ArrayList setpointDataList, ArrayList talons)
        {
            for(int i = 0; i < setpointDataList.Count; i++)
            {
                SetpointData setpointData = (SetpointData)setpointDataList[i];
                float setpointVal = (float)(setpointData.getSetpoint());
                CTRE.TalonSrx talon = (CTRE.TalonSrx)talons[i];
                if(talon.GetControlMode() != setpointData.getMode())
                {
                    talon.SetControlMode(setpointData.getMode());
                    Debug.Print(setpointData.getMode().ToString());
                }
                if(talon.GetSetpoint() != setpointVal)
                {
                    talon.Set(setpointVal);
                    Debug.Print(setpointVal.ToString());
                }  
            }
            

        }
    }  
}
