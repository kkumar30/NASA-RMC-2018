using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GameControllerInterface
{
    class CommandMessage
    {

        /*
         * Message Format:
         * 
         * |-------|-------|-------|-------|-------|-------|-------+-------|-------+------- // |-------
         *  start   msg no. sender  recver  m.type  no.data data_0          data_1              end
         *  
         * start    = byte
         * msg.no   = byte
         * sender   = byte
         * recver   = byte
         * m.type   = bye
         * no.data  = byte
         * data_N   = uint16
         * end      = byte
         */

        static byte message_number = 0;
        byte start_byte = 0x55;
        byte sender_byte = 0x00;
        byte receiver_byte = 0x01;
        byte end_byte = 0xAA;



        public CommandMessage()
        {
            
        }

        public byte[] formMessage(byte msg_type, UInt16[] data, byte data_size)
        {
            //number of bytes in message (7 for headers/end byte, remaining for data)
            uint byte_count = (uint)(7 + data_size * sizeof(UInt16));

            //allocate memory for the message
            byte[] msg = new byte[byte_count];

            //count the size of the message as its made
            int i = 0;

            //construct the message header
            msg[i++] = start_byte;          
            msg[i++] = message_number++;
            msg[i++] = sender_byte;
            msg[i++] = receiver_byte;
            msg[i++] = msg_type;
            msg[i++] = data_size;

            
            //construct the message's payload
            for(int j = 0; j < data_size; j++)
            {
                //parse the bits from the unsigned int into an array of bytes
                byte[] uint16_bytes = BitConverter.GetBytes(data[j]);
                for (int k = 0; k < uint16_bytes.Length; k++)
                {
                    //get the bits in the correct order and add these bytes to the message
                    msg[i++] = uint16_bytes[sizeof(UInt16) - k - 1];
                }
            }

            //construct the message footer
            msg[i++] = end_byte;
            
            //error in message generation, here's a debug print
            if(i != byte_count)
            {
                Console.WriteLine("Expected a message of size %d, but got a message of size %d", (int)byte_count, i);
            }
            //no issue generating message, show the message
            else
            {
                Console.WriteLine("Generated the Following Message");
                for(i = 0; i < byte_count - 1; i++)
                {
                    Console.Write("{0}:", msg[i]);
                }
                Console.WriteLine("{0}", msg[i]);
            }

            return msg;
        }

    }
}
