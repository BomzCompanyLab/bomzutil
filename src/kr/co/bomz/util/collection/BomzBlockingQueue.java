package kr.co.bomz.util.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ����ȭ ó���� �� BomzQueue
 * 
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see		BomzQueue
 */
public class BomzBlockingQueue<T> extends BomzQueue<T>{

	private final ReentrantLock lock = new ReentrantLock(false);

	/**
	 *	ť�� ũ�� ������ ���� ��� �����Ѵ� 
	 */
	public BomzBlockingQueue(){
		super();
	}
	
	/**
	 * ť�� �ִ� ũ�⸦ �����Ѵ�<p>
	 * ť�� ũ�� �̻��� �����Ͱ� ������ ���� ������ �����͸� �����ϰ� �ֽ� �����͸� �߰��Ѵ�<p>
	 * @param size 		ť�� �ִ� ũ��
	 */
	public BomzBlockingQueue(int size){
		super(size);
	}

	@Override
	public boolean offer(T obj){
		lock.lock();
		try{
			return super.offer(obj);
		}finally{
			lock.unlock();
		}
	}

	@Override
	public T peek() {
		lock.lock();
		try{
			return super.peek();
		}finally{
			lock.unlock();
		}
	}
	@Override
	public T poll() {
		lock.lock();
		try{
			return super.poll();
		}finally{
			lock.unlock();
		}
	}
	@Override
	public boolean addAll(Collection<? extends T> c) {
		lock.lock();
		
		try{
			return super.addAll(c);
		}finally{
			lock.unlock();
		}
	}
	@Override
	public void clear() {
		lock.lock();
		
		try{
			super.clear();
		}finally{
			lock.unlock();
		}
	}
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	@Override
	public Iterator<T> iterator() {
		lock.lock();
		try{
			return super.iterator();
		}finally{
			lock.unlock();
		}
	}
	@Override
	public int size() {
		return size;
	}
	@Override
	public Object[] toArray() {
		lock.lock();
		
		try{
			return super.toArray();			
		}finally{
			lock.unlock();
		}
	}
	@Override
	public <L> L[] toArray(L[] c) {
		lock.lock();
		
		try{
			return super.toArray(c);			
		}finally{
			lock.unlock();
		}
	}	
	
}
