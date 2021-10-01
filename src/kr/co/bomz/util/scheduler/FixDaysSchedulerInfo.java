package kr.co.bomz.util.scheduler;

/**
 * 
 * 지정한 시간/분에 지정한 횟수만큼 지정한 날짜에만 호출되는 스케줄이 필요할 때 사용
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see		AllDaysSchedulerInfo
 * @see		RepeatDaySchedulerInfo
 * @see		WeeksSchedulerInfo
 */
public class FixDaysSchedulerInfo extends SchedulerInfo{

	/*		스케줄 실행 날짜		*/
	private int[] days;
	
	/**
	 * 지정한 시간/분에 지정한 횟수만큼 지정한 날짜에만 호출되는 스케줄이 필요할 때 사용
	 */
	public FixDaysSchedulerInfo(){
		this.reset();
	}
	
	/**
	 * 스케줄 실행 일
	 * @param days		스케줄 실행 일
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
