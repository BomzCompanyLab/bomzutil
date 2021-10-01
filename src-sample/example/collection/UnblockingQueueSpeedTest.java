package example.collection;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import kr.co.bomz.util.collection.BomzQueue;

public class UnblockingQueueSpeedTest {

	public static void main(String[] args) {
		// Unblocking Queue Speed Test
		
		// test1. java.util.ArrayDeque
		test("ArrayDeque", new ArrayDeque<Integer>());
		// test2. BomzQueue
		test("BomzQueue", new BomzQueue<Integer>());
		// test3. java.util.PriorityQueue
		test("PriorityQueue", new PriorityQueue<Integer>());
		// test4. BomzQueue (max size fix)
		test("BomzQueue", new BomzQueue<Integer>(5000));		// max size
		// test5. LinkedList
		test("LinkedList", new LinkedList<Integer>());
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
