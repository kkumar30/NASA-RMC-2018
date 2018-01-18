import RobotState
from Constants import LOGGER

class NetworkMessage():
    
    def __init__(self, msg_str):
        self.type = "None"
        self.messageNumber = -1
        self.messageData = []
        self.parseMessage(msg_str)
        
        
    def parseMessage(self, msg_str):
        try:
            msg_str = msg_str.strip("\< \>\n\r")
            msg_parts1 = msg_str.split("|")
            self.type = RobotState.STATES[int(msg_parts1[0])]
            msg_parts2 = msg_parts1[1].split(":")
            self.messageNumber = int(msg_parts2[0])
            for i in range(1, len(msg_parts2)):
                self.messageData.append(float(msg_parts2[i]))
                
        #Some error occurred in parsing the message - probably malformed.
        except:
	    LOGGER.Moderate("Error on message:" + msg_str)
            pass
        
    def printMessage(self):
        print "Type: " + str(self.type)
        print "MessageNumber: " + str(self.messageNumber)
        for i in range(len(self.messageData)):
            print "Data " + str(i) + ": " + str(self.messageData[i])
        
if __name__ == "__main__":
    m1 = NetworkMessage("<1|0:5.0:0.75>\n\n")
    m1.printMessage()
    
        