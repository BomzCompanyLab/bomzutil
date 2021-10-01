package kr.co.bomz.util.timer;

/**
 * Ÿ�̸� ���񽺸� �̿��� ��� �ش� �߻� Ŭ������ �����Ͽ��� �Ѵ�
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public abstract class Timer {

	/*		�ݺ� �ֱ� (�и�������)		*/
	private final long repeatPeriod;
	
	/*		�ݺ� Ƚ��	*/
	private final int repeatCount;
	
	/*		���� �ݺ� ��		*/
	private static final int INFINITY_REPEAT_COUNT_VALUE = 0;
	
	/*		Ÿ�̸� ���̵�	*/
	private long timerId;
	
	/*		Ÿ�̸Ӱ� ����Ǿ�� �ϴ� �ð�		*/
	private long runningTime = -1L;
	
	/*		���� ����� �ݺ� Ƚ��		*/
	private int runningRepeatCount = 0;
	
	/**
	 * 		Ư�� �ֱ⸶�� ����ؼ� �ݺ��ϴ� Ÿ�̸�
	 * @param repeatPeriod		�ݺ� �ֱ� (ms)
	 */
	public Timer(long repeatPeriod){
		this(repeatPeriod, INFINITY_REPEAT_COUNT_VALUE);
	}

	/**
	 * 		Ư�� �ֱ⸶�� Ư�� Ƚ����ŭ �ݺ��Ǵ� Ÿ�̸�
	 * @param repeatPeriod		�ݺ��ֱ� (ms)
	 * @param repeatCount			�ݺ�Ƚ��. 0 ������ ���ϰ�� ���ѹݺ����� �����ȴ�
	 */
	public Timer(long repeatPeriod, int repeatCount){
		this.repeatPeriod = repeatPeriod;
		this.repeatCount = repeatCount <= INFINITY_REPEAT_COUNT_VALUE ? 
				INFINITY_REPEAT_COUNT_VALUE : repeatCount;
		
		this.runningTime = System.currentTimeMillis() + this.repeatPeriod;
	}
	
	/**		
	 * 		Ÿ�̸Ӱ� �����ؾ� �ϴ��� �˻�	����<p>
	 * 		�����ؾ� �� ��� true ����
	 */
	boolean isRunningTime(){
		if( this.runningTime == -1 || this.runningTime <= System.currentTimeMillis() ){
			this.runningRepeatCount++;
			return true;
		}
		
		return false;
	}
	
	/**
	 * 		Ÿ�̸� ���� �� �������� �����ؾ� �ϴ��� �˻�<p>
	 * 		����ؼ� �����ؾ� �� ��� false ����<p>
	 * 		Ÿ�̸Ӹ� �����ؾ� �� ��� true ����
	 */
	boolean isEndTimer(){
		if( this.repeatCount != INFINITY_REPEAT_COUNT_VALUE &&
				this.repeatCount <= this.runningRepeatCount)		return true;
		
		this.runningTime = System.currentTimeMillis() + this.repeatPeriod;
		return false;
	}
	
	/**		�ش� Ÿ�̸� ���̵� ����		*/
	void setTimerId(long timerId){
		this.timerId = timerId;
	}
	
	/**		�ش� Ÿ�̸� ���̵� ����		*/
	long getTimerId(){
		return this.timerId;
	}
	
	/**
	 * 		Ÿ�̸� ���� �� ����
	 */
	public abstract void execute();
	
	/**
	 * 		Ÿ�̸� ���� �� ����
	 */
	public abstract void stopTimer(TimerStopType timerStopType);
}
