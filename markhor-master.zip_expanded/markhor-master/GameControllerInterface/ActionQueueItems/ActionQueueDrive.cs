using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GameControllerInterface.ActionQueueItems
{
    class ActionQueueDrive : IActionQueue
    {
        //items to be sent over message
        private int left_drive_setpoint;
        private int right_drive_setpoint;
        private int time;

        //message info (ie type)
        private const byte message_id = 0x10;
        private const byte data_size = 3;

        //internal limits for values
        private const int DRIVE_STOP = 90;
        private const int DRIVE_MIN = 0;
        private const int DRIVE_MAX = 180;

        //description of action
        private String desc;

        public ActionQueueDrive(int left_drive_setpoint, int right_drive_setpoint, int time)
        {
            this.left_drive_setpoint = left_drive_setpoint;
            this.right_drive_setpoint = right_drive_setpoint;
            this.time = time;

            if(left_drive_setpoint == DRIVE_STOP && right_drive_setpoint == DRIVE_STOP)
            {
                this.desc = String.Format("Drive Motors Stopped for {0}ms.", this.time);
            }
            else 
            {
                this.desc = String.Format("Driving for {0}ms.", this.time);
            }
        }

        public String getDescription()
        {
            return this.desc;
        }

        public int getTime()
        {
            return this.time;
        }

        public String getActionType()
        {
            return "Drive";
        }

        public int checkValuesInBounds()
        {
            int num_violations = 0;

            if (left_drive_setpoint > DRIVE_MAX) { num_violations += 1; left_drive_setpoint = DRIVE_MAX; }
            if (left_drive_setpoint < DRIVE_MIN) { num_violations += 1; left_drive_setpoint = DRIVE_MIN; }

            if (right_drive_setpoint > DRIVE_MAX) { num_violations += 1; right_drive_setpoint = DRIVE_MAX; }
            if (right_drive_setpoint < DRIVE_MIN) { num_violations += 1; right_drive_setpoint = DRIVE_MIN; }

            if (time < 1) { num_violations += 1; time = 1; }

            return num_violations;
        }

        public byte[] generateMessage()
        {
            UInt16[] data = { (UInt16)left_drive_setpoint, (UInt16)right_drive_setpoint, (UInt16)time };
            CommandMessage msg = new CommandMessage();
            return msg.formMessage(message_id, data, data_size);
        }

    }


}
