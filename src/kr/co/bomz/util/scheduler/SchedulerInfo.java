package kr.co.bomz.util.scheduler;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 	상황에 따른 스케줄 동작을 위한 기본 구현체 
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see		AllDaysSchedulerInfo
 * @see		WeeksSchedulerInfo
 * @see		RepeatDaySchedulerInfo
 * @see		FixDaysSchedulerInfo
 *
 */
public abstract class SchedulerInfo {

	private Scheduler scheduler;
	/*		스케줄 시작 시간/분		*/
	private int[][] startHourAndMinute;
	
	/*		스케줄 시작 년/월/일		*/
	private int startYear = -1, startMonth, startDay;
	/*		스케줄 종료 년/월/일		*/
	private int endYear, endMonth, endDay;
	
	/*
	 *  하루동안 계속 반복일 경우 하루 최대 반속 수를 정한다.
	 *  최대 초당 한번씩 실행되므로 하루를 초로 계산하면 86400
	 *  만약을 위해 90000 으로 설정 
	 */
	private static final int MAX_REPEAT_SIZE = 90000;
	
	/**
	 * 상황에 따른 스케줄 동작을 위한 기본 구현체 
	 */
	protected SchedulerInfo(){}
	
	/**
	 * 스케줄러 정보 초기화
	 */
	protected void reset(){
		this.startYear = -1;
		this.startMonth = -1;
		this.startDay = -1;
		this.endYear = -1;
		this.endMonth = -1;
		this.endDay = -1;
		this.scheduler = null;
		this.startHourAndMinute = null;
	}
		
	/**
	 * 스케줄 반복 주기 (단위:일)
	 * @return		설정되지 않았을 경우 null 리턴
	 */
	protected Integer getRepeatDay(){
		return null;
	}
	
	/**
	 * 스케줄 실행 날짜
	 * @return		설정되지 않았을 경우 null 리턴
	 */
	protected int[] getDays(){
		return null;
	}
	
	/**
	 * 스케줄 실행 요일
	 * @return		설정되지 않았을 경우 null 리턴
	 */
	protected SchedulerWeek[] getWeeks(){
		return null;
	}
	
	/**
	 * 스케줄 실행 시작 년/월/일 설정
	 * @param year		스케줄 실행 년
	 * @param month		스케줄 실행 월
	 * @param day			스케줄 실행 일
	 */
	public void setStartDate(int year, int month, int day){
		this.startYear = year;
		this.startMonth = month;
		this.startDay = day;
	}
	
	/**
	 * 스케줄 실행 시작 날짜 설정
	 * @param startDate		스케줄 실행 시작 일자
	 */
	public void setStartDate(Calendar startDate){
		this.setStartDate(
				startDate.get(Calendar.YEAR), 
				startDate.get(Calendar.MONTH) + 1, 
				startDate.get(Calendar.DAY_OF_MONTH)
			);
	}
	
	/**
	 * 스케줄 실행 종료 년/월/일 설정
	 * @param year		스케줄 종료 년
	 * @param month		스케줄 종료 월
	 * @param day			스케줄 종료 일
	 */
	public void setEndDate(int year, int month, int day){
		this.endYear = year;
		this.endMonth = month;
		this.endDay = day;
	}
	
	/**
	 * 스케줄 실행 종료 날짜 설정
	 * @param endDate		스케줄 실행 종료 일자
	 */
	public void setEndDate(Calendar endDate){
		this.setEndDate(
				endDate.get(Calendar.YEAR), 
				endDate.get(Calendar.MONTH) + 1, 
				endDate.get(Calendar.DAY_OF_MONTH)
			);
	}
	
	/**
	 * 지정된 시간/분에 스케줄이 실행된다<p>
	 * Array [0][0] : 시<br>
	 * Array [0][1] : 분<p>
	 * 사용 예제)<br>
	 * <code>
	 * new int[][]{<br>
	 * <tab>{3, 30},		// time is 3:30<br>
	 * <tab>{6, 0},		// time is 6:00<br>
	 * <tab>{17, 27},	// time is 17:27<br>
	 * }
	 * </code>
	 * @param startHourAndMinute	스케줄이 실행될 시간/분 배열
	 */
	public void setStartHourAndMinute(int[][] startHourAndMinute){
		this.startHourAndMinute = startHourAndMinute;
	}
	
	/**
	 * 설정정된 시간/분부터 시작하여 repeatTime(분) 만큼 반복적으로 스케줄이 실행된다<p>
	 * 예) startHour:14 , startMinute:20 , repeatTime:6 일 경우
	 * 14:20 , 14:26 , 14:32 , 14:38 , 14:44 ... 자정 전까지 반복 호출된다
	 * @param startHour		스케줄 시작 시간
	 * @param startMinute		스케줄 시간 분
	 * @param repeatTime		스케줄 반복 주기 (단위:분)
	 */
	public void setStartHourAndMinute(int startHour, int startMinute, int repeatTime){
		this.startHourAndMinute =  this.getHourAndMinute(startHour, startMinute, repeatTime, MAX_REPEAT_SIZE);
	}
	
