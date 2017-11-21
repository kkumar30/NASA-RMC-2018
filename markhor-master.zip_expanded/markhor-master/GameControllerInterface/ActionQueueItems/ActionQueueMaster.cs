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
using System.Collections;

namespace GameControllerInterface.ActionQueueItems
{
    
    class ActionQueueMaster
    {
        private ArrayList actionQueue;


        private int current_action_time;


        public ActionQueueMaster()
        {
            actionQueue = new ArrayList();
        }

        //return true if the queue isnt empty
        public bool anyActions()
        {
            return actionQueue.Count != 0;
        }

        //get the dialouge associated with the top message in the queue,
        //so it can be written to the status box
        public String printTopActionStatus()
        {
            String msg = "No action to process";
            if (actionQueue.Count != 0)
            {
                //generate the dialouge for the item on the action queue
                msg = "Started" + ((IActionQueue)actionQueue[0]).getActionType() + "Action.\n";
            }

            return msg;
        }

        //get the outgoing message associated with the top item in the queue,
        //so it can be sent to the target device
        public byte[] processTopAction()
        {
            if(actionQueue.Count != 0)
            {
                //generate the first message for the item on the action queue
                byte [] current_message = ((IActionQueue)actionQueue[0]).generateMessage();

                //once we're done with item, remove it from the list
                actionQueue.RemoveAt(0);

                return current_message;
            }

            //only will be called if there are no messages in the queue
            return null;
        }

        //add a new action to the queue
        public void addNewAction(IActionQueue action)
        {
            actionQueue.Add(action);
        }
        
        //get the time/duration associated with the first item in the queue
        public int getCurrentActionTime()
        {
            if(this.anyActions())
            {
                return ((IActionQueue)actionQueue[0]).getTime();
            }
            else
            {
                return 0;
            }
        }
    }
}
