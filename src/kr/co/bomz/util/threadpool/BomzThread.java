package kr.co.bomz.util.threadpool;

import java.lang.Thread.State;

/**
 * 스레드풀을 이용할 스레드는 해당 클래스를 상속받아 구현해야 한다.<p>
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 */
public abstract class BomzThread{

	private Thread thread;
	private boolean run = false;
	protected BomzThreadPool pool;
		
	// 강제 종료 여부
	private boolean coercionClose = false;
	
	public BomzThread(){
//		TODO 메모리 누수로 인해 주석
//		this._this = this;
		
		this.thread = new Thread( new Runnable(){
			public void run(){
								
				while(true){
					
					if( coercionClose )		break;
					
					try {
						synchronized(thread){
							thread.wait();
						}
					} catch (InterruptedException e) {}
							
					if( coercionClose )		break;
					
					run = true;
					
					try {
						execute();
					} catch (Exception e) {
						System.err.println("Bomz thread pool execute() error : " + e.getMessage());
					}finally{
						threadClose();
					}
						
					if( pool == null )		break;
				}
				
			}
		});
		
		this.thread.start();
	}
	
	private final void threadClose(){
		this.close();
		this.poolEnqueue();		
	}
	
	// 스레드풀에 해당 스레드를 재사용이 가능하도록 반납한다
	private void poolEnqueue(){
		if( pool != null ){
			run = false;
//			TODO 메모리 누수로 인해 주석
//			pool.enqueue(_this);
			pool.enqueue(this);
		}
	}
	
	/**
	 * 스레드 풀에서 꺼내온 스레드 구현체를 실행시킨다
	 * @param parameters	실행 시 사용할 파라메터
	 * @return	스레드 동작 성공 시 true
	 */
	public final boolean start(Object ... parameters){
		if( this.run )			return false;

		try {
			this.executeParameter(parameters);
		} catch (Exception e1) {
			// 해당 스레드를 다시 큐에 추가
			this.threadClose();
			return false;
		}
		
		while( true ){
			if( thread.getState() == State.WAITING )	break;
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {}
		}
		
		synchronized(thread){
			thread.notify();
		}
		
		return true;
	}
	
	/**
	 * 스레드 풀 설정
	 * @param pool
	 */
	final void setThreadPool(BomzThreadPool pool){
		if( pool != null )	this.pool = pool;
	}
	
	/**
	 * BomzThread 구현 클래스 new 를 통한 생성시 한번만 호출된다.<p>
	 * 
	 * new BomzThreadPool(BomzThread.class , param1, param2, param3, ...)
	 * 에서 param1 ~ ... 정보를 초기화 할 때 사용된다
	 */
	protected void constructorParameter(Object ... parameters) throws Exception{}
	
	/**
	 * 동작 코드
	 */
	protected abstract void execute() throws Exception;
	
	/**
	 * 자원 반납
	 */
	protected void close(){}
	
	/**
	 * 스레드 풀에 의해 동작할 경우 파라메터 설정<p>
	 * start(Object ... parameter) 에서 추가한 파라메터를 설정한다
	 * @param parameters		start(Object ... parameter) 에서 넘겨진 파라메터
	 * @throws Exception	형변환 실패시 오류 발생
	 */
	protected void executeParameter(Object ... parameters) throws Exception{}
	
	/**
	 * BomzThreadPool.close() 호출 시 동작중인 스레드의 강제 종료를 위해 호출
	 */
	void coercionClose(boolean isSleep){
		this.coercionClose = true;		// 강제 종료 설정
		
		if( isSleep ){
			// 잠들어있는 스레드를 깨운다
			synchronized(thread){
				thread.notify();
			}
		}
	}
}
