package kr.co.bomz.util.text;

import java.util.Calendar;

/**
 * �� ���� ���� ��. ����(w)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class WeekInYearPattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.WEEK_OF_YEAR;
	}
	
	@Override
	int estimateLength() {
		return 2;
	}
}
