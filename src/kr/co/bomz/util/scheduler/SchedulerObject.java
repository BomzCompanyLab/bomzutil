package kr.co.bomz.util.scheduler;

/**
 * 
 * ���� ���� ������ ���� ����
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class SchedulerObject {

	// ���� �������� ���� ����. removeSchedule ȣ�� �� STOP ���� ����ȴ�
	private SchedulerState schedulerState = SchedulerState.START;
	
	private final long schedulerId;
	private final Scheduler scheduler;
	
	private Integer repeatDay; 
	private int[] days;
	private SchedulerWeek[] weeks;
	
	private int[][] hourAndMinute;
	
	private SchedulerRepeatType repeatType;
	
	private long startTime;
	
	/*
	 * 		-1 �� ��� ���� ��¥�� ����,
	 */
	private long endTime;
	
	private boolean checkStartDate = false;
	private int beforeDay;
	private boolean checkRepeat = false;
	
	/**
	 * 		�ش� ������ ������ �ݺ� Ÿ���� RepeatDay �� �����Ǿ� ���� ���� ���ȴ�
	 * 		�������� �����Ϸ��� ��¥�� �ش� ������ ��¥�� ������ �������� �����ϸ�,
	 * 		�ش� ���� ���� ���� ��¥�� �����Ѵ�
	 */
	private int nextRepeatYear, nextRepeatMonth, nextRepeatDay = -1;	
	
	/**
	 * 	�̹� ����� �ð��� ����ȴ�.
	 *  �Ϸ翡 ������ 
	 */
	private int executeHourAndMinuteIndex;
	private int[][] executeHourAndMinute;
	
	/**
	 * 
	 * @param scheduler		������ ������ ����ü
	 * @param schedulerId		������ ��� ���̵�
	 */
	SchedulerObject(Scheduler scheduler, long schedulerId){
		 this.scheduler = scheduler;
		 this.schedulerId = schedulerId;
	 }
	 
	/**
	 * ������ ����
	 */
	 void removeScheduler(){
		 this.schedulerState = SchedulerState.STOP;
	 }
	
	 /**
	  * ������ ���� �ð�
	  * @return		������ ���� �ð�
	  */
	long getStartTime() {
		return startTime;
	}

	/**
	 * ������ ���� �ð� ����
	 * @param startTime		������ ���� �ð�
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * ������ ���� �ð�
	 * @return		������ ���� �ð�
	 */
	long getEndTime() {
		return endTime;
	}

	/**
	 * ������ ���� �ð� ����
	 * @param endTime		������ ���� �ð�
	 */
	void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * ������ �ݺ��ֱ� ���� (����:��)
	 * @param repeatDay		������ �ݺ� �ֱ�
	 */
	void setRepeatDay(Integer repeatDay) {
		this.repeatDay = repeatDay;
		if( this.repeatDay != null )	this.repeatType = SchedulerRepeatType.REPEAT_DAY;
	}

	/**
	 * ������ ���� ���� ����
	 * @param weeks		������ ���� ����
	 */
	void setWeeks(SchedulerWeek[] weeks) {
		this.weeks = weeks;
		if( this.weeks != null )	this.repeatType = SchedulerRepeatType.WEEKS;
	}

	/**
	 *  ������ ���� ��¥ ����
	 * @param days		������ ���� ��¥
	 */
	void setDays(int[] days) {
		this.days = days;
		if( this.days != null )	this.repeatType = SchedulerRepeatType.DAYS;
	}

	/**
	 * ������ ���� �ð�/�� ����
	 * @param hourAndMinute		������ ���� �ð�/��
	 */
	void setHourAndMinute(int[][] hourAndMinute) {
		this.hourAndMinute = hourAndMinute;
	}
	
	/**
	 * ������ ���̵�
	 * @return		������ ���̵�
	 */
	public long getSchedulerId(){
		return this.schedulerId;
	}
	
	/**
	 * ������ ���� ����
	 * @return		������ ����
	 */
	SchedulerState getScheduleState(){
		return this.schedulerState;
	}
	
	/**
	 * 	������ ���°� STOP �������� ����
	 * @return		STOP ������ ��� true
	 */
	boolean isScheduleStateAsStop(){
		return this.schedulerState == SchedulerState.STOP;
	}
	
	/**
	 * ������ ����ü ����
	 * @return	������ ����ü
	 */
	Scheduler getScheduler(){
		return this.scheduler;
	}
	
	/**		������ ����ü ����		*/
	void startScheduler(){
		new Thread(new Runnable(){
			public void run(){
				// ������ ���� null �˻�
				if( scheduler != null )		scheduler.execute();
			}
		}).start();
		
	}
	
	/**
	 * ������ ����ü ���� ����
	 * @return		null �� ��� true
	 */
	public boolean isNullScheudler(){
		return this.scheduler == null;
	}
	
	/**
	 * ������ ���� ��¥ �˻� ��� ����
	 * @return		������ ���� ��¥ �˻� ��� ����
	 */
	boolean isCheckStartDate(){
		return this.checkStartDate;
	}
	
	/**
	 * ������ ���� ��¥ ���� ����
	 */
	void changeCheckStartDate(){
		this.checkStartDate = true;
	}

	/**
	 * ������ ���� ��� 
	 * @return		������ ���� ���
	 */
	public SchedulerRepeatType getRepeatType() {
		return repeatType;
	}

	/**
	 * ������ �ݺ� �ֱ� (����:��)
	 * @return		������ �ݺ� �ֱ� (����:��)
	 */
	Integer getRepeatDay() {
		return repeatDay;
	}
	
	/**
	 * 	������ ���� ��¥ �� ��û�� ��¥�� �ִ��� �˻�
	 * @param nowDay		��û ��¥
	 * @return		������ ��¥�� �ԷµǾ� ���� ��� true
	 */
	boolean isEqualsDays(int nowDay){
		for(int day : days){
			if( day == nowDay )	return true;
		}
		return false;
	}
	
	/**
	 * 	������ ���� ��¥ ���� ����
	 * @return		�����Ǿ����� �ʴٸ� true
	 */
	boolean isNullDays(){
		return days == null;
	}

	/**
	 * ������ ���� ����
	 * @return		������ ���� ����
	 */
	SchedulerWeek[] getWeeks() {
		return weeks;
	}

	int getHourAndMinuteSize(){
		return hourAndMinute.length;
	}
	
	int getHourAndMinuteValue(int a, int b){
		return hourAndMinute[a][b];
	}
	
	/**
	 * ���� �ð�/�� ������ null ������ �˻�
	 * @return		null �� ��� true
	 */
	boolean isNullHourAndMinute(){
		return hourAndMinute == null;
	}
	
	/**
	 * ������ �� ���� ��¥
	 * @return		������ �� ���� ��¥
	 */
	int getBeforeDay() {
		return beforeDay;
	}

	/**
	 * ������ �� ���� ��¥ ����
	 * @param beforeDay		�� ���� ��¥
	 */
	void setBeforeDay(int beforeDay) {
		this.beforeDay = beforeDay;
	}

	/**
	 * �ݺ� �ֱ� �˻� ���
	 * @return		�ݺ� �ֱ� �˻� ���
	 */
	boolean isCheckRepeat() {
		return checkRepeat;
	}

	/**
	 * �ݺ� �ֱ� �˻� ��� ����
	 * @param checkRepeat		�ݺ� �ֱ� �˻� ���
	 */
	void setCheckRepeat(boolean checkRepeat) {
		this.checkRepeat = checkRepeat;
	}
	
	/**
	 * ���� �ݺ� ���� ��
	 * @return		���� �ݺ� ���� ��
	 */
	int getNextRepeatYear() {
		return nextRepeatYear;
	}

	/**
	 * ���� �ݺ� ���� ��
	 * @return		���� �ݺ� ���� ��
	 */
	int getNextRepeatMonth() {
		return nextRepeatMonth;
	}

	/**
	 * ���� �ݺ� ���� ��
	 * @return		���� �ݺ� ���� ��
	 */
	int getNextRepeatDay() {
		return nextRepeatDay;
	}

	/**
	 * ���� �ݺ� ���� ��¥ ����
	 * @param nextRepeatYear			��
	 * @param nextRepeatMonth		��
	 * @param nextRepeatDay			��
	 */
	void setNextRepeatDay(int nextRepeatYear, int nextRepeatMonth, int nextRepeatDay) {
		this.nextRepeatYear = nextRepeatYear;
		this.nextRepeatMonth = nextRepeatMonth;
		this.nextRepeatDay = nextRepeatDay;
	}
	
	/**
	 *������ ���� �ð�/�� ����
	 * @param hour			��
	 * @param minute		��
	 */
	void addHourAndMinute(int hour, int minute){
		try{
			this.executeHourAndMinute[executeHourAndMinuteIndex][0] = hour;
			this.executeHourAndMinute[executeHourAndMinuteIndex++][1] = minute;
		}catch(Exception e){
			// ���� ������ �ʰ����� ������ ���� ó��.
		}
	}
	
	/**
	 * ������ ���� �ð�/�� ���� ����
	 */
	void clearHourAndMinute(){
		this.executeHourAndMinuteIndex = 0;
		this.executeHourAndMinute = new int[this.hourAndMinute.length][2];
	}
	
	/**
	 * ������ ���� �ð� �˻�
	 * @param hour			��
	 * @param minute		��
	 * @return					���� ���� ���
	 */
	boolean checkExecuteHourAndMinute(int hour, int minute){
		for(int i=0; i < this.executeHourAndMinuteIndex; i++){
			if( this.executeHourAndMinute[i][0] == hour && this.executeHourAndMinute[i][1] == minute )	return false;
		}
		
		return true;
	}
}
