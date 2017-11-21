import time
import sys

HEADER = '\033[95m'
OKBLUE = '\033[94m'
OKGREEN = '\033[92m'
WARNING = '\033[93m'
FAIL = '\033[91m'
ENDC = '\033[0m'
BOLD = '\033[1m'
UNDERLINE = '\033[4m'

class Logger:

    def __init__(self, level=5):
	self.setLogLevel(level)


    def Critical(self, msg):
	if(self.loggerLevel >= 1):
	    self.log(FAIL + BOLD + UNDERLINE + msg)

    def Severe(self, msg):
	if(self.loggerLevel >= 2):
	    self.log(HEADER + BOLD + msg)

    def Moderate(self, msg):
	if(self.loggerLevel >= 3):
	    self.log(WARNING + BOLD + msg)

    def Low(self, msg):
	if(self.loggerLevel >= 4):
	    self.log(OKBLUE + msg)

    def Debug(self, msg):
	if(self.loggerLevel >= 5):
	    self.log(OKGREEN + msg)

    def setLogLevel(self, level):
	self.loggerLevel = level

    def log(self, msg):
	print time.strftime("%H:%M:%S", time.gmtime()) + "---" + msg + ENDC
	sys.stdout.flush()

if __name__ == "__main__":
    logger = Logger(5)

    logger.Debug("Hello World")
    logger.Low("Test log should display")

    logger.setLogLevel(3)
    time.sleep(2)
    logger.Debug("This shouldnt appear")

    logger.Moderate("This should appear")
    
