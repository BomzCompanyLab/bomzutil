package kr.co.bomz.util.scheduler;

/**
 * 
 * 특정 요일에만 지정한 시간/분에 호출되는 스케줄이 필요할 때 사용
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see		AllDaysSchedulerInfo
 * @see		FixDaysSchedulerInfo
 * @see		RepeatDaySchedulerInfo
 */
public class WeeksSchedulerInfo extends SchedulerInfo{
	
	/*		스케줄 실행 요일		*/
	private SchedulerWeek[] weeks;
	
	/**
	 * 특정 요일에만 지정한 시간/분에 호출되는 스케줄이 필요할 때 사용
	 */
	public WeeksSchedulerInfo(){
		this.reset();
	}
	
	/**
	 * 스케줄 실행 요일
	 * @param weeks		요일
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
