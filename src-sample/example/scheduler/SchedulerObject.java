package example.scheduler;

import java.util.Calendar;

import kr.co.bomz.util.scheduler.Scheduler;

public class SchedulerObject implements Scheduler{

	@Override
	public void execute() {
		Calendar ca = Calendar.getInstance();
		
		System.out.println(
				"SchedulerTest Call : " +
				ca.get(Calendar.YEAR) + "�� " +
				(ca.get(Calendar.MONTH)+1) + "�� " +
				ca.get(Calendar.DAY_OF_MONTH) + "�� " +
				ca.get(Calendar.HOUR_OF_DAY) + "�� " +
				ca.get(Calendar.MINUTE) + "�� " +
				ca.get(Calendar.SECOND) + "��"
			);
	}

	@Override
	public void destroy() {
		System.out.println("SchedulerTest destroy");
	}

}
