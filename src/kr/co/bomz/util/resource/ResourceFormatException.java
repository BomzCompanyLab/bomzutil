package kr.co.bomz.util.resource;

/**
 * 	����Ʈ Ÿ���� ��û�� Ÿ���� �ƴ� ��� �߻�
 * 
 * @author Bomz
 * @version 1.1
 * @since 1.1
 *
 */
public class ResourceFormatException extends RuntimeException{
	
	private static final long serialVersionUID = 8957841082355162273L;

	ResourceFormatException(String errMsg){
		super(errMsg);
	}
	
}
