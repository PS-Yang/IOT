//# Define LED 13

int led = 13;
int count;
int count_init = 1000;
int delta = 100;
int countMax= 2000;
int countMin= 100;
char cmd; //-127~127
//set+tab
void setup()
{
	Serial.begin(9600);
	pinMode(led, OUTPUT);
	count=count_init;
}
//loop+tab
void loop()
{
	if(Serial.available() > 0)
	{
		cmd = Serial.read(); //assign 的意思
		switch (cmd) {
		    case '+':
		      // do something
		      delta = 100;
		      break;
		    case '-':
		      // do something
		      delta = -100 ;
		      break;
		    default:
		      // do something
		    Serial.println("Please input \'+\' and \'-\' only!");
		}

		if(count>countMin && count<countMax)
		{
		    count -= delta;
		}
		else
     	count = count_init;
		
		
	}
	
	Serial.println(count);
	digitalWrite(led, HIGH);
	delay(count); //2 second
	digitalWrite(led, LOW);
	delay(count);
}
