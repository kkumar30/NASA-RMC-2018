class DataMessage:
    
    def __init__(self, motorHandler, sensorHandler):
        self.message = ""
        self.motorHandler = motorHandler
        self.sensorHandler = sensorHandler
        self.addMotorHandlerDataToMessage()
        self.addSensorHandlerDataToMessage()
    
    def addMotorHandlerDataToMessage(self):
        self.message += self.motorHandler.getMotorDataMessage()
    
    def addSensorHandlerDataToMessage(self):
        self.message += self.sensorHandler.getSensorDataMessage()
        
    def addStateFinished(self):
        self.message += "<Finished>"
        

    def clear(self):
        self.message = ""

    def getMessage(self):
	return self.message    