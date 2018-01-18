"""
Sample program that uses a generated GRIP pipeline to detect red areas in an image and publish them to NetworkTables.
"""
import numpy  # requires the numpy+mkl pkg
# from scipy.spatial import distance as dist
from imutils import perspective
from imutils import contours
import numpy as np
import argparse
import imutils
import time
import cv2
import math
#import CameraClient
# import matplotlib.pyplot as plt
#test commit
# from Pipeline import GripPipeline



from grip import GripPipeline
from pipeline_with_angle import Pipeline
import serial

ser = serial.Serial('/dev/tty.usbserial', 9600)


cameraWidth = 640 #px
cameraHeight = 480 #px
#focalLength = 4 #mm http://support.logitech.com/en_us/article/17556
#widthOfObject = 292.1 #mm, 11.5in
widthOfObject = 152.4
widthOfTall = 100
widthOfWide = 200
widthOfTarget = 0

#CHANGE THIS TO THE REAL NUMBER
distToWall = 10;

dFoV = 74 #deg
dFoV_test = 60 #deg
foundAngle = 1
gotDist = False
diagonal = math.sqrt(math.pow(cameraWidth,2)+math.pow(cameraHeight,2))
# focalLength = (diagonal/2)/math.tan(math.radians(dFoV/2))     #Using a ratio with a known focal length and FOV of a similar logitech webcam
#focalLength = 3 #mm
focalLength = (diagonal/2)/math.tan(math.radians(dFoV_test/2))
print "first focal length",focalLength
#print "2nd focal length",focalLength2

# TODO##########
camera_x_center = cameraWidth/2   #in px
camera_y_center = cameraHeight/2  #in px

###############
def convertPxToMM(px):
   return (widthOfTarget*focalLength)/px

def getRobotAngle(heights, widths, minAreaIndex, maxAreaIndex):
    MAX_BOX_AREA = 1000
    MIN_BOX_AREA= 10
    curr_box1 = widths[maxAreaIndex]*heights[maxAreaIndex] #left
    curr_box2 = widths[minAreaIndex]*heights[minAreaIndex]
    # curr_angle = 90
    #if we are closer to the tall rectangle
    if heights[maxAreaIndex]> widths[maxAreaIndex]:
        widthOfTarget = widthOfTall
        ratio_angle = curr_box2/float(curr_box1)
        ser.write('l')
        print "we are on the left"


    #if we are closer to the wide rectangle
    elif heights[maxAreaIndex]< widths[maxAreaIndex]:
        widthOfTarget = widthOfWide
        ratio_angle = curr_box1/float(curr_box2)
        ser.write('r')
        print "we are on the right"

    print "Areas are \n"
    print "Left Area = ", curr_box1, "Right Area= ", curr_box2
    print "Diff = ", curr_box1 - curr_box2    
    print "Serial read: ",ser.read()
    print ratio_angle
    try:
        angle = math.degrees(math.asin(ratio_angle))
    except ValueError as e:
        print 'ValueError'
        # angle = -100
        angle = math.degrees(math.asin(1/float(ratio_angle)))
    print "Angle = ", angle
    return angle

def getContourAreas(heights, widths, minAreaIndex, maxAreaIndex):
    box1 = widths[maxAreaIndex]*heights[maxAreaIndex] #left
    box2 = widths[minAreaIndex]*heights[minAreaIndex] #right
    return box1, box2  

def center(pipeline):
    """
    Performs extra processing on the pipeline's outputs and publishes data to NetworkTables.
    :param pipeline: the pipeline that just processed an image
    :return: None
    """
    center_x_positions = []
    center_y_positions = []
    widths = []
    heights = []
    numContours = 0




    # Find the bounding boxes of the contours to get x, y, width, and height
    for contour in pipeline.filter_contours_output:
        x, y, w, h = cv2.boundingRect(contour)
        center_x_positions.append(x + w / 2)  # X and Y are coordinates of the top-left corner of the bounding box
        center_y_positions.append(y + h / 2)
        widths.append(w)
        heights.append(h)
        numContours+=1

    # print widths,heights

    # TODO implement this for an array

    # TODO implement a pxToFt function
    # We have now found hopefully only 2 contours
    # TODO find the distance from the center of the camera to the wall
    print "Number Contours=", numContours
    if numContours == 2:
        #Determine which target is closer to the robot
        ser.write('C')
        print widths
        print heights
        area = []
        for i in range(len(widths)):
            area.append(widths[i]*heights[i])
        areas = np.array(area)
        maxAreaIndex = np.argsort(areas)[-1]
        minAreaIndex = np.argsort(areas)[-2]


        curr_angle = getRobotAngle(heights,widths,minAreaIndex,maxAreaIndex)
        print curr_angle


def main():
    print('Creating video capture')

    cap = cv2.VideoCapture(1)
    print "framerate", cap.get(5)

    print('Creating pipeline')
    # pipeline = RedObjectPipeline()
    pipeline = Pipeline()

    print('Running pipeline')
    while cap.isOpened():
        have_frame, frame = cap.read()
        if have_frame:
            pipeline.process(frame)
            img1 = frame.copy()
            cv2.imshow("contours", cv2.drawContours(img1, pipeline.filter_contours_output, -1, (255, 0, 0), 3))
            center(pipeline)

        if cv2.waitKey(1) & 0xFF == ord("q"):
            break

    print('Capture closed')
    cap.release()

if __name__ == '__main__':
    main()
