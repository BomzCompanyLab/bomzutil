package kr.co.bomz.util.text;

import java.util.Calendar;

/**
 * �Ϲ� ���ڿ� ����.
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class StringPattern extends Pattern{

	private final StringBuilder msg;
	
	StringPattern(String msg){
		this.msg = new StringBuilder(msg);
	}
	
	/**
	 * ���� ���ڿ��� ���ο� ���ڿ� �߰�
	 * @param msg		�߰��� ���ڿ�
	 */
	void append(String msg){
		this.msg.append(msg);
	}
	
	@Override
	int getCalendarType() {
		return -1;
	}
	
	@Override
	int estimateLength() {
		return this.msg.length();
	}

	@Override
	void appendPatten(Calendar date, StringBuilder buffer){
		buffer.append( this.msg );
	}
}
