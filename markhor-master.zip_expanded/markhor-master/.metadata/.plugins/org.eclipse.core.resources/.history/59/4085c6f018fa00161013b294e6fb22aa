import time
import can

class MotorHandler:

	def __init__(self):
		self.motors = []

	def addMotor(self, motor):
		self.motors.append(motor)

	def updateMotors(self, update_msg):
		for motor in self.motors:
			motor.update(update_msg)

	def getMotorStateMessage(self):
		msg = ""
		for motor in self.motors:
			msg += motor.getStateMessage()
			msg += "\n"
		return msg
