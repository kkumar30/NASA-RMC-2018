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
int servoPin = 10;
int angleTurned = 0;
int pose = 0;
int data = 0;
boolean left;
boolean found = false;
// You can also make another motor on port M2
//Adafruit_DCMotor *myOtherMotor = AFMS.getMotor(2);

///CHANGE THIS
int ticksPerDeg = 14;

void setup() {
  Serial.begin(9600);           // set up Serial library at 9600 bps

  camera.attach(servoPin);
  camera.write(180);
  AFMS.begin();  // create with the default frequency 1.6KHz
  //AFMS.begin(1000);  // OR with a different frequency, say 1KHz
  rightDriveEnc.write(0);
  leftDriveEnc.write(0);

  // Set the speed to start, from 0 (off) to 255 (max speed)
  leftDrive->setSpeed(150);
  rightDrive->setSpeed(140);
  leftDrive->run(FORWARD);
  rightDrive->run(FORWARD);

  // turn on motor
  leftDrive->run(RELEASE);
  rightDrive->run(RELEASE);
  camera.write(40);
  delay(1000);
  leftDrive->run(FORWARD);
  rightDrive->run(FORWARD);
  delay(800);
  leftDrive->run(RELEASE);
  rightDrive->run(RELEASE);
  driveToZone();
  //  if(found){
  //    turnLeft(90);
  //  }

  //  while (!Serial.available()) {
  //  }
  //  turnRight(90);
  //  /
  ///turnLeft(130);
  //turnLeft(90);
  //  Serial.print(leftDriveEnc.read());

}

void loop() {

  //camera.write(180);
  //  angleTurned =sweepForTarget();
  //  delay(10);
  //  //centerWithTarget(angleTurned);
  //  Serial.println("Done");
  //  /centerWithTarget(sweepForTarget());
  //Serial.write('k');
  // /

  //  boolean side = determineOrientation(angle);
  //  if (side) {
  //    turnRight(30);
  //    driveToZone();
  //  }
  //  else if (!side) {
  //    turnLeft(120);
  //    driveToZone();

  //  }
}
void centerWithTarget(int cameraAngle) {
  switch (determineOrientation(cameraAngle)) {
    case 1:
      break;
  }
}
boolean determineOrientation(int angleWent) {
  //  while (!Serial.available() > 0) {
  //  }
  //  // read the incoming byte:
  //  char incomingByte = Serial.read();
  //  if (incomingByte == 'l') {
  //    left = true;
  //
  //  }
  //  else if (incomingByte == 'r') {
  //    left = false;
  //  }
  left = false;
  return left;
}

void turnRight(int degrees) {
  //RESET ENCODERS
  leftDriveEnc.write(0);
  rightDriveEnc.write(0);

  //Determine number of ticks needed
  int ticks = ticksPerDeg * degrees;

  //  leftDrive->setSpeed(150);
  //  rightDrive->setSpeed(140);

  while (leftDriveEnc.read() <  ticks && rightDriveEnc.read() <  ticks) {

    leftDrive->run(BACKWARD);
    rightDrive->run(BACKWARD);

  }
  //  if (leftDriveEnc.read() < (-1 * ticks) && rightDriveEnc.read() < (-1 * ticks)) {

  leftDrive->run(RELEASE);
  rightDrive->run(RELEASE);

}
void turnLeft(int degrees) {
  //RESET ENCODERS
  leftDriveEnc.write(0);
  rightDriveEnc.write(0);

  //Determine number of ticks needed
  int ticks = ticksPerDeg * degrees;

  leftDrive->setSpeed(150);
  rightDrive->setSpeed(140);

  while (leftDriveEnc.read() > (-1 * ticks) && rightDriveEnc.read() > (-1 * ticks)) {

    leftDrive->run(FORWARD);
    rightDrive->run(FORWARD);

  }
  //  if (leftDriveEnc.read() < (-1 * ticks) && rightDriveEnc.read() < (-1 * ticks)) {
  leftDrive->setSpeed(0);
  rightDrive->setSpeed(0);

  leftDrive->run(RELEASE);
  rightDrive->run(RELEASE);



}
void driveToZone() {
  //RESET ENCODERS
  leftDriveEnc.write(0);
  rightDriveEnc.write(0);

  //Determine number of ticks needed

  //  leftDrive->setSpeed(150);
  //  rightDrive->setSpeed(140);

  while (leftDriveEnc.read() < 3000 && rightDriveEnc.read() > -3000) {

    leftDrive->run(BACKWARD);
    rightDrive->run(FORWARD);

  }
  //  if (leftDriveEnc.read() < (-1 * ticks) && rightDriveEnc.read() < (-1 * ticks)) {

  leftDrive->run(RELEASE);
  rightDrive->run(RELEASE);


}
void sweepForTarget() {
  int angleTurned;
  found = false;
  for (int i = 180; i >= 40; i--) {
    camera.write(i);
    delay(20);
    Serial.print(found);

  }
  camera.write(180);
  found = true;

  Serial.print(found);


  ///
}


