using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using SlimDX.DirectInput;
using System.Net.Sockets;
using System.Net;
using GameControllerInterface.ActionQueueItems;

namespace GameControllerInterface
{
    public partial class Form1 : Form
    {

        ActionQueueMaster actionQueueMaster = new ActionQueueMaster();
        DirectInput Input = new DirectInput();
        SlimDX.DirectInput.Joystick stick;
        Joystick[] sticks;

        UdpClient udpClient = new UdpClient(11001);

        ControllerValues controllerValues = new ControllerValues();

        public Form1()
        {
            InitializeComponent();
            timer1.Interval = 200; 
            timer1.Enabled = true;      
        }
        
        
        private void btn_drive_setpoints_Click(object sender, EventArgs e)
        {
            int left_drive, right_drive, time;
            int num_violations = 0;
            try
            {
                left_drive = Int32.Parse(tbox_new_left_drive_setpoint.Text);
                right_drive = Int32.Parse(tbox_new_right_drive_setpoint.Text);
                time = Int32.Parse(tbox_aq_drive_time.Text);
                ActionQueueDrive action = new ActionQueueDrive(left_drive, right_drive, time);

                num_violations = action.checkValuesInBounds();
                if(num_violations > 0)
                {
                    tbox_status.AppendText(String.Format("{0} of the entered values were out the valid range. Each offending value was reduced to the closest valid value\n", num_violations));
                }

                actionQueueMaster.addNewAction(action);
                lbox_action_queue_list.Items.Add(action.getDescription());
                tbox_status.AppendText(String.Format("The requested drive action was added successfuly\n"));

            }
            catch
            {
                tbox_status.AppendText("One or more of the drive motor fields were left blank, which would have resulted in an invalid action item being created. As such, no entry was made.\n");
            }
               
        }

        //clicking the start queue button should immediately start the top item in the queue
        private void btn_start_queue_Click(object sender, EventArgs e)
        {
            //obviously we can't do anything if there are no items in the queue
            if(actionQueueMaster.anyActions())
            {
                tbox_status.AppendText("====EXECUTION STARTED====\n");
                //set a timer to expire at this actions duration (to trigger the next action)
                timer_action_queue.Interval = actionQueueMaster.getCurrentActionTime();

                //tell the user what's going on via the status box
                tbox_status.AppendText(actionQueueMaster.printTopActionStatus());

                //this is the message that will be sent to the robot
                byte[] outbound_message = actionQueueMaster.processTopAction();

                //send the message over UDP
                try
                {
                    udpClient.Connect("192.168.0.108", 11001);
                    Byte[] msg = outbound_message;
                    udpClient.Send(msg, msg.Length);
                }
                catch { }

                //remove this item from the queue list and start the timer for the next action
                lbox_action_queue_list.Items.RemoveAt(0);
                timer_action_queue.Enabled = true;
            }
            else
            {
                tbox_status.AppendText("There were no items in the action queue to run.\n");
            }
        }

        private void timer_action_queue_Tick(object sender, EventArgs e)
        {
            if (actionQueueMaster.anyActions())
            {
                //update the status box for the user
                tbox_status.AppendText(actionQueueMaster.printTopActionStatus());

                //generate the outbound message
                byte[] outbound_message = actionQueueMaster.processTopAction();

                //send the message over UDP
                try
                {
                    udpClient.Connect("192.168.0.108", 11001);
                    Byte[] msg = outbound_message;
                    udpClient.Send(msg, msg.Length);
                }
                catch { }
                lbox_action_queue_list.Items.RemoveAt(0);

                if (actionQueueMaster.anyActions())
                {
                    //reset the timer for the next action
                    timer_action_queue.Interval = actionQueueMaster.getCurrentActionTime();
                    timer_action_queue.Enabled = true;
                }

                
            }
            else
            {
                tbox_status.AppendText("====EXECUTION FINISHED====\n");
                timer_action_queue.Interval = 1;
                timer_action_queue.Enabled = false;
            }
        }
    }
}
