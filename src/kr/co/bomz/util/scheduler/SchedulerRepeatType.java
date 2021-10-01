package kr.co.bomz.util.scheduler;

/**
 * 
 * 스케줄러 반복 주기 타입
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
enum SchedulerRepeatType {
	/**		특정 요일에만 실행		*/
	WEEKS,					
	/**		일정한 주기로 실행		*/
	REPEAT_DAY,			
	/**		특정 날에만 실행		*/
	DAYS,						
	/**		설정되지 않은 기본 값. 오류		*/
	NONE						
}
