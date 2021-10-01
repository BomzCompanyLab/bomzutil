package example.threadpool;

import kr.co.bomz.util.threadpool.BomzThreadPool;

public class TestThreadPool03 {

	public static void main(String[] args){
		// thread size is 2, max size 5
		BomzThreadPool pool = new BomzThreadPool(2, 5, ThreadObject03.class, "hong");
		
		// ��� �����尡 ������� ��� ���Ϸ� �� �����尡 �ݳ��� ������ ���. �⺻�� true
		pool.setNonWait(false);
		
		pool.getThread().start();
		pool.getThread().start();
		pool.getThread().start();		// thread return wait.
		pool.getThread().start();
		
		// thread pool close
//		pool.closeThreadPool();
		
	}
}
