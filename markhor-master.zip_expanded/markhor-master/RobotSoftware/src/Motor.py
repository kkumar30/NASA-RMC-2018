import re
import Constants as CONSTANTS
from Constants import LOGGER
import MotorModes as MOTOR_MODES

class Motor:

	def __init__(self, motor_name, dev_id, mode):
		self.motor_name = motor_name
		self.deviceID = dev_id
		self.temperature_val = 0
		self.current_val = 0
		self.voltageOutput = 0
		self.setpoint_val = 0
		self.actual_val = 0
		self.speed = 0
		self.position = 0
		self.mode = mode
		self.current_val = 0
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
		#matchList = re.findall(r'<(\d+):(\d+):(\d+):(\d+):(\d+):(\-?\d+):(\d+):(\d+):(\d+):(\d+)>', update_info, re.M|re.I)
		#matchList = re.findall(r'<(\-?\d+):(\-?\d+):(\-?\d+):(\-?\d+):(\-?\d+):(\-?\d+):(\-?\d+):(\-?\d+):(\-?\d+):(\-?\d+)>', update_info, re.M|re.I)
		matchList = re.findall(r'<(\d+):(\-?\d+\.\d+):(\-?\d+\.\d+):(\-?\d+\.\d+):(\-?\d+\.\d+):(\-?\d+\.\d+):(\-?\d+\.\d+):(\d+):([01]):([01])>', update_info, re.M|re.I)
		if(len(matchList) == 0):
			LOGGER.Low("No Matches!:" + update_info)

		# check each match to see if this contains data for this motor controller
		for match in matchList:

			# match has enough data elements to contain valid data
			if(len(match) >= 10):

				# be ready to throw an error if any data is incorrect
				try:
					#try to read all the values before assigning anything
					msg_deviceID = int(match[0])
					msg_current = float(match[1])
					msg_temperature = float(match[2])
					msg_voltageOutput = float(match[3])
					msg_speed = float(match[4])
					msg_position = float(match[5])
					msg_setpoint = float(match[6])
					msg_controlMode = int(match[7])
					msg_forward_limit = bool(int(match[8]))
					msg_reverse_limit = bool(int(match[9]))

					if(msg_deviceID == self.deviceID):
						self.current_val = msg_current
						self.temperature_val = msg_temperature
						self.voltageOutput = msg_voltageOutput
						self.speed = msg_speed
						self.position = msg_position
						self.mode  = msg_controlMode
						self.actual_val = msg_setpoint
						self.forward_limit = msg_forward_limit
						self.reverse_limit = msg_reverse_limit

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

	def setSetpoint(self, mode, setpoint):
		self.setMode(mode)
		self.setpoint_val = setpoint
	    


	#def getStateMessage(self):
	#	return "<" + str(self.deviceID) + ":" + str(self.mode) + ":" + str(int(self.setpoint_val * 1000)) + ">"

	def getStateMessage(self):
		return "<" + str(self.deviceID) + ":" + str(self.mode) + ":" + str(self.setpoint_val) + ">"

	def getNetworkMessage(self):
		msg = "<"
		msg += str(self.deviceID) + ":"
		msg += str(self.current_val) + ":"
		msg += str(self.temperature_val) + ":"
		msg += str(self.voltageOutput) + ":"
		msg += str(self.speed) + ":"
		msg += str(self.position) + ":"
		msg += str(self.actual_val) + ":"
		msg += str(self.mode) + ":"
		msg += str(int(self.forward_limit)) + ":"
		msg += str(int(self.reverse_limit)) + ">"
		return msg

	