package kr.co.bomz.util.scheduler;

/**
 * 
 * 동작 중인 스케줄 상태 정보
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class SchedulerObject {

	// 현재 스케쥴의 동작 상태. removeSchedule 호출 시 STOP 으로 변경된다
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
	 * 		-1 일 경우 종료 날짜가 없음,
	 */
	private long endTime;
	
	private boolean checkStartDate = false;
	private int beforeDay;
	private boolean checkRepeat = false;
	
	/**
	 * 		해당 변수는 스케쥴 반복 타입이 RepeatDay 로 설정되어 있을 때만 사용된다
	 * 		스케쥴을 실행하려는 날짜와 해당 변수의 날짜가 같으면 스케쥴을 실행하며,
	 * 		해당 값을 다음 실행 날짜로 변경한다
	 */
	private int nextRepeatYear, nextRepeatMonth, nextRepeatDay = -1;	
	
	/**
	 * 	이미 실행된 시간이 저장된다.
	 *  하루에 동일한 
	 */
	private int executeHourAndMinuteIndex;
	private int[][] executeHourAndMinute;
	
	/**
	 * 
	 * @param scheduler		실행할 스케줄 구현체
	 * @param schedulerId		스케줄 등록 아이디
	 */
	SchedulerObject(Scheduler scheduler, long schedulerId){
		 this.scheduler = scheduler;
		 this.schedulerId = schedulerId;
	 }
	 
	/**
	 * 스케줄 삭제
	 */
	 void removeScheduler(){
		 this.schedulerState = SchedulerState.STOP;
	 }
	
	 /**
	  * 스케줄 시작 시간
	  * @return		스케줄 시작 시간
	  */
	long getStartTime() {
		return startTime;
	}

	/**
	 * 스케줄 시작 시간 설정
	 * @param startTime		스케줄 시작 시간
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * 스케줄 종료 시간
	 * @return		스케줄 종료 시간
	 */
	long getEndTime() {
		return endTime;
	}

	/**
	 * 스케줄 종료 시간 설정
	 * @param endTime		스케줄 종료 시간
	 */
	void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * 스케줄 반복주기 설정 (단위:일)
	 * @param repeatDay		스케줄 반복 주기
	 */
	void setRepeatDay(Integer repeatDay) {
		this.repeatDay = repeatDay;
		if( this.repeatDay != null )	this.repeatType = SchedulerRepeatType.REPEAT_DAY;
	}

	/**
	 * 스케줄 동작 요일 설정
	 * @param weeks		스케줄 동작 요일
	 */
	void setWeeks(SchedulerWeek[] weeks) {
		this.weeks = weeks;
		if( this.weeks != null )	this.repeatType = SchedulerRepeatType.WEEKS;
	}

	/**
	 *  스케줄 동작 날짜 설정
	 * @param days		스케줄 동작 날짜
	 */
	void setDays(int[] days) {
		this.days = days;
		if( this.days != null )	this.repeatType = SchedulerRepeatType.DAYS;
	}

	/**
	 * 스케줄 시작 시간/분 설정
	 * @param hourAndMinute		스케줄 시작 시간/분
	 */
	void setHourAndMinute(int[][] hourAndMinute) {
		this.hourAndMinute = hourAndMinute;
	}
	
	/**
	 * 스케줄 아이디
	 * @return		스케줄 아이디
	 */
	public long getSchedulerId(){
		return this.schedulerId;
	}
	
	/**
	 * 스케줄 상태 정보
	 * @return		스케줄 상태
	 */
	SchedulerState getScheduleState(){
		return this.schedulerState;
	}
	
	/**
	 * 	스케줄 상태가 STOP 상태인지 여부
	 * @return		STOP 상태일 경우 true
	 */
	boolean isScheduleStateAsStop(){
		return this.schedulerState == SchedulerState.STOP;
	}
	
	/**
	 * 스케줄 구현체 리턴
	 * @return	스케줄 구현체
	 */
	Scheduler getScheduler(){
		return this.scheduler;
	}
	
	/**		스케줄 구현체 실행		*/
	void startScheduler(){
		new Thread(new Runnable(){
			public void run(){
				// 만약을 위해 null 검사
				if( scheduler != null )		scheduler.execute();
			}
		}).start();
		
	}
	
	/**
	 * 스케줄 구현체 설정 여부
	 * @return		null 일 경우 true
	 */
	public boolean isNullScheudler(){
		return this.scheduler == null;
	}
	
	/**
	 * 스케줄 시작 날짜 검사 결과 여부
	 * @return		스케줄 시작 날짜 검사 결과 여부
	 */
	boolean isCheckStartDate(){
		return this.checkStartDate;
	}
	
	/**
	 * 스케줄 시작 날짜 상태 변경
	 */
	void changeCheckStartDate(){
		this.checkStartDate = true;
	}

	/**
	 * 스케줄 동작 방식 
	 * @return		스케줄 동작 방식
	 */
	public SchedulerRepeatType getRepeatType() {
		return repeatType;
	}

	/**
	 * 스케줄 반복 주기 (단위:일)
	 * @return		스케줄 반복 주기 (단위:일)
	 */
	Integer getRepeatDay() {
		return repeatDay;
	}
	
	/**
	 * 	스케줄 동작 날짜 중 요청한 날짜가 있는지 검사
	 * @param nowDay		요청 날짜
	 * @return		동일한 날짜가 입력되어 있을 경우 true
	 */
	boolean isEqualsDays(int nowDay){
		for(int day : days){
			if( day == nowDay )	return true;
		}
		return false;
	}
	
	/**
	 * 	스케줄 동작 날짜 설정 여부
	 * @return		설정되어있지 않다면 true
	 */
	boolean isNullDays(){
		return days == null;
	}

	/**
	 * 스케줄 동작 요일
	 * @return		스케줄 동작 요일
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
	 * 동작 시간/분 정보가 null 일인지 검사
	 * @return		null 일 경우 true
	 */
	boolean isNullHourAndMinute(){
		return hourAndMinute == null;
	}
	
	/**
	 * 스케줄 전 동작 날짜
	 * @return		스케줄 전 동작 날짜
	 */
	int getBeforeDay() {
		return beforeDay;
	}

	/**
	 * 스케줄 전 동작 날짜 설정
	 * @param beforeDay		전 동작 날짜
	 */
	void setBeforeDay(int beforeDay) {
		this.beforeDay = beforeDay;
	}

	/**
	 * 반복 주기 검사 결과
	 * @return		반복 주기 검사 결과
	 */
	boolean isCheckRepeat() {
		return checkRepeat;
	}

	/**
	 * 반복 주기 검사 결과 설정
	 * @param checkRepeat		반복 주기 검사 결과
	 */
	void setCheckRepeat(boolean checkRepeat) {
		this.checkRepeat = checkRepeat;
	}
	
	/**
	 * 다음 반복 실행 년
	 * @return		다음 반복 실행 년
	 */
	int getNextRepeatYear() {
		return nextRepeatYear;
	}

	/**
	 * 다음 반복 실행 월
	 * @return		다음 반복 실행 월
	 */
	int getNextRepeatMonth() {
		return nextRepeatMonth;
	}

	/**
	 * 다음 반복 실행 일
	 * @return		다음 반복 실행 일
	 */
	int getNextRepeatDay() {
		return nextRepeatDay;
	}

	/**
	 * 다음 반복 실행 날짜 설정
	 * @param nextRepeatYear			년
	 * @param nextRepeatMonth		월
	 * @param nextRepeatDay			일
	 */
	void setNextRepeatDay(int nextRepeatYear, int nextRepeatMonth, int nextRepeatDay) {
		this.nextRepeatYear = nextRepeatYear;
		this.nextRepeatMonth = nextRepeatMonth;
		this.nextRepeatDay = nextRepeatDay;
	}
	
	/**
	 *스케줄 동작 시간/분 설정
	 * @param hour			시
	 * @param minute		분
	 */
	void addHourAndMinute(int hour, int minute){
		try{
			this.executeHourAndMinute[executeHourAndMinuteIndex][0] = hour;
			this.executeHourAndMinute[executeHourAndMinuteIndex++][1] = minute;
		}catch(Exception e){
			// 여기 오지는 않겠지만 만약을 위해 처리.
		}
	}
	
	/**
	 * 스케줄 동작 시간/분 정보 삭제
	 */
	void clearHourAndMinute(){
		this.executeHourAndMinuteIndex = 0;
		this.executeHourAndMinute = new int[this.hourAndMinute.length][2];
	}
	
	/**
	 * 스케줄 동작 시간 검사
	 * @param hour			시
	 * @param minute		분
	 * @return					실행 여부 결과
	 */
	boolean checkExecuteHourAndMinute(int hour, int minute){
		for(int i=0; i < this.executeHourAndMinuteIndex; i++){
			if( this.executeHourAndMinute[i][0] == hour && this.executeHourAndMinute[i][1] == minute )	return false;
		}
		
		return true;
	}
}
