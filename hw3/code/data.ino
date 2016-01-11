int pin = A2;    // select the input pin for the potentiometer
int value = 0;  // variable to store the value coming from the sensor

void setup()
{
  Serial.begin(9600);
  pinMode(pin, INPUT); 
}

void loop() 
{  
  value = analogRead(pin);    
  Serial.println(value);   
  delay(1000);//延遲1秒 
}
