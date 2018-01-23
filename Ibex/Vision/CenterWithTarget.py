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
import CameraClient
# import matplotlib.pyplot as plt
#test commit
# from Pipeline import GripPipeline
from grip import GripPipeline


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
        print "we are on the left"


    #if we are closer to the wide rectangle
    elif heights[maxAreaIndex]< widths[maxAreaIndex]:
        widthOfTarget = widthOfWide
        ratio_angle = curr_box1/float(curr_box2)
        print "we are on the right"

    print "Areas are \n"
    print "Left Area = ", curr_box1, "Right Area= ", curr_box2
    print ratio_angle
    angle = math.degrees(math.asin(ratio_angle))
    print "Angle = ", angle
    return angle


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

        #print widths,heights

    # TODO implement this for an array

    #TODO implement a pxToFt function
    #We have now found hopefully only 2 contours
    #TODO find the distance from the center of the camera to the wall

    if numContours == 2:
        #Determine which target is closer to the robot

        print widths
        print heights
        area = []
        for i in range(len(widths)):
            area.append(widths[i]*heights[i])
        areas = np.array(area)
        maxAreaIndex = np.argsort(areas)[-1]
        minAreaIndex = np.argsort(areas)[-2]

        #################GETTING ROBOT LOCATION AND ANGLE FROM THE AREAS########################
        # MAX_BOX_AREA = 1000
        # MIN_BOX_AREA= 10
        # curr_box1 = widths[maxAreaIndex]*heights[maxAreaIndex] #left
        # curr_box2 = widths[minAreaIndex]*heights[minAreaIndex]

        curr_angle = getRobotAngle(heights,widths,minAreaIndex,maxAreaIndex)
        print curr_angle
	############################################################################################

        #get close enough to the center of the closest target
        # x_diff = center_x_positions[maxAreaIndex]-camera_x_center
        # convertPxToMM(x_diff)






        # If on the left
        # gen_angle = MAX_BOX_AREA-curr_box1/float(curr_box2)
        # print gen_angle
        # print math.degrees(math.asin(gen_angle))

        # print area
        # #convertPxToMM(x_diff1, widthOfTarget)
        # yaw1 = math.degrees(math.atan(x_diff1/focalLength))
        # distToTarget1 = distToWall/math.cos(math.radians(yaw1))
        #
        # #Second Contour
        # x_diff2 = center_x_positions[1] - camera_x_center
        # yaw2 = math.degrees(math.atan(x_diff2/focalLength))
        #
        # distToTarget2 = distToWall/math.cos(math.radians(yaw2))
        #
        # print "Yaw1 ", yaw1, " distance 1", distToTarget1
        # print "Yaw2 ", yaw2, " distance 2", distToTarget2
        #
        #
        #


        # print center_x_positions[0],center_y_positions[0],widths[0],heights[0]
        # distToWall = convertPxToMM(widths[0])
        # # if(distToWall/1000 > .5):
        # #     ser.write('1')
        #
        #
        #
        # print "Horizontal Distance to object", distToWall/1000, "m"
        # #With this distance we can use trig with the center of the camera to the x coordinate of the object to find the diagonal distance
        # #find the difference in X
        # # while True:
        #     # x_diff = convertPxToIn(center_x_positions[1]-camera_x_center)
        # print "x diff", x_diff, "px"
        #
        # yaw_dist = distToWall/math.cos(yaw)
        #
        # print "yaw", math.degrees(yaw), "deg"
        # print "yaw distance", yaw_dist/1000, "m"
        # global foundAngle
        # global gotDist
        # if(math.degrees(yaw)>5):
        #     ser.write('4')
        #     foundAngle = 1
        # elif(math.degrees(yaw)<-5):
        #     ser.write('3')
        #     foundAngle = 1
        # else:
        #     if(foundAngle):
        #         ser.write('1')
        #         foundAngle = 0
        #     elif(distToWall/1000 <.35):
        #          ser.write('0')
        #          gotDist = True
        #     # This Sends the signal to the mini robot to turn left
        #     # ser.write(3)
        #
        # #Now figure out the other diagonal distance with the y coordinate of the object
        # y_diff = abs(center_y_positions[0]-camera_y_center)
        # pitch = math.atan(y_diff/focalLength)
        # diagonalDist = yaw_dist/math.cos(pitch)
        # print "pitch angle", math.degrees(pitch), "deg"
        # print "Total Distance to object", diagonalDist/1000, "m"
        #



def main():
    print('Creating video capture')
    cap = cv2.VideoCapture(1)
    print "framerate", cap.get(5)

    print('Creating pipeline')
    # pipeline = RedObjectPipeline()
    pipeline = GripPipeline()

    print('Running pipeline')
    while cap.isOpened():
        have_frame, frame = cap.read()
        if have_frame:
            pipeline.process(frame)
            img1 = frame.copy()
            CameraClient.sendIMGtoMaster(img1)
            cv2.imshow("contours", cv2.drawContours(img1, pipeline.filter_contours_output, -1, (255, 0, 0), 3))
            center(pipeline)

        if cv2.waitKey(1) & 0xFF == ord("q"):
            break

    print('Capture closed')
    cap.release()

if __name__ == '__main__':
    main()
