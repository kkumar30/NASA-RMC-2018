import time

class MotorHandler:

	def __init__(self):
		self.motors = []

	def addMotor(self, motor):
		self.motors.append(motor)

	def updateMotors(self, update_msg):
		for motor in self.motors:
			motor.update(update_msg)

	#Get status message of each motor
	def getMotorStateMessage(self):
		msg = ""
		for motor in self.motors:
			msg += motor.getStateMessage()
		msg += "\n\r"
		return msg

	def getMotorNetworkMessage(self):
		msg = ""
		for motor in self.motors:
			msg += motor.getNetworkMessage()
		return msg
