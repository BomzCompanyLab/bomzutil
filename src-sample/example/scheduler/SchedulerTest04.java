package example.scheduler;

import kr.co.bomz.util.scheduler.AllDaysSchedulerInfo;
import kr.co.bomz.util.scheduler.BomzScheduler;

/*
 *	scheduler test 04
 *	start date = now
 *	end date = 2014.07.11
 *	call date = all days
 *	call time = 14:20 , 14:25 and 14:30
 *
 */
public class SchedulerTest04 {

	public static void main(String[] args){
		BomzScheduler scheduler = new BomzScheduler();
		
		AllDaysSchedulerInfo allDays = new AllDaysSchedulerInfo();
		
		allDays.setScheduler(new SchedulerObject());
		
		/*
		 * schedule end date
		 * yyyy-mm-dd (2014.07.11)
		 */
		allDays.setEndDate(2014, 7, 11);
		
		/*
		 * scheduler call time. 14:20 , 14:25 and 14:30
		 * parameter [0] = start hour
		 * parameter [1] = start minute
		 * parameter [2] = repeat minute
		 * parameter [3] = repeat count
		 */
		allDays.setStartHourAndMinute(14, 20, 5, 3);
		
		long id = scheduler.addSchduler(allDays);
		
		// remove scheduler
//		scheduler.removeScheduler(id);
		
		// close Bomz scheduler
//		scheduler.closeScheduler();
		
	}
}
