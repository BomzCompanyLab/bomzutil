package example.timer;

import kr.co.bomz.util.timer.Timer;
import kr.co.bomz.util.timer.TimerStopType;

public class TimerObject extends Timer{

	public TimerObject(long repeatPeriod) {
		super(repeatPeriod);
	}
	
	public TimerObject(long repeatPeriod, int repeatCount) {
		super(repeatPeriod, repeatCount);
	}

	@Override
	public void execute() {
		System.out.println("Timer is call");
	}

	@Override
	public void stopTimer(TimerStopType timerStopType) {
		System.out.println("Timer is stop : " + timerStopType.name());
	}

}
