package kr.co.bomz.util.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * 큐의 최대크기를 넘었을 때 데이터가 들어오면 처음 데이터를 삭제하고 마지막에 들어온 데이터를 추가한다.<p>
 * 
 * 최대 크기를 지정하지 않았을 경우 제한 없이 데이터를 계속 저장한다<p>
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
	 *	큐의 크기 제한이 없이 계속 증가한다 
	 */
	public BomzQueue(){
		super();
		this.changeQueueSize = true;
	}
	
	/**
	 * 큐의 최대 크기를 지정한다<p>
	 * 큐의 크기 이상의 데이터가 들어오면 가장 오래된 데이터를 삭제하고 최신 데이터를 추가한다<p>
	 * @param size 		큐의 최대 크기
	 */
	public BomzQueue(int size){
		super(size);
	}

	/**
	 * 데이터를 큐에 저장한다
	 * @return	데이터 저장 성공 시 true
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
	 * 커서 위치에 있는 데이터를 전송한다<p>
	 * 단 리턴한 데이터를 삭제하지 않으며, 다음에 또 peek() 를 호출하면 같은 데이터가 전송된다
	 * @return	커서 위치의 데이터
	 */
	@Override
	public T peek() {
		if( super.size == 0 )	return null;
		return super.collection[cursor];
	}

	/**
	 * 커서 위치에 있는 데이터를 전송한 후 데이터를 삭제한다
	 * @return	커서 위치의 데이터
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
	 * Collection 의 모든 데이터를 큐에 추가한다
	 * @return		데이터 추가 성공 여부
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

	/**		지원하지 않는다		*/
	@Override
	public boolean contains(Object arg0) {
		throw new UnsupportedOperationException();
	}
	/**		지원하지 않는다		*/
	@Override
	public boolean containsAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}
	/**		지원하지 않는다		*/
	@Override
	public boolean remove(Object arg0) {
		throw new UnsupportedOperationException();
	}
	/**		지원하지 않는다		*/
	@Override
	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}
	/**		지원하지 않는다		*/
	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 현재 커서 위치의 데이터를 리턴하지만 큐에서 삭제하진 않는다<p>
	 * 단, 데이터가 없을 경우 NoSuchElementException 예외가 발생한다
	 * @return		커서 위치의 데이터
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
	 * 커서 위치의 데이터를 리턴하면서 삭제한다<p>
	 * 만약 데이터가 없을 경우 NoSuchElementException 예외가 발생한다
	 * @return		커서 위치의 데이터
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
	 * 데이터를 큐에 추가한다<p>
	 * 단 데이터 등록 실패 시 IllegalStateException 예외가 발생한다
	 * @return		데이터 등록 성공 여부
	 */
	@Override
	public boolean add(T obj) {
		if( offer(obj) )
			return true;
		else
			throw new IllegalStateException("Queue add exception");
	}
	
}
