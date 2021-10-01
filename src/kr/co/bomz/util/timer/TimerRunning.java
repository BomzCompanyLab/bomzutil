package kr.co.bomz.util.timer;

import kr.co.bomz.util.threadpool.BomzThread;

/**
 * 타이머 동작 스레드
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see Timer
 * @see BomzTimer
 */
public class TimerRunning extends BomzThread{

	private BomzTimer timerService;
	private Timer timer;
	
	@Override
	protected void execute() throws Exception {
		this.timer.execute();
	}

	@Override
	protected void executeParameter(Object ... parameters) throws Exception{
		this.timer = (Timer)parameters[0];
	}
	
	@Override
	protected void constructorParameter(Object ... parameters) throws Exception{
		this.timerService = (BomzTimer)parameters[0];
	}
	
	@Override
	protected void close(){
		if( this.timer.isEndTimer() ){
			this.timerService.removeTimer(this.timer);	
		}
		
		this.timer = null;
	}
}
