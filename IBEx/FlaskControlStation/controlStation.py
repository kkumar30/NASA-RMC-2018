from flask import Flask, render_template, Response, request, redirect 	#you probably have this already, pip install flask
from flask_assets import Bundle, Environment		#pip install flask_assets
from camera import VideoCamera
import json, math
import sys
from threading import Thread
# import motorTest

print "Printing GUI Motor Message:"
# print motorTest.guiMotorMessage

app = Flask(__name__)
js = Bundle('test.js', 'motorData.js', 'modeSwitcher.js', 'jquery-3.3.1.min.js', 'p5.min.js', output='gen/main.js') #add whatever js files you need i.e. Bundle('test.js', 'file1.js', fileX.js', output=...)

assets = Environment(app)
assets.register('main_js', js)

opMode = 0;
msgs = []
m1 = []
m2 = []
m3 = []
m4 = []
m5 = []


def printmsgsthread(threadname):
    sys.path.insert(0, "C:\Users\Rayyan\Documents\GitHub\NASA-RMC-2018\IBEx\RobotSoftware\src")
    import motorTest
    global msgs
    while True:
        # print "Messages are:"
        # print motorTest.guiMotorMessage
        msgs = parsemsgs(motorTest.guiMotorMessage)
        # print "printing parsed message: ", msgs

def parsemsgs(test):
  a = test.split("><")
  lefttest = [x.replace(">", "") for x in a]
  righttest = [x.replace("<", "") for x in lefttest]
  final = [(x.split(":")) for x in righttest]
  # print "Final parsed", final
  return final

# def flaskupdate(message=""):
# 	listed_message = parsemsgs(message)
# 	fh = open("..//..//flask.txt", "w")
# 	# lines_of_text = [['1', '0', '0', '0', '0'],  ["1", '0', '0', '1.0', '0'], ["1", '0', '0', '0', '0']]
# 	for lin in listed_message:
# 		for val in lin:
# 			fh.write(val + "-")
# 		fh.write("\n")
# 	fh.close()
# 	return listed_message

@app.route('/')
def index():
	return render_template('index.html')

@app.route('/video_feed')
def video_feed():
    return Response(gen(VideoCamera()), mimetype='multipart/x-mixed-replace; boundary=frame')

def gen(camera):
    while True:
        frame = camera.get_frame()

        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n\r\n')

@app.route("/getServerData")
def send():
    # global msgs
    arr = msgs
    #print msgs
    values = ""
    values = json.dumps(arr)
    print(values)
    return values

@app.route("/getOpMode")
def sendMode():
    global opMode
    value = json.dumps([opMode])
    return value

@app.route('/receiver', methods = ['POST'])
def main():
    # global msgs
    runmotorThread = Thread(target=printmsgsthread, args=("Thread-2",))

    runmotorThread.start()
    # while(1):
        # print "*********************************************************************"
        # print msgs
        # print "*********************************************************************"

        # with open("..//flask.txt") as f:
        #     content = f.readlines()
        # f.close()
        # # you may also want to remove whitespace characters like \n at the end of each line
        # content = msgs


        # if len(content)>0:
        #     m1 = content[0]
        #     del m1[4]
    	# m2 = content[1]
    	# m3 = [0,0,1,0,0]
    	# m4 = [0,0,0,1,0]
    	# m5 = [0,0,0,0,1]

@app.route('/stopProgram', methods = ['POST'])
def modeStop():
    global opMode
    opMode = 0
    return Response()

@app.route('/teleop', methods = ['POST'])
def modeTeleop():
    global opMode
    opMode = 1
    return Response()

@app.route('/autonomous', methods = ['POST'])
def modeAuto():
    global opMode
    opMode = 2
    return Response()

if __name__ == '__main__':

    app.run(host="130.215.211.230", debug=True, threaded=True)