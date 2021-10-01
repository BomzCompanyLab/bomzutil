package kr.co.bomz.util.threadpool;

import java.util.Queue;

import kr.co.bomz.util.collection.BomzQueue;

/**
 *
 * 	������ Ǯ <p>
 * 
 *  BomzThread �� ��ӹ��� Ŭ������ �����ڿ� ���ڰ����� �Ѱ��ָ� �ȴ�<p>
 *  ���� ������ Ǯ�� ũ�⸦ �������� �ʾҴٸ� DEFAULT_POOL_MAX_SIZE ������ ������ ��ü�� �����ȴ�<p>
 *  
 *  ����� ���� getThread() �޼ҵ带 ���� ������ ��ü�� �Ѱܹ��� �� ������<p>
 *  ��� ������ �����尡 ���� ���� ������ ���� ��� ������ �����尡 ���涧���� ����ϰų� ���ο� �����带 �����Ѵ�<p>
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
	
	// �������� ���� ����
	private boolean close = false;
	
	private final int defaultThreadSize;
	private final int maxThreadSize;
	
	/**
	 * ������ Ǯ ���� �� �ʱ�ȭ
	 * @param clazz		kr.co.bomz.util.threadpool.BomzThread ����ü
	 * @param parameters		�ʱ�ȭ �Ķ����
	 * @throws RuntimeException		�ʱ�ȭ ������ �߻�
	 */
	public BomzThreadPool(Class<? extends BomzThread> clazz, Object ... parameters) throws RuntimeException{
		this(3, 5, clazz, parameters);
	}
	
	/**
	 * ������ Ǯ ���� �� �ʱ�ȭ
	 * @param clazz		kr.co.bomz.util.threadpool.BomzThread ����ü
	 * @param parameters		�ʱ�ȭ �Ķ����
	 * @throws RuntimeException		�ʱ�ȭ ������ �߻�
	 */
	public BomzThreadPool(int defaultThreadSize, int maxThreadSize, Class<? extends BomzThread> clazz, Object ... parameters) throws RuntimeException{
		this.clazz = clazz;
		this.parameters = parameters;
		this.defaultThreadSize = defaultThreadSize;
		this.maxThreadSize = maxThreadSize;
		
		this.execute(clazz, defaultThreadSize, parameters);
	}
	
	/*		�����ڿ��� ȣ��. ������ ��ü ���� �� ť�� �߰�		*/
	private void execute(Class<? extends BomzThread> clazz, int defaultThreadSize, Object ... parameters) throws RuntimeException{
		
		// �⺻ ������ �غ�
		for(int i=0; i < defaultThreadSize; i++){
			try {
				this.queue.offer( this.createThread(true, clazz, parameters) );
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
		}
	}
	
	/*		������ ��ü ����		*/
	private BomzThread createThread(boolean isInit, Class<? extends BomzThread> clazz, Object ... parameters) throws Exception{
		BomzThread th = clazz.newInstance();
		if( parameters.length != 0 )			th.constructorParameter(parameters);
		
		if( isInit )		th.setThreadPool(this);
		
		return th;
	}
	
	/**
	 * BomzThread ���� ���� �� �ٽ� ������Ǯ�� ��ϵǱ� ���� ȣ��ȴ�
	 * @param thread
	 */
	final synchronized void enqueue(BomzThread thread){
		
		if( this.close ){
			// ���� ������Ǯ�� ���� ������ ���
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
	 * ������Ǯ���� ���� ���� ������ ��û<p>
	 * 
	 * ��� �����尡 ��� ���� ��� �⺻������ ���ο� �����带 �����Ͽ� �����ϸ�
	 * 
	 * setNonWait(false) �� ȣ���Ͽ��� ��� ��� ���� �����尡 �ݳ��� ������ ����Ѵ�<br>
	 * 
	 * close() ȣ�� �� BomzThreadPoolCloseException �� �߻��ϸ�
	 * 
	 * ������Ǯ�� �ٽ� ����� ��� reset() �� ȣ���Ͽ� ������Ǯ ������ �˷����Ѵ�<br>
	 * 
	 * @return ������ ��ü
	 * @throws BomzThreadPoolCloseException close() ȣ�� �� �ش� �޼ҵ� ȣ�� ��
	 * @throws RuntimeException ������ ���� ����
	 */
	public final synchronized BomzThread getThread() throws BomzThreadPoolCloseException, RuntimeException{
		
		// ���� �����ε� ������ ��û���� ��� ���� �߻�
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
					// �ڿ��� ���� ��� enqueue �� ���� �ڿ��� �߰��ɶ����� ���
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
	 * ���� ������� �������� ����
	 * @return		������� ������ ����
	 */
	public int size(){
		return queue.size();
	}
	
	/**		
	 * 	��� �����尡 ������� �� ��û�� ���� �������� �����带 ��ٸ��� �ƴϸ�
	 * 	���ο� �����带 �������� ����
	 * 		 
	 * @return	true �� ��� ���ο� �����带 ����
	 */
	public boolean isNonWait(){
		return this.nonWait;
	}
	
	/**		
	 * 		��� �����尡 ������� �� ��û�� ���� �������� �����带 ��ٸ��� �ƴϸ�
	 * 		���ο� �����带 �������� ����<p>
	 * 
	 * 		true �� ���� �� ��� �����尡 ������̶�� ���ο� �����带 �����Ѵ�<p>
	 * 		�⺻�� true
	 * 		 
	 */
	public void setNonWait(boolean nonWait){
		this.nonWait = nonWait;
	}
	
	/**
	 * 		������Ǯ���� �����ϴ� ��� �����带 �����Ų��
	 */
	public void closeThreadPool(){
		this.close = true;
		
		// ������� ������ ���� ���� �۾� ����
		while( !this.queue.isEmpty() ){
			this.queue.poll().coercionClose(true);
		}
		
	}
	
	/**
	 * close() �� ���� ����� �����Ǿ��� ������Ǯ�� ����� ȣ��
	 * 
	 * @throws BomzThreadPoolCloseException close ���� ���� ���¿��� ȣ�� �� ���ܰ� �߻��Ѵ�
	 */
	public void reset() throws BomzThreadPoolCloseException{
		// close() �� ȣ��Ǿ������� Ȯ���Ѵ�
		if( !this.close )		throw new BomzThreadPoolCloseException();
				
		this.execute(this.clazz, this.defaultThreadSize, this.parameters);
		
		this.close = false;		// ���� ���� ����
	}
	
	/**
	 * ���� ������Ǯ ���� ����
	 * @return		����Ǿ����� ��� true
	 */
	public boolean isClose(){
		return this.close;
	}
}
