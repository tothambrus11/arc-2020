#include "PRIZM.h";

PRIZM prizm;
EXPANSION expansion;

const double UNIT_ENCODER_COUNTS = 585;
const double UNIT_TURN_ONE_ROTATION = 4195;
const byte TOPIC_SET_SPEED_FORWARD = 1;
const byte TOPIC_STOP = 0;
const byte TOPIC_TURN_ROTATIONS = 2;
const byte TOPIC_GO_FORWARD_UNITS = 3;
const byte TOPIC_LED_STATE = 4;
const byte TOPIC_WAIT_FOR_LEFT_BUTTON_PRESSED = 5;
const byte TOPIC_WAIT_FOR_RIGHT_BUTTON_PRESSED = 6;
const byte TOPIC_STOP_LEFT = 7;
const byte TOPIC_STOP_RIGHT = 8;
const byte TOPIC_WAIT_FOR_ANY_BUTTON_PRESSED = 9;
const byte TOPIC_ARM_LEFT = 10;
const byte TOPIC_ARM_RIGHT = 11;
const byte TOPIC_ARM_UP = 12;


const byte leftSensorPin = 2;
const byte rightSensorPin = 9;

void setup() {

  // put your setup code here, to run once:
  Serial.begin(57600);
  
  prizm.PrizmBegin();
  prizm.setMotorInvert(1, true);

  pinMode(leftSensorPin, INPUT);
  pinMode(rightSensorPin, INPUT);
  prizm.setServoSpeed(1,70);
  prizm.setServoSpeed(2,40);
}

void move(double leftUnits, double rightUnits, double speed) {
  prizm.resetEncoders();
  prizm.setMotorTargets(speed, leftUnits * UNIT_ENCODER_COUNTS, speed, rightUnits * UNIT_ENCODER_COUNTS);
}

//Positive rotation is left
void turn(double rotations, byte speed) {
  prizm.resetEncoders();
  prizm.setMotorTargets(speed, -rotations * UNIT_TURN_ONE_ROTATION, speed, rotations * UNIT_TURN_ONE_ROTATION);
}

void waitForMotors(){
  while(prizm.readMotorBusy(1) || prizm.readMotorBusy(2));
}

byte topic;

void loop() {
  // put your main code here, to run repeatedly:
  if (Serial.available() > 0) {
    topic = Serial.read();
    if (topic == TOPIC_SET_SPEED_FORWARD) {
      waitForByte();
      byte speed = Serial.read();
      prizm.setMotorSpeeds(speed, speed);
    } else if (topic == TOPIC_STOP) {
      prizm.setMotorSpeeds(0,0);
    } else if(topic == TOPIC_TURN_ROTATIONS){
      float rotations = Serial.parseFloat();
      byte speed = _readByte();
      byte shouldWait = _readByte();
      turn(rotations, speed);
      if(shouldWait){
        waitForMotors();
        ok();
      }
    } else if(topic == TOPIC_GO_FORWARD_UNITS){
      float units = Serial.parseFloat();
      byte speed = _readByte();
      move(units, units, speed);
      byte shouldWait = _readByte();
      if(shouldWait){
        waitForMotors();
        ok();
      }
    } else if(topic == TOPIC_LED_STATE){
      byte state = _readByte();
      expansion.setMotorPower(1, 1, state);
    } else if(topic == TOPIC_WAIT_FOR_LEFT_BUTTON_PRESSED){
      while(!leftButtonIsPressed());
      ok();
    } else if(topic == TOPIC_WAIT_FOR_RIGHT_BUTTON_PRESSED) {
      while(!rightButtonIsPressed());
      ok(); 
    } else if(topic == TOPIC_STOP_LEFT){
      prizm.setMotorSpeed(1, 0);
    } else if(topic == TOPIC_STOP_RIGHT){
      prizm.setMotorSpeed(2, 0);  
    } else if(topic == TOPIC_WAIT_FOR_ANY_BUTTON_PRESSED){
      bool a = 0;
      bool b = 0;
      while(a == 0 && b == 0){
        a = leftButtonIsPressed();
        b = rightButtonIsPressed();  
      }
      Serial.write(a ? 0x12 : 0x13);
    } else if(topic == TOPIC_ARM_LEFT){
      prizm.setServoPosition(1,100 + 40);
    } else if(topic == TOPIC_ARM_RIGHT){
      prizm.setServoPosition(1, 100 - 40);
    } else if(topic == TOPIC_ARM_UP){
      byte angle = _readByte();
      prizm.setServoPosition(2, angle);
    }
  }
}

bool leftButtonIsPressed(){
  return !digitalRead(leftSensorPin);  
}

bool rightButtonIsPressed(){
  return !digitalRead(rightSensorPin);  
}

void ok(){
  Serial.write(0x11); // OK RESPONSE  
}
void waitForByte() {
  while (Serial.available() == 0);
}

byte _readByte(){
  waitForByte();
  return Serial.read();
}
