package kr.co.bomz.util.threadpool;

/**
 * 
 * BomzThreadPool 이 종료 상태에서 getThread() 호출시 발생한다<p>
 * 
 * BomzThreadPool의 close() 호출 시 종료상태가되며 종료 상태를 해제하기 위해서는 reset() 메소드를 호출하면 된다
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
