package example.tcp;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import kr.co.bomz.util.tcp.StringReader;

/*
 * if Server message is 'abcdef[[123456]]7890'
 * client read message is '123456'
 * java source :
 * 		StringReader sr = new StringReader("[[", "]]");
 * 		String readMsg = sr.readMessage();
 * 
 * 
 * 1. SocketChannel example.
 * 		sr.setSocketChannel( socketChannel );
 * 
 */
public class StringReader_NIO_Test {

	private final int port;
	private SocketChannel sc;
	private StringReader sr = new StringReader("[[", "]]");
	
	private int count;
	
	public static void main(String[] args) throws Exception{
		StringReader_NIO_Test test = new StringReader_NIO_Test(10111);
		
		// setting charset
//		test.sr.setCharSet("UTF-8");
		
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
		if( this.sc != null )		return true;
		
		try{
			this.sc = SocketChannel.open( new InetSocketAddress("127.0.0.1", this.port) );
			this.sc.configureBlocking(false);
			this.sr.setSocketChannel(this.sc);
			
			this.count = 1;
			return true;
		}catch(Exception e){
			System.err.println("server connect error");
			try{		Thread.sleep(1000);		}catch(Exception e1){}
			this.close();
			return false;
		}
	}
	
	private StringReader_NIO_Test(int port){
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
				
		if( this.sc != null ){
			try{		this.sc.close();		}catch(Exception e){}
			this.sc = null;
		}
	}
}
