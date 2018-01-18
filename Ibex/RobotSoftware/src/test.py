import serial

port = serial.Serial("/dev/arduino", baudrate=115200, timeout=3.0)
