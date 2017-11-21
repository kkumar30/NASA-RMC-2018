import time
import copy
import SocketServer
import threading
import pygame
from threading import Thread

import Constants as CONSTANTS
from Constants import LOGGER
import MotorModes as MOTOR_MODES

from Motor import Motor
from MotorHandler import MotorHandler
from Sensor import Sensor
from SensorHandler import SensorHandler
from RobotState import RobotState
from SerialHandler import SerialHandler
from NetworkHandler import NetworkHandler
from MessageQueue import MessageQueue
from JoystickReader import JoystickReader
from NetworkClient import NetworkClient
from NetworkMessage import NetworkMessage
from Servo import Servo
import BeepCodes as BEEPCODES

from time import gmtime, strftime
#from main import inboundMessageQueue

LOGGER.Low("Beginning Program Execution")

motorHandlerLock = threading.Lock()
#sensorHandlerLock = threading.Lock()
#LOGGER.Low("Motor Handler Lock: " + str(motorHandlerLock))

def motorCommunicationThread():
	while True:
		motorHandlerLock.acquire()
		inboundMotorMessage = motorSerialHandler.getMessage()
		motorHandler.updateMotors(inboundMotorMessage)
		outboundMotorMessage = motorHandler.getMotorStateMessage()
		motorSerialHandler.sendMessage(outboundMotorMessage)
		motorHandlerLock.release()
	
def sensorCommunicationThread():
	while True:
		#sensorHandlerLock.acquire()
		inboundSensorMessage = sensorSerialHandler.getMessage()
		sensorHandler.updateSensors(inboundSensorMessage)
		outboundSensorMessage = sensorHandler.getServoStateMessage()
		LOGGER.Debug(outboundSensorMessage)
		sensorSerialHandler.sendMessage(outboundSensorMessage)
		#sensorHandlerLock.release()
		
def ceaseAllMotorFunctions():
	leftDriveMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
	rightDriveMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
	collectorDepthMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
	collectorScoopsMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
	winchMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)

#initialize handlers
LOGGER.Debug("Initializing handlers...")
motorHandler = MotorHandler()
sensorHandler = SensorHandler()

	
if CONSTANTS.USING_SENSOR_BOARD:
	LOGGER.Debug("Initializing sensor serial handler...")
	sensorSerialHandler = SerialHandler(CONSTANTS.SENSOR_BOARD_PORT)
	sensorSerialHandler.initSerial()

if CONSTANTS.USING_MOTOR_BOARD:
	LOGGER.Debug("Initializing motor serial handler...")
	motorSerialHandler = SerialHandler(CONSTANTS.MOTOR_BOARD_PORT)
	motorSerialHandler.initSerial()
	#motorSerialHandler.sendMessage("<ResetDriveEncoders>\n")


#initialize network comms & server thread
if CONSTANTS.USING_NETWORK_COMM:
	networkClient = NetworkClient(CONSTANTS.CONTROL_STATION_IP, CONSTANTS.CONTROL_STATION_PORT)
	inboundMessageQueue = MessageQueue()
	networkClient.setInboundMessageQueue(inboundMessageQueue)
	outboundMessageQueue = MessageQueue()
	lastReceivedMessageNumber = -1
	currentReceivedMessageNumber = -1
	stateStartTime = -1

# setup some variables that will be used with each iteration of the loop
currentMessage = NetworkMessage("")

# initialize motors
LOGGER.Debug("Initializing motor objects...")
leftDriveMotor       = Motor("LeftDriveMotor",       CONSTANTS.LEFT_DRIVE_DEVICE_ID,       MOTOR_MODES.K_PERCENT_VBUS)
rightDriveMotor      = Motor("RightDriveMotor",      CONSTANTS.RIGHT_DRIVE_DEVICE_ID,      MOTOR_MODES.K_PERCENT_VBUS)
collectorScoopsMotor = Motor("CollectorScoopsMotor", CONSTANTS.COLLECTOR_SCOOPS_DEVICE_ID, MOTOR_MODES.K_PERCENT_VBUS)
collectorDepthMotor  = Motor("CollectorDepthMotor",  CONSTANTS.COLLECTOR_DEPTH_DEVICE_ID,  MOTOR_MODES.K_PERCENT_VBUS)
winchMotor           = Motor("WinchMotor",           CONSTANTS.WINCH_DEVICE_ID,            MOTOR_MODES.K_PERCENT_VBUS)

