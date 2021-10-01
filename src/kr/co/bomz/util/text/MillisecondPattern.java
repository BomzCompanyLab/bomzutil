package kr.co.bomz.util.text;

import java.util.Calendar;

/**
 * �и������� ����. ����(S)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class MillisecondPattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.MILLISECOND;
	}

	@Override
	int estimateLength() {
		return 3;
	}
}
