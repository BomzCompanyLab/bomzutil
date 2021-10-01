package kr.co.bomz.util.tcp;

import java.io.InputStream;
import java.nio.channels.SocketChannel;

/**
 * Ŭ���̾�Ʈ ��忡�� ���Ǵ� ������ ���� ��ƿ
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see		ByteReader
 * @see		StringReader
 * @param <T>
 */
public interface Reader<T>{
	
	/**
	 * ������ ���� ���� ũ��
	 */
	public static final int DEFAULT_BUFFER_SIZE = 200;
	
	/**
	 * TCP ������� �����͸� �����Ͽ� ������ �������� ������ ã�Ƴ��� �����Ѵ�.<br>
	 * 
	 * ���� ������ ���� �� ������ ����Ǿ��ٸ� ConnectException() �� �߻��Ѵ�.
	 */
	public T readMessage() throws Exception;
		
	/**
	 * readMessage() ��� �� STX , ETX ���� ����<br>
	 * �⺻�� true
	 * @return		STX , ETX ���� ����. ���� �� true
	 */
	public boolean isCutMessage();

	/**
	 * readMessage() ��� �� STX , ETX ���� ���� ����
	 * @param cutMessage		true ���� �� STX , ETX ����
	 */
	public void setCutMessage(boolean cutMessage);
		
	/**
	 * ���� ���� �� �ڵ����� inputStream.close() �Ǵ� socketChannel.close() ȣ�� ����
	 * @return		true ���� �� �ڵ����� close() ȣ��
	 */
	public boolean isConnectionAutoClose();

	/**
	 * ���� ���� �� �ڵ����� inputStream.close() �Ǵ� socketChannel.close() ȣ�� ���� ����.
	 * �⺻ �� true
	 * @param connectionAutoClose		close() �޼ҵ� ȣ�� ����
	 */
	public void setConnectionAutoClose(boolean connectionAutoClose);

	/**
	 * ���� ���� ���� ó��
	 */
	public void close();
		
	/**
	 * ������ ������ ���� java.io.InputStream ����
	 * @param is		������ ���� ��Ʈ��
	 */
	public void setInputStream(InputStream is);
	
	/**
	 * ������ ������ ���� java.nio.SocketChannel ����
	 * @param sc		������ ���� ����ä��
	 */
	public void setSocketChannel(SocketChannel sc);
	
	/**
	 * ������ ������ ���� java.io.Reader ����
	 * @param reader		������ ���� ����
	 */
	public void setReader(java.io.Reader reader);
	
	/**
	 * ���� �����Ϳ� -1 ���� �� ��� ConnectException �߻� ����
	 * �⺻�� true
	 * @param check		true ���� �� -1 ���̸� ConnectException �߻�
	 */
	public void setCheckDisconnect(boolean check);
}
