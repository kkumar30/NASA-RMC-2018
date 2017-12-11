
//#include<IOpins.h>
#include "IOpins.h"

void setup(){}

void loop(){
  digitalWrite(lmbrkpin,lmbrake>0);                     // if left brake>0 then engage electronic braking for left motor
  digitalWrite(lmdirpin,lmspeed>0);                     // if left speed>0 then left motor direction is forward else reverse
  analogWrite (lmpwmpin,abs(lmspeed));                  // set left PWM to absolute value of left speed - if brake is engaged then PWM controls braking
   
  digitalWrite(rmbrkpin,rmbrake>0);                     // if right brake>0 then engage electronic braking for right motor
  digitalWrite(rmdirpin,rmspeed>0);                     // if right speed>0 then right motor direction is forward else reverse
  analogWrite (rmpwmpin,abs(rmspeed));                  // set right PWM to absolute value of right speed - if brake is engaged then PWM controls braking
  
}


