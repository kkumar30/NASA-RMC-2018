import re
import Constants as CONSTANTS
import MotorModes as MOTOR_MODES

class Motor:

	def __init__(self, motor_name, dev_id, mode):
		self.motor_name = motor_name
		self.deviceID = dev_id
		self.setpoint_val = 0
		self.actual_val = 0
		self.mode = mode
		self.forward_limit = False
		self.reverse_limit = False

	'''
	Takes a string representing containing motor information
	If the device ID of this motor matches the string, update the values
	accordingly.  If it does not match, ignore the message. Similarly, if
	the message is invald, ignore it
	'''
	def update(self, update_info):
		# message contains info for all motors, need to parse info specific to this motor
		matchList = re.findall(r'<(\d+):(\d+):(\d+):(\d+)>', update_info, re.M|re.I)

		# check each match to see if this contains data for this motor controller
		for match in matchList:

			# match has enough data elements to contain valid data
			if(len(match) >= 4):

				# be ready to throw an error if any data is incorrect
				try:
					#try to read all the values before assigning anything
					msg_deviceID = int(match[0])
					msg_feedback_val = int(match[1])
					msg_forward_limit = bool(int(match[2]))
					msg_reverse_limit = bool(int(match[3]))

					if(msg_deviceID == self.deviceID):
						self.forward_limit = msg_forward_limit
						self.reverse_limit = msg_reverse_limit
						self.actual_val = msg_feedback_val

				except:
					print "ERROR: Unable to parse one or more of the message components. Device ID: " + str(self.deviceID)

	# set the speed of the motor from -1.0 to 1.0
	def setSpeed(self, speed):
		if(speed > CONSTANTS.MAX_SPEED):
			speed = CONSTANTS.MAX_SPEED
		if(speed < CONSTANTS.MIN_SPEED):
			speed = CONSTANTS.MIN_SPEED
		self.setpoint_val = speed

	# set the mode of the motor 
	def setMode(self, mode):
		if mode in MOTOR_MODES.ALL_MODES:
			self.mode = mode
		else:
			print "ERROR: Invalid mode selected. Device ID: ", + str(self.deviceID)

	def getStateMessage(self):
		return "<" + str(self.deviceID) + ":" + str(self.mode) + ":" + str(self.setpoint_val) + ">"


	