package kr.co.bomz.util.tcp;

/**
 * 	���� ���� ������ ����� ��� �߻�
 * @author Bomz
 * @version 1.0
 * @since 1.0
 *
 */
public class NumberRangeException extends RuntimeException{

	private static final long serialVersionUID = 8720100740671279546L;

	public NumberRangeException(){}
	
	public NumberRangeException(String errMsg){
		super(errMsg);
	}
}
