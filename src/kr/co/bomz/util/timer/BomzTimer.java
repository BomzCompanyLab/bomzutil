package kr.co.bomz.util.timer;

import kr.co.bomz.util.threadpool.BomzThread;
import kr.co.bomz.util.threadpool.BomzThreadPool;


/**
 * 
 * ���� �ð��� �ֱ�� �ݺ� ȣ���ϴ� Ÿ�̸�<p>
 * 
 * <code>
 * public class MyTimer extends Timer{<br>
 * &nbsp;&nbsp;...<br>
 * }<p>
 * ...<p>
 * public class Main{<br>
 * &nbsp;&nbsp;public static void main(String[] args){<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;...<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;BomzTimer timer = new BomzTimer();<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;long t1_id = timer.addTimer(new MyTimer(3000));	// 3�ʸ��� ����<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;long t2_id = timer.addTimer(new MyTimer(60000, 10));	// 60�ʸ��� �� 10�� ����<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;...<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;timer.removeTimer(t2_id);<br>
 * &nbsp;&nbsp;}<br>
 * }
 * </code>
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class BomzTimer{

	/*		Ÿ�̸Ӹ� �����ϱ� ���� ���̵�		*/
	private long timerId = 281L;
	
	/**		Ÿ�̸� ���� ���� �˻� �ֱ� (���� : ms)		*/
	public static final long DEFAULT_REPEAT_CHECK_TIME = 500;
	
	private BomzTimerRun threadObj;
	private java.util.Map<Long, Timer> timerInfoMap;
	
	private BomzThreadPool timerThreadPool;
	
	private final long TIMER_SLEEP_TIME;
	
	private final Object lock = new Object();
	
	/**		Bomz Timer		*/
	public BomzTimer(){
		this(DEFAULT_REPEAT_CHECK_TIME);
	}
	
	/**
	 * Ÿ�̸� �ݺ� �˻� �ֱ⸦ ������ �� �ִ�<p>
	 * �и�����Ʈ �����̸� 500���� ���� ��� �⺻ �� 500���� �����ȴ�
	 * 
	 * @param repeatCheckTime		Ÿ�̸� ���� ���� �˻� ��� �ð�
	 */
	public BomzTimer(long repeatCheckTime){
		this.TIMER_SLEEP_TIME = repeatCheckTime < DEFAULT_REPEAT_CHECK_TIME ? DEFAULT_REPEAT_CHECK_TIME : repeatCheckTime;
	}
	
	/*		Ÿ�̸� ���̵� ����		*/
	private final synchronized long getTimerId(){
		if( this.threadObj == null ){
			this.threadObj = new BomzTimerRun();
			this.threadObj.start();
		}
		
		if( this.timerInfoMap == null )		this.timerInfoMap = new java.util.HashMap<Long, Timer>();
		
		if( this.timerThreadPool == null ){		// ù ���� �� ����� ����
			this.timerThreadPool = new BomzThreadPool(kr.co.bomz.util.timer.TimerRunning.class, this);
		}else	 if( this.timerThreadPool.isClose() ){			// close ���¿��� ��� ����� 
			this.timerThreadPool.reset();
		}
		
		return this.timerId++;
	}
		
	/**		Ÿ�̸� �߰�		*/
	public long addTimer(Timer timer){
		synchronized( this.lock ){
			long timerId = this.getTimerId();
			timer.setTimerId( timerId );
			this.timerInfoMap.put(timerId, timer);
			
			return timerId;
		}
	}
	
	/**		Ÿ�̸� ����		*/
	void removeTimer(Timer timer){
		synchronized( this.lock ){
			this.timerInfoMap.remove(timer.getTimerId());
		}
		
		timer.stopTimer(TimerStopType.STOP_SYSTEM);
	}
	
	/**
	 * Ÿ�̸� ����
	 * @param timerId	Ÿ�̸� ���̵�
	 */
	public void removeTimer(long timerId){
		Timer timer;
		synchronized( this.lock ){
			timer = this.timerInfoMap.remove(timerId);		
		}
		
		if( timer != null ){
			timer.stopTimer(TimerStopType.STOP_USER);
		}
	}
	
	/**
	 * Ÿ�̸� ����
	 */
	public void closeTimer(){
		synchronized( lock ){
			if( this.threadObj != null ){
				this.threadObj.run = false;
				this.threadObj = null;
			}
						
			if( this.timerInfoMap != null ){
				java.util.Iterator<Timer> iter = this.timerInfoMap.values().iterator();
				while( iter.hasNext() ){
					iter.next().stopTimer(TimerStopType.STOP_CLOSE);
				}
				this.timerInfoMap.clear();
			}
			
			if( this.timerThreadPool != null )	this.timerThreadPool.closeThreadPool();
		}
		
	}
	
	/**
	 * Ÿ�̸��� ���� ���θ� �ǽð� Ȯ���Ѵ�
	 * @author Bomz
	 * @since 1.0
	 * @version 1.0
	 *
	 */
	class BomzTimerRun extends Thread{
				
		private boolean run = true;
		
		public void run(){
			
			Timer timer;
			BomzThread timerThread;
			java.util.Iterator<Timer> timers;
			
			while( this.run ){
							
				synchronized( lock ){
					timers = timerInfoMap.values().iterator();
					while( timers.hasNext() ){
						timer = timers.next();
						if( timer.isRunningTime() ){
							timerThread = timerThreadPool.getThread();
							timerThread.start(timer);
						}
					}
				}
				
				// �������̶�� ��� ����
				if( this.run )			try{		Thread.sleep(TIMER_SLEEP_TIME);		}catch(Exception e){}
			}
			
		}
	}
}
