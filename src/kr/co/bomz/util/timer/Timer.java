package kr.co.bomz.util.timer;

/**
 * 타이머 서비스를 이용할 경우 해당 추상 클래스를 구현하여야 한다
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public abstract class Timer {

	/*		반복 주기 (밀리세컨드)		*/
	private final long repeatPeriod;
	
	/*		반복 횟수	*/
	private final int repeatCount;
	
	/*		무한 반복 값		*/
	private static final int INFINITY_REPEAT_COUNT_VALUE = 0;
	
	/*		타이머 아이디	*/
	private long timerId;
	
	/*		타이머가 실행되어야 하는 시간		*/
	private long runningTime = -1L;
	
	/*		현재 수행된 반복 횟수		*/
	private int runningRepeatCount = 0;
	
	/**
	 * 		특정 주기마다 계속해서 반복하는 타이머
	 * @param repeatPeriod		반복 주기 (ms)
	 */
	public Timer(long repeatPeriod){
		this(repeatPeriod, INFINITY_REPEAT_COUNT_VALUE);
	}

	/**
	 * 		특정 주기마다 특정 횟수만큼 반복되는 타이머
	 * @param repeatPeriod		반복주기 (ms)
	 * @param repeatCount			반복횟수. 0 이하의 값일경우 무한반복으로 설정된다
	 */
	public Timer(long repeatPeriod, int repeatCount){
		this.repeatPeriod = repeatPeriod;
		this.repeatCount = repeatCount <= INFINITY_REPEAT_COUNT_VALUE ? 
				INFINITY_REPEAT_COUNT_VALUE : repeatCount;
		
		this.runningTime = System.currentTimeMillis() + this.repeatPeriod;
	}
	
	/**		
	 * 		타이머가 동작해야 하는지 검사	수행<p>
	 * 		동작해야 할 경우 true 리턴
	 */
	boolean isRunningTime(){
		if( this.runningTime == -1 || this.runningTime <= System.currentTimeMillis() ){
			this.runningRepeatCount++;
			return true;
		}
		
		return false;
	}
	
	/**
	 * 		타이머 동작 후 다음에도 동작해야 하는지 검사<p>
	 * 		계속해서 동작해야 할 경우 false 리턴<p>
	 * 		타이머를 종료해야 할 경우 true 리턴
	 */
	boolean isEndTimer(){
		if( this.repeatCount != INFINITY_REPEAT_COUNT_VALUE &&
				this.repeatCount <= this.runningRepeatCount)		return true;
		
		this.runningTime = System.currentTimeMillis() + this.repeatPeriod;
		return false;
	}
	
	/**		해당 타이머 아이디 설정		*/
	void setTimerId(long timerId){
		this.timerId = timerId;
	}
	
	/**		해당 타이머 아이디 리턴		*/
	long getTimerId(){
		return this.timerId;
	}
	
	/**
	 * 		타이머 실행 시 수행
	 */
	public abstract void execute();
	
	/**
	 * 		타이머 종료 시 수행
	 */
	public abstract void stopTimer(TimerStopType timerStopType);
}
