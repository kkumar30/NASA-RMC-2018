#include <Encoder.h>

/*
  This is a test sketch for the Adafruit assembled Motor Shield for Arduino v2
  It won't work with v1.x motor shields! Only for the v2's with built in PWM
  control

  For use with the Adafruit Motor Shield v2
  ---->	http://www.adafruit.com/products/1438
*/

#include <Wire.h>
#include <Adafruit_MotorShield.h>
#include "utility/Adafruit_MS_PWMServoDriver.h"
#include "Servo.h"

Encoder rightDriveEnc(2, 3);
Encoder leftDriveEnc(18, 19);
// Create the motor shield object with the default I2C address
Adafruit_MotorShield AFMS = Adafruit_MotorShield();
// Or, create it with a different I2C address (say for stacking)
// Adafruit_MotorShield AFMS = Adafruit_MotorShield(0x61);

// Select which 'port' M1, M2, M3 or M4. In this case, M1
Adafruit_DCMotor *leftDrive = AFMS.getMotor(1);
Adafruit_DCMotor *rightDrive = AFMS.getMotor(2);
Servo camera;
int servoPin = 0;


// You can also make another motor on port M2
//Adafruit_DCMotor *myOtherMotor = AFMS.getMotor(2);

///CHANGE THIS
float ticksPerDeg = 0;

void setup() {
  Serial.begin(9600);           // set up Serial library at 9600 bps
  Serial.println("Adafruit Motorshield v2 - DC Motor test!");

  camera.attach(servoPin);
  camera.write(0);
  AFMS.begin();  // create with the default frequency 1.6KHz
  //AFMS.begin(1000);  // OR with a different frequency, say 1KHz
  rightDriveEnc.write(0);
  leftDriveEnc.write(0);
  Serial.println("stuck1");
  // Set the speed to start, from 0 (off) to 255 (max speed)
  leftDrive->setSpeed(150);
  rightDrive->setSpeed(150);
  leftDrive->run(FORWARD);
  rightDrive->run(FORWARD);

  // turn on motor
  leftDrive->run(RELEASE);
  rightDrive->run(RELEASE);
}

void loop() {
  centerWithTarget(sweepForTarget);
  Serial.println("Done");
}
void centerWithTarget(int cameraAngle) {
  switch (determineOrientation(cameraAngle)) {
    case 1:
      break;
  }
}
int determineOrientation(int angleWent){
  int pose = 1;
  return pose;
}
void turnRight(int degrees) {
  //RESET ENCODERS
  leftDriveEnc.write(0);
  rightDriveEnc.write(0);

  //Determine number of ticks needed
  int ticks = ticksPerDeg * degrees;

  leftDrive->run(FORWARD);
  rightDrive->run(BACKWARD);

  while (rightDriveEnc.read() < ticks || leftDriveEnc.read() > (-1 * ticks)) {
    if (rightDriveEnc.read() > ticks) {
      rightDrive->run(RELEASE);
    }
    if (leftDriveEnc.read() < (-1 * ticks)) {
      leftDrive->run(RELEASE);
    }
  }

}
void turnLeft(int degrees) {
  //RESET ENCODERS
  leftDriveEnc.write(0);
  rightDriveEnc.write(0);

  //Determine number of ticks needed
  int ticks = ticksPerDeg * degrees;

  leftDrive->run(FORWARD);
  rightDrive->run(BACKWARD);

  while (leftDriveEnc.read() < ticks || rightDriveEnc.read() > (-1 * ticks)) {
    if (leftDriveEnc.read() > ticks) {
      leftDrive->run(RELEASE);
    }
    if (rightDriveEnc.read() < (-1 * ticks)) {
      rightDrive->run(RELEASE);
    }
  }

}
int sweepForTarget() {
  int angleTurned;
  for (int i; i = 0; i <= 180) {
    camera.write(i);
    if (Serial.read() == "s") {
      angleTurned = i;
    }
  }
  return angleTurned;
}

