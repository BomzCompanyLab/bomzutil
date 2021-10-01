package kr.co.bomz.util.resource;

/**
 * 
 * ResourceBundle ���� ���� �� �߻�
 * 
 * @author Bomz
 * @since 1.1
 * @version 1.1
 *
 */
public class ResourceBundleCreateException extends RuntimeException{

	private static final long serialVersionUID = 7294492320160601766L;

	public ResourceBundleCreateException(String errMsg){
		super(errMsg);
	}
}
