class Servo:

    def __init__(self):
	self.setpoint = 0

    def getSetpoint(self):
	return self.setpoint

    def setSetpoint(self, setpoint):
	self.setpoint = setpoint
