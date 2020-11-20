#define M1A D5
#define M1B D6
#define M2A D7
#define M2B D8
#define M3A D3
#define M3B D4
#define M4A D1
#define M4B D2

long counter1 = 0;
long counter2 = 0;
long counter3 = 0;
long counter4 = 0;

void setup() {
  // put your setup code here, to run once:
  pinMode(M1A, INPUT_PULLUP);
  pinMode(M2A, INPUT_PULLUP);
  pinMode(M3A, INPUT_PULLUP);
  pinMode(M4A, INPUT_PULLUP);

  pinMode(M1B, INPUT_PULLUP);
  pinMode(M2B, INPUT_PULLUP);
  pinMode(M3B, INPUT_PULLUP);
  pinMode(M4B, INPUT_PULLUP);

  attachInterrupt(digitalPinToInterrupt(M1A), m1Interrupt, CHANGE);
  attachInterrupt(digitalPinToInterrupt(M2A), m2Interrupt, CHANGE);
  attachInterrupt(digitalPinToInterrupt(M3A), m3Interrupt, CHANGE);
  attachInterrupt(digitalPinToInterrupt(M4A), m4Interrupt, CHANGE);
  Serial.begin(115200);
}


void m1Interrupt() {
  if (digitalRead(M1A) == digitalRead(M1B)) {
    counter1--;
  } else {
    counter1++;
  }
}

void m2Interrupt() {
  if (digitalRead(M2A) == digitalRead(M2B)) {
    counter2++;
  } else {
    counter2--;
  }
}

void m3Interrupt() {
  if (digitalRead(M3A) == digitalRead(M3B)) {
    counter3++;
  } else {
    counter3--;
  }
}

void m4Interrupt() {
  if (digitalRead(M4A) == digitalRead(M4B)) {
    counter4++;
  } else {
    counter4--;
  }
}

void loop() {
  Serial.println(String(counter1) + " " + String(counter2) + " " + String(counter3) + " " + String(counter4));
}
