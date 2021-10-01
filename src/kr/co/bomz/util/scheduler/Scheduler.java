package kr.co.bomz.util.scheduler;

/**
 * 스케줄 동작 조건이 만족되었을 경우 동작할 구현체는 해당 인터페이스를 구현해야한다
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public interface Scheduler {
	
	/**
	 * 	지정된 조건이 되었을 경우 실행할 내용을 구현하며,
	 *  오래 걸리는 작업의 경우 내부적으로 스레드를 생성하여 처리하길 권장한다.<br>
	 */
	void execute();
	
	/**
	 * 사용자가 강제로 스케쥴을 종료하였거나
	 * 종료일자가 되어 스케쥴이 종료되었을 경우 호출된다
	 */
	void destroy();
}
