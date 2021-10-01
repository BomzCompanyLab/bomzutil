package kr.co.bomz.util.collection;

import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * 큐 , 셋 , 맵 등에서 공통적으로 사용하는 데이터를 관리하는 추상 클래스
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	BomzQueue
 * @see	BomzBlockingQueue
 *
 */
public abstract class AbstractBomzCollection<T> {

	/**
	 * 사용자가 크기를 지정하지않았을 경우 기본 사용 크기
	 */
	protected static  final int DEFAULT_COLLECTION_SIZE = 20;
	
	/**
	 * 실제 생성된 저장공간 크기. 
	 */
	protected final int COLLECTION_SIZE;
	
	/**
	 * 데이터
	 */
	protected transient T[] collection;
	/**
	 * 저장된 데이터 개수
	 */
	protected transient int size = 0;
	/**
	 * 데이터를 꺼내올 커서 위치
	 */
	protected transient int cursor = 0;
		
	/**
	 * 기본 생성자<br>
	 * 저장공간의 크기를 지정하지 않았으므로 기본 크기인<br>
	 * DEFAULT_COLLECTION_SIZE 의 값을 사용한다
	 */
	AbstractBomzCollection(){
		this(DEFAULT_COLLECTION_SIZE);
	}
	
	/**
	 * 사용자 지정 크기 생성자<br>
	 * 사용자가 지정한 크기만큼 저장공간을 생성한다<br>
	 */
	@SuppressWarnings("unchecked")
	AbstractBomzCollection(int size){
		COLLECTION_SIZE = size;
		collection = (T[])new Object[size];
	}
			
	/**
	 * 	새로운 배열을 생성하여 기존 배열의 값을 복사한다
	 */
	protected T[] modifyQueue(){
		int queueSize = size;
		queueSize <<= 2;
		return this.modifyQueue(queueSize);
	}
	
	/**
	 * 	새로운 배열을 생성하여 기존 배열의 값을 복사한다
	 */
	@SuppressWarnings("unchecked")
	protected T[] modifyQueue(int queueSize){
		Object[] newQueue = new Object[ (queueSize < size)?size : queueSize ];
		
		if( size == 0 ){
			// 사이즈가 0 일 경우 새로 생성한 배열 적용
			return (T[])newQueue;
		}
		
		return this.modifyQueue(newQueue);
	}
		
	/**
	 * 	새로운 배열을 생성하여 기존 배열의 값을 복사한다
	 */
	@SuppressWarnings("unchecked")
	protected T[] modifyQueue(Object[] newQueue){
		int tmp = (collection.length - cursor) < size ? collection.length - cursor : size;
		
		if( cursor != 0 ){
			// 커서가 0 번째에 있지 않을 경우 뒷부분 값을 먼저 복사
			System.arraycopy(collection, cursor, newQueue, 0, tmp);
		}

		if( tmp < size ){
			// 기존 배열의 0번째 부터 나머지 복사
			System.arraycopy(collection, 0, newQueue, tmp, size - tmp);
		}else if( tmp == size && cursor == 0 ){
			// 기존 배열의 0번째 부터 나머지 복사
			System.arraycopy(collection, 0, newQueue, 0, size);
		}
		
		return (T[])newQueue;
	}
	
	/**
	 * 현재 저장된 데이터가 있는지 여부
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * 저장공간 초기화
	 */
	@SuppressWarnings("unchecked")
	public void clear() {
		cursor = 0;
		size = 0;
		collection = (T[])new Object[COLLECTION_SIZE];	
	}
	
	/**
	 * 저장된 데이터를 java.util.Iterator 형식으로 생성
	 */
	public Iterator<T> iterator() {
		return new Iter();
	}
	
	/**
	 * 저장된 데이터의 개수
	 */
	public int size() {
		return this.size;
	}

	/**
	 * 저장된 데이터를 오브젝트 배열 형식으로 생성
	 */
	public Object[] toArray() {
		return this.modifyQueue(this.size);		
	}

	/**
	 * 저장된 데이터를 사용자가 파라메터로 넘겨준 배열에 대입
	 */
	@SuppressWarnings("unchecked")
	public <L> L[] toArray(L[] c){
		
		if( c == null )	throw new NullPointerException();
	
		if( c.length < this.size )
			c = (L[])java.lang.reflect.Array.newInstance(c.getClass().getComponentType(), this.size);
		
		this.modifyQueue(c);
		
		return c;
	}
	
	/**
	 * 
	 * 저장된 데이터를 java.util.Iterator 형식으로 생성한다
	 * 
	 * @author Bomz
	 * @since 1.0
	 * @version 1.0
	 *
	 */
	private class Iter implements Iterator<T>{
		
		private final ReentrantLock lock = new ReentrantLock();
		private int _cursor = 0;
		private int _size;
		private Object[] _queue;
		
		@SuppressWarnings("unchecked")
		public Iter(){
			this._size = size;
			this._queue = (T[])new Object[size]; 
			
			this.createIterator();
		}

		public boolean hasNext() {
			lock.lock();
			try{
				return (_cursor >= _size) ? false : true;
			}finally{
				lock.unlock();
			}
		}
		
		@SuppressWarnings("unchecked")
		public T next() {
			lock.lock();
			try{
				if( _cursor >= size )
					throw new IllegalStateException();
				
				return (T)_queue[_cursor++];
			}finally{
				lock.unlock();
			}
		}

		public void remove() {
			lock.lock();
			try{
				_cursor++;
			}finally{
				lock.unlock();
			}
		}
		
		private void createIterator(){
			this._queue = modifyQueue(size);
		}
	}
}
