import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class IOTfinal implements SerialPortEventListener {
	SerialPort serialPort;//©w¸qserialportª«¥ó

	private BufferedReader input;//«Å§iinput buffer
	private static final int TIME_OUT = 2000;//³]©wµ¥«Ýport¶}±Òªº®É¶¡¡A³æ¦ì¬°²@¬í
	private static final int DATA_RATE = 9600;//³]©wbaud rate¬°9600

	//³]©wserver IP,±b¸¹,±K½X…£
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";//³]©wJDBC driver  
	static final String DB_URL = "jdbc:mysql://127.0.0.1/iot_final";//server IP«á±µ¸ê®Æ®w¦WºÙ
	static final String table = "final";
	static final String USER = "USER";
	static final String PASS = "PASS";
	
	public void initialize() {
		CommPortIdentifier portId = null;//©w¸qCommPortIdentifierª«¥ó¡A±±¨î±µ¦¬±µ¦¬port
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();//Àx¦s©Ò¦³¦³®Äªºport

		while (portEnum.hasMoreElements()) {//±½¹L©Ò¦³ªºport
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();//©w¸qcurrPortId
				if (currPortId.getName().equals("COM4")) {//³]©warduino serial port
					portId = currPortId;
					break;
				}	
		}
		
		if (portId == null) {//¦pªGcom port³]©w¿ù»~¡A·|¥X²{³o¤@¦æ
			System.out.println("Could not find COM port.");
			return;
		}
		
		try {//³s±µport
			//open serial port
			serialPort = (SerialPort) portId.open(this.getClass().getName(),TIME_OUT);
			
			//³]©wport parameters
			serialPort.setSerialPortParams(DATA_RATE,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

			//open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
//			OutputStream os=serialPort.getOutputStream();
			// add event listeners
			serialPort.addEventListener(this);// Registers a SerialPortEventListener object to listen for SerialEvents.
			serialPort.notifyOnDataAvailable(true);//Expresses interest in receiving notification when input data is available.

//			while(true)
//			{
//				System.out.print("½Ð¿é¤J:");
//				Scanner scanner = new Scanner(System.in);
//				String str = scanner.next();
//				if(str.equals("O"))
//				{
//					try {
//						os.write('O');
//						os.flush();
//		            } catch (IOException e) {
//		            	 e.printStackTrace();
//		            	 System.out.println(e);
//		            }
//				}
//				else if(str.equals("X"))
//				{
//					try {
//						os.write('X');
//						os.flush();
//		            } catch (IOException e) {
//		            	 e.printStackTrace();
//		            	 System.out.println(e);
//		            }
//				}
//				else if(str.equals("A"))
//					break;
//			}
			
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	//³B²zserial port¨Æ¥ó,Åª¨ú¸ê®Æ¨Ãprint¥X¨Ó
	public void serialEvent(SerialPortEvent oEvent) {
        Connection connection = null;//«Ø¥ßConnectionª«¥ó
        Statement statement = null;//«Ø¥ßStatementª«¥ó
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {//if data available on serial port
			try {
				//³s±µmysql database
	            connection = DriverManager.getConnection(DB_URL, USER, PASS);
	           // System.out.println("SQL Connection to database established!");
	            
				String inputLine=input.readLine();
				String sql="INSERT INTO "+table+" (sound,shock,light,type,count) VALUES ("+inputLine+")";
				System.out.println(sql);
				//°õ¦æquery
	            statement = connection.createStatement();
	            PreparedStatement pstmt = connection.prepareStatement(sql);
	            pstmt.executeUpdate();
	            
	            pstmt.close();
	            statement.close();
	            connection.close();
			} catch (SQLException e) {
	        	//Handle errors for JDBC
	            System.out.println("Connection Failed! Check output console");
	            return;
	        } catch (Exception e) {
				//System.err.println(e.toString());
			}finally {
	            try
	            {
	                if(connection != null)
	                    connection.close();
	               // System.out.println("Connection closed !!");
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IOTfinal main = new IOTfinal();//creates an object of the class
		main.initialize();
		
    	//call to ensure the driver is registered
        try
        {
            Class.forName(JDBC_DRIVER);
        } 
        catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found !!");
            return;
        }
        
		System.out.println("Started");
	}

}
