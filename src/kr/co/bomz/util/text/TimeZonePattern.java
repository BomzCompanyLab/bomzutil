package kr.co.bomz.util.text;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * ���� 'z' �Ǵ� 'Z' ����� ���� �߻�Ŭ����
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	TimeZoneGeneralPattern
 * @see	TimeZoneRFCPattern
 *
 */
public abstract class TimeZonePattern extends Pattern{
	
	protected static final HashMap<TimeZoneInfo, String> cachePattern = new HashMap<TimeZoneInfo, String>();
	
	protected Locale locale;
	protected TimeZone timeZone;
	
	@Override
	void appendPatten(Calendar date, StringBuilder buffer){
		
		TimeZoneInfo timeZoneInfo = new TimeZoneInfo(this.locale, this.timeZone);
		String value = cachePattern.get(timeZoneInfo);
		
		if( value != null ){
			buffer.append( value );
		}else{
			value = this.getTimeZoneValue(date);
			cachePattern.put(timeZoneInfo, value);
			buffer.append( value );
		}
				
	}
	
	/**
	 * ���� 'z' �Ǵ� 'Z' �� �´� ���� ���� ����
	 * 
	 * @param date		������ ������ ��¥ ����
	 * @return				���� 'z' �Ǵ� 'Z' �� �´� ���� ����
	 */
	protected abstract String getTimeZoneValue(Calendar date); 
	
	@Override
	void setLocale(Locale locale){
		this.locale = locale;
	}
	
	@Override
	void setTimeZone(TimeZone timeZone){
		this.timeZone = timeZone;
	}
	
	/**
	 * ĳ�� ����� ���� TimeZone ��� ����
	 * @author Bomz
	 * @since 1.0
	 * @version 1.0
	 *
	 */
	protected class TimeZoneInfo{
		private Locale locale;
		private TimeZone timeZone;
		
		/**
		 * ĳ�� ����� ���� TimeZone ����
		 * @param locale				��������
		 * @param timeZone		ǥ�ؽð���
		 */
		protected TimeZoneInfo(Locale locale, TimeZone timeZone){
			this.locale = locale;
			this.timeZone = timeZone;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((locale == null) ? 0 : locale.hashCode());
			result = prime * result
					+ ((timeZone == null) ? 0 : timeZone.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TimeZoneInfo other = (TimeZoneInfo) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (locale == null) {
				if (other.locale != null)
					return false;
			} else if (!locale.equals(other.locale))
				return false;
			if (timeZone == null) {
				if (other.timeZone != null)
					return false;
			} else if (!timeZone.equals(other.timeZone))
				return false;
			return true;
		}

		private TimeZonePattern getOuterType() {
			return TimeZonePattern.this;
		}
		
	}
	
}
