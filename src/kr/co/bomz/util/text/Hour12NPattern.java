package kr.co.bomz.util.text;

import java.util.Calendar;

/**
 * �ð� ����. 0~11�ð����� ǥ��. ����(K)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class Hour12NPattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.HOUR;
	}

	@Override
	int estimateLength() {
		return 2;
	}
}
