package kr.co.bomz.util.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * ť�� �ִ�ũ�⸦ �Ѿ��� �� �����Ͱ� ������ ó�� �����͸� �����ϰ� �������� ���� �����͸� �߰��Ѵ�.<p>
 * 
 * �ִ� ũ�⸦ �������� �ʾ��� ��� ���� ���� �����͸� ��� �����Ѵ�<p>
 * 
 * 
 * @author Bomz
 * @since 1.0
 * @param <T>
 * @version 1.0
 * @see		BomzBlockingQueue
 */
public class BomzQueue<T> extends AbstractBomzCollection<T> implements Queue<T> {
	
	private transient boolean changeQueueSize = false;
	
	private transient T data;
		
	/**
	 *	ť�� ũ�� ������ ���� ��� �����Ѵ� 
	 */
	public BomzQueue(){
		super();
		this.changeQueueSize = true;
	}
	
	/**
	 * ť�� �ִ� ũ�⸦ �����Ѵ�<p>
	 * ť�� ũ�� �̻��� �����Ͱ� ������ ���� ������ �����͸� �����ϰ� �ֽ� �����͸� �߰��Ѵ�<p>
	 * @param size 		ť�� �ִ� ũ��
	 */
	public BomzQueue(int size){
		super(size);
	}

	/**
	 * �����͸� ť�� �����Ѵ�
	 * @return	������ ���� ���� �� true
	 */
	@Override
	public boolean offer(T obj){
			
		if( this.changeQueueSize ){
			if( super.collection.length <= super.size ){
				super.collection = super.modifyQueue();
				super.cursor = 0;
			}
		}else	 if( super.collection.length <= super.size ){
			this.poll();
		}
			
		super.collection[
           		( (super.cursor + super.size) >= super.collection.length ) ? 
           				(super.cursor + super.size) - super.collection.length : (super.cursor + super.size)  
           ] = obj;
		
		super.size++;
		
		return true;
	}

	/**
	 * Ŀ�� ��ġ�� �ִ� �����͸� �����Ѵ�<p>
	 * �� ������ �����͸� �������� ������, ������ �� peek() �� ȣ���ϸ� ���� �����Ͱ� ���۵ȴ�
	 * @return	Ŀ�� ��ġ�� ������
	 */
	@Override
	public T peek() {
		if( super.size == 0 )	return null;
		return super.collection[cursor];
	}

	/**
	 * Ŀ�� ��ġ�� �ִ� �����͸� ������ �� �����͸� �����Ѵ�
	 * @return	Ŀ�� ��ġ�� ������
	 */
	@Override
	public T poll() {
		
		if( super.size <= 0 )	return null;
		
		this.data = super.collection[cursor];
		super.collection[cursor] = null;
		
		super.size--;
		if( ++cursor >= super.collection.length )		cursor = 0;
		
		if( this.changeQueueSize && super.size > super.COLLECTION_SIZE){
			int length = super.collection.length;
			length >>= 2;
			if( length > super.size ){
				super.collection = this.modifyQueue(length);
				cursor = 0;
			}
		}
		
		return this.data;
	}

	/**
	 * Collection �� ��� �����͸� ť�� �߰��Ѵ�
	 * @return		������ �߰� ���� ����
	 */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		
		if( c == null )	throw new NullPointerException();
		if( c == this )	throw new IllegalArgumentException();
		
		boolean flag = false;
		Iterator<? extends T> iter = c.iterator();
			
		while( iter.hasNext() ){
			if( this.offer(iter.next() ) )	flag = true;
		}
			
		return flag;
	}

	/**		�������� �ʴ´�		*/
	@Override
	public boolean contains(Object arg0) {
		throw new UnsupportedOperationException();
	}
	/**		�������� �ʴ´�		*/
	@Override
	public boolean containsAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}
	/**		�������� �ʴ´�		*/
	@Override
	public boolean remove(Object arg0) {
		throw new UnsupportedOperationException();
	}
	/**		�������� �ʴ´�		*/
	@Override
	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}
	/**		�������� �ʴ´�		*/
	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * ���� Ŀ�� ��ġ�� �����͸� ���������� ť���� �������� �ʴ´�<p>
	 * ��, �����Ͱ� ���� ��� NoSuchElementException ���ܰ� �߻��Ѵ�
	 * @return		Ŀ�� ��ġ�� ������
	 */
	@Override
	public T element() {
		T t = peek();
		
		if( t == null )
			throw new NoSuchElementException();
		else
			return t;
		
	}
	
	/**
	 * Ŀ�� ��ġ�� �����͸� �����ϸ鼭 �����Ѵ�<p>
	 * ���� �����Ͱ� ���� ��� NoSuchElementException ���ܰ� �߻��Ѵ�
	 * @return		Ŀ�� ��ġ�� ������
	 */
	@Override
	public T remove() {
		T t = poll();
		if( t == null )
			throw new NoSuchElementException();
		else
			return t;
	}
	
	/**
	 * �����͸� ť�� �߰��Ѵ�<p>
	 * �� ������ ��� ���� �� IllegalStateException ���ܰ� �߻��Ѵ�
	 * @return		������ ��� ���� ����
	 */
	@Override
	public boolean add(T obj) {
		if( offer(obj) )
			return true;
		else
			throw new IllegalStateException("Queue add exception");
	}
	
}
