class SensorHandler:

	def __init__(self):
		self.sensors = []
		self.servos = []

	def addSensor(self, sensor):
		self.sensors.append(sensor)

	def addServo(self, servo):
		self.servos.append(servo)

	def updateSensors(self, update_msg):
		for sensor in self.sensors:
			sensor.update(update_msg)

	def printSensorValues(self):
		print self.getSensorValues()
	
	def getServoStateMessage(self):
		message = "<"
		for i in range(len(self.servos)):
			message += str(self.servos[i].getSetpoint())
			if(i != len(self.servos) - 1):
				message += ":"
		message += ">"
		return message

	def getSensorValues(self):
		sensorMsg = ""
		for sensor in self.sensors:
			sensorMsg += str(sensor.getValue()) + '|';
		return sensorMsg
