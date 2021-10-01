package kr.co.bomz.util.threadpool;

import java.lang.Thread.State;

/**
 * ������Ǯ�� �̿��� ������� �ش� Ŭ������ ��ӹ޾� �����ؾ� �Ѵ�.<p>
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 */
public abstract class BomzThread{

	private Thread thread;
	private boolean run = false;
	protected BomzThreadPool pool;
		
	// ���� ���� ����
	private boolean coercionClose = false;
	
	public BomzThread(){
//		TODO �޸� ������ ���� �ּ�
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
	
	// ������Ǯ�� �ش� �����带 ������ �����ϵ��� �ݳ��Ѵ�
	private void poolEnqueue(){
		if( pool != null ){
			run = false;
//			TODO �޸� ������ ���� �ּ�
//			pool.enqueue(_this);
			pool.enqueue(this);
		}
	}
	
	/**
	 * ������ Ǯ���� ������ ������ ����ü�� �����Ų��
	 * @param parameters	���� �� ����� �Ķ����
	 * @return	������ ���� ���� �� true
	 */
	public final boolean start(Object ... parameters){
		if( this.run )			return false;

		try {
			this.executeParameter(parameters);
		} catch (Exception e1) {
			// �ش� �����带 �ٽ� ť�� �߰�
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
	 * ������ Ǯ ����
	 * @param pool
	 */
	final void setThreadPool(BomzThreadPool pool){
		if( pool != null )	this.pool = pool;
	}
	
	/**
	 * BomzThread ���� Ŭ���� new �� ���� ������ �ѹ��� ȣ��ȴ�.<p>
	 * 
	 * new BomzThreadPool(BomzThread.class , param1, param2, param3, ...)
	 * ���� param1 ~ ... ������ �ʱ�ȭ �� �� ���ȴ�
	 */
	protected void constructorParameter(Object ... parameters) throws Exception{}
	
	/**
	 * ���� �ڵ�
	 */
	protected abstract void execute() throws Exception;
	
	/**
	 * �ڿ� �ݳ�
	 */
	protected void close(){}
	
	/**
	 * ������ Ǯ�� ���� ������ ��� �Ķ���� ����<p>
	 * start(Object ... parameter) ���� �߰��� �Ķ���͸� �����Ѵ�
	 * @param parameters		start(Object ... parameter) ���� �Ѱ��� �Ķ����
	 * @throws Exception	����ȯ ���н� ���� �߻�
	 */
	protected void executeParameter(Object ... parameters) throws Exception{}
	
	/**
	 * BomzThreadPool.close() ȣ�� �� �������� �������� ���� ���Ḧ ���� ȣ��
	 */
	void coercionClose(boolean isSleep){
		this.coercionClose = true;		// ���� ���� ����
		
		if( isSleep ){
			// �����ִ� �����带 �����
			synchronized(thread){
				thread.notify();
			}
		}
	}
}
