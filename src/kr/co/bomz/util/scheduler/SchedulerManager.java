package kr.co.bomz.util.scheduler;

import java.util.ArrayList;
import java.util.Queue;

import kr.co.bomz.util.collection.BomzBlockingQueue;


/**
 * 등록된 스케줄러의 실행/삭제를 담당한다
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class SchedulerManager extends Thread{
	
	private final long SCHEDULE_SLEEP_TIME = 600;
	
	private Queue<SchedulerObject> schedulerQueue = new BomzBlockingQueue<SchedulerObject>();
	
	private ArrayList<Long> deleteSchedulerIdList = new ArrayList<Long>();
	
	private boolean close = false;
	
	SchedulerManager(){}
	
	
	/**
	 * 스케줄러 추가
	 * @param object		스케줄러 정보
	 * @return				스케줄러 등록 성공 여부
	 */
	boolean insertScheduler(SchedulerObject object){
		return this.schedulerQueue.offer( object );
	}
	
	/**
	 * 등록된 스케줄러 삭제
	 * @param schedulerId		스케줄러 아이디
	 * @return					스케줄러 삭제 성공 여부
	 */
	boolean removeScheduler(long schedulerId){
		return this.deleteSchedulerIdList.add(schedulerId);
	}
	
	/**
	 * 등록된 스케줄러 실행/삭제 처리
	 */
	public void run(){
		
		int queueSize, index;
		SchedulerObject schedulerObject;
		SchedulerTimeCheck schedulerTimeCheck = new SchedulerTimeCheck(this);
		
		while( !this.close ){
			
			if( this.schedulerQueue.isEmpty() ){
				try {		Thread.sleep( SCHEDULE_SLEEP_TIME );		} catch (InterruptedException e) {}
				continue;
			}
			
			queueSize = this.schedulerQueue.size();
			
			for(index=0; index < queueSize; index++){
				schedulerObject = this.schedulerQueue.poll();
				
				if( schedulerObject == null )		break;
				
				if( this.deleteSchedulerIdList.indexOf(schedulerObject.getSchedulerId()) != -1 ){
					this.deleteSchedulerIdList.remove(schedulerObject.getSchedulerId());
					schedulerObject.removeScheduler();
				}
				
				schedulerTimeCheck.execute(schedulerObject);
				schedulerObject = null;
				try{		Thread.sleep(1);		}catch(Exception e){}		// CPU 부하 방지
			}
			
			try {		Thread.sleep( SCHEDULE_SLEEP_TIME );		} catch (InterruptedException e) {}
			
		}
		
		// 스레드 종료 시 처리
		this.schedulerQueue = null;
		this.deleteSchedulerIdList = null;
	}
	
	/**		스케쥴 매니저 종료		*/
	void closeScheduleManager(){
		this.close = true;
	}
}