# initialize motor handler and add motors
LOGGER.Debug("Linking motors to motor handler...")
motorHandler.addMotor(leftDriveMotor)
motorHandler.addMotor(rightDriveMotor)
motorHandler.addMotor(collectorScoopsMotor)
motorHandler.addMotor(collectorDepthMotor)
motorHandler.addMotor(winchMotor)

# initialize encoder reset flags
driveEncoderResetFlag = False
scoopEncoderResetFlag = False
depthEncoderResetFlag = False
winchEncoderResetFlag = False

# initialize sensors
LOGGER.Debug("Initializing sensor objects...")
leftDriveCurrentSense = Sensor("LeftDriveCurrentSense")
rightDriveCurrentSense = Sensor("RightDriveCurrentSense")
collectorDepthCurrentSense = Sensor("CollectorDepthCurrentSense")
collectorScoopsCurrentSense = Sensor("CollectorScoopsCurrentSense")
winchMotorCurrentSense = Sensor("WinchMotorCurrentSense")
scoopReedSwitch = Sensor("ScoopReedSwitch")
bucketMaterialDepthSense = Sensor("BucketMaterialDepthSense")

ratchetServo = Servo()
camServo1 = Servo()
camServo2 = Servo()
camServo3 = Servo()
camServo4 = Servo()

# initialize sensor handler and add sensors
LOGGER.Debug("Linking sensor objects to sensor handler...")
sensorHandler.addSensor(leftDriveCurrentSense)
sensorHandler.addSensor(rightDriveCurrentSense)
sensorHandler.addSensor(collectorDepthCurrentSense)
sensorHandler.addSensor(collectorScoopsCurrentSense)
sensorHandler.addSensor(winchMotorCurrentSense)
sensorHandler.addSensor(scoopReedSwitch)
sensorHandler.addSensor(bucketMaterialDepthSense)

sensorHandler.addServo(ratchetServo)
sensorHandler.addServo(camServo1)
sensorHandler.addServo(camServo2)
sensorHandler.addServo(camServo3)
sensorHandler.addServo(camServo4)

# initialize robotState
LOGGER.Debug("Initializing robot state...")
robotState = RobotState()

# initialize joystick, if using joystick
if CONSTANTS.USING_JOYSTICK:
	LOGGER.Debug("Initializing joystick...")
	pygame.init()
	pygame.joystick.init()
	joystick1 = pygame.joystick.Joystick(0)
	joystick1.init()
	jReader = JoystickReader(joystick1)
	
ceaseAllMotorFunctions()

if CONSTANTS.USING_MOTOR_BOARD:
	LOGGER.Debug("Initializing motor board thread...")
	motorCommThread = Thread(target=motorCommunicationThread)
	motorCommThread.daemon = True
	motorCommThread.start()

if CONSTANTS.USING_SENSOR_BOARD:
	LOGGER.Debug("Initializing sensor board thread...")
	sensorCommThread = Thread(target=sensorCommunicationThread)
	sensorCommThread.daemon = True
	sensorCommThread.start()


# final line before entering main loop
robotEnabled = True


BEEPCODES.happy1()
LOGGER.Debug("Initialization complete, entering main loop...")

