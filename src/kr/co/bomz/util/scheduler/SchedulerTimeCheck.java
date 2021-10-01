package kr.co.bomz.util.scheduler;

import java.util.Calendar;

/**
 * 
 * �����ٷ� ���� �ð����� �˻��Ѵ�
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public final class SchedulerTimeCheck{
	
	/**		���� ��¥ �� �� ���Ǵ� ��ü		*/
	private final Calendar calendar = Calendar.getInstance();
	/**		���� ���� ��¥ �񱳽� ���Ǵ� ��ü		*/
	private Calendar tmpCalendar = null;
	
	private final SchedulerManager schedulerManager;
	
	private SchedulerObject schedulerObject;
	
	public SchedulerTimeCheck(SchedulerManager schedulerManager){
		this.schedulerManager = schedulerManager;
	}
	
	/**
	 * �����ٷ� ���� ���� ���θ� �˻��Ѵ�<br>
	 * ���������� �Ǿ��� ��� �����ϰ�, ���� ���� �Ǿ��� ��� �������� �����Ų��
	 */
	protected void execute(SchedulerObject schedulerObject){
		this.schedulerObject = schedulerObject;
		if( this.schedulerObject == null )							return;
		
		try{
			if( this.schedulerObject.isNullScheudler() )		return;
			
			if( this.schedulerObject.isScheduleStateAsStop() ){
				// �����ڿ� ���� ������ �������� ����Ǿ��� ���
				this.schedulerObject.getScheduler().destroy();
				return;
			}
			
			if( _execute() ){
				// ���� ������ ������ ���� ������Ŵ����� ���
				this.schedulerManager.insertScheduler( this.schedulerObject );
			}else{
				// ������ ����
				// �ڿ� ����
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
		
		// ������ ���� ��¥�� �������� �˻�
		if( !this.checkStartDate(timeInMillis) )		return true;
		// ������ ���� ��¥�� �Ǿ����� �˻�
		if( !this.checkEndDate(timeInMillis) )			return false;
		// ������ ������ ���� ��¥�� �´��� �˻�
		// ���� ��¥�� �´ٸ� ���� �ð��� �´����� �˻�
		try{
			// ��¥ �˻�
			if( !this.checkRepeat() )			return true;
			// �ð� �˻�
			
			if( !checkHourAndMinute(this.calendar.get(Calendar.HOUR_OF_DAY), this.calendar.get(Calendar.MINUTE)) )		return true;
		}catch(Exception e){
			// ���� �߻� �� �ݺ� �ֱ� �˻縦 ���� ��ü�� ���̰ų� �߸��Ǿ��� �����
			// �̷� ��� �������� ������
			return false;
		}
		
		// ��� ���ǿ� �����Ƿ� ������ ����
		this.schedulerObject.startScheduler();
		
		return true;
		
	}
	
	private boolean checkHourAndMinute(int nowHour, int nowMinute) throws Exception{
		
		if( this.schedulerObject.isNullHourAndMinute() )
			throw new Exception("[������ ���� ���� �ð� ���� NULL ���̹Ƿ� �������� �����մϴ�] ��������̵� : " + this.schedulerObject.getSchedulerId());
		
		
		int length = this.schedulerObject.getHourAndMinuteSize();
		
		for(int i=0; i < length; i++){
			if( this.schedulerObject.getHourAndMinuteValue(i, 0) == nowHour && this.schedulerObject.getHourAndMinuteValue(i, 1) == nowMinute ){
				// ���� �ð��� ���� �ð��� ���� ���
				if( this.schedulerObject.checkExecuteHourAndMinute(nowHour, nowMinute) ){
					// ���� ������� ���� �ð��̹Ƿ� ������ ����
					this.schedulerObject.addHourAndMinute(nowHour, nowMinute);
					return true;
					
				}else{
					// �̹� �����ߴ� �ð��̹Ƿ� �������� �������� ����
					break;
				}
				
			}
		}
		
		return false;
	}
	
	/**
	 * 	������ �ݺ� �ֱ� �˻縦 �����Ͼ��ϴ��� Ȯ���Ѵ�
	 */
	private boolean checkRepeat() throws Exception{
		
		final int nowDay = this.calendar.get(Calendar.DAY_OF_MONTH);
		// ������ �̹� ���� �ݺ� ���θ� �˻��Ͽ��ٸ� ���Ӱ� �˻��� �ʿ䰡 ����
		if( this.schedulerObject.getBeforeDay() == nowDay )		return this.schedulerObject.isCheckRepeat();
		
		// ������ �����ߴ� ������ �ð��� �ʱ�ȭ�Ѵ�.
		this.schedulerObject.clearHourAndMinute();
		
		boolean result = this.checkRepeat(this.schedulerObject.getRepeatType());
		this.schedulerObject.setCheckRepeat( result );
		
		// ���� �˻� ��¥�� ���� ��¥�� ����
		this.schedulerObject.setBeforeDay( nowDay );
		
		return result;
	}
	
	private boolean checkRepeat(SchedulerRepeatType repeatType) throws Exception{
		// �ݺ� �ֱ� ��Ŀ� ���� �б�
		switch( repeatType ){
		case REPEAT_DAY:		// ������ �ֱ�� ����
			return this.checkRepeatDay();
			
		case DAYS:						// Ư�� ������ ����
			return this.checkDays(this.calendar.get(Calendar.DAY_OF_MONTH));
			
		case WEEKS:					// Ư�� ���Ͽ��� ����
			// ���� ������ �����´�. 
			// -1 �� �ϴ� ������ enum �� ���� 0 ���� �����ϱ� ����
			// ���ϰ��� 1 ���� �����ϸ� �Ͽ����� ������
			return this.checkWeeks(this.calendar.get(Calendar.DAY_OF_WEEK) - 1);
			
		default:
			// ���������� ������ ��� �̰����� ���� �ʾƾ� ��. ������ ������ ���� ó��
			// �α״� ���� �߻� ��ġ���� ���
			throw new Exception("[������ �ݺ� ������ ���� �������� ��� NULL ���̹Ƿ� �������� �����մϴ�] ��������̵� : " + this.schedulerObject.getSchedulerId());
		}
	}
	
	/**
	 * 	������ �ֱ�� �������� ������ ��� �������� ������ ���� �Ǿ����� �˻��Ѵ�.
	 * 	������ ���� �� ������ ������ ���� ��¥�� �����Ѵ�
	 */
	private boolean checkRepeatDay() throws Exception{
		Integer repeatDay = this.schedulerObject.getRepeatDay();
		if( repeatDay == null ){
			throw new Exception("[������ ���� ���� ���� �� ���� �Ⱓ���� ���� ������ NULL ������ �Ǿ� �־� �������� �����մϴ�] ��������̵� : " + this.schedulerObject.getSchedulerId());
		}
		
		int nextRepeatDay = this.schedulerObject.getNextRepeatDay();
		
		if( nextRepeatDay == -1 || 
				( 
						nextRepeatDay == this.calendar.get(Calendar.DAY_OF_MONTH) &&
						this.schedulerObject.getNextRepeatYear() == this.calendar.get(Calendar.YEAR) && 
						this.schedulerObject.getNextRepeatMonth() == (this.calendar.get(Calendar.MONTH) + 1)
				)
				){
			
			// ù �������̰ų� ���࿹������ ���� ��¥�� ���� ��� 
			// ���� ���� ��¥�� �����Ѵ�.
			long timeInMillis = this.calendar.getTimeInMillis();
			
			if( this.tmpCalendar == null )		this.tmpCalendar = Calendar.getInstance();
			else												this.tmpCalendar.setTimeInMillis(timeInMillis);
			
			// 86400000 �� �Ϸ� 24�ð��� �и�������� ǥ���� ��
			this.tmpCalendar.setTimeInMillis( timeInMillis + (86400000*repeatDay) );
			this.schedulerObject.setNextRepeatDay(this.tmpCalendar.get(Calendar.YEAR), (this.tmpCalendar.get(Calendar.MONTH) + 1), this.tmpCalendar.get(Calendar.DAY_OF_MONTH) );
			
			return true;
		}else{
			// ���� ������ ���� ��¥�� ���� �ʾ���.
			return false;
		}
		
	}
	
	/**
	 * 	�������� ������ ������ �Ǿ����� �˻��Ѵ�
	 */
	private boolean checkWeeks(int nowWeek) throws Exception{
	
		
		SchedulerWeek[] weeks = this.schedulerObject.getWeeks();
		if( weeks == null ){
			throw new Exception("[������ ���� ���� ���� �� ���Ϸ� ���� ������ NULL ������ �Ǿ� �־� �������� �����մϴ�] ��������̵� : " + this.schedulerObject.getSchedulerId());
		}
		
		for(SchedulerWeek week : weeks){
			if( nowWeek == week.ordinal() )		return true;
		}
		
		return false;
	}
	
	/**
	 * 	�������� ������ ��¥�� �Ǿ����� �˻��Ѵ�
	 */
	private boolean checkDays(int nowDay) throws Exception{
		
		if( this.schedulerObject.isNullDays() ){
			throw new Exception("[������ ���� ���� ���� �� ��¥�� ���� ������ NULL ������ �Ǿ� �־� �������� �����մϴ�] ��������̵� : " + this.schedulerObject.getSchedulerId());
		}
		
		return this.schedulerObject.isEqualsDays(nowDay);
	}
	
	/**
	 * 		�������� ���� ��¥�� �������� �˻��Ѵ�
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
	 * 		�������� ���� ��¥�� �Ǿ����� �˻��Ѵ�
	 */
	private boolean checkEndDate(long timeInMillis){
		
		long value = this.schedulerObject.getEndTime();
		if( value == -1 )		return true;
		
		if( timeInMillis >= value )		return false;
		else										return true;
	}
	

}
