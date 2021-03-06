import serial
import time
import sys

from Constants import LOGGER
from threading import Lock
class SerialHandler():

    def __init__(self, port):
        self.ser = serial.Serial(timeout=1)
        self.ser.baudrate = 115200
        self.ser.port = port
        self.inbound_buffer = bytearray()
        self.msg = ""

    def initSerial(self):
        ser_port_str = str(self.ser.port)
        print "Attempting to open serial port: " + ser_port_str
        for i in range(3):
            try:
                self.ser.open()
                print "SUCCESS: Serial port opened on port: " + ser_port_str
                return;
            except:
                print "FAILED: Open serial on port: " + ser_port_str
                if(i != 2):
                    print "Retry in 5 seconds"
                time.sleep(5)

        print "CRITICAL FAILURE: Unable to open serial on port: " + ser_port_str                

        self.ser.setDTR(False)

        time.sleep(0.1)
        sys.exit("Could not begin serial communications with SensorBoard")

    def sendMessage(self, msg):
        #print "HERE"
        #if self.ser.isOpen():
        #print self.ser.isOpen()
        self.ser.write(msg.encode())
#        LOGGER.Debug("Sent:" + msg)

    def _readline(self):
        eol = b'\r'
        leneol = len(eol)
        #line = bytearray()
        line = ''
        while True:
            c = self.ser.read(1)
            if c:
                line += c
#                LOGGER.Debug(str(line))
                #if line[-leneol:] == eol:
                if c == eol:
                    break
            else:
                break
#        LOGGER.Debug(str(line))
        return bytes(line)

    def getMessage(self):
        #newMsg = ""
        #eol = b'\r'
        #leneol = len(eol)
      #  self.ser.flushInput()
      
        if(self.ser.isOpen()):
            #LOGGER.Debug("Num of bytes in buffer "+str(self.ser.inWaiting()))
            #line = self.ser.read(96) 
            line = self._readline()
            #for i in range(2):
            #   c = self.ser.read(1)
            #   if c:
            #       self.inbound_buffer += c
            #       if self.inbound_buffer[-leneol:] == eol:
            #           newMsg = str(self.inbound_buffer)
            #           self.inbound_buffer = bytearray()
        #LOGGER.Debug(str(line))
        return str(line)
        #return newMsg

if __name__ == "__main__":
    sh = SerialHandler('COM3')
