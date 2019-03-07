#include <SoftwareSerial.h>
//SoftwareSerial mySerial(13, 12); // RX, TX

#define enA 6
#define in1 3
#define in2 5
#define in3 9
#define in4 10
#define enB 8

void setup() {
  Serial.begin(9600);//Menerima input dari bluetooth
  
  pinMode(enA, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);
  pinMode(enB, OUTPUT);
}

char input = '0';

void loop() {
  if(Serial.available() > 0){
    input = Serial.read();   
    Serial.println(input);
  }else{
//    Serial.println("No data!");
  }


 switch(input){      
    case '1': {majuLurus();}; break;
    case '2': {majuKiri();}; break;
    case '3': {majuKanan();}; break;
    case '4': {mundurLurus();}; break;
    case '5': {mundurKiri();}; break;
    case '6': {mundurKanan();}; break;
    default: {diam();}; break;
  }

  mesinNyala();
}

void majuLurus(){
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
}

void mundurLurus(){
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

void diam(){
  digitalWrite(in1, HIGH);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, HIGH);
}

void mundurKiri(){
  digitalWrite(in1, HIGH);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

void majuKiri(){
  digitalWrite(in1, HIGH);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
}

void majuKanan(){
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, HIGH);
}

void mundurKanan(){
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, HIGH);
}

void mesinNyala(){
  analogWrite(enA, 255);
  analogWrite(enB, 255);
}
