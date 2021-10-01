package kr.co.bomz.util.timer;

/**
 *		타이머 종료 시 종료에 대한 내용
 *
 * @author Bomz
 * @since 1.0
 * @version 1.0
 */
public enum TimerStopType {
	/**		사용자에 의한 강제 종료		*/
	STOP_USER,
	/**		시스템에 의한 종료		*/
	STOP_SYSTEM,
	/**		BomzTimer.close() 호출로 인한 강제 종료		*/
	STOP_CLOSE
}
