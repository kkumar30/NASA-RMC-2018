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
from scipy.interpolate import interp1d

from time import gmtime, strftime
#from main import inboundMessageQueue
LOGGER.Low("Beginning Program Execution")


#Threading stuff... i still don't know this
motorHandlerLock = threading.Lock()
sensorHandlerLock = threading.Lock()
#LOGGER.Low("Motor Handler Lock: " + str(motorHandlerLock))
stopped = True
joyMap= interp1d([-1.0,1.0],[-.5,.5])
guiMotorMessage =""
def motorCommunicationThread():
	global guiMotorMessage
	while True:

		motorHandlerLock.acquire()
		#get the messages of each motor status from the HERO and update our motor values
		inboundMotorMessage = motorSerialHandler.getMessage()
		motorHandler.updateMotors(inboundMotorMessage)
		guiMotorMessage= inboundMotorMessage
		#Gets the listed messages
		# flaskupdate(inboundMotorMessage)

		#Get our motor state message and send that to the HERO
		outboundMotorMessage = motorHandler.getMotorStateMessage()
		motorSerialHandler.sendMessage(outboundMotorMessage)
		motorHandlerLock.release()




def sensorCommunicationThread():
	while True:
		LOGGER.Debug("Here")
		sensorHandlerLock.acquire()
		inboundSensorMessage = sensorSerialHandler.getMessage()
		sensorHandler.updateSensors(inboundSensorMessage)

		# outboundSensorMessage = sensorHandler.getServoStateMessage()
		LOGGER.Debug(inboundSensorMessage)
		# sensorSerialHandler.sendMessage(outboundSensorMessage)
		sensorHandlerLock.release()

def ceaseAllMotorFunctions():
	#Stop all motors
	# leftDriveMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
	# rightDriveMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
	# collectorDepthMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
	# collectorScoopsMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
	# winchMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
	testMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
	poopMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
# #initialize handlers
# LOGGER.Debug("Initializing handlers...")
motorHandler = MotorHandler()
sensorHandler = SensorHandler()
#
lswitch = Sensor("LimitSwitch")
sensorHandler.addSensor(lswitch)

if CONSTANTS.USING_SENSOR_BOARD:
	LOGGER.Debug("Initializing sensor serial handler...")
	sensorSerialHandler = SerialHandler(CONSTANTS.SENSOR_BOARD_PORT)
	sensorSerialHandler.initSerial()
	val = ""
	val = sensorHandler.getSensorValues()
	LOGGER.Debug(val)

if CONSTANTS.USING_MOTOR_BOARD:
	LOGGER.Debug("Initializing motor serial handler...")
	motorSerialHandler = SerialHandler(CONSTANTS.MOTOR_BOARD_PORT)
	motorSerialHandler.initSerial()

if CONSTANTS.USING_NETWORK_COMM:
	networkClient = NetworkClient(CONSTANTS.CONTROL_STATION_IP, CONSTANTS.CONTROL_STATION_PORT)
	inboundMessageQueue = MessageQueue()
	networkClient.setInboundMessageQueue(inboundMessageQueue)
	outboundMessageQueue = MessageQueue()
	lastReceivedMessageNumber = -1
	currentReceivedMessageNumber = -1
	stateStartTime = -1

# flaskupdate("test")
# setup some variables that will be used with each iteration of the loop
# currentMessage = NetworkMessage("")

# initialize motors
LOGGER.Debug("Initializing motor objects...")
# leftDriveMotor       = Motor("LeftDriveMotor",       CONSTANTS.LEFT_DRIVE_DEVICE_ID,       MOTOR_MODES.K_PERCENT_VBUS)
# rightDriveMotor      = Motor("RightDriveMotor",      CONSTANTS.RIGHT_DRIVE_DEVICE_ID,      MOTOR_MODES.K_PERCENT_VBUS)
# collectorScoopsMotor = Motor("CollectorScoopsMotor", CONSTANTS.COLLECTOR_SCOOPS_DEVICE_ID, MOTOR_MODES.K_PERCENT_VBUS)
# collectorDepthMotor  = Motor("CollectorDepthMotor",  CONSTANTS.COLLECTOR_DEPTH_DEVICE_ID,  MOTOR_MODES.K_PERCENT_VBUS)
# winchMotor           = Motor("WinchMotor",           CONSTANTS.WINCH_DEVICE_ID,            MOTOR_MODES.K_PERCENT_VBUS)
testMotor= Motor("TestMotor",1, MOTOR_MODES.K_PERCENT_VBUS)
poopMotor= Motor("PoopMotor",2, MOTOR_MODES.K_PERCENT_VBUS)
# initialize motor handler and add motors
LOGGER.Debug("Linking motors to motor handler...")
# limitSwitch = r("")
# motorHandler.addMotor(leftDriveMotor)
# motorHandler.addMotor(rightDriveMotor)
# motorHandler.addMotor(collectorScoopsMotor)
# motorHandler.addMotor(collectorDepthMotor)
# motorHandler.addMotor(winchMotor)
motorHandler.addMotor(testMotor)
motorHandler.addMotor(poopMotor)

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

def tankDrive(joyRead1,joyRead2):
	testMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, joyRead1)
	poopMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, joyRead2)
def turnLeftTime(secs):
	initTime = time.time()
	while time.time() < (initTime + secs):
		testMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, .3)
		poopMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, .3)
	ceaseAllMotorFunctions()
BEEPCODES.happy1()
# LOGGER.Debug("Initialization complete, entering main loop...")
#
# test_speed_val = -1.0
global done
done = False
while robotEnabled:
#	if not done:
#		done = True
#		turnLeftTime(4)
	# ceaseAllMotorFunctions()

#	x = "hi"
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
				# ceaseAllMotorFunctions()


	# +----------------------------------------------+
	# |              Current State Logic             |
	# +----------------------------------------------+
	# State machine handles the robot's current states
	if CONSTANTS.USING_NETWORK_COMM and connected:
		if(not inboundMessageQueue.isEmpty()):
			currentMessage = inboundMessageQueue.getNext()
			# currentMessage.printMessage()
			lastReceivedMessageNumber = currentReceivedMessageNumber
			currentReceivedMessageNumber = currentMessage.messageNumber

			#new message has arrived, process
		if(currentMessage.type == "MSG_MOTOR_VALUES"):
			tankDrive(currentMessage.messageData[0], currentMessage.messageData[1])
	    #testMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, -0.3)
    #poopMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.3)
# # def runmotors(threadname):
	# while robotEnabled:
		# print "in motor thread"
    #pygame.event.get()
    #jReader.updateValues()
    #tankDrive(jReader.getAxisValues())
		#
	# testMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, .1)
