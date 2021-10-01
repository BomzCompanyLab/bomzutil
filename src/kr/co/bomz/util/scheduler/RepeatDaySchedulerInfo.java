package kr.co.bomz.util.scheduler;

/**
 * 
 * 스케줄 시작날짜로부터 주기적인 간격으로 지정한 시간/분에 호출되는 스케줄이 필요할 때 사용
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see		AllDaysSchedulerInfo
 * @see		FixDaysSchedulerInfo
 * @see		WeeksSchedulerInfo
 */
public class RepeatDaySchedulerInfo extends SchedulerInfo{

	/*		스케줄 반복 주기 (단위:일)		*/
	private Integer repeatDay;
	
	/**
	 * 스케줄 시작날짜로부터 주기적인 간격으로 지정한 시간/분에 호출되는 스케줄이 필요할 때 사용
	 */
	public RepeatDaySchedulerInfo(){
		this.reset();
	}
	
	/**
	 * 스케줄 반복 주기를 일 단위로 설정<p>
	 * 예) 시작 일이 2014.01.03 으로 설정되었고 repeatDay 값이 4일 경우
	 * 2014.01.03 , 2014.01.07 , 2014.01.11 , 2014.01.15 ~ 날짜에 스케줄이 실행 됨
	 * @param repeatDay		스케줄 반복 주기 (단위:일)
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
