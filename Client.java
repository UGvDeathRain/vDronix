import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

public class Client {
	Socket socket;
	OutputStream out;
	static BufferedReader in;
	
	public Client(String ip, int port) {
		connect(ip, port);
		
	}
	
	public void connect(String ip, int port){
		//Create socket connection
		   try{
		     socket = new Socket(ip, port);
		     out = socket.getOutputStream();
		     in = new BufferedReader(new InputStreamReader(
		                socket.getInputStream()));
		   } catch (UnknownHostException e) {
		     System.out.println("Unknown host : " + ip + ":" + port);
		     System.exit(1);
		   } catch  (IOException e) {
		     System.out.println("No I/O");
		     System.exit(1);
		}
	}
	
	public static byte[] toByteArray(String s) {
	    return DatatypeConverter.parseHexBinary(s);
	}
	
	public void send(String data) {
		//byte[] command = {(byte)0x81, (byte)0x01, (byte)0x06, (byte)0x01, (byte)0x01, (byte)0x01, (byte)0x03, (byte)0x01, (byte)0xff};
		try {
			out.write(toByteArray(data));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		final Client client = new Client("127.0.0.1", 1337);
		new Thread("listen") {
			public void run() {
				while(true) {
				try{
				     String line = in.readLine();
				     if(line == null) {
				    	 System.out.println("Server stopped !");
				    	 System.exit(0);
				     }
				     System.out.println("Text received: " + line);
				   } catch (IOException e){
				     System.err.println("Read failed");
				     System.exit(1);
				   }
				}
			}
		}.start();
		new Thread("send") {
			public void run() {
				Scanner sc = new Scanner(System.in);
				while(true) {
					try{
						String line = sc.nextLine();
						client.send(line);
					} catch (Exception e){
					     System.err.println("Write failed : not hex format ? ( length % 2 == 0)");
					}
				}
			}
		}.start();
		//client.send("test");
	}
}
