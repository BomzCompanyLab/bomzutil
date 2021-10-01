package example.scheduler;

import kr.co.bomz.util.scheduler.BomzScheduler;
import kr.co.bomz.util.scheduler.FixDaysSchedulerInfo;

/*
 *	scheduler test 01 
 *	call days = 2, 4, 15 and 24
 *	call time = 3:30 , 6:00 , 17:27 
 *
 */
public class SchedulerTest01 {

	public static void main(String[] args){
		BomzScheduler scheduler = new BomzScheduler();
		
		FixDaysSchedulerInfo fixDays = new FixDaysSchedulerInfo();
		
		fixDays.setScheduler(new SchedulerObject());
		
		// fix day is 2, 4, 15 and 24
		fixDays.setDays(new int[]{2, 4, 15, 24});
		
		// scheduler call time
		fixDays.setStartHourAndMinute(
			new int[][]{
				{3, 30},		// time is 3:30
				{6, 0},		// time is 6:00
				{17, 27},	// time is 17:27
			}
		);
		
		long id = scheduler.addSchduler(fixDays);
		
		// remove scheduler
//		scheduler.removeScheduler(id);
		
		// close Bomz scheduler
//		scheduler.closeScheduler();
		
	}
}
