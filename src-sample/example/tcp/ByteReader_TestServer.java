package example.tcp;

import java.io.BufferedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ByteReader_TestServer {

	private final int port;
	private ServerSocket sSocket;
	private Socket socket;
	private BufferedOutputStream bos;
	
	private Random random = new Random();
	
	private final byte[] stx = new byte[]{0x01, 0x02};
	private final byte[] etx = new byte[]{0x09, 0x01};
	
	private int count;
	
	public static void main(String[] args){
		ByteReader_TestServer server = new ByteReader_TestServer(10111);
		
		while( true ){
			if( !server.serverOpen() )		continue;
			if( !server.accept() )				continue;
			server.writeMessage();
			try{		Thread.sleep(1500);		}catch(Exception e){}
		}
	}
	
	private ByteReader_TestServer(int port){
		this.port = port;
	}
	
	private void writeMessage(){
		byte[] randomData = new byte[random.nextInt(10) + 1];
		random.nextBytes(randomData);
		
		try{	
			this.bos.write(0x03);		// dumy data
			this.bos.write(0x07);		// dumy data
			this.bos.write(0x01);		// dumy data
			this.bos.write(0x06);		// dumy data
			this.bos.write(stx);		// stx
			this.bos.write( randomData );		// random body data
			this.bos.write( etx );		// etx
			this.bos.write(0xAB);		// dumy data
			this.bos.write(0x19);		// dumy data
			
			this.bos.flush();
			
			System.out.println("write message count is " + this.count++);
		}catch(Exception e){
			e.printStackTrace();
			this.close();
		}
	}
	
	private boolean accept(){
		if( this.socket != null )		return true;
		
		try{
			this.socket = this.sSocket.accept();
			this.bos = new BufferedOutputStream( this.socket.getOutputStream() );
			this.count = 1;
			System.out.println("accept client.");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			try{		Thread.sleep(1000);		}catch(Exception e1){}
			this.close();
			return false;
		}
	}
	
	private void close(){
		if( this.bos != null ){
			try{		this.bos.close();		}catch(Exception e){}
			this.bos = null;
		}
		
		if( this.socket != null ){
			try{		this.socket.close();		}catch(Exception e){}
			this.socket = null;
		}
		
		System.out.println("disconnect client");
	}
	
	private boolean serverOpen(){
		if( this.sSocket != null )		return true;
		
		try{
			this.sSocket = new ServerSocket(this.port);
			System.out.println("start server. port=" + this.port);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			try{		Thread.sleep(1000);		}catch(Exception e1){}
			return false;
		}
	}
	
}
