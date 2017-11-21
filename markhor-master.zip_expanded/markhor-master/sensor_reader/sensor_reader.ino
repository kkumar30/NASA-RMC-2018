#include <Servo.h>

const unsigned int RATCHET_PIN = 2;
const unsigned int CAM_SERVO_1_PIN = 3;
const unsigned int CAM_SERVO_2_PIN = 4;
const unsigned int CAM_SERVO_3_PIN = 5;
const unsigned int CAM_SERVO_4_PIN = 6;

unsigned int ratchetMotorSetpoint;
unsigned int camServo1Setpoint;
unsigned int camServo2Setpoint;
unsigned int camServo3Setpoint;
unsigned int camServo4Setpoint;

Servo ratchetMotor;
Servo camServo1;
Servo camServo2;
Servo camServo3;
Servo camServo4;

String inboundMessage = "";

void setup() 
{
  ratchetMotor.attach(RATCHET_PIN);
  camServo1.attach(CAM_SERVO_1_PIN);
  camServo2.attach(CAM_SERVO_2_PIN);
  camServo3.attach(CAM_SERVO_3_PIN);
  camServo4.attach(CAM_SERVO_4_PIN);
  
  Serial.begin(250000);
}

void loop() 
{
  String inboundMessage = "";
  String outboundMessage = "";
  bool foundEndOfLine = false;
  while(1)
  {
  
    outboundMessage = "";
    outboundMessage += makeMessageString("ScoopReedSwitch", analogRead(A0));
    outboundMessage += makeMessageString("BucketMaterialDepthSense", analogRead(A1));
    //Serial.println(outboundMessage);

    if(Serial.available() > 0)
    {
      int maxReadCount = 10;
      for(int i = 0; (i < maxReadCount) && !foundEndOfLine; i++)
      {
        if(Serial.available())
        {
          //<180:0:90:45:135>
          char readByte = Serial.read();
          inboundMessage += readByte;
          if(readByte == '>')
          {
            foundEndOfLine = true;
          }//endif
          
        }//endif
        
      }//endfor
      
    }//endif

    if(foundEndOfLine)
    {
      foundEndOfLine = false;
      parseInboundMessage(inboundMessage);
      writeMotorValues();
      clearInboundMessage(inboundMessage);
    }
    
  }

  
}

void parseInboundMessage(String message)
{
  const int openIndex = message.indexOf('<');
  const int closeIndex = message.indexOf('>');
  const int colonIndex1 = message.indexOf(':');
  const int colonIndex2 = message.indexOf(':', colonIndex1+1);
  const int colonIndex3 = message.indexOf(':', colonIndex2+1);
  const int colonIndex4 = message.indexOf(':', colonIndex3+1);

  //if any of the above failed, the message is malformed and the system not attempt to do anything to it.
  if(openIndex == -1 || closeIndex == -1 || colonIndex1 == -1 || colonIndex2 == -1 || colonIndex3 == -1 || colonIndex4 == -1)
  {
    return;
  }

  String val1 = message.substring(openIndex+1, colonIndex1);
  String val2 = message.substring(colonIndex1+1, colonIndex2);
  String val3 = message.substring(colonIndex2+1, colonIndex3);
  String val4 = message.substring(colonIndex3+1, colonIndex4);
  String val5 = message.substring(colonIndex4+1, closeIndex);

//  Serial.print("[" + val1 + "]");
//  Serial.print("[" + val2 + "]");
//  Serial.print("[" + val3 + "]");
//  Serial.print("[" + val4 + "]");
//  Serial.println("[" + val5 + "]");

  ratchetMotorSetpoint = val1.toInt();
  camServo1Setpoint = val2.toInt();
  camServo2Setpoint = val3.toInt();
  camServo3Setpoint = val4.toInt();
  camServo4Setpoint = val5.toInt();
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
  return msg;
}

void writeMotorValues()
{
  ratchetMotor.write(ratchetMotorSetpoint);
  camServo1.write(camServo1Setpoint);
  camServo2.write(camServo2Setpoint);
  camServo3.write(camServo3Setpoint);
  camServo4.write(camServo4Setpoint);
}

