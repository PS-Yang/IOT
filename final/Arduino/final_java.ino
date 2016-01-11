//# Define LED 13

int soundpin = A5;
int shockpin = A4;
int lightpin = A3;
int powerpin = 9;  
int led = 13;
int sound = 0;
int shock = 0;
int light = 0;
int count = 0;
int day = 8;
char type = 'O'; //-127~127
//set+tab
void setup()
{
  Serial.begin(9600);
  pinMode(powerpin,OUTPUT);
  pinMode(led, OUTPUT);
}
//loop+tab
void loop()
{
	switch (type) 
  	{
		    case 'O':
		    digitalWrite(powerpin,HIGH); 
		    digitalWrite(led, HIGH);
		      break;
		    case 'X':
		    digitalWrite(powerpin,LOW);
		    digitalWrite(led, LOW);
		      break;
		    default:
		      break;
		    //Serial.println(cmd);
	}
	sound = analogRead(soundpin);	
	shock = analogRead(shockpin);	 
	light = analogRead(lightpin);	
	Serial.println(String(sound)+","+String(shock)+","+String(light)+",'"+String(type)+"',"+String(day));
	delay(1000);
  	if(shock>250)
  	{
  		type='O';
  		count=0;
  	}
  	else if(count >= 10)
  	{
  	  type='X';
  	  count=0;
  	}
  	count++;	 
}
