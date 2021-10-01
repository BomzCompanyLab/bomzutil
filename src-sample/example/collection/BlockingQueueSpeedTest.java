package example.collection;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

import kr.co.bomz.util.collection.BomzBlockingQueue;

public class BlockingQueueSpeedTest {

	public static void main(String[] args) {
		// Blocking Queue Speed Test
		
		// test1. java.util.ArrayBlockingQueue
		test("ArrayBlockingQueue", new ArrayBlockingQueue<Integer>(5000));
		System.gc();
				
		// test2. BomzBlockingQueue
		test("BomzQueue", new BomzBlockingQueue<Integer>());
		System.gc();
		
		// test3. java.util.LinkedBlockingDeque
		test("PriorityQueue", new LinkedBlockingDeque<Integer>());
		System.gc();
		
		// test4. BomzBlockingQueue (max size fix)
		test("BomzQueue", new BomzBlockingQueue<Integer>(5000));		// max size
		System.gc();
		
		// test5. PriorityBlockingQueue
		test("LinkedList", new PriorityBlockingQueue<Integer>());
	}

	public static void test(String name, Queue<Integer> queue){
		long startTime = System.currentTimeMillis();
		
		for(int i=0; i < 90000; i++)		queue.offer(i);
		for(int i=0; i < 3000; i++)		queue.poll();
		for(int i=0; i < 10000; i++)		queue.offer(i);
		while( !queue.isEmpty() )		queue.poll();
		for(int i=0; i < 40000; i++)		queue.offer(i);
		for(int i=0; i < 35000; i++)		queue.poll();
		
		System.out.println(name + "=" + (System.currentTimeMillis() - startTime) );
	}
	
}
