from flask import Flask, render_template, Response 	#you probably have this already, pip install flask
from flask_assets import Bundle, Environment		#pip install flask_assets
from camera import VideoCamera						

app = Flask(__name__)
js = Bundle('test.js', output='gen/main.js') #add whatever js files you need i.e. Bundle('test.js', 'file1.js', fileX.js', output=...)

assets = Environment(app)
assets.register('main_js', js)


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

if __name__ == '__main__':
	app.run(debug=True) #runs on localhost port 5000
	#app.run(host="130.215.11.31", debug=True)