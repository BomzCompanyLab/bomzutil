package kr.co.bomz.util.resource;

/**
 * 	리소트 타입이 요청한 타입이 아닐 경우 발생
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