test_speed_val = -1.0
while robotEnabled:

	loopStartTime = time.time()
	#print strftime("%H:%M:%S.", gmtime()) + str(int((time.time()*1000) % 1000)) + ": ",

	currentState = robotState.getState()
	lastState = robotState.getLastState()

	# +----------------------------------------------+
	# |                Communication                 |
	# +----------------------------------------------+

	if CONSTANTS.USING_NETWORK_COMM:
		connected = False
		while(not connected):
			try:
				if(outboundMessageQueue.isEmpty()):
					networkClient.send(motorHandler.getMotorNetworkMessage()+"\n\r")
				else:
					networkClient.send(outboundMessageQueue.getNext())
				connected = True
			except:
				LOGGER.Critical("Could not connect to network, attempting to reconnect...")
				ceaseAllMotorFunctions()

	
	# +----------------------------------------------+
	# |              Current State Logic             |
	# +----------------------------------------------+
	# State machine handles the robot's current states
	if CONSTANTS.USING_NETWORK_COMM and connected:
		
		if(not inboundMessageQueue.isEmpty()):
			currentMessage = inboundMessageQueue.getNext()
			currentMessage.printMessage()
			lastReceivedMessageNumber = currentReceivedMessageNumber
			currentReceivedMessageNumber = currentMessage.messageNumber
			
		#new message has arrived, process
		if(lastReceivedMessageNumber != currentReceivedMessageNumber):
			
			stateStartTime = time.time()
			robotState.setState(currentMessage.type)
			print currentMessage.type
			
			if(currentMessage.type == "MSG_STOP"):
				LOGGER.Debug("Received a MSG_STOP")
				
			elif(currentMessage.type == "MSG_DRIVE_TIME"):
				LOGGER.Debug("Received a MSG_DRIVE_TIME")

			elif(currentMessage.type == "MSG_ROTATE_TIME"):
				LOGGER.Debug("Received a MSG_ROTATE_TIME")
				
			elif(currentMessage.type == "MSG_SCOOP_TIME"):
				LOGGER.Debug("Received a MSG_SCOOP_TIME")
				
			elif(currentMessage.type == "MSG_DEPTH_TIME"):
				LOGGER.Debug("Received a MSG_DEPTH_TIME")
				
			elif(currentMessage.type == "MSG_BUCKET_TIME"):
				LOGGER.Debug("Received a MSG_BUCKET_TIME")
			
			elif(currentMessage.type == "MSG_DRIVE_DISTANCE"):
				LOGGER.Low("Received a MSG_DRIVE_DISTANCE")
				leftDriveMotor.setMode(MOTOR_MODES.K_POSITION)
				rightDriveMotor.setMode(MOTOR_MODES.K_POSITION)
				#before we start executing anything in the message for DriveDistance below
				#the encoder reset must be sent to the motor board.
				driveEncoderResetFlag = True
				LOGGER.Low("About to acquire motor handler lock")
				motorHandlerLock.acquire()
				LOGGER.Low("About to send message to Reset Encoders")
				motorSerialHandler.sendMessage("<ResetDriveEncoders>\n")
				LOGGER.Low("SENT MESSAGE TO RESET")
				motorHandlerLock.release()

			elif(currentMessage.type == "MSG_BUCKET_POSITION"):
				winchMotor.setMode(MOTOR_MODES.K_POSITION)
				winchEncoderResetFlag = True
				LOGGER.Low("Acquiring Lock")
				motorHandlerLock.acquire()
				motorSerialHandler.sendMessage("<ResetWinchEncoder>\n")
				motorHandlerLock.release()
				LOGGER.Low("Releasing Lock")

			elif(currentMessage.type == "MSG_MOTOR_VALUES"):
				LOGGER.Debug("Received a MSG_MOTOR_VALUES")
				print "MADE IT 1"
			
			elif(currentMessage.type == "MSG_RATCHET_POSITION"):
				LOGGER.Debug("Received a MSG_RATCHET_POSITION")
		    
			else:
				LOGGER.Moderate("Received an invalid message.")
				
		#
		# MSG_STOP:
		# Stop all motors immediately
		#
		if(currentMessage.type == "MSG_STOP"):
			ceaseAllMotorFunctions()
			outboundMessageQueue.add("Finished\n")
		
		#
		# MSG_DRIVE_TIME:
		# Drive forward/backward with both motors at the same value
		# Data 0: The time in seconds the robot should drive
		# Data 1: The power/speed to drive at
		#
		elif(currentMessage.type == "MSG_DRIVE_TIME"):
			currentMessage.printMessage()
			if(time.time() < stateStartTime + currentMessage.messageData[0]):
				driveSpeed = currentMessage.messageData[1]
				leftDriveMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, driveSpeed)
				rightDriveMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, -driveSpeed)
			else:
				ceaseAllMotorFunctions()
				outboundMessageQueue.add("Finished\n")

		#
		# MSG_ROTATE_TIME:
		# Drive forward/backward with both motors at the same value
		# Data 0: The time in seconds the robot should drive
		# Data 1: The power/speed to drive at
		#
		elif(currentMessage.type == "MSG_ROTATE_TIME"):
			currentMessage.printMessage()
			if(time.time() < stateStartTime + currentMessage.messageData[0]):
				rotateSpeed  = currentMessage.messageData[1]
				leftDriveMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, rotateSpeed)
				rightDriveMotor.setSpeed(MOTOR_MODES.K_PERCENT_VBUS, rotateSpeed)
			else:
				ceaseAllMotorFunctions()
				outboundMessageQueue.add("Finished\n")


		#
		# MSG_SCOOP_TIME:
		# Drive the scoops for a set time at a specified speed
		# Data 0: The time in seconds the scoop motor should run
		# Data 1: The power/speed to run the motor at
		#		
		elif(currentMessage.type == "MSG_SCOOP_TIME"):
			currentMessage.printMessage()
			if(time.time() < stateStartTime + currentMessage.messageData[0]):
				scoopSpeed = currentMessage.messageData[1]
				collectorScoopsMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, scoopSpeed)
			else:
				ceaseAllMotorFunctions()
				outboundMessageQueue.add("Finished\n")
		#
		# MSG_DEPTH_TIME:
		# Drive the depth motor for a set time at a specified power/speed
		# Data 0: The time in seconds the depth motor should run
		# Data 1: The power/speed to run the motor at
		#			
		elif(currentMessage.type == "MSG_DEPTH_TIME"):
			currentMessage.printMessage()
			if(time.time() < stateStartTime + currentMessage.messageData[0]):
				depthSpeed = currentMessage.messageData[1]
				collectorDepthMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, depthSpeed)
			else:
				ceaseAllMotorFunctions()
				outboundMessageQueue.add("Finished\n")
		#
		# MSG_BUCKET_TIME:
		# Drive the bucket for a set time at a specified speed
		# Data 0: The time in seconds the bucket motor should run
		# Data 1: The power/speed to run the motor at
		#			
		elif(currentMessage.type == "MSG_BUCKET_TIME"):
			currentMessage.printMessage()
			if(time.time() < stateStartTime + currentMessage.messageData[0]):
				bucketSpeed = currentMessage.messageData[1]
				winchMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, bucketSpeed)
			else:
				ceaseAllMotorFunctions()
				outboundMessageQueue.add("Finished\n")
		
				
		elif(currentMessage.type == "MSG_DRIVE_DISTANCE"):
			currentMessage.printMessage()
			positionVal = -currentMessage.messageData[0]

			LOGGER.Low("Check setpoint: " + str(positionVal))
			LOGGER.Low("Check left enc: " + str(leftDriveMotor.position))
			LOGGER.Low("Check right enc: " + str(rightDriveMotor.position))

			if(driveEncoderResetFlag):
				ceaseAllMotorFunctions()
				if((abs(leftDriveMotor.position) < 1) and (abs(rightDriveMotor.position) < 1)):
				    LOGGER.Low("Encoders reset.")
				    driveEncoderResetFlag = False
				    leftDriveMotor.setSetpoint(MOTOR_MODES.K_POSITION, positionVal)
    				    rightDriveMotor.setSetpoint(MOTOR_MODES.K_POSITION, -positionVal)

			#elif( (not (abs(leftDriveMotor.position) - abs(positionVal)) < 2) or
			#      (not (abs(rightDriveMotor.position) - abs(positionVal)) < 2)):
			#	pass
			elif( (abs(leftDriveMotor.position) < abs(0.95 * positionVal)) or
			      (abs(rightDriveMotor.position) < abs(0.95 * positionVal))):
				pass

			else:
				ceaseAllMotorFunctions()
				outboundMessageQueue.add("Finished\n")
			#if(leftDriveMotor.getDistance() > startingDistance + currentMessage.messageData[0]):
			#	driveSpeed = currentMessage.messageData[1]
			#	leftDriveMotor.setSpeed(driveSpeed)
			#	rightDriveMotor.setSpeed(-driveSpeed)	
			#else:
			#	ceaseAllMotorFunctions()
			#	outboundMessageQueue.add("Finished\n")

		elif(currentMessage.type == "MSG_BUCKET_POSITION"):
			currentMessage.printMessage()
			positionVal = currentMessage.messageData[0]
			LOGGER.Low("Check Setpoint: " + str(positionVal))
			LOGGER.Low("Check Encoder:  " + str(winchMotor.position))
			LOGGER.Low("Check Velocity: " + str(winchMotor.speed))

			if(winchEncoderResetFlag):
				ceaseAllMotorFunctions()
				if(abs(winchMotor.position) < 1):
					LOGGER.Low("Winch Encoder Reset.")
					winchEncoderResetFlag = False
			elif(not abs(winchMotor.position - positionVal) < 2):
				winchMotor.setSetpoint(MOTOR_MODES.K_POSITION, positionVal)
			else:
				ceaseAllMotorFunctions()
				outboundMessageQueue.add("Finished\n")
			
		elif(currentMessage.type == "MSG_MOTOR_VALUES"):
			leftDriveMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, currentMessage.messageData[0])
			rightDriveMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS,currentMessage.messageData[1])
			collectorScoopsMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS,currentMessage.messageData[2])
			collectorDepthMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS,currentMessage.messageData[3])
			winchMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS,currentMessage.messageData[4])

		elif(currentMessage.type == "MSG_RATCHET_POSITION"):
			ratchetServo.setSetpoint(int(currentMessage.messageData[0]))
			outboundMessageQueue.add("Finished\n")
		

	if CONSTANTS.USING_JOYSTICK:
		#pygame.event.get()
		#jReader.updateValues()
		#leftDriveMotor.setSpeed(jReader.axis_y1)
		#rightDriveMotor.setSpeed(jReader.axis_y2)
		leftDriveMotor.setSpeed(1.0)
		rightDriveMotor.setSpeed(-1.0)
		collectorDepthMotor.setSpeed(0)
		collectorScoopsMotor.setSpeed(0)
		winchMotor.setSpeed(0)
	
#	else:
#		if(test_speed_val > 1.0):
#			test_speed_val = 0
#		else:
#			test_speed_val += 0.001
#		test_speed_val += 0.001
		#leftDriveMotor.setSpeed(0)
		#rightDriveMotor.setSpeed(0)
		#collectorDepthMotor.setSpeed(0)
		#collectorScoopsMotor.setSpeed(0)
		#winchMotor.setSpeed(0)
		
	#LOGGER.Low("LMotor Speed: " + str(leftDriveMotor.speed) + " RMotor Speed: " + str(rightDriveMotor.speed))
	#LOGGER.Low("LMotor Position: " + str(leftDriveMotor.position) + "RMotor Position: " + str(rightDriveMotor.position))
	#sleep to maintain a more constant thread time (specified in Constants.py)


	loopEndTime = time.time()
	loopExecutionTime = loopEndTime - loopStartTime
	sleepTime = CONSTANTS.LOOP_DELAY_TIME - loopExecutionTime
	if(sleepTime > 0):
		time.sleep(sleepTime)



