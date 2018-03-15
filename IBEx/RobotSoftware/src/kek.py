import time
import copy
import SocketServer
import threading
import pygame
import cv2
import numpy
import Constants as CONSTANTS
import MotorModes as MOTOR_MODES
import BeepCodes as BEEPCODES


from threading import Thread
from Constants import LOGGER
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
from scipy.interpolate import interp1d
from TargetPipeline import TargetFinder
from AngleWithTarget import center
from time import gmtime, strftime

cameraOutput = ""

#from main import inboundMessageQueue
LOGGER.Low("Beginning Program Execution")


#Threading stuff... i still don't know this
motorHandlerLock = threading.Lock()
#sensorHandlerLock = threading.Lock()
cameraHandlerLock = threading.Lock()

#LOGGER.Low("Motor Handler Lock: " + str(motorHandlerLock))
stopped = True
joyMap= interp1d([-1.0,1.0],[-.5,.5])
fakeCameraAngle = 330
fakeside = "Left"

def motorCommunicationThread():
    while True:

        motorHandlerLock.acquire()
        
        inboundMotorMessage = motorSerialHandler.getMessage()
#        LOGGER.Debug(inboundMotorMessage)
        motorHandler.updateMotors(inboundMotorMessage)
        
        outboundMotorMessage = motorHandler.getMotorStateMessage()
        motorSerialHandler.sendMessage(outboundMotorMessage)
        motorHandlerLock.release()

def sensorCommunicationThread():
    while True:
#        time.sleep(.5)
#        sensorHandlerLock.acquire()
        
        inboundSensorMessage = sensorSerialHandler.getMessage()
        sensorHandler.updateSensors(inboundSensorMessage)
        
        outboundServoMessage = sensorHandler.getServoStateMessage()
        sensorSerialHandler.sendMessage(outboundServoMessage)
#        sensorHandlerLock.release()

def ceaseAllMotorFunctions():
    #Stop all motors
    testMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
    poopMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, 0.0)
    #time.sleep(10)
# #initialize handlers
motorHandler = MotorHandler()
sensorHandler = SensorHandler()


                            
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

#currentMessage = NetworkMessage("")

# initialize motors
LOGGER.Debug("Initializing motor objects...")
testMotor= Motor("TestMotor",1, MOTOR_MODES.K_PERCENT_VBUS)
poopMotor= Motor("PoopMotor",2, MOTOR_MODES.K_PERCENT_VBUS)

#initialize sensors
LOGGER.Debug("Initializing sensorobjects...")
orientationAngle = Sensor("IMU")

#initialize servos
LOGGER.Debug("Initializing servo objects...")
cameraServo = Servo()

# initialize motor handler and add motors
LOGGER.Debug("Linking motors to motor handler...")
motorHandler.addMotor(testMotor)
motorHandler.addMotor(poopMotor)

#Initialize sensor handler and add sensors and motors
LOGGER.Debug("Linking sensors and servos to sensor handler...")
sensorHandler.addServo(cameraServo)
sensorHandler.addSensor(orientationAngle)

#initialize encoder reset flags
driveEncoderResetFlag = False
scoopEncoderResetFlag = False
depthEncoderResetFlag = False
winchEncoderResetFlag = False

# initialize robotState
# LOGGER.Debug("Initializing robot state...")
robotState = RobotState()

def cameraCommunicationThread():
    global frame
    global cameraOutput
    LOGGER.Debug("Starting Cameraaaaaaaaaaaa")
    pipeline = TargetFinder()
    while cap.isOpened():
        cameraHandlerLock.acquire()
        have_frame, frame = cap.read()
        ret, jpeg = cv2.imencode(".jpg",frame)
        cameraOutput = jpeg.tobytes()
        #LOGGER.Debug(str(len(frame)))
        cameraHandlerLock.release()

#initialize some opencv stuff
if CONSTANTS.USING_CAMERA:
    LOGGER.Debug("Initializing Camera")
    cap = cv2.VideoCapture(CONSTANTS.CAMERA_INDEX)
    pipeline = TargetFinder()
    cap.release()
    #camThread = Thread(target=cameraCommunicationThread)
    #camThread.daemon = True
    #camThread.start()

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

def alignWithTarget(side, angle):
    if side == "Left":
        if 0 < angle <= 90:
            LOGGER.Debug("Left side facing right")
            turnRightTime(2)
        
        elif 90 < angle <= 180:
            LOGGER.Debug("Left side facing away")
            turnLeftTime(2)
        
        elif 180 < angle <= 270:
            LOGGER.Debug("Left side facing left")
            turnLeftTime(4)
     
        elif 270 < angle <= 360:
            LOGGER.Debug("Left side facing towards the target")
            turnRightTime(3.5)
    
    elif side == "Right":
        if 0 < angle <= 90:
            LOGGER.Debug("Right side facing towards")
            turnLeftTime(4)
        
        elif 90 < angle <= 180:
            LOGGER.Debug("Right side facing right")
            turnRightTime(4)
        
        elif 180 < angle <= 270:
            LOGGER.Debug("Right side facing away")
            turnRightTime(2)
        
        elif 270 < angle <= 360:
            LOGGER.Debug("Right side facing left")
            turnLeftTime(2)
    else:
        LOGGER.Debug("Side is incorrect")

