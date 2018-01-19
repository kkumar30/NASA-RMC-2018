import serial
import time
connected = False
ser = serial.Serial('/dev/ttyUSB0', 9600, timeout=1)
# time.sleep(2)
# ser = serial.Serial('/dev/ttyACM1', 9600)
# ser.write('0')
ser.write('0')
connected=False
while True:
        ser.write('2')
        time.sleep(1)
        print ser.read()
