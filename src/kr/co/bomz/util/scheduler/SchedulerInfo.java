package kr.co.bomz.util.scheduler;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 	��Ȳ�� ���� ������ ������ ���� �⺻ ����ü 
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
	/*		������ ���� �ð�/��		*/
	private int[][] startHourAndMinute;
	
	/*		������ ���� ��/��/��		*/
	private int startYear = -1, startMonth, startDay;
	/*		������ ���� ��/��/��		*/
	private int endYear, endMonth, endDay;
	
	/*
	 *  �Ϸ絿�� ��� �ݺ��� ��� �Ϸ� �ִ� �ݼ� ���� ���Ѵ�.
	 *  �ִ� �ʴ� �ѹ��� ����ǹǷ� �Ϸ縦 �ʷ� ����ϸ� 86400
	 *  ������ ���� 90000 ���� ���� 
	 */
	private static final int MAX_REPEAT_SIZE = 90000;
	
	/**
	 * ��Ȳ�� ���� ������ ������ ���� �⺻ ����ü 
	 */
	protected SchedulerInfo(){}
	
	/**
	 * �����ٷ� ���� �ʱ�ȭ
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
	 * ������ �ݺ� �ֱ� (����:��)
	 * @return		�������� �ʾ��� ��� null ����
	 */
	protected Integer getRepeatDay(){
		return null;
	}
	
	/**
	 * ������ ���� ��¥
	 * @return		�������� �ʾ��� ��� null ����
	 */
	protected int[] getDays(){
		return null;
	}
	
	/**
	 * ������ ���� ����
	 * @return		�������� �ʾ��� ��� null ����
	 */
	protected SchedulerWeek[] getWeeks(){
		return null;
	}
	
	/**
	 * ������ ���� ���� ��/��/�� ����
	 * @param year		������ ���� ��
	 * @param month		������ ���� ��
	 * @param day			������ ���� ��
	 */
	public void setStartDate(int year, int month, int day){
		this.startYear = year;
		this.startMonth = month;
		this.startDay = day;
	}
	
	/**
	 * ������ ���� ���� ��¥ ����
	 * @param startDate		������ ���� ���� ����
	 */
	public void setStartDate(Calendar startDate){
		this.setStartDate(
				startDate.get(Calendar.YEAR), 
				startDate.get(Calendar.MONTH) + 1, 
				startDate.get(Calendar.DAY_OF_MONTH)
			);
	}
	
	/**
	 * ������ ���� ���� ��/��/�� ����
	 * @param year		������ ���� ��
	 * @param month		������ ���� ��
	 * @param day			������ ���� ��
	 */
	public void setEndDate(int year, int month, int day){
		this.endYear = year;
		this.endMonth = month;
		this.endDay = day;
	}
	
	/**
	 * ������ ���� ���� ��¥ ����
	 * @param endDate		������ ���� ���� ����
	 */
	public void setEndDate(Calendar endDate){
		this.setEndDate(
				endDate.get(Calendar.YEAR), 
				endDate.get(Calendar.MONTH) + 1, 
				endDate.get(Calendar.DAY_OF_MONTH)
			);
	}
	
	/**
	 * ������ �ð�/�п� �������� ����ȴ�<p>
	 * Array [0][0] : ��<br>
	 * Array [0][1] : ��<p>
	 * ��� ����)<br>
	 * <code>
	 * new int[][]{<br>
	 * <tab>{3, 30},		// time is 3:30<br>
	 * <tab>{6, 0},		// time is 6:00<br>
	 * <tab>{17, 27},	// time is 17:27<br>
	 * }
	 * </code>
	 * @param startHourAndMinute	�������� ����� �ð�/�� �迭
	 */
	public void setStartHourAndMinute(int[][] startHourAndMinute){
		this.startHourAndMinute = startHourAndMinute;
	}
	
	/**
	 * �������� �ð�/�к��� �����Ͽ� repeatTime(��) ��ŭ �ݺ������� �������� ����ȴ�<p>
	 * ��) startHour:14 , startMinute:20 , repeatTime:6 �� ���
	 * 14:20 , 14:26 , 14:32 , 14:38 , 14:44 ... ���� ������ �ݺ� ȣ��ȴ�
	 * @param startHour		������ ���� �ð�
	 * @param startMinute		������ �ð� ��
	 * @param repeatTime		������ �ݺ� �ֱ� (����:��)
	 */
	public void setStartHourAndMinute(int startHour, int startMinute, int repeatTime){
		this.startHourAndMinute =  this.getHourAndMinute(startHour, startMinute, repeatTime, MAX_REPEAT_SIZE);
	}
	
	/**
	 * ������ �ð�/�к��� �����Ͽ� repeatTime(��) ��ŭ �ݺ������� �������� ����ȴ�<p>
	 * ��) startHour:14 , startMinute:20 , repeatTime:6 , repeatTime:3 �� ���
	 * 14:20 , 14:26 , 14:32 �� �Ϸ翡 3�� ȣ��
	 * @param startHour		������ ���� �ð�
	 * @param startMinute		������ �ð� ��
	 * @param repeatTime		������ �ݺ� �ֱ� (����:��)
	 * @param repeatCount	������ �ݺ� Ƚ��		
	 */
	public void setStartHourAndMinute(int startHour, int startMinute, int repeatTime, int repeatCount) throws SchedulerAddException{
		if( repeatCount <= 0 ){
			System.err.println("������ �ݺ� ������ ���� repeatSize ���� 0 ���� Ŀ���մϴ�. �Է°�:" + repeatCount);
			throw new SchedulerAddException("������ �ݺ� ������ ���� repeatSize ���� 0 ���� Ŀ���մϴ�. �Է°�:" + repeatCount);
		}
		this.startHourAndMinute =  this.getHourAndMinute(startHour, startMinute, repeatTime, repeatCount);
	}
	
	/*		�ݺ� �ֱ⿡ ���� ������ ���� ���� �ð�/���� ���		*/
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
			startMinute += repeatTime;		// �ð��� ���Ѵ�
			
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
	 * ���� ��/��/���� �������� �ʾ��� ��� ���� ��¥�� �����Ѵ�
	 */
	void checkStartDate(){
		// �̹� �������ڰ� �����Ǿ� �ִٸ� ó������ �ʴ´�
		if( this.startYear != -1 )		return;
		
		Calendar cal = Calendar.getInstance();
		this.startYear = cal.get(Calendar.YEAR);
		this.startMonth = cal.get(Calendar.MONTH) + 1;
		this.startDay = cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * ������ ���� ����ü ����
	 * @param scheduler		������ ���� ����ü
	 */
	public void setScheduler(Scheduler scheduler){
		this.scheduler = scheduler;
	}
	
	/**
	 * ������ ������ ���� ����ü ����
	 * @return		������ ������ ���� ����ü
	 */
	public Scheduler getScheduler() {
		return scheduler;
	}

	/**
	 * ������ �����ٷ� ���� �ð�/�� ���� ����
	 * @return		�����ٷ� ����/��
	 */
	public int[][] getStartHourAndMinute() {
		return startHourAndMinute;
	}

	/**
	 * �����ٷ� ������ (��)
	 * @return		�����ٷ� ������ (��)
	 */
	public int getStartYear() {
		return startYear;
	}

	/**
	 * �����ٷ� ������ (��)
	 * @return		�����ٷ� ������ (��)
	 */
	public int getStartMonth() {
		return startMonth;
	}

	/**
	 * �����ٷ� ������ (��)
	 * @return		�����ٷ� ������ (��)
	 */
	public int getStartDay() {
		return startDay;
	}

	/**
	 * �����ٷ� ������ (��)
	 * @return		�����ٷ� ������ (��)
	 */
	public int getEndYear() {
		return endYear;
	}

	/**
	 * �����ٷ� ������ (��)
	 * @return		�����ٷ� ������ (��)
	 */
	public int getEndMonth() {
		return endMonth;
	}

	/**
	 * �����ٷ� ������ (��)
	 * @return	�����ٷ� ������ (��)
	 */
	public int getEndDay() {
		return endDay;
	}
	
	
}