#reachedTarget = False
def centerWithTarget():

    #foundTarget = False    
    #side = ""
    #LOGGER.Debug(str(cap.isOpened()))
    #while cap.isOpened():
    #    have_frame, frame = cap.read()
    #    if have_frame:
    #        contours = pipeline.process(frame)
    #        pan()        

    #        LOGGER.Debug("Contours "+ str(len(contours)))
     #       if len(contours)==2:
     #           foundTarget = True
     #           side = center(contours)
     #           LOGGER.Debug("SIDE " + center(contours))
     #           break
     #   if foundTarget:
     #       break
               
    #cap.release()
    #global reachedTarget
    #LOGGER.Debug(str(reachedTarget))
    #if reachedTarget == False:
    LOGGER.Debug(fakeside)
    alignWithTarget(fakeside, fakeCameraAngle)
    driveForwardTime(3.5)
    #    reachedTarget = True
    #cameraServo.setSetpoint(0)
    #Logger.Debug("Side " + center(contours)+ " Angle" + angle)
    
def turnLeftTime(seconds):
    LOGGER.Debug("TURNING LEFT")
    initTime = time.time()
   
    while time.time() < initTime+seconds:
        testMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, .6)       
        poopMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, .6)       
    ceaseAllMotorFunctions()
        
def turnRightTime(seconds):
    initTime = time.time()
    LOGGER.Debug("Turning RIGHT")
    
    while time.time()< initTime+seconds:
        testMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, -.6)
        poopMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, -.6)
    ceaseAllMotorFunctions()

def driveForwardTime(seconds):
    initTime = time.time()
    LOGGER.Debug("Driving Forward")
    while time.time()<initTime+seconds:
        testMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, -.6)
        poopMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, .6)
    ceaseAllMotorFunctions()
    global reachedTarget
    reachedTarget = True

def pan(): 
    if cameraServo.getSetpoint()<100:
        cameraServo.setSetpoint((cameraServo.getSetpoint()+5))
        time.sleep(1)
def turnLeftTicks(ticks):
    initTicks = poopMotor.position
    desPosition = initTicks-ticks
    if poopMotor.position > desPosition:
        LOGGER.Debug("Desired Position: "+str( desPosition) + "Encoder Ticks: "+ str(poopMotor.position))
        testMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, .6)       
        poopMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, .6)       
    #ceaseAllMotorFunctions()
        
#    servoVal = 0
#    for i in range(servoVal,100):
#        cameraServo.setSetpoint(servoVal)
#        servoVal+=5

BEEPCODES.happy1()
LOGGER.Debug("Initialization complete, entering main loop...")
startTime =time.time()
#time.sleep(0.5)
# test_speed_val = -1.0
#centerWithTarget()

#camPos = 0
#cameraServo.setSetpoint(100)
#time.sleep(2)
#cameraServo.setSetpoint(0)
#time.sleep(2)
#cameraServo.setSetpoint(100)
#time.sleep(4)
#for i in range(camPos,180):
#    cameraServo.setSetpoint(i)
#    camPos = i
#    time.sleep(.3)
while robotEnabled:
    loopStartTime = time.time()
    
    currentState = robotState.getState()
    lastState = robotState.getLastState()
    #for angle in [10,30,50]:
    #time.sleep(3)
    #cameraServo.setSetpoint(100)
    #time.sleep(1)
    #cameraServo.setSetpoint(0)
    #time.sleep(1)
#    for pos in range(50,80):
#        cameraServo.setSetpoint(pos)
        #time.sleep(1)
    #cameraServo.setSetpoint(120)
    #pan()  
          
#    centerWithTarget()
#    turnLeftTime(5)
    #ceaseAllMotorFunctions()
#    centerWithTarget()
#    cameraServo.setSetpoint(0)
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
       # if lastReceivedMessageNumber != currentReceivedMessageNumber:
        stateStartTime = time.time()
        robotState.setState(currentMessage.type)
            
            #prints the message recieved and reset the encoders if necessary            
        if currentMessage.type == "MSG_MOTOR_VALUES":
            LOGGER.Debug("Recieved a MSG_MOTOR_VALUES")

        if(currentMessage.type == "MSG_MOTOR_VALUES"):
            #turnLeftTime(3)
            #turnRightTime(3)
            #ceaseAllMotorFunctions()
            #LOGGER.Debug(str(poopMotor.position))
#       if poopMotor.position >10:
            #driveEncoderResetFlag = True
            #motorHandlerLock.acquire()
        #motorSerialHandler.sendMessage("<ResetDriveEncoders>\n")
        #motorHandlerLock.release() 
                #tankDrive(currentMessage.messageData[0], currentMessage.messageData[1])
            print "HERE"
            ceaseAllMotorFunctions()


    loopEndTime = time.time()
    loopExecutionTime = loopEndTime-loopStartTime
    sleepTime = CONSTANTS.LOOP_DELAY_TIME - loopExecutionTime
    if sleepTime>0:
        time.sleep(sleepTime)







#    if CONSTANTS.USING_CAMERA:
#   printTargets()
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
#   poopMotor.setSetpoint(MOTOR_MODES.K_PERCENT_VBUS, .3)
    



