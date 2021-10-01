package kr.co.bomz.util.text;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * TimeZone ����. ����(z)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class TimeZoneGeneralPattern extends TimeZonePattern{
		
	@Override
	int getCalendarType() {
		return -1;
	}
	
	@Override
	int estimateLength() {
		return 3;
	}
		
	@Override
	protected String getTimeZoneValue(Calendar date){
		return this.timeZone.getDisplayName(
				(this.timeZone.useDaylightTime() && date.get(Calendar.DST_OFFSET) != 0) ,
				TimeZone.SHORT ,
				this.locale
			);
	}
	
}
