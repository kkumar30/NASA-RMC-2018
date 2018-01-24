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


#Threading stuff... i still don't know this
motorHandlerLock = threading.Lock()
#sensorHandlerLock = threading.Lock()
#LOGGER.Low("Motor Handler Lock: " + str(motorHandlerLock))
stopped = True


def motorCommunicationThread():
	while True:

		motorHandlerLock.acquire()
		#get the messages of each motor status from the HERO and update our motor values
		inboundMotorMessage = motorSerialHandler.getMessage()
		motorHandler.updateMotors(inboundMotorMessage)

		#Get our motor state message and send that to the HERO
		outboundMotorMessage = motorHandler.getMotorStateMessage()
		motorSerialHandler.sendMessage(outboundMotorMessage)


		motorHandlerLock.release()

# def sensorCommunicationThread():
# 	while True:
# 		#sensorHandlerLock.acquire()
# 		inboundSensorMessage = sensorSerialHandler.getMessage()
# 		sensorHandler.updateSensors(inboundSensorMessage)
#
# 		outboundSensorMessage = sensorHandler.getServoStateMessage()
# 		LOGGER.Debug(outboundSensorMessage)
# 		sensorSerialHandler.sendMessage(outboundSensorMessage)
# 		#sensorHandlerLock.release()
#
# def ceaseAllMotorFunctions():
# 	#Stop all motors
# 	# leftDriveMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
# 	# rightDriveMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
# 	# collectorDepthMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
# 	# collectorScoopsMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
# 	# winchMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
# 	testMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
# #initialize handlers
# LOGGER.Debug("Initializing handlers...")
motorHandler = MotorHandler()
#

if CONSTANTS.USING_MOTOR_BOARD:
	LOGGER.Debug("Initializing motor serial handler...")
	motorSerialHandler = SerialHandler(CONSTANTS.MOTOR_BOARD_PORT)
	# for i in range(10):
	# 	try:
	# 		motorSerialHandler = SerialHandler("COM0" + str(i))		
	motorSerialHandler.initSerial()
		# except:
		# 	LOGGER.Debug("Not getting COM" + str(i))


# setup some variables that will be used with each iteration of the loop
# currentMessage = NetworkMessage("")

# initialize motors
LOGGER.Debug("Initializing motor objects...")
# leftDriveMotor       = Motor("LeftDriveMotor",       CONSTANTS.LEFT_DRIVE_DEVICE_ID,       MOTOR_MODES.K_PERCENT_VBUS)
# rightDriveMotor      = Motor("RightDriveMotor",      CONSTANTS.RIGHT_DRIVE_DEVICE_ID,      MOTOR_MODES.K_PERCENT_VBUS)
# collectorScoopsMotor = Motor("CollectorScoopsMotor", CONSTANTS.COLLECTOR_SCOOPS_DEVICE_ID, MOTOR_MODES.K_PERCENT_VBUS)
# collectorDepthMotor  = Motor("CollectorDepthMotor",  CONSTANTS.COLLECTOR_DEPTH_DEVICE_ID,  MOTOR_MODES.K_PERCENT_VBUS)
# winchMotor           = Motor("WinchMotor",           CONSTANTS.WINCH_DEVICE_ID,            MOTOR_MODES.K_PERCENT_VBUS)
testMotor			 = Motor("TestMotor",			 1,									   MOTOR_MODES.K_PERCENT_VBUS)

# initialize motor handler and add motors
LOGGER.Debug("Linking motors to motor handler...")
# motorHandler.addMotor(leftDriveMotor)
# motorHandler.addMotor(rightDriveMotor)
# motorHandler.addMotor(collectorScoopsMotor)
# motorHandler.addMotor(collectorDepthMotor)
# motorHandler.addMotor(winchMotor)
motorHandler.addMotor(testMotor)

# initialize encoder reset flags
driveEncoderResetFlag = False
scoopEncoderResetFlag = False
depthEncoderResetFlag = False
winchEncoderResetFlag = False

# initialize robotState
# LOGGER.Debug("Initializing robot state...")
robotState = RobotState()

# initialize joystick, if using joystick
if CONSTANTS.USING_JOYSTICK:
	LOGGER.Debug("Initializing joystick...")
	pygame.init()
	pygame.joystick.init()
	joystick1 = pygame.joystick.Joystick(0)
	joystick1.init()
	jReader = JoystickReader(joystick1)


# ceaseAllMotorFunctions()

if CONSTANTS.USING_MOTOR_BOARD:
	LOGGER.Debug("Initializing motor board thread...")
	#Sets up an isr essentially using the motorCommunicationThread()
	motorCommThread = Thread(target=motorCommunicationThread)
	motorCommThread.daemon = True
	motorCommThread.start()

if CONSTANTS.USING_SENSOR_BOARD:
	LOGGER.Debug("Initializing sensor board thread...")
	#sets up an isr essentially using the sensorCommunicationThread
	sensorCommThread = Thread(target=sensorCommunicationThread)
	sensorCommThread.daemon = True
	sensorCommThread.start()


# final line before entering main loop
robotEnabled = True


# BEEPCODES.happy1()
# LOGGER.Debug("Initialization complete, entering main loop...")
#
# test_speed_val = -1.0

while robotEnabled:
	testMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS,0.2)



	#
	# loopStartTime = time.time()
	#
	# currentState = robotState.getState()
	# lastState = robotState.getLastState()
	#
	#
	# # +----------------------------------------------+
	# # |                Communication                 |
	# # +----------------------------------------------+
	#
	# if raw_input == 't':
    #     usingController = True
	#

	# while usingController:
    #     y1 = jReader.getAxisValues()
	# 	testMotor.setSetpoint(CONSTANTS.K_PERCENT_VBUS, y1)
	# 	if raw_input == 'q':
	# 		usingController = False
	# 		ceaseAllMotorFunctions()

	loopEndTime = time.time()
	# loopExecutionTime = loopEndTime - loopStartTime
	# sleepTime = CONSTANTS.LOOP_DELAY_TIME - loopExecutionTime
	# if(sleepTime > 0):
	# 	time.sleep(sleepTime)
