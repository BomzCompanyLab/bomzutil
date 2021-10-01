package kr.co.bomz.util.text;

import java.util.Calendar;

/**
 * 시간 패턴. 1 ~ 24으로 표현. 약자(k)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class Hour24NPattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.HOUR_OF_DAY;
	}

	@Override
	void appendPatten(Calendar date, StringBuilder buffer){
		
		int value = date.get( Calendar.HOUR_OF_DAY );
		
		if( value == 0 )		value = 24;
		
		int intLength = this.intLength(value);
		if( intLength < this.repeat )		this.appendEmptyData(this.repeat - intLength, buffer);
		
		buffer.append(value);
	}
	
	@Override
	int estimateLength() {
		return 2;
	}
}
