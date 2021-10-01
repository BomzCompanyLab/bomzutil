package kr.co.bomz.util.text;

import java.util.Calendar;

/**
 * �ð� ����. 1~12�ð����� ǥ��. ����(h)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class Hour12Pattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.HOUR;
	}

	@Override
	void appendPatten(Calendar date, StringBuilder buffer){
		
		int value = date.get( Calendar.HOUR );
		
		if( value == 0 )		value = 12;
		
		int intLength = this.intLength(value);
		if( intLength < this.repeat )		this.appendEmptyData(this.repeat - intLength, buffer);

		buffer.append(value);
	}
	
	@Override
	int estimateLength() {
		return 2;
	}
}
