package kr.co.bomz.util.timer;

import kr.co.bomz.util.threadpool.BomzThread;
import kr.co.bomz.util.threadpool.BomzThreadPool;


/**
 * 
 * 일정 시간을 주기로 반복 호출하는 타이머<p>
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
 * &nbsp;&nbsp;&nbsp;&nbsp;long t1_id = timer.addTimer(new MyTimer(3000));	// 3초마다 실행<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;long t2_id = timer.addTimer(new MyTimer(60000, 10));	// 60초마다 총 10번 실행<br>
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

	/*		타이머를 제어하기 위한 아이디		*/
	private long timerId = 281L;
	
	/**		타이머 동작 여부 검사 주기 (단위 : ms)		*/
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
	 * 타이머 반복 검사 주기를 지정할 수 있다<p>
	 * 밀리세컨트 단위이며 500보다 작을 경우 기본 값 500으로 설정된다
	 * 
	 * @param repeatCheckTime		타이머 종료 여부 검사 대기 시간
	 */
	public BomzTimer(long repeatCheckTime){
		this.TIMER_SLEEP_TIME = repeatCheckTime < DEFAULT_REPEAT_CHECK_TIME ? DEFAULT_REPEAT_CHECK_TIME : repeatCheckTime;
	}
	
	/*		타이머 아이디 생성		*/
	private final synchronized long getTimerId(){
		if( this.threadObj == null ){
			this.threadObj = new BomzTimerRun();
			this.threadObj.start();
		}
		
		if( this.timerInfoMap == null )		this.timerInfoMap = new java.util.HashMap<Long, Timer>();
		
		if( this.timerThreadPool == null ){		// 첫 동작 시 제대로 생성
			this.timerThreadPool = new BomzThreadPool(kr.co.bomz.util.timer.TimerRunning.class, this);
		}else	 if( this.timerThreadPool.isClose() ){			// close 상태였을 경우 재시작 
			this.timerThreadPool.reset();
		}
		
		return this.timerId++;
	}
		
	/**		타이머 추가		*/
	public long addTimer(Timer timer){
		synchronized( this.lock ){
			long timerId = this.getTimerId();
			timer.setTimerId( timerId );
			this.timerInfoMap.put(timerId, timer);
			
			return timerId;
		}
	}
	
	/**		타이머 삭제		*/
	void removeTimer(Timer timer){
		synchronized( this.lock ){
			this.timerInfoMap.remove(timer.getTimerId());
		}
		
		timer.stopTimer(TimerStopType.STOP_SYSTEM);
	}
	
	/**
	 * 타이머 삭제
	 * @param timerId	타이머 아이디
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
	 * 타이머 종료
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
	 * 타이머의 동작 여부를 실시간 확인한다
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
				
				// 동작중이라면 잠시 쉬기
				if( this.run )			try{		Thread.sleep(TIMER_SLEEP_TIME);		}catch(Exception e){}
			}
			
		}
	}
}
