using System;
using System.Text;
using Microsoft.SPOT;

namespace Ibex_Motor_Control
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

    }
}
