package kr.co.bomz.util.scheduler;

/**
 * 
 * ������ �ð�/�п� ������ Ƚ����ŭ ���� ȣ��Ǵ� �������� �ʿ��� �� ���
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see		FixDaysSchedulerInfo
 * @see		RepeatDaySchedulerInfo
 * @see		WeeksSchedulerInfo
 */
public class AllDaysSchedulerInfo extends SchedulerInfo{

	/**
	 * ������ �ð�/�п� ������ Ƚ����ŭ ���� ȣ��Ǵ� �������� �ʿ��� �� ���
	 */
	public AllDaysSchedulerInfo(){
		reset();
	}
	
	@Override
	protected Integer getRepeatDay() {
		return 1;
	}
}
