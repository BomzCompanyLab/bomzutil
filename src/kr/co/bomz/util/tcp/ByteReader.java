package kr.co.bomz.util.tcp;

import java.io.InputStream;
import java.io.Reader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


/**
 * 
 * 데이터의 STX와 ETX가 정해져있는 바이트 단위의 메세지를 수신하는 경우 해당 유틸을 이용해서 쉽게 해당 부분만 수신할 수 있다.<p>
 * 
 * 예) 0x12 0x33 0x02 0x00 0x12 0x43 0x03 0xA3, 0x81<p>
 * 
 * STX가 0x33 , 0x02 이고, ETX 가 0x43 , 0x03 일 경우 아래와 같이 사용할 수 있다<p>
 * <code>
 * ByteReader br = new ByteReader(new byte[]{0x33,0x02} , new byte[]{0x43,0x03});<br>
 * br.setInputStream(inputStream);<br>
 * byte[] readData = br.readMessage();<p>
 * </code>
 * 
 * 이 경우 readData 값은 0x00 , 0x12 가 된다
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class ByteReader implements kr.co.bomz.util.tcp.Reader<byte[]>{

	private InputStream is;
	private SocketChannel sc;
	private ByteBuffer bb;
	
	private final byte[] stx;
	private final int stxLength;
	private final byte[] etx;
	private final int etxLength;
	
	private boolean cutMessage = true;
	private boolean connectionAutoClose = true;
	
	private int startPoint;
	private int lastPoint;
	
	private int readSize;
	
	private byte[] byteData = new byte[DEFAULT_BUFFER_SIZE];
	
	private byte[] readMsg;		// 모든 수신 데이터
	private byte[] returnMsg;
		
//	// 수신 데이터 HEX 형식으로 포맷 출력
//	private boolean debugLogHexFormat = false;
	
	// 수신 결과가 -1 일 경우 ConnectException 발생 여부
	private boolean checkDisconnect = true;
	
	// checkDisconnect 가 false 일 경우 read() 의 응답값으로 -1 이 10번 와야 에러로 처리하도록 사용된다
	private int errCount;
	
	// errCount 를 에러로 판단할 최대 값
	private int maxErrCount = 10;
	
	/**
	 * STX 와 ETX 가 정의된 바이트 타입의 데이터 수신 시 사용
	 * @param stx		데이터 시작 값
	 * @param etx		데이터 종료 값
	 */
	public ByteReader(byte[] stx, byte[] etx){
		this.stx = stx;
		this.etx = etx;
		this.stxLength = this.stx.length;
		this.etxLength = this.etx.length;
	}
			
	@Override
	public byte[] readMessage() throws Exception{
		
		if( this.is == null && this.sc == null )		return null;
		
		this.readSize = 0;
		this.errCount = 0;
		
		while( true ){
			if( this.readSize == -1 && !this.checkDisconnect ){
				if( this.errCount++ > this.maxErrCount )		return null;
				
				try{		Thread.sleep(100);		}catch(Exception e){}
			}
			
			if( !this.checkEtxPoint() )		continue;
			if( !this.checkStxPoint() )		continue;
			break;
		}
		
		if( this.cutMessage ){		// STX 자름
			byte[] tmp = new byte[this.returnMsg.length - this.stxLength - this.startPoint];
			System.arraycopy(this.returnMsg, this.stxLength + this.startPoint, tmp, 0, tmp.length);
			this.returnMsg = null;
			
			return tmp;
		}else{				// ETX 추가
			byte[] tmp = new byte[this.returnMsg.length + this.etxLength - this.startPoint];
			System.arraycopy(this.returnMsg, this.startPoint, tmp, 0, this.returnMsg.length - this.startPoint);
			System.arraycopy(this.etx, 0, tmp, this.returnMsg.length - this.startPoint, this.etxLength);
			this.returnMsg = null;
					
			return tmp;
		}
	}
	
	// etx 앞의 데이터 복사
	private boolean checkStxPoint(){
		this.returnMsg = new byte[this.lastPoint];
		System.arraycopy(this.readMsg, 0, this.returnMsg, 0, this.lastPoint);
		
		// etx 앞의 데이터 삭제 [etx 문자도 포함해서 삭제]
		byte[] tmp = new byte[this.readMsg.length - this.lastPoint - this.etxLength];
		System.arraycopy(this.readMsg, this.lastPoint + this.etxLength, tmp, 0, tmp.length);
		this.readMsg = tmp;
		
		// 뒤에부터 stx 검색
		boolean checkFlag;
		final int loopLength = this.stxLength - 1;
		for(int i=this.lastPoint-1; i >= loopLength; i--){
			if( this.returnMsg[i] == this.stx[loopLength] ){
				// stx 맨 마지막 값이 같을 경우 그 앞부분 검색
				checkFlag = true;
				for(int j=1; j < this.stxLength; j++){
					if( this.returnMsg[i-j] != this.stx[loopLength-j] ){
						checkFlag = false;
						break;
					}
				}
				
				if( checkFlag ){		// stx 
					this.startPoint = i - loopLength;
					return true;
				}
				
			}
		}
				
		return false;
	}
	
	private boolean checkEtxPoint() throws Exception{
		
		if( this.readMsg == null )		this.read();
		if( this.readMsg == null )		return false;
				
		boolean checkFlag;
		final int loopLength = this.readMsg.length;// - this.etxLength;
		for(int i=0; i < loopLength; i++){
			if( this.readMsg[i] == this.etx[0] ){
				checkFlag = true;
				for(int j=1; j < this.etxLength; j++){
					if( this.readMsg[i+j] != this.etx[j] ){
						checkFlag = false;
						break;
					}
				}
				
				if( checkFlag ){
					this.lastPoint = i;
					return true;
				}
			}
		}
		
		this.read();
		return false;
	}
	
	private void read() throws Exception{
		try{
			if( this.is != null ){
				this.readSize = this.is.read( this.byteData );
			}else if( this.sc != null ){
				this.bb.clear();
				this.readSize = this.sc.read(bb);
				if( this.readSize <= 0 ){
					try{		Thread.sleep(1);		}catch(Exception e){}
					return;		// no read data
				}
				this.bb.flip();
				this.bb.get(this.byteData, 0, this.bb.limit());
			}
			
			if( this.readSize == -1 && this.checkDisconnect )				throw new ConnectException();
			if( this.readSize == -1 && !this.checkDisconnect )			return;
			
			if( this.readSize > 0 ){
				// 데이터 추가
				if( this.readMsg == null ){		// 첫 데이터
					this.readMsg = new byte[this.readSize];
					System.arraycopy(this.byteData, 0, this.readMsg, 0, this.readSize);
				}else{		// 기존 데이터가 있을 경우
					byte[] tmp = new byte[this.readMsg.length + this.readSize];
					System.arraycopy(this.readMsg, 0, tmp, 0, this.readMsg.length);
					System.arraycopy(this.byteData, 0, tmp, this.readMsg.length, this.readSize);
					this.readMsg = tmp;
				}
				
//				if( this.debugLog )		this.readMessageLog();		// 로그 출력
			}
		}catch(SocketTimeoutException e){
			throw e;
		}catch(Exception e){
			this.close();
			throw e;
		}
	}
		
