package kr.co.bomz.util.threadpool;

import java.util.Queue;

import kr.co.bomz.util.collection.BomzQueue;

/**
 *
 * 	스레드 풀 <p>
 * 
 *  BomzThread 를 상속받은 클래스를 생성자에 인자값으로 넘겨주면 된다<p>
 *  따로 스레드 풀의 크기를 지정하지 않았다면 DEFAULT_POOL_MAX_SIZE 값으로 스레드 객체가 생성된다<p>
 *  
 *  사용할 때는 getThread() 메소드를 통해 스레드 객체를 넘겨받을 수 있으며<p>
 *  사용 가능한 스레드가 없을 때는 설정에 따라 사용 가능한 스레드가 생길때까지 대기하거나 새로운 스레드를 생성한다<p>
 *  
 *  <code>
 *  public class MyThread1 extends BomzThread{<br>
 * &nbsp;&nbsp;...<br>
 * &nbsp;&nbsp;protected void executeParameter(Object ... parameters) throws Exception{<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;this.addr = parameters[0].toString();<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;this.age = Integer.parseInt(parameter[1]);<br>
 *  &nbsp;&nbsp;}<br>
 *  &nbsp;&nbsp;...<br>
 * 	}<p>
 *  <p>
 *  public class MyThread2 extends BomzThread{<br>
 *  &nbsp;&nbsp;public MyThread2(String name, int age){<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;...<br>
 *  &nbsp;&nbsp;}<br>
 *  &nbsp;&nbsp;...<br>
 *  }<br>
 *  ...<p>
 *  <p>
 *  public class Main{<p>
 *  &nbsp;&nbsp;public static void main(String[] args){<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;BomzThreadPool pool_1 = new BomzThreadPool(MyThread1.class);<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;BomzThreadPool pool_2 = new BomzThreadPool(MyThread2.class, "jully", 27);<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;BomzThreadPool pool_3 = new BomzThreadPool(15, MyThread1.class);<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;...<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;pool1.getThread().start("gangnam 132-1", 45);<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;pool2.getThread().start();<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;...<br>
 *  &nbsp;&nbsp;}<br>
 *  }
 *  </code>
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	BomzThread
 *
 */
public class BomzThreadPool {
	
	private final Queue<BomzThread> queue = new BomzQueue<BomzThread>();

	private final Class<? extends BomzThread> clazz;
	private final Object[] parameters;
	
	private boolean nonWait = true;
	
	// 스레드플 종료 여부
	private boolean close = false;
	
	private final int defaultThreadSize;
	private final int maxThreadSize;
	
	/**
	 * 스레드 풀 생성 및 초기화
	 * @param clazz		kr.co.bomz.util.threadpool.BomzThread 구현체
	 * @param parameters		초기화 파라메터
	 * @throws RuntimeException		초기화 오류시 발생
	 */
	public BomzThreadPool(Class<? extends BomzThread> clazz, Object ... parameters) throws RuntimeException{
		this(3, 5, clazz, parameters);
	}
	
	/**
	 * 스레드 풀 생성 및 초기화
	 * @param clazz		kr.co.bomz.util.threadpool.BomzThread 구현체
	 * @param parameters		초기화 파라메터
	 * @throws RuntimeException		초기화 오류시 발생
	 */
	public BomzThreadPool(int defaultThreadSize, int maxThreadSize, Class<? extends BomzThread> clazz, Object ... parameters) throws RuntimeException{
		this.clazz = clazz;
		this.parameters = parameters;
		this.defaultThreadSize = defaultThreadSize;
		this.maxThreadSize = maxThreadSize;
		
		this.execute(clazz, defaultThreadSize, parameters);
	}
	
