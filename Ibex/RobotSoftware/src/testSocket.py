from NetworkClient import NetworkClient
from MessageQueue import MessageQueue
import Constants as CONSTANTS
from Constants import LOGGER

CONTROL_STATION_IP = "130.215.122.163"

networkClient = NetworkClient(CONTROL_STATION_IP, 11000)
inboundMessageQueue = MessageQueue()
networkClient.setInboundMessageQueue(inboundMessageQueue)
outboundMessageQueue = MessageQueue()
lastReceivedMessageNumber = -1
currentReceivedMessageNumber = -1
stateStartTime = -1

while True:
    if CONSTANTS.USING_NETWORK_COMM:
        connected = False
        while(not connected):
            try:
                if(outboundMessageQueue.isEmpty()):
                    networkClient.send("Hiiiii\n\r")
                else:
                    networkClient.send(outboundMessageQueue.getNext())

                LOGGER.Debug("Connected to socket")
                connected = True
            except Exception as e:
                # print e
                LOGGER.Debug("Could not connect to network, attempting to reconnect...")
                # print (connected)

    if CONSTANTS.USING_NETWORK_COMM and connected:
        if(not inboundMessageQueue.isEmpty()):
    		currentMessage = inboundMessageQueue.getNext()
    		currentMessage.printMessage()
    		lastReceivedMessageNumber = currentReceivedMessageNumber
    		currentReceivedMessageNumber = currentMessage.messageNumber