//	// 수신 데이터 로그 출력
//	private void readMessageLog(){
//		StringBuilder buffer = new StringBuilder();
//		for(int i=0; i < this.readSize; i++){
//			if( this.debugLogHexFormat )		buffer.append( Integer.toHexString(this.byteData[i]) );
//			else									buffer.append( this.byteData[i] );
//			buffer.append(" ");
//		}
//	}
	
	/**
	 * 데이터 수신을 위한 java.io.InputStream 설정
	 * @param is		데이터 수신 스트림
	 */
	@Override
	public void setInputStream(InputStream is){
		this.memoryClear();
		this.is = is;
		this.memoryInit();
	}
	
	/**
	 * 데이터 수신을 위한 java.nio.SocketChannel 설정
	 * @param sc		데이터 수신 소켓채널
	 */
	@Override
	public void setSocketChannel(SocketChannel sc){
		this.memoryClear();
		this.sc = sc;
		this.memoryInit(); 
	}

	private void memoryClear(){
		this.readMsg = null;
		if( this.connectionAutoClose ){
			this.close();
		}
	}
	
	private void memoryInit(){
		if( this.is != null ){
			if( this.bb != null )					this.bb = null;
		}else if( this.sc != null ){
			if( this.bb == null )					this.bb = ByteBuffer.allocateDirect(DEFAULT_BUFFER_SIZE);
		}
		
	}

	@Override
	public boolean isCutMessage() {
		return this.cutMessage;
	}

	@Override
	public void setCutMessage(boolean cutMessage) {
		this.cutMessage = cutMessage;
	}

	@Override
	public boolean isConnectionAutoClose() {
		return this.connectionAutoClose;
	}

	@Override
	public void setConnectionAutoClose(boolean connectionAutoClose) {
		this.connectionAutoClose = connectionAutoClose;
	}

	@Override
	public void close() {
		if( this.is != null ){
			try{		this.is.close();		}catch(Exception e){}
			this.is = null;
		}
		
		if( this.sc != null ){
			try{		this.sc.close();		}catch(Exception e){}
			this.sc = null;
		}
		
		this.readMsg = null;
	}

//	@Override
//	public void setDebugLog(boolean on) {
//		this.debugLog = on;
//	}
	
//	/**
//	 * 수신 데이터를 16진수 형식으로 변환하여 로그 출력
//	 * @param on		16진수 형식으로 변경 여부. 기본값 false
//	 */
//	public void setDebugLogHexFormat(boolean on){
//		this.debugLogHexFormat = on;
//	}

	@Override
	public void setReader(Reader reader) throws UnsupportedOperationException{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCheckDisconnect(boolean check) {
		this.checkDisconnect = check;
	}
	
	public void setMaxReadWaitTime(int second){
		this.maxErrCount = second * 10;
	}
}
