package kr.co.bomz.util.scheduler;

/**
 * 
 * 지정한 시간/분에 지정한 횟수만큼 매일 호출되는 스케줄이 필요할 때 사용
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
	 * 지정한 시간/분에 지정한 횟수만큼 매일 호출되는 스케줄이 필요할 때 사용
	 */
	public AllDaysSchedulerInfo(){
		reset();
	}
	
	@Override
	protected Integer getRepeatDay() {
		return 1;
	}
}
