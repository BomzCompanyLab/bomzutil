package example.threadpool;

import kr.co.bomz.util.threadpool.BomzThreadPool;

public class TestThreadPool02 {

	public static void main(String[] args){
		BomzThreadPool pool = new BomzThreadPool(ThreadObject02.class, "hong");
		
		pool.getThread().start("Seoul gangnam");
		pool.getThread().start("Incheon");
		pool.getThread().start("Jeju");
		
		// thread pool close
//		pool.closeThreadPool();
		
	}
}
