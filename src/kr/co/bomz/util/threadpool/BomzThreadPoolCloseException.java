package kr.co.bomz.util.threadpool;

/**
 * 
 * BomzThreadPool �� ���� ���¿��� getThread() ȣ��� �߻��Ѵ�<p>
 * 
 * BomzThreadPool�� close() ȣ�� �� ������°��Ǹ� ���� ���¸� �����ϱ� ���ؼ��� reset() �޼ҵ带 ȣ���ϸ� �ȴ�
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see BomzThreadPool
 *
 */
public class BomzThreadPoolCloseException extends RuntimeException{

	private static final long serialVersionUID = -3313910417544080802L;

	public BomzThreadPoolCloseException(){
		super();
	}
}
