from flask import Flask, render_template, Response
#from camera import VideoCamera
#import motorTest #import the file where u run the motors (future main)
#import sys
#sys.path.insert(0, "home/dom/PARMPUTITHERE/NASA-RMC-2018/IBEx/RobotSoftware/src")
#print sys.path
from  motorTest import cameraOutput as image
#print str(len(motorTest.frame))
#frame_ = motorTest.frame


app = Flask(__name__)

@app.route('/')
def index():
    print "11111111111111111111111"
    return render_template('index.html')

def gen(image):
    #print "K"
    while True:
       # print "K"
        #frame = camera.get_frame()
     #   processed_frame = frame.__processed_function()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + image + b'\r\n\r\n')

@app.route('/video_feed')
def video_feed():
    #frame = motorTest.___dadadafunction__()
    #print "K"
    return Response(gen(image),
                    mimetype='multipart/x-mixed-replace; boundary=frame')

if __name__ == '__main__':
    app.run(host='192.168.0.150', debug=True, threaded = False, use_reloader=False)
