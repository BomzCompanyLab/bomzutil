package kr.co.bomz.util.scheduler;

/**
 * �����ٷ� ��� ���� �� �߻�
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class SchedulerAddException extends RuntimeException{

	private static final long serialVersionUID = 1930947051952335916L;

	SchedulerAddException(String errMsg){
		super(errMsg);
	}

}
