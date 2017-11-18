"""
Sample program that uses a generated GRIP pipeline to detect red areas in an image and publish them to NetworkTables.
"""
import numpy  # requires the numpy+mkl pkg
from scipy.spatial import distance as dist
from imutils import perspective
from imutils import contours
import numpy as np
import argparse
import imutils
import time
import cv2
import math
# import matplotlib.pyplot as plt
#test commit
# from Pipeline import GripPipeline
from redObject import RedObjectPipeline
from techbible import GripPipeline

cameraWidth = 640 #px
cameraHeight = 480 #px
#focalLength = 4 #mm http://support.logitech.com/en_us/article/17556
# widthOfObject = 292.1 #mm, 11.5in
widthOfObject = 152.4
dFoV = 74 #deg
dFoV_test = 60 #deg

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
   return (widthOfObject*focalLength)/px

def getDistance(pipeline):
    """
    Performs extra processing on the pipeline's outputs and publishes data to NetworkTables.
    :param pipeline: the pipeline that just processed an image
    :return: None
    """
    center_x_positions = []
    center_y_positions = []
    widths = []
    heights = []




    # Find the bounding boxes of the contours to get x, y, width, and height
    for contour in pipeline.filter_contours_output:
        x, y, w, h = cv2.boundingRect(contour)
        center_x_positions.append(x + w / 2)  # X and Y are coordinates of the top-left corner of the bounding box
        center_y_positions.append(y + h / 2)
        widths.append(w)
        heights.append(h)

        #print widths,heights

    # TODO implement this for an array
    #TODO implement a pxToFt function
    #We have now found hopefully only one contour
    #TODO find the distance from the center of the camera to the wall
    if len(widths) == 1:
        print center_x_positions[0],center_y_positions[0],widths[0],heights[0]
        distToWall = convertPxToMM(widths[0])
        print "Horizontal Distance to object", distToWall/1000, "m"
        #With this distance we can use trig with the center of the camera to the x coordinate of the object to find the diagonal distance
        #find the difference in X

        # x_diff = convertPxToIn(center_x_positions[1]-camera_x_center)
        x_diff = abs(center_x_positions[0] - camera_x_center)
        print "x diff", x_diff, "px"
        yaw = math.atan(x_diff/focalLength)
        yaw_dist = distToWall/math.cos(yaw)
        print "yaw", math.degrees(yaw), "deg"
        print "yaw distance", yaw_dist/1000, "m"

        #Now figure out the other diagonal distance with the y coordinate of the object
        y_diff = abs(center_y_positions[0]-camera_y_center)
        pitch = math.atan(y_diff/focalLength)
        diagonalDist = yaw_dist/math.cos(pitch)
        print "pitch angle", math.degrees(pitch), "deg"
        print "Total Distance to object", diagonalDist/1000, "m"




def main():
    print('Creating video capture')
    cap = cv2.VideoCapture(2)
    print "framerate", cap.get(5)

    print('Creating pipeline')
    #pipeline = RedObjectPipeline()
    pipeline = GripPipeline()
    print('Running pipeline')
    while cap.isOpened():
        have_frame, frame = cap.read()
        if have_frame:
            pipeline.process(frame)
            img1 = frame.copy()

            cv2.imshow("contours", cv2.drawContours(img1, pipeline.filter_contours_output, -1, (255, 0, 0), 3))
            getDistance(pipeline)

        if cv2.waitKey(1) & 0xFF == ord("q"):
                break
    print('Capture closed')
    cap.release()

if __name__ == '__main__':
    main()
