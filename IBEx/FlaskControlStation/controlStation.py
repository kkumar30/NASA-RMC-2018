from flask import Flask, render_template, Response, request, redirect 	#you probably have this already, pip install flask
from flask_assets import Bundle, Environment		#pip install flask_assets
from camera import VideoCamera
import json, math					

app = Flask(__name__)
js = Bundle('test.js', 'motorData.js', 'modeSwitcher.js', 'jquery-3.3.1.min.js', 'p5.min.js', output='gen/main.js') #add whatever js files you need i.e. Bundle('test.js', 'file1.js', fileX.js', output=...)

assets = Environment(app)
assets.register('main_js', js)

opMode = 0;

m1 = []
m2 = []
m3 = []
m4 = []
m5 = []

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
    global m1
    global m2
    global m3
    global m4
    global m5

    arr = [m1, m2, m3, m4, m5]
    values = json.dumps(arr)
    #print(values)
    return values

@app.route("/getOpMode")
def sendMode():
    global opMode
    value = json.dumps([opMode])
    return value

@app.route('/receiver', methods = ['POST'])
def main():
    global m1
    global m2
    global m3
    global m4
    global m5

    while(1):
        with open("..//flask.txt") as f:
            content = f.readlines()
        f.close()
        # you may also want to remove whitespace characters like `\n` at the end of each line
        content = [x.split("-") for x in content]
        print content[0]


        if len(content)>0:
            m1 = content[0]
            del m1[4]
    	m2 = content[1]
    	m3 = [0,0,1,0,0]
    	m4 = [0,0,0,1,0]
    	m5 = [0,0,0,0,1]

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
	#app.run(debug=True, threaded=True) #runs on localhost port 5000
	app.run(host="130.215.211.230", debug=True, threaded=True)