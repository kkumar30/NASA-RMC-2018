import socket
import Constants as CONSTANTS
from NetworkMessage import NetworkMessage

class NetworkClient:
    
    def __init__(self, ip_addr, port):
        self.ip_addr = ip_addr
        self.port = port
        self.buffer_size = 1024
        
    def setInboundMessageQueue(self, queue):
        self.inboundMessageQueue = queue
    '''
    def send(self, msg_str):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.settimeout(0.5)
        try:
            s.connect((self.ip_addr, self.port))
            s.send(msg_str)
            data = s.recv(self.buffer_size)
            self.inboundMessageQueue.add(NetworkMessage(str(data)))
            print "Received: " + str(data)
            s.close()
        except:
            print "Error connecting to control station!"
    '''

    def send(self, msg_str):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.settimeout(0.5)
        s.connect((self.ip_addr, self.port))
        s.send(msg_str)
        data = s.recv(self.buffer_size)
        self.inboundMessageQueue.add(NetworkMessage(str(data)))
        #print "Received: " + str(data)
        s.close()
