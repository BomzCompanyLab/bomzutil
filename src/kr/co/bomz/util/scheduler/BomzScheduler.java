package kr.co.bomz.util.scheduler;

import java.util.Calendar;


/**
 * 
 *  ���ǿ� �´� �ð��� �Ǹ� Scheduler �������̽��� �����ϴ� �����ٷ�<p>
 *  
 *  �����ٿ� ��밡���� ���ǿ��� ������ �͵��� �ִ�<p>
 *  
 *  1. Ư�� ��¥ Ư�� �ð����� ����<p>
 *  2. Ư�� ���� Ư�� �ð����� ����<p>
 *  3. ������ ��� ������ �������� Ư�� ��/�ð� �ֱ�� ����<p>
 *  4. ������ ��� ������ �������� Ư�� �� �ֱ�� ����<p>
 *  5. ��� ������ ���� ��¥�� ���� ��¥�� ����<p>
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public final class BomzScheduler{

	private SchedulerManager schedulerManager = null;
	
	// ��ϵ� �������� Ű ��
	// �⺻���� 2472182�� ������ ������ ����.
	private volatile long schedulerId = 2472182L;
	
	private final Object lock = new Object();
	
	public BomzScheduler(){}
	
	
	
	/**
	 * 	�ش� ������ Ű���� ����ϴ� �������� �����ٷ����� �����Ѵ�
	 */
	public boolean removeScheduler(long schedulerKey){
		return this.schedulerManager.removeScheduler(schedulerKey);
	}
	
	/**
	 * �����ٷ� ����
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
	 * �����ٷ� ���
	 * @param info		�����ٷ� ���� ���� ��
	 * @return			�����ٷ� ���̵�
	 * @throws			�����ٷ� ���� ���� �� �߻�
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
		
		// ��ȿ�� �˻� ����
		if( scheduler == null )		throw new SchedulerAddException("������ ������ ���� Scheduler ���� ��ü�� NULL");
		if( hourAndMinute == null )		throw new SchedulerAddException("������ ������ ���� �ð� �� �� ������ NULL");
		if( hourAndMinute.length <= 0 )		throw new SchedulerAddException("�������� ������ �� �ִ� �ð� ������ �����ϴ�");
		
		int checkCount =0;
		
		if( repeatDay != null  ){
			if( repeatDay > 0 )			checkCount++;
			else			throw new SchedulerAddException("������ �ݺ� ������ ���� repeatDay ���� 1���� Ŀ���մϴ�");
		}
		
		if( days != null ){
			int daysLength = days.length;
			if( daysLength > 31 || daysLength <= 0 ){
				throw new SchedulerAddException("������ �ݺ� ������ ���� days ���� ������ 1���� 31���̿��� �մϴ�. ���簳��:" + daysLength);
			}else 
				checkCount++;
		}
		
		if( weeks != null ){
			int weeksLength = weeks.length;
			if( weeksLength > 7 || weeksLength <= 0 ){
				throw new SchedulerAddException("������ �ݺ� ������ ���� weeks ���� ������ 1���� 7���̿��� �մϴ�. ���簳��:" + weeksLength);
			}else
				checkCount++;
		}
		
		if( checkCount != 1 )		throw new SchedulerAddException("�������� �����ϱ� ���� ������ �߸��Ǿ����ϴ�");
		// ��ȿ�� �˻� ����
		
		// closeScheduler() �߰��� ���� ���� �߻� ������ ���� ����ȭ
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
				// ������ ���� �ð��� ���� ���
				object.setEndTime( -1 );
			}else{
				// ������ ���� �ð��� ���� ���
				value = this.getTime(endYear, endMonth, endDay, false);
				object.setEndTime( value );
			}
					
			this.schedulerManager.insertScheduler( object );
			
			// Memory leak ���� ���� �ʱ�ȭ �۾�
			hourAndMinute = null;
			days = null;
			
			return schedulerId;
		}
		
	}
	
	private long getTime(int year, int month, int day, boolean startTime) throws SchedulerAddException{
				
		if( year < 1900 )		throw new SchedulerAddException("��¥�� �⵵�� 1900 �� ������ �� �� �����ϴ�. �Է°�:" + year);
		if( month < 1 || month > 12 )		throw new SchedulerAddException("��¥�� ���� 1���� 12 ������ ���̿��� �մϴ�. �Է°�:" + month);
		if( day < 1 || day > 31 )		throw new SchedulerAddException("��¥�� ���� 1���� 31 ������ ���̿��� �մϴ�. �Է°�:" + day);
	
		Calendar calendar = Calendar.getInstance();
		
		if( !(startTime && year == -1 && month == -1 && day == -1) ){
			// ���� ��/��/���� �����Ǿ����� ���� ���� �Ʒ� ó���� ���� �ʴ´�.
			// �ֳĸ� ��� ��/��/���� ����ϱ� ����.
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, (month -1) );
			
			// calendar.set(Calendar.DAY_OF_MONTH, day) �� ȣ���ϱ� ���� ���ؾ� ����� ���´�
			if( calendar.getActualMaximum(Calendar.DAY_OF_MONTH) < day )
				throw new SchedulerAddException("����� �� ���� ��¥�� �Է��Ͽ����ϴ�. �Է°�:" + year + "�� " + month + "�� " + day + "��");
			
			calendar.set(Calendar.DAY_OF_MONTH, day);
		}
		
		calendar.set(Calendar.HOUR_OF_DAY, startTime ? 0 : 23);
		calendar.set(Calendar.MINUTE, startTime ? 0 : 59);
		calendar.set(Calendar.SECOND, startTime ? 0 : 59);
		
		
		
		calendar.set(Calendar.DAY_OF_MONTH, day);
		
		long result = calendar.getTimeInMillis();
		
		if( result <= 0 )
			throw new SchedulerAddException("����� �� ���� ��¥�� �Է��Ͽ����ϴ�. �Է°�:" + year + "�� " + month + "�� " + day + "��");
		
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
