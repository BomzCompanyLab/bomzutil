package example.scheduler;

import java.util.Calendar;

import kr.co.bomz.util.scheduler.BomzScheduler;
import kr.co.bomz.util.scheduler.RepeatDaySchedulerInfo;

/*
 *	scheduler test 03 
 *	start date = now
 *	end date = 2014.07.11
 *	call date = now , now + 3 , now + 6 , now + 9 ... 
 *	call time = 14:20 , 14:25 and 14:30
 *
 */
public class SchedulerTest03 {

	public static void main(String[] args){
		BomzScheduler scheduler = new BomzScheduler();
		
		RepeatDaySchedulerInfo repeatDay = new RepeatDaySchedulerInfo();
		
		repeatDay.setScheduler(new SchedulerObject());
		
		/*
		 * if now date is 2014. 01. 15
		 * scheduler call is 
		 * 2014.01.15 , 2014.01.18 , 2014.01.21 , 2014.01.24 .... 
		 */
		repeatDay.setRepeatDay(3);
		
		// start date. now!
		repeatDay.setStartDate(Calendar.getInstance());
		
		/*
		 * schedule end date
		 * yyyy-mm-dd (2014.07.11)
		 */
		repeatDay.setEndDate(2014, 7, 11);
		
		/*
		 * scheduler call time. 14:20 , 14:25 and 14:30
		 * parameter [0] = start hour
		 * parameter [1] = start minute
		 * parameter [2] = repeat minute
		 * parameter [3] = repeat count
		 */
		repeatDay.setStartHourAndMinute(14, 20, 5, 3);
		
		long id = scheduler.addSchduler(repeatDay);
		
		// remove scheduler
//		scheduler.removeScheduler(id);
		
		// close Bomz scheduler
//		scheduler.closeScheduler();
		
	}
}
