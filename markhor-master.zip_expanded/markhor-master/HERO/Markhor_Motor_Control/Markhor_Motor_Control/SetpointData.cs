using System;
using System.Text;
using Microsoft.SPOT;

namespace Markhor_Motor_Control
{
    class SetpointData
    {
        private int deviceID;
        private CTRE.TalonSrx.ControlMode mode;
        private Double setpoint;
        //private float setpoint_converted;
        
        public SetpointData(int deviceID, int mode, Double setpoint)
        {
            this.deviceID = deviceID;
            this.mode = (CTRE.TalonSrx.ControlMode)mode;
            this.setpoint = setpoint;
           // setpoint_converted = convertSetpoint();
        }

        public int getDeviceID() { return deviceID; }
        public CTRE.TalonSrx.ControlMode getMode() { return mode; }
        public Double getSetpoint() { return setpoint; }
        //public float getConvertedSetpoint() { return setpoint_converted; }
        


//        private float convertSetpointToVbus()
//        {
//            return (float)(setpoint / 1000.0);
//        }

//        private float convertSetpointToCurrent()
//        {
//            return (float)(setpoint / 1000.0);
//        }

//        private float convertSetpoint()
//        {
//            switch(mode)
//            {
//                case CTRE.TalonSrx.ControlMode.kPercentVbus:
//                    return convertSetpointToVbus();
//                case CTRE.TalonSrx.ControlMode.kCurrent:
//                    return convertSetpointToCurrent();
//                default:
//                    return (float)0.0;
//            }
//        }


    }
}
