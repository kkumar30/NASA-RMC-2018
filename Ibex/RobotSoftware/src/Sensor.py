import re

class Sensor:

	def __init__(self, name):
		self.sensor_name = name
		self.value = 0

	def getValue(self):
		return self.value

	def getSensorName(self):
		return self.sensor_name

	def update(self, update_info):
		# message contains info for all motors, need to parse info specific to this motor
		matchList = re.findall(r'<([A-Za-z_]\w+):(\d+)>', update_info, re.M|re.I)

		# check each match to see if this contains data for this motor controller
		for match in matchList:

			# match has enough data elements to contain valid data
			if(len(match) >= 2):

				# be ready to throw an error if any data is incorrect
				try:
					#try to read all the values before assigning anything
					msg_sensor_name = str(match[0])
					msg_sensor_value = int(match[1])

					if(msg_sensor_name == self.sensor_name):
						self.value = msg_sensor_value
				except:
					print "ERROR: Unable to parse one or more of the message components. Device ID: " + str(self.sensor_name)
		