	/*		생성자에서 호출. 스레드 객체 생성 후 큐에 추가		*/
	private void execute(Class<? extends BomzThread> clazz, int defaultThreadSize, Object ... parameters) throws RuntimeException{
		
		// 기본 스레드 준비
		for(int i=0; i < defaultThreadSize; i++){
			try {
				this.queue.offer( this.createThread(true, clazz, parameters) );
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
		}
	}
	
	/*		스레드 객체 생성		*/
	private BomzThread createThread(boolean isInit, Class<? extends BomzThread> clazz, Object ... parameters) throws Exception{
		BomzThread th = clazz.newInstance();
		if( parameters.length != 0 )			th.constructorParameter(parameters);
		
		if( isInit )		th.setThreadPool(this);
		
		return th;
	}
	
	/**
	 * BomzThread 에서 동작 후 다시 스레드풀에 등록되기 위해 호출된다
	 * @param thread
	 */
	final synchronized void enqueue(BomzThread thread){
		
		if( this.close ){
			// 현재 스레드풀이 종료 상태일 경우
			thread.coercionClose(true);
			return;		
		}
		
		if( thread != null ){
			if( queue.size() <= this.maxThreadSize ){
				this.queue.offer(thread);
				notify();
				
			}else	{
				thread.coercionClose(true);
				thread = null;
			}
			
		}
	}
	
	/**
	 * 
	 * 스레드풀에서 관리 중인 스레드 요청<p>
	 * 
	 * 모든 스레드가 사용 중일 경우 기본적으로 새로운 스레드를 생성하여 리턴하며
	 * 
	 * setNonWait(false) 를 호출하였을 경우 사용 중인 스레드가 반납될 때까지 대기한다<br>
	 * 
	 * close() 호출 시 BomzThreadPoolCloseException 이 발생하며
	 * 
	 * 스레드풀을 다시 사용할 경우 reset() 을 호출하여 스레드풀 재사용을 알려야한다<br>
	 * 
	 * @return 스레드 객체
	 * @throws BomzThreadPoolCloseException close() 호출 후 해당 메소드 호출 시
	 * @throws RuntimeException 스레드 생성 실패
	 */
	public final synchronized BomzThread getThread() throws BomzThreadPoolCloseException, RuntimeException{
		
		// 종료 상태인데 스레드 요청했을 경우 예외 발생
		if( this.close )		throw new BomzThreadPoolCloseException();
		
		BomzThread thread;
		
		if( this.nonWait ){
			thread  = queue.poll();
			
			if( thread == null ){
				try {
					thread = createThread(false, this.clazz, this.parameters);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			
		}else{
			while(true){
				if( queue.size() <= 0 ){
					// 자원이 없을 경우 enqueue 를 통해 자원이 추가될때까지 대기
					try {
						if( queue.size() <= 0 )		wait();
					} catch (InterruptedException e) {}
				}
				
				thread  = queue.poll();
				if( thread  != null )	break;
			}
			
		}
		
		return thread;
	}
	
	/**
	 * 현재 대기중인 스레드의 개수
	 * @return		대기중인 스레드 개수
	 */
	public int size(){
		return queue.size();
	}
	
	/**		
	 * 	모든 스레드가 사용중일 때 요청이 오면 동작중인 스레드를 기다릴지 아니면
	 * 	새로운 스레드를 생성할지 여부
	 * 		 
	 * @return	true 일 경우 새로운 스레드를 생성
	 */
	public boolean isNonWait(){
		return this.nonWait;
	}
	
	/**		
	 * 		모든 스레드가 사용중일 때 요청이 오면 동작중인 스레드를 기다릴지 아니면
	 * 		새로운 스레드를 생성할지 여부<p>
	 * 
	 * 		true 로 설정 시 모든 스레드가 사용중이라면 새로운 스레드를 생성한다<p>
	 * 		기본값 true
	 * 		 
	 */
	public void setNonWait(boolean nonWait){
		this.nonWait = nonWait;
	}
	
	/**
	 * 		스레드풀에서 관리하는 모든 스레드를 종료시킨다
	 */
	public void closeThreadPool(){
		this.close = true;
		
		// 대기중인 스레드 강제 종료 작업 수행
		while( !this.queue.isEmpty() ){
			this.queue.poll().coercionClose(true);
		}
		
	}
	
	/**
	 * close() 를 통해 사용이 중지되었던 스레드풀을 재사용시 호출
	 * 
	 * @throws BomzThreadPoolCloseException close 되지 않은 상태에서 호출 시 예외가 발생한다
	 */
	public void reset() throws BomzThreadPoolCloseException{
		// close() 가 호출되었었는지 확인한다
		if( !this.close )		throw new BomzThreadPoolCloseException();
				
		this.execute(this.clazz, this.defaultThreadSize, this.parameters);
		
		this.close = false;		// 현재 상태 변경
	}
	
	/**
	 * 현재 스레드풀 종료 여부
	 * @return		종료되어있을 경우 true
	 */
	public boolean isClose(){
		return this.close;
	}
}
