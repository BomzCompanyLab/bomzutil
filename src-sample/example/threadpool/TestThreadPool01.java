package example.threadpool;

import kr.co.bomz.util.threadpool.BomzThreadPool;

public class TestThreadPool01 {

	public static void main(String[] args){
		BomzThreadPool pool = new BomzThreadPool(ThreadObject01.class);
		
		for(int i=0; i < 20; i++){
			if( i % 2 == 0 )			pool.getThread().start();
			else if( i % 3 == 0 )			pool.getThread().start("i%3==0 is", i);
			else							pool.getThread().start(i);
		}
		
		// thread pool close
//		pool.closeThreadPool();
		
	}
}
