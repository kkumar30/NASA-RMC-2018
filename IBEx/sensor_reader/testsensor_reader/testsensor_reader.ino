#include <Servo.h>
#include <Wire.h>
#include <LSM303.h>
#include <L3G.h>

L3G gyro;
String msg = "";

const unsigned int CAM_SERVO_1_PIN = 9;
const unsigned int CAM_SERVO_2_PIN = 4;
const unsigned int TEST_IR = 2;//For the IR Sensor
const unsigned int LIMIT1_PIN = 7;
const unsigned int LIMIT2_PIN = 8;
const unsigned int BUMP1_PIN = 11;
const unsigned int BUMP2_PIN = 12;

const int ledPin = 6;
int testcamservoSetpoint;
int prevSetpoint;
int ledVal = 0;

unsigned int ratchetMotorSetpoint;
unsigned int camServo1Setpoint;
unsigned int camServo2Setpoint;
unsigned int camServo3Setpoint;
unsigned int camServo4Setpoint;
unsigned int testIRval; //For IR Sensor

Servo testCameraServo;
Servo camServo1;
Servo camServo2;
Servo camServo3;
Servo camServo4;
LSM303 compass;

String inboundMessage = "";

void setup()
{
  testCameraServo.attach(CAM_SERVO_1_PIN);
  //    camServo1.attach(CAM_SERVO_1_PIN);
  camServo2.attach(CAM_SERVO_2_PIN);
  //  camServo3.attach(CAM_SERVO_3_PIN);
  //  camServo4.attach(CAM_SERVO_4_PIN);
  pinMode(8, INPUT_PULLUP);
  pinMode(ledPin, OUTPUT);      //LED Pin
  pinMode(LIMIT1_PIN, INPUT_PULLUP);
  pinMode(LIMIT2_PIN, INPUT_PULLUP);
  pinMode(BUMP1_PIN, INPUT_PULLUP);
  pinMode(BUMP2_PIN, INPUT_PULLUP);
  Serial.begin(115200);
  Serial1.begin(115200);

  Wire.begin();
  if (!gyro.init())
  {
    Serial.println("Failed to autodetect gyro type!");
    while (1);
  }
  testCameraServo.write(0);
  camServo2.write(0);
  prevSetpoint = 90;
  gyro.enableDefault();

  digitalWrite(ledPin, 0);
  delay(500);
  digitalWrite(ledPin, 1);
  delay(500);
  digitalWrite(ledPin, 0);
  delay(500);
  digitalWrite(ledPin, 1);
  delay(500);
  digitalWrite(ledPin, 0);
  delay(500);
  digitalWrite(ledPin, 1);
  delay(500);
  digitalWrite(ledPin, 0);
}

void loop()
{
  String inboundMessage = "";
  String outboundMessage = "";
  bool foundEndOfLine = false;
  while (1)
  {
    int lim1 = digitalRead(LIMIT1_PIN);
    int lim2= digitalRead(LIMIT2_PIN);
    int bump1 = digitalRead(BUMP1_PIN);
    int bump2 = digitalRead(BUMP2_PIN);
    
    gyro.read();
    float dps = (float)gyro.g.x * 8.75 / 1000;
    dps += 0.55; //additional Calibration
    outboundMessage = "";
    //float heading = compass.heading();
    //    outboundMessage += makeMessageString("ScoopReedSwitch", analogRead(A0));
    //    outboundMessage += makeMessageString("BucketMaterialDepthSense", analogRead(A1));
    //    outboundMessage += makeMessageString("LimitSwitch", digitalRead(8));
    //    outboundMessage += makeMessageString("IRSensor", analogRead(TEST_IR));
    outboundMessage += makeMessageString("IMU", abs(dps));
    outboundMessage += makeMessageString("LED", float(ledVal));
    outboundMessage += makeMessageString("UpperLimitSwitch", float(lim1));
    outboundMessage += makeMessageString("LowerLimitSwitch", float(lim2));
    outboundMessage += makeMessageString("Bump1", float(bump1));
    outboundMessage += makeMessageString("Bump2", float(bump2));
    
    //    Serial.println("There");

    //    delay(100);
    if (outboundMessage.length() > 0) {
      outboundMessage += "\r";
    }

    //    Serial.flush();
    //    Serial.println((Serial.available()));
    //*****************************READ THE SERVO DATA AND WRITE SERVO*****************************************//
    if (Serial.available() > 0) {
      char data = Serial.read();
      //      Serial1.println(data);
      if (data == '<') {
      }

      else if (data == '>') {
        if (msg.toInt() < 0) {
          msg = msg.charAt(msg.length() - 1);
        }

        if (msg.toInt() == 8888) {
          pingLed();
        }
        if (msg.toInt() == 666) {
          testCameraServo.write(0);
          camServo2.write(0);
          delay(100);
          testCameraServo.detach();
          camServo2.detach();
          
        }
        
        else {
          const int colonIndex = msg.indexOf(':');
          testcamservoSetpoint = msg.substring(0,colonIndex).toInt();
          camServo2Setpoint = msg.substring(colonIndex+1).toInt();
//          testcamservoSetpoint = msg.toInt();
//          camServo2Setpoint = msg.toInt();
          writeMotorValues();
        }
        //**************************SENDING IMU MESSAGE HERE*******************************************
        for (int i = 0; i <= outboundMessage.length(); i++) {
          Serial.write(outboundMessage[i]);
        }
        outboundMessage = "";
        //********************************************************************************************

        msg = "";
      }

      else {
        msg += data;// - 48;
      }
    }

  }
}


