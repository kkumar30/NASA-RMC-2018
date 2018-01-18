using System;
using Microsoft.SPOT;
using System;
using System.Collections;
using System.Text.RegularExpressions;

namespace Ibex_Motor_Control
{
    class MessageParser
    {
        public MessageParser()
        {

        }

        //Parses a string and returns an ArrayList of SetpointData
        public static ArrayList parseMessage(String msg)
        {
            ArrayList controlData = new ArrayList();
            String pattern = @"<([0-9]+):([0-9]+):([-0-9]+\.[0-9]+)>";
            MatchCollection mc = Regex.Matches(msg, pattern);

            foreach(Match m in mc)
            {
                String data = m.Value;
                data = data.Substring(1, data.Length - 2);
                String[] subparts = data.Split(':');
                int deviceID = Int32.Parse(subparts[0]);
                int mode = Int32.Parse(subparts[1]);
                Double setpoint = Double.Parse(subparts[2]);
                controlData.Add(new SetpointData(deviceID, mode, setpoint));
            }

            return controlData;
        }
    }
}

