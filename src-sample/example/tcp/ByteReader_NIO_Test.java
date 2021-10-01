package example.tcp;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import kr.co.bomz.util.tcp.ByteReader;

/*
 * if Server message is '0xAA 0x04 0x01 0x02 0xB4 0x3A 0x09 0x01 0xC3 0xF1'
 * client read message is '0xB4 0x3A'
 * java source :
 * 		ByteReader br = new ByteReader(new byte[]{0x01,0x02}, new byte[]{0x09, 0x01});
 * 		byte[] readMsg = br.readMessage();
 * 
 * 
 * 1. BufferedInputStream example.
 * 		br.setInputStream( new BufferedInputStream(socket.getInputStream()) );
 * 
 * 2. SocketChannel example.
 * 		br.setSocketChannel( socketChannel );
 */
public class ByteReader_NIO_Test {

	private final int port;
	private SocketChannel sc;
	private ByteReader br = new ByteReader(
			new byte[]{0x01, 0x02} ,		// stx 
			new byte[]{0x09, 0x01}			// etx
		);
	
	private int count;
	
	public static void main(String[] args) throws Exception{
		ByteReader_NIO_Test test = new ByteReader_NIO_Test(10111);
				
		// default value is true. stx and etx value use.
//		test.br.setCutMessage(false);
		
		byte[] msg;
		while( true ){
			if( !test.connect() )		continue;
			
			msg = test.readMessage();
			if( msg == null )		continue;
			
			System.out.print(test.count++ + "=");
			for(byte b : msg)		System.out.print( Integer.toHexString(b) + " ");
			System.out.println();
		}
	}

	private boolean connect(){
		if( this.sc != null )		return true;
		
		try{
			this.sc = SocketChannel.open(new InetSocketAddress("127.0.0.1", port));
			
			// SocketChannel
			this.br.setSocketChannel( this.sc );
			
			this.count = 1;
			return true;
		}catch(Exception e){
			System.err.println("server connect error");
			try{		Thread.sleep(1000);		}catch(Exception e1){}
			this.close();
			return false;
		}
	}
	
	private ByteReader_NIO_Test(int port){
		this.port = port;
	}
	
	private byte[] readMessage(){
		try{
			return this.br.readMessage();
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
