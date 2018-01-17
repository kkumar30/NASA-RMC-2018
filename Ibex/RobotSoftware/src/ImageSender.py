import socket
#import cv2
import numpy
import Constants as CONSTANTS

class ImageSender:

    def __init__(self):
	self.image = None
        self.port = CONSTANTS.CAMERA_PORT1
        self.server_addr = CONSTANTS.CONTROL_STATION_IP

    def sendImage(self, filename):
	self.image = open(filename, 'rb')
	
	data = numpy.array(self.image)
	stringData = data.tostring()
	print len(stringData)

	sock = socket.socket()
	sock.connect((self.server_addr, self.port))
	
	#send the number of bytes in the image
	#sock.send(str(len(stringData)).ljust(16));

	#begin sending the image
	image_packet = self.image.read(1024)
	while(image_packet):
	    sock.send(image_packet)
	    image_packet = self.image.read(1024)
	self.image.close()
	sock.close()


if __name__ == "__main__":
    im = ImageSender()
    im.sendImage("robot.jpg")