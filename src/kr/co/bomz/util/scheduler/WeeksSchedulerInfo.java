package kr.co.bomz.util.scheduler;

/**
 * 
 * Ư�� ���Ͽ��� ������ �ð�/�п� ȣ��Ǵ� �������� �ʿ��� �� ���
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see		AllDaysSchedulerInfo
 * @see		FixDaysSchedulerInfo
 * @see		RepeatDaySchedulerInfo
 */
public class WeeksSchedulerInfo extends SchedulerInfo{
	
	/*		������ ���� ����		*/
	private SchedulerWeek[] weeks;
	
	/**
	 * Ư�� ���Ͽ��� ������ �ð�/�п� ȣ��Ǵ� �������� �ʿ��� �� ���
	 */
	public WeeksSchedulerInfo(){
		this.reset();
	}
	
	/**
	 * ������ ���� ����
	 * @param weeks		����
	 */
	public void setWeek(SchedulerWeek[] weeks){
		this.weeks = weeks;
	}
	
	@Override
	protected SchedulerWeek[] getWeeks() {
		return weeks;
	}
	
	@Override
	public void reset(){
		this.weeks = null;
		super.reset();
	}
}
