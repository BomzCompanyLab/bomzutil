package kr.co.bomz.util.text;

import java.util.Calendar;

/**
 * �ð� ����. 0 ~ 23���� ǥ��. ����(H)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class Hour24Pattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.HOUR_OF_DAY;
	}

	@Override
	int estimateLength() {
		return 2;
	}
}
