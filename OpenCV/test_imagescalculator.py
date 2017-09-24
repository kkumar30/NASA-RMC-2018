import numpy  # requires the numpy+mkl pkg
from scipy.spatial import distance as dist
from imutils import perspective
from imutils import contours
import numpy as np
import argparse
import imutils
import cv2
import time
# import matplotlib.pyplot as plt


#To find the midpoint
def midpoint(point1, point2):
	return (point1[0] + point2[0]) * 0.5, (point1[1] + point2[1]) * 0.5

#To find the marker
def find_marker(image):
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY) #convert image to grayscale
    # blur = cv2.cvtColor(gray, (5,5), 0)

    # --------GAUSSIAN BLUR--------------------------------------------------------
    # Gaussian Blurring to remove noise
    # Define a kernel size for Gaussian smoothing / blurring
    kernel_size = 5
    blur = cv2.GaussianBlur(gray, (kernel_size, kernel_size), 0)

    edged = cv2.Canny(blur, 35, 125)
    edged = cv2.Canny(blur, 100, 200)
    # Find the contours in the edged image and keep the largest one;
    # we'll assume that this is our collection sticker in the image
    # cv2.findContours(edged.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    (_, cnts, _) = cv2.findContours(edged.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    print len(cnts)
    # plt.imshow(edged)
    # plt.show()
#    (cnts, _) = cv2.findContours(edged.copy(), cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)
    c = max(cnts, key=cv2.contourArea)
    # print cv2.minAreaRect(c)
    """The output of cv2.minAreaRect() is ((x, y), (w, h), angle)"""
    return cv2.minAreaRect(c)


# compute and return the distance from the maker to the camera
def distance_to_camera(knownWidth, focalLength, perWidth):
    print "perWidth", perWidth
    # print "focalLength", focalLength
    # print "returning = ", (knownWidth * focalLength) / perWidth
    return (knownWidth * focalLength) / perWidth

# initialize the known distance from the camera to the object, which in this case is 24 inches
KNOWN_DISTANCE = 48.0
# initialize the known object width, which in this case, the piece of paper is 11 inches wide
KNOWN_WIDTH = 11.0
KNOWN_FOCAL_LENGTH = 543.458329634
# initialize the list of images that we'll be using
IMAGE_PATHS = ["test_images/48.jpg", "test_images/24.jpg"]

# load the first image that contains an object that is KNOWN TO BE 2 feet
# from our camera, then find the paper marker in the image, and initialize
# the focal length
# image = cv2.imread(IMAGE_PATHS[0])
image = cv2.imread("test_images/48.jpg")
marker = find_marker(image)
focalLength = (marker[1][0] * KNOWN_DISTANCE) / KNOWN_WIDTH
dis = distance_to_camera(KNOWN_WIDTH, focalLength, marker[1][0])
# print "Focal length "  , focalLength
print "Distance to camera =", dis

"""
Better display of the image files
with the number at the bottom og the screen 
"""
# loop over the images
for imagePath in IMAGE_PATHS:
    # load the image, find the marker in the image, then compute the
    # distance to the marker from the camera
    image = cv2.imread(imagePath)
    marker = find_marker(image)
    inches = distance_to_camera(KNOWN_WIDTH, focalLength, marker[1][0])
    print "Distance to camera =", inches

    # draw a bounding box around the image and display it
    # box = np.int0(cv2.cv.BoxPoints(marker))
    box = np.int0(cv2.boxPoints(marker))
    cv2.drawContours(image, [box], -1, (0, 255, 0), 2)
    cv2.putText(image, "%.2fft" % (inches / 12),
                (image.shape[1] - 200, image.shape[0] - 20), cv2.FONT_HERSHEY_SIMPLEX,
                2.0, (0, 255, 0), 3)

    cv2.imshow("image", image)
    cv2.imwrite(imagePath + "_output.png", image)
    cv2.waitKey(0)



