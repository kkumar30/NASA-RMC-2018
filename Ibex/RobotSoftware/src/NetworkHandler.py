import socket
import SocketServer
import thread

class NetworkHandler(SocketServer.BaseRequestHandler):

    def __init__(self, inboundMessageQueue, outboundMessageQueue):
        self.inboundMessageQueue = inboundMessageQueue
        self.outboundMessageQueue = outboundMessageQueue
        pass
    
    
    def handle(self):
        data = self.request.recv(1024).strip()
        self.inboundMessageQueue.add(str(data))
        if(not self.outboundMessageQueue.isEmpty()):
            self.request.sendall(self.outboundMessageQueue.getNext())
        
    
    

        