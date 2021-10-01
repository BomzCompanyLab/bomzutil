package example.tcp;

import java.io.InputStreamReader;
import java.net.Socket;

import kr.co.bomz.util.tcp.StringReader;

/*
 * if Server message is 'abcdef[[123456]]7890'
 * client read message is '123456'
 * java source :
 * 		StringReader sr = new StringReader("[[", "]]");
 * 		String readMsg = sr.readMessage();
 * 
 * 
 * 1. BufferedInputStream example.
 * 		sr.setInputStream( new BufferedInputStream(socket.getInputStream()) );
 * 
 * 2. BufferedReader example.
 * 		sr.setReader( new InputStreamReader(socket.getInputStream()) );
 */
public class StringReader_IO_Test {

	private final int port;
	private Socket socket;
	
	/*
	 * stx : [[
	 * etx : ]]
	 */
	private StringReader sr = new StringReader("[[", "]]");
	
	private int count;
	
	public static void main(String[] args) throws Exception{
		StringReader_IO_Test test = new StringReader_IO_Test(10111);
		
		// setting charset
		test.sr.setCharSet("UTF-8");
		
		// default value is true. stx and etx value use.
//		test.sr.setCutMessage(false);
		
				
		String msg;
		while( true ){
			if( !test.connect() )		continue;
			
			msg = test.readMessage();
			if( msg == null )		continue;
			
			System.out.println(test.count++ + "=" + msg);
		}
	}

	private boolean connect(){
		if( this.socket != null )		return true;
		
		try{
			this.socket = new Socket("127.0.0.1", this.port);
			
			// 1. BufferedInputStream
//			this.sr.setInputStream( new BufferedInputStream(socket.getInputStream()) );
			
			// 2. BufferedReader
			this.sr.setReader( new InputStreamReader(socket.getInputStream()) );
			
			this.count = 1;
			return true;
		}catch(Exception e){
			System.err.println("server connect error");
			try{		Thread.sleep(1000);		}catch(Exception e1){}
			this.close();
			return false;
		}
	}
	
	private StringReader_IO_Test(int port){
		this.port = port;
	}
	
	private String readMessage(){
		try{
			return this.sr.readMessage();
		}catch(Exception e){
			e.printStackTrace();
			this.close();
			return null;
		}
	}
	
	private void close(){
				
		if( this.socket != null ){
			try{		this.socket.close();		}catch(Exception e){}
			this.socket = null;
		}
	}
}
