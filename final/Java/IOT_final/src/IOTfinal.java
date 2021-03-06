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
	SerialPort serialPort;//定義serialport物件

	private BufferedReader input;//宣告input buffer
	private static final int TIME_OUT = 2000;//設定等待port開啟的時間，單位為毫秒
	private static final int DATA_RATE = 9600;//設定baud rate為9600

	//設定server IP,帳號,密碼��
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";//設定JDBC driver  
	static final String DB_URL = "jdbc:mysql://127.0.0.1/iot_final";//server IP後接資料庫名稱
	static final String table = "final";
	static final String USER = "USER";
	static final String PASS = "PASS";
	
	public void initialize() {
		CommPortIdentifier portId = null;//定義CommPortIdentifier物件，控制接收接收port
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();//儲存所有有效的port

		while (portEnum.hasMoreElements()) {//掃過所有的port
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();//定義currPortId
				if (currPortId.getName().equals("COM4")) {//設定arduino serial port
					portId = currPortId;
					break;
				}	
		}
		
		if (portId == null) {//如果com port設定錯誤，會出現這一行
			System.out.println("Could not find COM port.");
			return;
		}
		
		try {//連接port
			//open serial port
			serialPort = (SerialPort) portId.open(this.getClass().getName(),TIME_OUT);
			
			//設定port parameters
			serialPort.setSerialPortParams(DATA_RATE,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

			//open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
//			OutputStream os=serialPort.getOutputStream();
			// add event listeners
			serialPort.addEventListener(this);// Registers a SerialPortEventListener object to listen for SerialEvents.
			serialPort.notifyOnDataAvailable(true);//Expresses interest in receiving notification when input data is available.

//			while(true)
//			{
//				System.out.print("請輸入:");
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
	
	//處理serial port事件,讀取資料並print出來
	public void serialEvent(SerialPortEvent oEvent) {
        Connection connection = null;//建立Connection物件
        Statement statement = null;//建立Statement物件
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {//if data available on serial port
			try {
				//連接mysql database
	            connection = DriverManager.getConnection(DB_URL, USER, PASS);
	           // System.out.println("SQL Connection to database established!");
	            
				String inputLine=input.readLine();
				String sql="INSERT INTO "+table+" (sound,shock,light,type,count) VALUES ("+inputLine+")";
				System.out.println(sql);
				//執行query
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