void parseInboundMessage(String message)
{
  const int openIndex = message.indexOf('<');
  const int closeIndex = message.indexOf('>');
  const int colonIndex1 = message.indexOf(':');
  const int colonIndex2 = message.indexOf(':', colonIndex1+1);
  //  const int colonIndex3 = message.indexOf(':', colonIndex2+1);
  //  const int colonIndex4 = message.indexOf(':', colonIndex3+1);

  //  //if any of the above failed, the message is malformed and the system not attempt to do anything to it.
  //  if(openIndex == -1 || closeIndex == -1 || colonIndex1 == -1 || colonIndex2 == -1 || colonIndex3 == -1 || colonIndex4 == -1)
  //  {
  //    return;
  //  }
  //  String val1 = message.substring(openIndex + 1, closeIndex);
  String val1 = message.substring(openIndex + 1, colonIndex1);
  //  if (val1.toInt() % 10 == 0) {
  //
  //    digitalWrite(13, HIGH);
  //  }
  //  else {
  //    digitalWrite(13, LOW);
  //  }
  String val2 = message.substring(colonIndex1 + 1, colonIndex2);
  String val3 = message.substring(colonIndex2+1, closeIndex);
  //  String val4 = message.substring(colonIndex3+1, colonIndex4);
  //  String val5 = message.substring(colonIndex4+1, closeIndex);

  //  Serial.print("[" + val1 + "]");
  //  Serial.print("[" + val2 + "]");
  //  Serial.print("[" + val3 + "]");
  //  Serial.print("[" + val4 + "]");
  //  Serial.println("[" + val5 + "]");

  testcamservoSetpoint = val1.toInt();
  camServo2Setpoint = val2.toInt();
  ledVal = val3.toInt();
  //    Serial3.println("P = " +   testcamservoSetpoint);

  //  ratchetMotorSetpoint = val1.toInt();
  //  camServo1Setpoint = val2.toInt();
  //  camServo2Setpoint = val3.toInt();
  //  camServo3Setpoint = val4.toInt();
  //  camServo4Setpoint = val5.toInt();
}


void clearInboundMessage(String message)
{
  message = "";
}

String makeMessageString(String sensorName, int sensorValue)
{
  String msg = "<";
  msg += sensorName;
  msg += ":";
  msg += String(sensorValue);
  msg += ">";
  //  msg += "\r";
  return msg;
}


//For the Compass
String makeMessageString(String sensorName, float sensorValue)
{
  String msg = "<";
  msg += sensorName;
  msg += ":";
  msg += String(sensorValue);
  msg += ">";
  //  msg += "\r";
  return msg;
}

void writeMotorValues()
{
  //    Serial1.print("Wrote:");
  Serial1.print(testcamservoSetpoint);
  testCameraServo.write(testcamservoSetpoint);
  Serial1.print(camServo2Setpoint);
  camServo2.write(camServo2Setpoint);
  //  camServo1.write(camServo1Setpoint);
  //  camServo2.write(camServo2Setpoint);
  //  camServo3.write(camServo3Setpoint);
  //  camServo4.write(camServo4Setpoint);
}

void pingLed() {
  if (ledVal == 0) {
    digitalWrite(ledPin, 1);
    ledVal = 1;
  }
  else if (ledVal == 1) {
    digitalWrite(ledPin, 0);
    ledVal = 0;
  }
}

