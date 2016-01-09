
#include <SoftwareSerial.h>

// sensor
int soundpin = A5;
int shockpin = A4;
int lightpin = A3;
int powerpin = 9;  
int led = 13;
int sound = 0;
int shock = 0;
int light = 0;
int count = 0;
char type = 'O'; //-127~127

//connect
String SID = "WIFIID";
String PWD = "WIFIPASSWORD";
String IP = "serverip";
String file = "serverpath";

String str = ""; //http 回覆狀態

SoftwareSerial esp8266(8,9); 
// connect 8 to TX of wifi-esp8266
// connect 9 to RX of wifi-esp8266


void setup() {
    // enable debug serial
    Serial.begin(115200); 
    // enable software serial
    esp8266.begin(115200);
    pinMode(powerpin,OUTPUT);
    pinMode(led, OUTPUT);
    init_wifi();  //設定ESP8266,改變模式,連線wifi
}


void loop() {
   /*
   *  ESP8266 回覆http狀態
   */
   while(esp8266.available())
   {
     Serial.print(esp8266.readString());
     delay(100);
   }

  
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
  //上傳
   uploadData();

   
}


/*
 *  Setting ESP8266
*/
void init_wifi(){
  Serial.println("=======================================");
  Serial.println("|---  Esp8266 Setting  ---|\n");
  sendCommand("AT+RST",5000); // reset module
  sendCommand("AT+CWMODE=1",2000); // configure as access point
  sendCommand("AT+CWJAP=\""+SID+"\",\""+PWD+"\"",5000);
  sendCommand("AT+CIPMUX=0",2000); // configure for single connections
  Serial.println("\n|---  Setting Finish  ---|");
  Serial.println("=======================================");
}

/*
 *  Setting Esp8266 (Send Command)
*/
void sendCommand(String command, const int timeout)
{
    String response = "";    
    esp8266.println(command); // send the read character to the esp8266   
    long int time = millis();   
    while( (time+timeout) > millis())
    {
      while(esp8266.available())
      {    
        // The esp has data so display its output to the serial window 
        response = esp8266.readString(); // read the next character.
      }  
    }    

    Serial.println(command +" : "+ response);
    delay(100);
}

/* 
  Upload Data
*/
void uploadData()
{
  // convert to string
  // TCP connection
  String cmd = "AT+CIPSTART=\"TCP\",\"";
  cmd += IP; //host
  cmd += "\",80";
  esp8266.println(cmd);
   
  if(esp8266.find("Error")){
    Serial.println("AT+CIPSTART error");
    return;
  }
  // prepare GET string
  String getStr = "GET /"+file+"?value=";
  getStr += String(sound)+","+String(shock)+","+String(light)+",'"+String(type)+"'";
  getStr +=" HTTP/1.1\r\nHost:"+IP;
  getStr += "\r\n\r\n";

  // send data length
  cmd = "AT+CIPSEND=";
  cmd += String(getStr.length());
  esp8266.println(cmd);

  if(esp8266.find(">")){
    esp8266.print(getStr);
  }
  else{
    esp8266.println("AT+CIPCLOSE");
    // alert user
    Serial.println("AT+CIPCLOSE");
  }
  if(shock>0)
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
  delay(200);  
}