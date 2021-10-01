package kr.co.bomz.util.tcp;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 
 * 문자열의 STX 와 ETX가 정해져 있을 경우 해당 유틸을 이용해서 쉽게 해당 부분만 수신할 수 있다.<p>
 * 
 * 데이터 예)'abcd[[efg12345ab]]cdefg'<p>
 * <code>
 * StringReader sr = new StringReader("[[", "]]");<br>
 * sr.setInputStream(inputStream);<br>
 * String readData = sr.readMessage();<p>
 * </code>
 * 
 * 이 경우 readData 값은 'efg12345ab' 가 된다
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class StringReader implements kr.co.bomz.util.tcp.Reader<String>{
	
	private Reader reader;
	private char[] charData;
	
	private InputStream is;
	private byte[] byteData;
	
	private SocketChannel sc;
	private ByteBuffer bb;
	
	private final String stx;
	private final int stxLength;
	private final String etx;
	private final int etxLength;
	
	private boolean cutMessage = true;
	
	private boolean connectionAutoClose = true;
	
	private int readSize;
	private String returnMsg;
	private StringBuilder readMessageBuilder = new StringBuilder();
	private int startPoint;
	private int lastPoint;
	
	private String charSet;
	
//	// 수신 데이터 로그 출력
//	private boolean debugLog = false;
	
	// 수신 결과가 -1 일 경우 ConnectException 발생 여부
	private boolean checkDisconnect = true;
	
	private int bufferSize = kr.co.bomz.util.tcp.Reader.DEFAULT_BUFFER_SIZE;
	
	/**
	 * 수신 데이터의 STX 와 ETX 가 정해져있을 경우 사용된다
	 * @param stx		데이터의 시작 문자
	 * @param etx		데이터의 종료 문자
	 */
	public StringReader(String stx, String etx){
		this.stx = stx;
		this.etx = etx;
		this.stxLength = this.stx.length();
		this.etxLength = this.etx.length();
	}

	@Override
	public String readMessage() throws Exception{
		
		if( this.reader == null && this.is == null && this.sc == null )		return null;
		
		while( true ){
			if( !this.checkEtxPoint() )		continue;
			if( !this.checkStxPoint() )		continue;
			break;
		}
		
		if( this.cutMessage ){
			return this.returnMsg.substring(this.startPoint + stxLength);
		}else{
			return this.returnMsg.substring(this.startPoint) + this.etx;
		}
	}
	
	private boolean checkStxPoint(){
		this.returnMsg = this.readMessageBuilder.substring(0, this.lastPoint);
		this.readMessageBuilder.delete(0, this.lastPoint + this.etxLength);		// delete data
		
		this.startPoint = this.returnMsg.lastIndexOf(this.stx);		// no etx		
		return this.startPoint != -1;
	}
	
	private boolean checkEtxPoint() throws Exception{
		this.lastPoint = this.readMessageBuilder.indexOf(this.etx);
		
		if( this.lastPoint < 0 ){
			this.read();
			return false;
		}else{
			return true;
		}
	}
	
	private String getReadMessage() throws UnsupportedEncodingException{
		
		if( this.reader != null ){		// Reader
			
			return String.valueOf( this.charData, 0, this.readSize);
			
		}else{		// InputStream or SocketChannel
			return this.charSet == null ?	
					new String( this.byteData, 0, readSize) : 
					new String( this.byteData, 0, readSize, this.charSet);
		}
	}
	
	private void read() throws Exception{
		try{
			if( this.is != null ){
				this.readSize = this.is.read( this.byteData );
			}else if( this.reader != null ){
				this.readSize = this.reader.read( this.charData );
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
			
			if( this.readSize > 0 ){
//				if( this.debugLog && Log.isDebug() ){		// 수신 데이터 로그 출력
//					String msg = this.getReadMessage();
//					this.readMessageBuilder.append(msg);
//					Log.debug(msg);
//				}else{			// 로그 없이 처리
					this.readMessageBuilder.append( this.getReadMessage() );
//				}
			}
		}catch(SocketTimeoutException e){
			throw e;
		}catch(Exception e){
			this.close();
			throw e;
		}
	}
	
	/**
	 * 수신 데이터의 캐릭터셋 설정
	 * @param charSet		캐릭터셋
	 */
	public void setCharSet(String charSet){
		this.charSet = charSet;
	}
	
	/**
	 * 현재 설정된 캐릭터셋. 별도로 설정하지 않았을 경우 null
	 * @return		캐릭터셋
	 */
	public String getCharSet(){
		return this.charSet;
	}
	
	/**
	 * 데이터 수신을 위한 java.io.Reader 설정
	 * @param reader		데이터 수신 리더
	 */
	@Override
	public void setReader(Reader reader){
		this.memoryClear();
		this.reader = reader;
		this.memoryInit();
	}
	
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

	public void setBufferSize(int bufferSize) throws NumberRangeException{
		if( bufferSize <= 0 )		throw new NumberRangeException("1~10000 까지만 설정가능합니다");
		if( bufferSize > 10000 )		throw new NumberRangeException("1~10000 까지만 설정가능합니다");
		
		this.bufferSize = bufferSize;
	}
	
	private void memoryClear(){
		
		if( this.connectionAutoClose ){
			this.close();
		}else{
			// this.connectionAutoClose 가 true 일 경우 this.close(); 에서 StringBuilder 새로 생성
			this.readMessageBuilder = new StringBuilder();
		}
	}
	
	private void memoryInit(){
		if( this.reader != null ){
			if( this.charData == null )			this.charData = new char[ this.bufferSize ];
			if( this.byteData != null )			this.byteData = null;
			if( this.bb != null )					this.bb = null;
		}else if( this.is != null ){
			if( this.charData != null )			this.charData = null;
			if( this.byteData == null )			this.byteData = new byte[ this.bufferSize];
			if( this.bb != null )					this.bb = null;
		}else if( this.sc != null ){
			if( this.charData != null )			this.charData = null;
			if( this.byteData == null )			this.byteData = new byte[ this.bufferSize];
			if( this.bb == null )					this.bb = ByteBuffer.allocateDirect(this.bufferSize);
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
		return connectionAutoClose;
	}

	@Override
	public void setConnectionAutoClose(boolean connectionAutoClose) {
		this.connectionAutoClose = connectionAutoClose;
	}

	@Override
	public void close(){
		if( this.reader != null ){
			try{		this.reader.close();		}catch(Exception e){}
			this.reader = null;
		}
		
		if( this.is != null ){
			try{		this.is.close();		}catch(Exception e){}
			this.is = null;
		}
		
		if( this.sc != null ){
			try{		this.sc.close();		}catch(Exception e){}
			this.sc = null;
		}
		
		this.readMessageBuilder = new StringBuilder();
	}
	
	@Override
	public void setCheckDisconnect(boolean check) {
		this.checkDisconnect = check;
	}
}
