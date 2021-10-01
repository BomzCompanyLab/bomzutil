package kr.co.bomz.util.scheduler;

/**
 * 
 * ������ ���۳�¥�κ��� �ֱ����� �������� ������ �ð�/�п� ȣ��Ǵ� �������� �ʿ��� �� ���
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see		AllDaysSchedulerInfo
 * @see		FixDaysSchedulerInfo
 * @see		WeeksSchedulerInfo
 */
public class RepeatDaySchedulerInfo extends SchedulerInfo{

	/*		������ �ݺ� �ֱ� (����:��)		*/
	private Integer repeatDay;
	
	/**
	 * ������ ���۳�¥�κ��� �ֱ����� �������� ������ �ð�/�п� ȣ��Ǵ� �������� �ʿ��� �� ���
	 */
	public RepeatDaySchedulerInfo(){
		this.reset();
	}
	
	/**
	 * ������ �ݺ� �ֱ⸦ �� ������ ����<p>
	 * ��) ���� ���� 2014.01.03 ���� �����Ǿ��� repeatDay ���� 4�� ���
	 * 2014.01.03 , 2014.01.07 , 2014.01.11 , 2014.01.15 ~ ��¥�� �������� ���� ��
	 * @param repeatDay		������ �ݺ� �ֱ� (����:��)
	 */
	public void setRepeatDay(int repeatDay){
		this.repeatDay = repeatDay;
	}
	
	@Override
	protected Integer getRepeatDay() {
		return repeatDay;
	}
	
	@Override
	public void reset(){
		this.repeatDay = null;
		super.reset();
	}
}
