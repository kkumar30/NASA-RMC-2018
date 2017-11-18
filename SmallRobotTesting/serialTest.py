import serial
import time

port=serial.Serial("/dev/ttyUSB0",9600)

"""
Commands -
0- Stop/Pause
1- Forward
2- Backward
"""


def main():
    while True:
         send = raw_input("Enter in what you want to send:" )
         if send in ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]:
             print(ord(send))
             port.write(send)
         else:
             print("Invalid Value Entered.")

if __name__ == '__main__':
    main()
