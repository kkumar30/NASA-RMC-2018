#include <Servo.h>
#include <Wire.h>
#include <LSM303.h>
#include <L3G.h>

L3G gyro;
String msg = "";

const unsigned int RATCHET_PIN = 9; // was 2
const unsigned int CAM_SERVO_1_PIN = 3;
const unsigned int CAM_SERVO_2_PIN = 4;
const unsigned int CAM_SERVO_3_PIN = 5;
const unsigned int CAM_SERVO_4_PIN = 6;
const unsigned int TEST_IR = 2;//For the IR Sensor
unsigned int testcamservoSetpoint;

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
  testCameraServo.attach(RATCHET_PIN);
  camServo1.attach(CAM_SERVO_1_PIN);
  camServo2.attach(CAM_SERVO_2_PIN);
  camServo3.attach(CAM_SERVO_3_PIN);
  camServo4.attach(CAM_SERVO_4_PIN);
  pinMode(8, INPUT);
  digitalWrite(8, HIGH);       // turn on pullup resistors
  Serial.begin(115200);
//  Serial3.begin(115200);

  Wire.begin();
  if (!gyro.init())
  {
    Serial.println("Failed to autodetect gyro type!");
    while (1);
  }

  gyro.enableDefault();

}

void loop()
{
  String inboundMessage = "";
  String outboundMessage = "";
  bool foundEndOfLine = false;
  while (1)
  {
    gyro.read();
    float dps = (float)gyro.g.x * 8.75 / 1000;
    dps += 0.45; //additional Calibration
    outboundMessage = "";
    //float heading = compass.heading();
    //    outboundMessage += makeMessageString("ScoopReedSwitch", analogRead(A0));
    //    outboundMessage += makeMessageString("BucketMaterialDepthSense", analogRead(A1));
    //    outboundMessage += makeMessageString("LimitSwitch", digitalRead(8));
    //    outboundMessage += makeMessageString("IRSensor", analogRead(TEST_IR));
    outboundMessage += makeMessageString("IMU", dps);
    //    Serial.println("There");

    //    delay(100);
    if (outboundMessage.length() > 0) {
      outboundMessage += "\r";
    }
    //    Serial.println(outboundMessage);
    //    delay(100);
    for (int i = 0; i <= outboundMessage.length(); i++) {
      Serial.write(outboundMessage[i]);
      //      delay(100);
    }

    delay(10);
    //*****************************READ THE SERVO DATA AND WRITE SERVO*****************************************//
    if (Serial.available() > 0) {
      char data = Serial.read();
//      Serial3.print(data);
      if (data == '<') {
      }

      else if (data == '>') {
        if (msg.toInt() < 0) {
          msg = msg.charAt(msg.length() - 1);
        }
        testcamservoSetpoint = msg.toInt();
        msg = "";
      }

      else {
        msg += data - 48;
      }

    }
    writeMotorValues();
    analogWrite(A2, 255);
    if (testcamservoSetpoint == 40){
      analogWrite(A3, 255);
    }
    //********************************************************************************************************//
  }
}
void parseInboundMessage(String message)
{
  const int openIndex = message.indexOf('<');
  const int closeIndex = message.indexOf('>');
  //  const int colonIndex1 = message.indexOf(':');
  //  const int colonIndex2 = message.indexOf(':', colonIndex1+1);
  //  const int colonIndex3 = message.indexOf(':', colonIndex2+1);
  //  const int colonIndex4 = message.indexOf(':', colonIndex3+1);

  //  //if any of the above failed, the message is malformed and the system not attempt to do anything to it.
  //  if(openIndex == -1 || closeIndex == -1 || colonIndex1 == -1 || colonIndex2 == -1 || colonIndex3 == -1 || colonIndex4 == -1)
  //  {
  //    return;
  //  }
  String val1 = message.substring(openIndex + 1, closeIndex);
  //  if (val1.toInt() % 10 == 0) {
  //
  //    digitalWrite(13, HIGH);
  //  }
  //  else {
  //    digitalWrite(13, LOW);
  //  }
  //  String val2 = message.substring(colonIndex1+1, colonIndex2);
  //  String val3 = message.substring(colonIndex2+1, colonIndex3);
  //  String val4 = message.substring(colonIndex3+1, colonIndex4);
  //  String val5 = message.substring(colonIndex4+1, closeIndex);

  //  Serial.print("[" + val1 + "]");
  //  Serial.print("[" + val2 + "]");
  //  Serial.print("[" + val3 + "]");
  //  Serial.print("[" + val4 + "]");
  //  Serial.println("[" + val5 + "]");

  testcamservoSetpoint = val1.toInt();
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
  testCameraServo.write(testcamservoSetpoint);
//  camServo1.write(camServo1Setpoint);
//  camServo2.write(camServo2Setpoint);
//  camServo3.write(camServo3Setpoint);
//  camServo4.write(camServo4Setpoint);
}