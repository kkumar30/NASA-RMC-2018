import pygame
import time

def deadband(value):
    if value > -0.1 and value < 0.1:
        return 0.0
    else:
        return value
    

class JoystickReader:
    
    def __init__(self, joystick):
        self.joystick = joystick
        self.axis_x1 = 0.0
        self.axis_y1 = 0.0
        self.axis_x2 = 0.0
        self.axis_y2 = 0.0
        self.axis_lt = 0.0
        self.axis_rt = 0.0
        
        self.btn_a = False
        self.btn_b = False
        self.btn_x = False
        self.btn_y = False
        self.btn_lb = False
        self.btn_rb = False
        self.btn_back = False
        self.btn_start = False
        
        self.dpad_up = False
        self.dpad_down = False
        self.dpad_left = False
        self.dpad_right = False
    
    def updateValues(self):
        #axes
        #self.axis_x1 = deadband(self.joystick.get_axis(0))
        self.axis_y1 = deadband(self.joystick.get_axis(1))
        #self.axis_x2 = deadband(self.joystick.get_axis(4))
        self.axis_y2 = deadband(self.joystick.get_axis(4))
        #self.axis_lt = deadband(self.joystick.get_axis(2))
        #self.axis_rt = deadband(self.joystick.get_axis(5))
        #buttons
        #self.btn_a = self.joystick.get_button(0)
        #self.btn_b = self.joystick.get_button(1)
        #self.btn_x = self.joystick.get_button(2)
        #self.btn_y = self.joystick.get_button(3)
        #self.btn_lb = self.joystick.get_button(4)
        #self.btn_rb = self.joystick.get_button(5)
        #self.btn_back = self.joystick.get_button(6)
        #self.btn_start = self.joystick.get_button(7)
        
        #dpad
        #x,y = self.joystick.get_hat(0)
        #self.dpad_left = (x == -1)
        #self.dpad_right = (x == 1)
        #elf.dpad_up = (y == 1)
        #self.dpad_down = (y == -1)
            
        
    def printAxes(self):
        print self.axis_x1, self.axis_y1, self.axis_x2, self.axis_y2, self.axis_lt, self.axis_rt
        
    def printButtons(self):
        print self.btn_a, self.btn_b, self.btn_x, self.btn_y, self.btn_lb, self.btn_rb, self.btn_back, self.btn_start

    def printDPad(self):
        print self.dpad_left, self.dpad_right, self.dpad_up, self.dpad_down
        


if __name__ == "__main__":
    pygame.init()
    pygame.joystick.init()
    joystick1 = pygame.joystick.Joystick(0)
    joystick1.init()
    jReader = JoystickReader(joystick1)
    while True:
        pygame.event.get()
        jReader.updateValues()
        #jReader.printDPad()
        jReader.printAxes()
