import NetworkMessage

class MessageQueue():
    
    def __init__(self):
        self.queue = []
        
    def getSize(self):
        return len(self.queue)
    
    def isEmpty(self):
        return len(self.queue) == 0
    
    def makeEmpty(self):
        self.queue = []
    
    def add(self, msg):
        self.queue.append(msg)
        
    def peek(self):
        if(self.isEmpty()):
            return None
        else:
            return self.queue[0]
        
    def getNext(self):
        if(self.isEmpty()):
            return None
        else:
            msg = self.queue.pop(0)
            return msg


if __name__ == "__main__":
    m1 = NetworkMessage.NetworkMessage("<1|0:5.0:-0.75>")
    m2 = NetworkMessage.NetworkMessage("<0|1>")
    queue = MessageQueue()
    print "Queue size: " + str(queue.getSize())
    queue.add(m1)
    queue.add(m2)
    print "Queue size: " + str(queue.getSize())
    queue.peek().printMessage()
    queue.getNext()
    print "Queue size: " + str(queue.getSize())
    queue.peek().printMessage()
    queue.getNext()
    print "Queue size: " + str(queue.getSize())