package kr.co.bomz.util.tcp;

import java.io.InputStream;
import java.nio.channels.SocketChannel;

/**
 * 클라이언트 모드에서 사용되는 데이터 수신 유틸
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see		ByteReader
 * @see		StringReader
 * @param <T>
 */
public interface Reader<T>{
	
	/**
	 * 데이터 수신 버퍼 크기
	 */
	public static final int DEFAULT_BUFFER_SIZE = 200;
	
	/**
	 * TCP 방식으로 데이터를 수신하여 지정된 시작점과 끝점을 찾아내어 리턴한다.<br>
	 * 
	 * 만약 데이터 수신 중 연결이 종료되었다면 ConnectException() 이 발생한다.
	 */
	public T readMessage() throws Exception;
		
	/**
	 * readMessage() 사용 시 STX , ETX 포함 여부<br>
	 * 기본값 true
	 * @return		STX , ETX 포함 여부. 포함 시 true
	 */
	public boolean isCutMessage();

	/**
	 * readMessage() 사용 시 STX , ETX 포함 여부 설정
	 * @param cutMessage		true 설정 시 STX , ETX 포함
	 */
	public void setCutMessage(boolean cutMessage);
		
	/**
	 * 연결 종료 시 자동으로 inputStream.close() 또는 socketChannel.close() 호출 여부
	 * @return		true 설정 시 자동으로 close() 호출
	 */
	public boolean isConnectionAutoClose();

	/**
	 * 연결 종료 시 자동으로 inputStream.close() 또는 socketChannel.close() 호출 여부 설정.
	 * 기본 값 true
	 * @param connectionAutoClose		close() 메소드 호출 여부
	 */
	public void setConnectionAutoClose(boolean connectionAutoClose);

	/**
	 * 연결 정상 종료 처리
	 */
	public void close();
		
	/**
	 * 데이터 수신을 위한 java.io.InputStream 설정
	 * @param is		데이터 수신 스트림
	 */
	public void setInputStream(InputStream is);
	
	/**
	 * 데이터 수신을 위한 java.nio.SocketChannel 설정
	 * @param sc		데이터 수신 소켓채널
	 */
	public void setSocketChannel(SocketChannel sc);
	
	/**
	 * 데이터 수신을 위한 java.io.Reader 설정
	 * @param reader		데이터 수신 리더
	 */
	public void setReader(java.io.Reader reader);
	
	/**
	 * 수신 데이터에 -1 값이 올 경우 ConnectException 발생 여부
	 * 기본값 true
	 * @param check		true 설정 시 -1 값이면 ConnectException 발생
	 */
	public void setCheckDisconnect(boolean check);
}
