package kr.co.bomz.util.scheduler;

/**
 * 
 * ������ �ð�/�п� ������ Ƚ����ŭ ������ ��¥���� ȣ��Ǵ� �������� �ʿ��� �� ���
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see		AllDaysSchedulerInfo
 * @see		RepeatDaySchedulerInfo
 * @see		WeeksSchedulerInfo
 */
public class FixDaysSchedulerInfo extends SchedulerInfo{

	/*		������ ���� ��¥		*/
	private int[] days;
	
	/**
	 * ������ �ð�/�п� ������ Ƚ����ŭ ������ ��¥���� ȣ��Ǵ� �������� �ʿ��� �� ���
	 */
	public FixDaysSchedulerInfo(){
		this.reset();
	}
	
	/**
	 * ������ ���� ��
	 * @param days		������ ���� ��
	 */
	public void setDays(int[] days){
		this.days = days;
	}
	
	@Override
	protected int[] getDays() {
		return days;
	}
	
	@Override
	public void reset(){
		this.days = null;
		super.reset();
	}
}
