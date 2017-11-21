using System;
using Microsoft.SPOT;
using System;
using System.Collections;
using System.Text.RegularExpressions;

namespace Markhor_Motor_Control
{
    class MessageParser
    {
        public MessageParser()
        {

        }

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


            /*
            try
            {
                String[] msg_parts = msg.Split('|');
                //msg format: <DEVICE_ID:MODE:SETPOINT>
                foreach (String part in msg_parts)
                {
                    try
                    {
                        //check the end delimiters for the full message
                        if (part[0].Equals('<') && part[part.Length - 1].Equals('>'))
                        {
                            String[] subparts = part.Split(':');
                            int deviceID = Int32.Parse(subparts[0]);
                            int mode = Int32.Parse(subparts[1]);
                            int setpoint = Int32.Parse(subparts[2]);
                            controlData.Add(new SetpointData(deviceID, mode, setpoint));
                        }
                    }
                    catch { }
                }

            }
            catch
            {
                return new ArrayList();
            }
            */

            return controlData;
        }
    }
}

