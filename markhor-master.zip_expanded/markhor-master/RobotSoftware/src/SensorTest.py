from SensorHandler import SensorHandler
from Sensor import Sensor
import time


sensorHandler = SensorHandler('COM3')
sensor0 = Sensor('Sensor 0')
sensor1 = Sensor('Sensor 1')
sensor2 = Sensor('Sensor 2') 
sensor3 = Sensor('Sensor 3')

sensorHandler.addSensor(sensor0)
sensorHandler.addSensor(sensor1)
sensorHandler.addSensor(sensor2)
sensorHandler.addSensor(sensor3)

while True:	
	print 'Staring the loop'
	sensorHandler.read()