	/**
	 * 시정된 시간/분부터 시작하여 repeatTime(분) 만큼 반복적으로 스케줄이 실행된다<p>
	 * 예) startHour:14 , startMinute:20 , repeatTime:6 , repeatTime:3 일 경우
	 * 14:20 , 14:26 , 14:32 총 하루에 3번 호출
	 * @param startHour		스케줄 시작 시간
	 * @param startMinute		스케줄 시간 분
	 * @param repeatTime		스케줄 반복 주기 (단위:분)
	 * @param repeatCount	스케줄 반복 횟수		
	 */
	public void setStartHourAndMinute(int startHour, int startMinute, int repeatTime, int repeatCount) throws SchedulerAddException{
		if( repeatCount <= 0 ){
			System.err.println("스케쥴 반복 실행을 위한 repeatSize 값은 0 보다 커야합니다. 입력값:" + repeatCount);
			throw new SchedulerAddException("스케쥴 반복 실행을 위한 repeatSize 값은 0 보다 커야합니다. 입력값:" + repeatCount);
		}
		this.startHourAndMinute =  this.getHourAndMinute(startHour, startMinute, repeatTime, repeatCount);
	}
	
	/*		반복 주기에 따른 스케줄 실행 시작 시간/분을 계산		*/
	private int[][] getHourAndMinute(int startHour, int startMinute, int repeatTime, int repeatSize){
		
		if( repeatTime <= 0 )	return null;
		
		ArrayList<Integer> hourList = new ArrayList<Integer>();
		ArrayList<Integer> minuteList = new ArrayList<Integer>();
		
		int count = 0;
		while(repeatSize > 0 &&  count++ < repeatSize){
			if( startHour >= 24 )			break;
			if( startMinute >= 60 )		break;
			
			hourList.add(startHour);
			minuteList.add(startMinute);
			startMinute += repeatTime;		// 시간을 더한다
			
			startHour += startMinute / 60;
			startMinute = startMinute % 60;

		}
		
		int scheduleSize = hourList.size();
		
		if( scheduleSize <= 0 )	return new int[][]{};
		
		int[][] result = new int[scheduleSize][2];
		for(int i=0; i < scheduleSize; i++){
			result[i][0] = hourList.get(i);
			result[i][1] = minuteList.get(i);
		}
		
		return result;
	}
	
	/**
	 * 시작 년/월/일이 지정되지 않았을 경우 현재 날짜를 지정한다
	 */
	void checkStartDate(){
		// 이미 시작일자가 설정되어 있다면 처리하지 않는다
		if( this.startYear != -1 )		return;
		
		Calendar cal = Calendar.getInstance();
		this.startYear = cal.get(Calendar.YEAR);
		this.startMonth = cal.get(Calendar.MONTH) + 1;
		this.startDay = cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 스케줄 동작 구현체 설정
	 * @param scheduler		스케줄 동작 구현체
	 */
	public void setScheduler(Scheduler scheduler){
		this.scheduler = scheduler;
	}
	
	/**
	 * 설정된 스케줄 동작 구현체 리턴
	 * @return		설정된 스케줄 동작 구현체
	 */
	public Scheduler getScheduler() {
		return scheduler;
	}

	/**
	 * 설정된 스케줄러 시작 시간/분 정보 리턴
	 * @return		스케줄러 시작/분
	 */
	public int[][] getStartHourAndMinute() {
		return startHourAndMinute;
	}

	/**
	 * 스케줄러 실행일 (년)
	 * @return		스케줄러 실행일 (년)
	 */
	public int getStartYear() {
		return startYear;
	}

	/**
	 * 스케줄러 실행일 (월)
	 * @return		스케줄러 실행일 (월)
	 */
	public int getStartMonth() {
		return startMonth;
	}

	/**
	 * 스케줄러 실행일 (일)
	 * @return		스케줄러 실행일 (일)
	 */
	public int getStartDay() {
		return startDay;
	}

	/**
	 * 스케줄러 종료일 (년)
	 * @return		스케줄러 종료일 (년)
	 */
	public int getEndYear() {
		return endYear;
	}

	/**
	 * 스케줄러 종료일 (월)
	 * @return		스케줄러 종료일 (월)
	 */
	public int getEndMonth() {
		return endMonth;
	}

	/**
	 * 스케줄러 종료일 (일)
	 * @return	스케줄러 종료일 (일)
	 */
	public int getEndDay() {
		return endDay;
	}
	
	
}
