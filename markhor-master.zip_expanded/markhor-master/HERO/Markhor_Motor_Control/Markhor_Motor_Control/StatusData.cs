using System;

namespace Markhor_Motor_Control
{
    class StatusData
    {   
        private int talonDeviceID;          //fixed device id
        private float talonCurrent;           //current in mA
        private float talonTemperature;       //temp in milli-degC
        private float talonVoltage;           //voltage in mV
        private float talonPosition;          //position in encoder ticks
        private float talonSpeed;             //encoder ticks/100ms
        private float talonSetpoint;          //setpoint based on control mode

        private CTRE.TalonSrx.ControlMode controlMode;    //talon control mode

        private int talonForwardLimitReached;
        private int talonReverseLimitReached;
        

        private CTRE.TalonSrx talon;

        public StatusData(int talonDeviceID, CTRE.TalonSrx talon)
        {
            this.talonDeviceID = talonDeviceID;
            this.talon = talon;
        }

        public void updateStatusData()
        {
            talonCurrent = (talon.GetOutputCurrent());          
            talonTemperature = (talon.GetTemperature());          
            talonVoltage = (talon.GetOutputVoltage());         
            talonSpeed = (talon.GetSpeed());                           
            talonPosition = (talon.GetPosition());
            talonSetpoint = (talon.GetSetpoint());                     
            talonForwardLimitReached = talon.IsFwdLimitSwitchClosed() ? 1 : 0;
            talonReverseLimitReached = talon.IsRevLimitSwitchClosed() ? 1 : 0;
            controlMode = talon.GetControlMode();
        }

        public String getOutboundMessage()
        {
            String outboundMessage = "<";

            outboundMessage += talonDeviceID.ToString() + ":";      //0
            outboundMessage += talonCurrent.ToString() + ":";       //1
            outboundMessage += talonTemperature.ToString() + ":";   //2
            outboundMessage += talonVoltage.ToString() + ":";       //3
            outboundMessage += talonSpeed.ToString() + ":";         //4
            outboundMessage += talonPosition.ToString() + ":";      //5
            outboundMessage += talonSetpoint.ToString() + ":";      //6

            outboundMessage += controlMode.ToString() + ":";        //7

            outboundMessage += talonForwardLimitReached.ToString() + ":";   //8
            outboundMessage += talonReverseLimitReached.ToString();         //9

            outboundMessage += ">";
            return outboundMessage;
        }

        public String getOutboundMessage2()
        {
            return "<--->";
        }
    }
}
