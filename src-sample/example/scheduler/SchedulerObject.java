package example.scheduler;

import java.util.Calendar;

import kr.co.bomz.util.scheduler.Scheduler;

public class SchedulerObject implements Scheduler{

	@Override
	public void execute() {
		Calendar ca = Calendar.getInstance();
		
		System.out.println(
				"SchedulerTest Call : " +
				ca.get(Calendar.YEAR) + "년 " +
				(ca.get(Calendar.MONTH)+1) + "월 " +
				ca.get(Calendar.DAY_OF_MONTH) + "일 " +
				ca.get(Calendar.HOUR_OF_DAY) + "시 " +
				ca.get(Calendar.MINUTE) + "분 " +
				ca.get(Calendar.SECOND) + "초"
			);
	}

	@Override
	public void destroy() {
		System.out.println("SchedulerTest destroy");
	}

}
