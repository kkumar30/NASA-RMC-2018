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

from Pipeline import GripPipeline


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

        print widths,heights





def main():
    print('Creating video capture')
    cap = cv2.VideoCapture(0)
    print "framerate", cap.get(5)

    print('Creating pipeline')
    pipeline = GripPipeline()

    print('Running pipeline')
    while cap.isOpened():
        have_frame, frame = cap.read()
        if have_frame:
            pipeline.process(frame)
            img1 = frame.copy()
            cv2.drawContours(img1, pipeline.filter_contours_output, -1, (255, 0, 0), 3)
            cv2.imshow("contours", img1)
            getDistance(pipeline)

    print('Capture closed')


if __name__ == '__main__':
    main()