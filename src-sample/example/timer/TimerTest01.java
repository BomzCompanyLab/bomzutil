package example.timer;

import kr.co.bomz.util.timer.BomzTimer;

public class TimerTest01 {

	public static void main(String[] args) {
		BomzTimer bomzTimer = new BomzTimer();
		long timerId = bomzTimer.addTimer(new TimerObject(2000));		// Call every 2 seconds
		
		try{		Thread.sleep(10000);		}catch(Exception e){}
		
		// close timer
		bomzTimer.removeTimer(timerId);
		
		// close bomz timer thread
		bomzTimer.closeTimer();
		
	}

}
