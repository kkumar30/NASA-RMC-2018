import cv2

cap = cv2.VideoCapture(-1)
print cap.isOpened()

while True:
    ret, frame = cap.read()
    cv2.imshow("Frame", frame)

    if cv2.waitKey(1) & 0xFF == ord("q"):
        break

cap.release()
