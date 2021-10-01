package kr.co.bomz.util.text;

import java.util.Calendar;

/**
 * �� ���� �� ��Ȥ Ƚ�� ����. ����(F)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class DayOfWeekInMonthPattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.DAY_OF_WEEK_IN_MONTH;
	}
	
	@Override
	int estimateLength() {
		return 2;
	}

}
