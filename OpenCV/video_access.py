import cv2
import matplotlib.pyplot as plt

cap = cv2.VideoCapture(1)
print cap.isOpened()

while True:
    ret, frame = cap.read()
    plt.imshow(frame)
    plt.show()
    if cv2.waitKey(1) & 0xFF == ord("q"):
        break


cap.release()
