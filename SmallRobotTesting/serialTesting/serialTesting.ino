#include <Servo.h>
Servo servo;
char incomingByte = 'a';
char outByte ='0';
void setup() {
  Serial.begin(9600);
  //  servo.attach(10);/
  //  servo.write(100);/
  pinMode(13, OUTPUT);
  digitalWrite(13, LOW);
  while (!Serial.available()) {

  }
}

void loop() {
  while (!Serial.available() > 0) {
  }
  // read the incoming byte:
  incomingByte = Serial.read();

  toggleLed(incomingByte);
//  /Serial.flush();

}
void toggleLed(char inByte) {
  outByte+=1;
  Serial.write(outByte);
  digitalWrite(13, !digitalRead(13));
}

