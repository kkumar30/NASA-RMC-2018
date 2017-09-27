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
#import matplotlib.pyplot as plt

# from Pipeline import GripPipeline
from redObject import RedObjectPipeline

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

    #TODO##########
    camera_x_center =  #in px
    camera_y_center =  #in px

    ###############


    # Find the bounding boxes of the contours to get x, y, width, and height
    for contour in pipeline.filter_contours_output:
        x, y, w, h = cv2.boundingRect(contour)
        center_x_positions.append(x + w / 2)  # X and Y are coordinates of the top-left corner of the bounding box
        center_y_positions.append(y + h / 2)
        widths.append(w)
        heights.append(h)

        print widths,heights

    # TODO implement this for an array
    #TODO implement a pxToFt function
    #We have now found hopefully only one contour
    #TODO find the distance from the center of the camera to the wall

    # distToWall =

    #With this distance we can use trig with the center of the camera to the x coordinate of the object to find the diagonal distance
    #find the difference in X

    # x_diff = convertPxToFt(abs(center_x_positions()-camera_x_center))
    # yaw = atan(x_diff/distToWall)
    # yaw_dist = distToWall*cos(yaw)

    #Now figure out the other diagonal distance with the y coordinate of the object
    # y_diff = convertPxToFt(abs(center_y_positions()-camera_y_center))
    pitch = atan(y_diff/distToWall)
    # diagonalDist = yaw_dist*cos(pitch)
    print "Total Distance to object" diagonalDist "ft"
    print "Horizontal Distance to object" distToWall "ft"



def main():
    print('Creating video capture')
    cap = cv2.VideoCapture(1)
    print "framerate", cap.get(5)

    print('Creating pipeline')
    pipeline = RedObjectPipeline()

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