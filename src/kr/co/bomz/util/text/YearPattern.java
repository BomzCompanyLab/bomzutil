package kr.co.bomz.util.text;

import java.util.Calendar;

/**
 * �⵵ ����. ����(y)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class YearPattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.YEAR;
	}

	@Override
	int estimateLength() {
		return super.repeat;
	}
	
	@Override
	void appendPatten(Calendar date, StringBuilder buffer){
		int pow = 1;
		for(int i=0; i < super.repeat; i++){
			pow *= 10;
			if( pow <= 0 )		break;		// y �� �ʹ� ���� �Ἥ ��Ʈ���� �ִ� ���� �Ѿ��� ���
		}
		
		
//		String value = pow <= 0 ?
//					Integer.toString(date.get( Calendar.YEAR )) :		// y �� �ʹ� ���� ���� ���
//					Integer.toString(date.get( Calendar.YEAR ) % pow);
	
		int value = pow <= 0 ?
				date.get( Calendar.YEAR ) :		// y �� �ʹ� ���� ���� ���
				date.get( Calendar.YEAR ) % pow;
		
		this.appendEmptyData(this.repeat - super.intLength(value), buffer);
		
		buffer.append( value );
	}
	
}
