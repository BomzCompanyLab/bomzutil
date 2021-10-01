package kr.co.bomz.util.tcp;

/**
 * 	지정 값의 범위를 벗어났을 경우 발생
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
