package example.timer;

import kr.co.bomz.util.timer.BomzTimer;

public class TimerTest02 {

	public static void main(String[] args) {
		BomzTimer bomzTimer = new BomzTimer();
		bomzTimer.addTimer(new TimerObject(1000, 3));		// Call every 1 seconds 3 times
		
		try{		Thread.sleep(5000);		}catch(Exception e){}
				
		// close bomz timer thread
		bomzTimer.closeTimer();
	}

}
