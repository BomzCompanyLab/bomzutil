package example.scheduler;

import kr.co.bomz.util.scheduler.BomzScheduler;
import kr.co.bomz.util.scheduler.SchedulerWeek;
import kr.co.bomz.util.scheduler.WeeksSchedulerInfo;

/*
 *	scheduler test 02 
 *	call week = monday, tuesday
 *	start date = 2014. 05. 15
 *	call time = 14:20 , 14:25 , 14:30 ... 23:50 , 23:55
 *
 */
public class SchedulerTest02 {

	public static void main(String[] args){
		BomzScheduler scheduler = new BomzScheduler();
		
		WeeksSchedulerInfo weeks = new WeeksSchedulerInfo();
		
		weeks.setScheduler(new SchedulerObject());
		
		// scheduler call week is monday and tuesday
		weeks.setWeek(
			new SchedulerWeek[]{	
					SchedulerWeek.MONDAY,
					SchedulerWeek.TUESDAY
				}
			);
		
		// yyyy-mm-dd (2014-05-15) schedule start
		weeks.setStartDate(2014, 5, 15);
		
		/*
		 * scheduler call time. 14:20 , 14:25 , 14:30 ... 23:50 , 23:55
		 * parameter [0] = start hour
		 * parameter [1] = start minute
		 * parameter [2] = repeat minute
		 */
		weeks.setStartHourAndMinute(14, 20, 5);
		
		long id = scheduler.addSchduler(weeks);
		
		// remove scheduler
//		scheduler.removeScheduler(id);
		
		// close Bomz scheduler
//		scheduler.closeScheduler();
		
	}
}
