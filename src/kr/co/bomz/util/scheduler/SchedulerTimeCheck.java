package kr.co.bomz.util.scheduler;

import java.util.Calendar;

/**
 * 
 * 스케줄러 동작 시간인지 검사한다
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public final class SchedulerTimeCheck{
	
	/**		현재 날짜 비교 시 사용되는 객체		*/
	private final Calendar calendar = Calendar.getInstance();
	/**		다음 실행 날짜 비교시 사용되는 객체		*/
	private Calendar tmpCalendar = null;
	
	private final SchedulerManager schedulerManager;
	
	private SchedulerObject schedulerObject;
	
	public SchedulerTimeCheck(SchedulerManager schedulerManager){
		this.schedulerManager = schedulerManager;
	}
	
	/**
	 * 스케줄러 동작 가능 여부를 검사한다<br>
	 * 실행조건이 되었을 경우 실행하고, 종료 날이 되었을 경우 스케줄을 종료시킨다
	 */
	protected void execute(SchedulerObject schedulerObject){
		this.schedulerObject = schedulerObject;
		if( this.schedulerObject == null )							return;
		
		try{
			if( this.schedulerObject.isNullScheudler() )		return;
			
			if( this.schedulerObject.isScheduleStateAsStop() ){
				// 관리자에 의해 강제로 스케쥴이 종료되었을 경우
				this.schedulerObject.getScheduler().destroy();
				return;
			}
			
			if( _execute() ){
				// 다음 스케쥴 실행을 위해 스케쥴매니저에 등록
				this.schedulerManager.insertScheduler( this.schedulerObject );
			}else{
				// 스케쥴 종료
				// 자원 해제
				this.schedulerObject.getScheduler().destroy();
			}
		
		}finally{
			this.schedulerObject = null;
			this.tmpCalendar = null;
		}
	}
	
	private boolean _execute(){
		
		long timeInMillis = System.currentTimeMillis();
		this.calendar.setTimeInMillis(timeInMillis);
		
		// 스케쥴 시작 날짜가 지났는지 검사
		if( !this.checkStartDate(timeInMillis) )		return true;
		// 스케쥴 종료 날짜가 되었는지 검사
		if( !this.checkEndDate(timeInMillis) )			return false;
		// 오늘이 스케쥴 실행 날짜가 맞는지 검사
		// 실행 날짜가 맞다면 실행 시간이 맞는지도 검사
		try{
			// 날짜 검사
			if( !this.checkRepeat() )			return true;
			// 시간 검사
			
			if( !checkHourAndMinute(this.calendar.get(Calendar.HOUR_OF_DAY), this.calendar.get(Calendar.MINUTE)) )		return true;
		}catch(Exception e){
			// 오류 발생 시 반복 주기 검사를 위한 객체가 널이거나 잘못되었을 경우임
			// 이럴 경우 스케쥴을 삭제함
			return false;
		}
		
		// 모든 조건에 맞으므로 스케쥴 실행
		this.schedulerObject.startScheduler();
		
		return true;
		
	}
	
	private boolean checkHourAndMinute(int nowHour, int nowMinute) throws Exception{
		
		if( this.schedulerObject.isNullHourAndMinute() )
			throw new Exception("[스케쥴 실행 시작 시간 값이 NULL 값이므로 스케쥴을 삭제합니다] 스케쥴아이디 : " + this.schedulerObject.getSchedulerId());
		
		
		int length = this.schedulerObject.getHourAndMinuteSize();
		
		for(int i=0; i < length; i++){
			if( this.schedulerObject.getHourAndMinuteValue(i, 0) == nowHour && this.schedulerObject.getHourAndMinuteValue(i, 1) == nowMinute ){
				// 현재 시간과 실행 시간이 같을 경우
				if( this.schedulerObject.checkExecuteHourAndMinute(nowHour, nowMinute) ){
					// 아직 실행되지 않은 시간이므로 스케쥴 실행
					this.schedulerObject.addHourAndMinute(nowHour, nowMinute);
					return true;
					
				}else{
					// 이미 동작했던 시간이므로 스케쥴을 시작하지 않음
					break;
				}
				
			}
		}
		
		return false;
	}
	
	/**
	 * 	스케쥴 반복 주기 검사를 수행하야하는지 확인한다
	 */
	private boolean checkRepeat() throws Exception{
		
		final int nowDay = this.calendar.get(Calendar.DAY_OF_MONTH);
		// 기존에 이미 오늘 반복 여부를 검사하였다면 새롭게 검사할 필요가 없음
		if( this.schedulerObject.getBeforeDay() == nowDay )		return this.schedulerObject.isCheckRepeat();
		
		// 기존에 실행했던 스케쥴 시간을 초기화한다.
		this.schedulerObject.clearHourAndMinute();
		
		boolean result = this.checkRepeat(this.schedulerObject.getRepeatType());
		this.schedulerObject.setCheckRepeat( result );
		
		// 이전 검사 날짜를 현재 날짜로 변경
		this.schedulerObject.setBeforeDay( nowDay );
		
		return result;
	}
	
	private boolean checkRepeat(SchedulerRepeatType repeatType) throws Exception{
		// 반복 주기 방식에 따라 분기
		switch( repeatType ){
		case REPEAT_DAY:		// 일정한 주기로 실행
			return this.checkRepeatDay();
			
		case DAYS:						// 특정 날에만 실행
			return this.checkDays(this.calendar.get(Calendar.DAY_OF_MONTH));
			
		case WEEKS:					// 특정 요일에만 실행
			// 현재 요일을 가져온다. 
			// -1 을 하는 이유는 enum 의 값은 0 부터 시작하기 때문
			// 리턴값은 1 부터 시작하며 일요일이 시작임
			return this.checkWeeks(this.calendar.get(Calendar.DAY_OF_WEEK) - 1);
			
		default:
			// 정상적으로 동작할 경우 이곳에는 오지 않아야 함. 하지만 만약을 위해 처리
			// 로그는 오류 발생 위치에서 출력
			throw new Exception("[스케쥴 반복 실행을 위한 설정값이 모두 NULL 값이므로 스케쥴을 삭제합니다] 스케쥴아이디 : " + this.schedulerObject.getSchedulerId());
		}
	}
	
	/**
	 * 	일정한 주기로 스케쥴을 실행할 경우 스케쥴을 실행한 날이 되었는지 검사한다.
	 * 	스케쥴 실행 시 다음번 스케쥴 실행 날짜를 변경한다
	 */
	private boolean checkRepeatDay() throws Exception{
		Integer repeatDay = this.schedulerObject.getRepeatDay();
		if( repeatDay == null ){
			throw new Exception("[스케쥴 실행 간격 조건 중 일정 기간마다 실행 조건이 NULL 값으로 되어 있어 스케쥴을 삭제합니다] 스케쥴아이디 : " + this.schedulerObject.getSchedulerId());
		}
		
		int nextRepeatDay = this.schedulerObject.getNextRepeatDay();
		
		if( nextRepeatDay == -1 || 
				( 
						nextRepeatDay == this.calendar.get(Calendar.DAY_OF_MONTH) &&
						this.schedulerObject.getNextRepeatYear() == this.calendar.get(Calendar.YEAR) && 
						this.schedulerObject.getNextRepeatMonth() == (this.calendar.get(Calendar.MONTH) + 1)
				)
				){
			
			// 첫 실행일이거나 실행예정일이 현재 날짜와 같을 경우 
			// 다음 실행 날짜를 변경한다.
			long timeInMillis = this.calendar.getTimeInMillis();
			
			if( this.tmpCalendar == null )		this.tmpCalendar = Calendar.getInstance();
			else												this.tmpCalendar.setTimeInMillis(timeInMillis);
			
			// 86400000 는 하루 24시간을 밀리세컨드로 표현한 값
			this.tmpCalendar.setTimeInMillis( timeInMillis + (86400000*repeatDay) );
			this.schedulerObject.setNextRepeatDay(this.tmpCalendar.get(Calendar.YEAR), (this.tmpCalendar.get(Calendar.MONTH) + 1), this.tmpCalendar.get(Calendar.DAY_OF_MONTH) );
			
			return true;
		}else{
			// 아직 스케쥴 시작 날짜가 되지 않았음.
			return false;
		}
		
	}
	
	/**
	 * 	스케쥴을 실행할 요일이 되었는지 검사한다
	 */
	private boolean checkWeeks(int nowWeek) throws Exception{
	
		
		SchedulerWeek[] weeks = this.schedulerObject.getWeeks();
		if( weeks == null ){
			throw new Exception("[스케쥴 실행 간격 조건 중 요일로 실행 조건이 NULL 값으로 되어 있어 스케쥴을 삭제합니다] 스케쥴아이디 : " + this.schedulerObject.getSchedulerId());
		}
		
		for(SchedulerWeek week : weeks){
			if( nowWeek == week.ordinal() )		return true;
		}
		
		return false;
	}
	
	/**
	 * 	스케쥴을 실행할 날짜가 되었는지 검사한다
	 */
	private boolean checkDays(int nowDay) throws Exception{
		
		if( this.schedulerObject.isNullDays() ){
			throw new Exception("[스케줄 실행 간격 조건 중 날짜로 실행 조건이 NULL 값으로 되어 있어 스케쥴을 삭제합니다] 스케쥴아이디 : " + this.schedulerObject.getSchedulerId());
		}
		
		return this.schedulerObject.isEqualsDays(nowDay);
	}
	
	/**
	 * 		스케쥴의 시작 날짜가 지났는지 검사한다
	 */
	private boolean checkStartDate(long timeInMillis){
		
		if( this.schedulerObject.isCheckStartDate() )		return true;
		
		if( timeInMillis >= this.schedulerObject.getStartTime() ){
			this.schedulerObject.changeCheckStartDate();
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 		스케쥴의 종료 날짜가 되었는지 검사한다
	 */
	private boolean checkEndDate(long timeInMillis){
		
		long value = this.schedulerObject.getEndTime();
		if( value == -1 )		return true;
		
		if( timeInMillis >= value )		return false;
		else										return true;
	}
	

}
