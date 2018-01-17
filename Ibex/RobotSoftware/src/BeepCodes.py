import os


def happy1():
    os.system("(beep -f 1319 && beep -f 1047 && beep -f 1319 && beep -f 1568 && beep -f 2093)&")


def heartbeat():
    os.system("(beep -f 2000 -r 2 -d 25 -l 50)&")

def error():
    os.system("(beep -f 440 -r 5 -d 500)&")