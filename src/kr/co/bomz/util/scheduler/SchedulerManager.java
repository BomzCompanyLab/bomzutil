package kr.co.bomz.util.scheduler;

import java.util.ArrayList;
import java.util.Queue;

import kr.co.bomz.util.collection.BomzBlockingQueue;


/**
 * ��ϵ� �����ٷ��� ����/������ ����Ѵ�
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
	 * �����ٷ� �߰�
	 * @param object		�����ٷ� ����
	 * @return				�����ٷ� ��� ���� ����
	 */
	boolean insertScheduler(SchedulerObject object){
		return this.schedulerQueue.offer( object );
	}
	
	/**
	 * ��ϵ� �����ٷ� ����
	 * @param schedulerId		�����ٷ� ���̵�
	 * @return					�����ٷ� ���� ���� ����
	 */
	boolean removeScheduler(long schedulerId){
		return this.deleteSchedulerIdList.add(schedulerId);
	}
	
	/**
	 * ��ϵ� �����ٷ� ����/���� ó��
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
				try{		Thread.sleep(1);		}catch(Exception e){}		// CPU ���� ����
			}
			
			try {		Thread.sleep( SCHEDULE_SLEEP_TIME );		} catch (InterruptedException e) {}
			
		}
		
		// ������ ���� �� ó��
		this.schedulerQueue = null;
		this.deleteSchedulerIdList = null;
	}
	
	/**		������ �Ŵ��� ����		*/
	void closeScheduleManager(){
		this.close = true;
	}
}
