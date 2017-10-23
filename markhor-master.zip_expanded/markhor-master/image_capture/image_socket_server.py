import socket
import errno
import time
import cv2
import struct
import io
import numpy as np
import Queue
import traceback


def main():
    try:
        server_socket = socket.socket()
        server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        server_socket.bind(('0.0.0.0', 5001))
        server_socket.listen(0)
        connection = server_socket.accept()[0].makefile('rb')

        cv2.namedWindow("test-h264", cv2.WINDOW_NORMAL)
        video = cv2.VideoCapture(connection)
        while True:
            ret,frame = video.read()           
            cv2.imshow("test-h264",frame)
    except Exception as e:
        traceback.print_exc()
    finally:
        connection.close()
        server_socket.close()

if __name__ == "__main__":
    main()
