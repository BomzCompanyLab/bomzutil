package example.threadpool;

import kr.co.bomz.util.threadpool.BomzThreadPool;

public class TestThreadPool03 {

	public static void main(String[] args){
		// thread size is 2, max size 5
		BomzThreadPool pool = new BomzThreadPool(2, 5, ThreadObject03.class, "hong");
		
		// 모든 스레드가 사용중일 경우 사용완료 된 스레드가 반납될 때까지 대기. 기본값 true
		pool.setNonWait(false);
		
		pool.getThread().start();
		pool.getThread().start();
		pool.getThread().start();		// thread return wait.
		pool.getThread().start();
		
		// thread pool close
//		pool.closeThreadPool();
		
	}
}
