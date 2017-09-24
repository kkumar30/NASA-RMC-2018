import cv2
import math

videoFile = "movie.avi"
#imagesFolder = "/other/images"
cap = cv2.VideoCapture(videoFile)
print cap.isOpened()
frameRate = cap.get(5) #frame rate
print frameRate



while(cap.isOpened()):
    frameId = cap.get(1) #current frame number
    ret, frame = cap.read()
    if (ret != True):
        break
    if (frameId % 10 == 0):
        filename = str(int(frameId))
        cv2.imshow(filename, frame)
        cv2.imshow('edges', frame)
    	# cv2.imshow('original', image)

    	if cv2.waitKey(10) & 0xff == ord('q'):
        	break

cv2.destroyAllWindows()
cap.release()
print "Done!"