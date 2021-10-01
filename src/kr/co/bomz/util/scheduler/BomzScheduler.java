package kr.co.bomz.util.scheduler;

import java.util.Calendar;


/**
 * 
 *  조건에 맞는 시간이 되면 Scheduler 인터페이스를 수행하는 스케줄러<p>
 *  
 *  스케줄에 사용가능한 조건에는 다음의 것들이 있다<p>
 *  
 *  1. 특정 날짜 특정 시간마다 실행<p>
 *  2. 특정 요일 특정 시간마다 실행<p>
 *  3. 스케줄 등록 시점을 기준으로 특정 분/시간 주기로 실행<p>
 *  4. 스케줄 등록 시점을 기준으로 특정 일 주기로 실행<p>
 *  5. 모든 조건은 시작 날짜와 종료 날짜를 지정<p>
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public final class BomzScheduler{

	private SchedulerManager schedulerManager = null;
	
	// 등록된 스케줄의 키 값
	// 기본값을 2472182로 설정한 이유는 없음.
	private volatile long schedulerId = 2472182L;
	
	private final Object lock = new Object();
	
	public BomzScheduler(){}
	
	
	
	/**
	 * 	해당 스케줄 키값을 사용하는 스케줄을 스케줄러에서 삭제한다
	 */
	public boolean removeScheduler(long schedulerKey){
		return this.schedulerManager.removeScheduler(schedulerKey);
	}
	
	/**
	 * 스케줄러 종료
	 */
	public void closeScheduler(){
		synchronized( this.lock ){
			if( this.schedulerManager != null ){
				this.schedulerManager.closeScheduleManager();
				this.schedulerManager = null;
			}
		}
	}
	
	/**
	 * 스케줄러 등록
	 * @param info		스케줄러 동작 설정 값
	 * @return			스케줄러 아이디
	 * @throws			스케줄러 설저 오류 시 발생
	 */
	public long addSchduler(SchedulerInfo info) throws SchedulerAddException{
		
		info.checkStartDate();
		
		return this.addScheduler(
				info.getScheduler(),
				info.getStartHourAndMinute(),
				info.getRepeatDay(),
				info.getDays(),
				info.getWeeks(),
				info.getStartYear(),
				info.getStartMonth(),
				info.getStartDay(),
				info.getEndYear(),
				info.getEndMonth(),
				info.getEndDay()
			);
	}
	
	private long addScheduler(Scheduler scheduler, int[][] hourAndMinute, 
			Integer repeatDay, int[] days, SchedulerWeek[] weeks,
			int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) throws SchedulerAddException{
		
		// 유효성 검사 시작
		if( scheduler == null )		throw new SchedulerAddException("스케줄 실행을 위한 Scheduler 구현 객체가 NULL");
		if( hourAndMinute == null )		throw new SchedulerAddException("스케줄 실행을 위한 시간 및 분 정보가 NULL");
		if( hourAndMinute.length <= 0 )		throw new SchedulerAddException("스케줄을 실행할 수 있는 시간 정보가 없습니다");
		
		int checkCount =0;
		
		if( repeatDay != null  ){
			if( repeatDay > 0 )			checkCount++;
			else			throw new SchedulerAddException("스케줄 반복 실행을 위한 repeatDay 값은 1보다 커야합니다");
		}
		
		if( days != null ){
			int daysLength = days.length;
			if( daysLength > 31 || daysLength <= 0 ){
				throw new SchedulerAddException("스케줄 반복 실행을 위한 days 값의 개수는 1부터 31사이여야 합니다. 현재개수:" + daysLength);
			}else 
				checkCount++;
		}
		
		if( weeks != null ){
			int weeksLength = weeks.length;
			if( weeksLength > 7 || weeksLength <= 0 ){
				throw new SchedulerAddException("스케줄 반복 실행을 위한 weeks 값의 개수는 1부터 7사이여야 합니다. 현재개수:" + weeksLength);
			}else
				checkCount++;
		}
		
		if( checkCount != 1 )		throw new SchedulerAddException("스케줄을 동작하기 위한 조건이 잘못되었습니다");
		// 유효성 검사 종료
		
		// closeScheduler() 추가로 인한 오류 발생 방지를 위한 동기화
		synchronized( this.lock ){
			long schedulerId = this.getSchedulerId();
			
			final SchedulerObject object = new SchedulerObject(scheduler, schedulerId);
			
			object.setRepeatDay(repeatDay);
			object.setDays(days);
			object.setWeeks(weeks);
			
			object.setHourAndMinute(hourAndMinute);
			
			long value = this.getTime(startYear, startMonth, startDay, true);
			object.setStartTime( value );
			
			if( endYear <= 0 && endMonth <= 0 && endDay <= 0 ){
				// 스케줄 종료 시간이 없을 경우
				object.setEndTime( -1 );
			}else{
				// 스케줄 종료 시간이 있을 경우
				value = this.getTime(endYear, endMonth, endDay, false);
				object.setEndTime( value );
			}
					
			this.schedulerManager.insertScheduler( object );
			
			// Memory leak 으로 인한 초기화 작업
			hourAndMinute = null;
			days = null;
			
			return schedulerId;
		}
		
	}
	
	private long getTime(int year, int month, int day, boolean startTime) throws SchedulerAddException{
				
		if( year < 1900 )		throw new SchedulerAddException("날짜의 년도는 1900 년 이전이 될 수 없습니다. 입력값:" + year);
		if( month < 1 || month > 12 )		throw new SchedulerAddException("날짜의 월은 1부터 12 사이의 값이여야 합니다. 입력값:" + month);
		if( day < 1 || day > 31 )		throw new SchedulerAddException("날짜의 일은 1부터 31 사이의 값이여야 합니다. 입력값:" + day);
	
		Calendar calendar = Calendar.getInstance();
		
		if( !(startTime && year == -1 && month == -1 && day == -1) ){
			// 시작 년/월/일이 설정되어있지 않을 경우는 아래 처리를 하지 않는다.
			// 왜냐면 등록 년/월/일을 사용하기 때문.
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, (month -1) );
			
			// calendar.set(Calendar.DAY_OF_MONTH, day) 을 호출하기 전에 비교해야 제대로 나온다
			if( calendar.getActualMaximum(Calendar.DAY_OF_MONTH) < day )
				throw new SchedulerAddException("사용할 수 없는 날짜를 입력하였습니다. 입력값:" + year + "년 " + month + "월 " + day + "일");
			
			calendar.set(Calendar.DAY_OF_MONTH, day);
		}
		
		calendar.set(Calendar.HOUR_OF_DAY, startTime ? 0 : 23);
		calendar.set(Calendar.MINUTE, startTime ? 0 : 59);
		calendar.set(Calendar.SECOND, startTime ? 0 : 59);
		
		
		
		calendar.set(Calendar.DAY_OF_MONTH, day);
		
		long result = calendar.getTimeInMillis();
		
		if( result <= 0 )
			throw new SchedulerAddException("사용할 수 없는 날짜를 입력하였습니다. 입력값:" + year + "년 " + month + "월 " + day + "일");
		
		return result;
	}
	

	private long getSchedulerId(){

		if( this.schedulerManager == null ){
			this.schedulerManager = new SchedulerManager();
			this.schedulerManager.start();
		}
		return this.schedulerId++;
	}
}
