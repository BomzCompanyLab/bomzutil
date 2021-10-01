package kr.co.bomz.util.text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * 
 * java.util.Calendar �� java.util.Date �� ��¥�� ���ϴ� ������ ���ڿ��� ����.<p>
 * 
 * ��� ������ ���ڴ� �Ʒ��� ������, ���� ������ �������� ���ϴ� ��¥ ��������� �����Ѵ�
 * 
 * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Chart shows pattern letters, date/time component, presentation, and examples.">
 *     <tr bgcolor="#ccccff">
 *         <th align=left>����</th>
 *         <th align=left>����</th>
 *         <th align=left>���� Ÿ��</th>
 *         <th align=left>��� ��</th>
 *     </tr>
 *     <tr>
 *         <td><code>G</code></td>
 *         <td>Era designator</td>
 *         <td><a href="#text">Text</a></td>
 *         <td><code>AD</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>y</code></td>
 *         <td>Year</td>
 *         <td><a href="#year">Year</a></td>
 *         <td><code>1996</code>; <code>96</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>M</code></td>
 *         <td>Month in year</td>
 *         <td><a href="#month">Month</a></td>
 *         <td><code>July</code>; <code>Jul</code>; <code>07</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>w</code></td>
 *         <td>Week in year</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>27</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>W</code></td>
 *         <td>Week in month</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>2</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>D</code></td>
 *         <td>Day in year</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>189</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>d</code></td>
 *         <td>Day in month</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>10</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>F</code></td>
 *         <td>Day of week in month</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>2</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>E</code></td>
 *         <td>Day in week</td>
 *         <td><a href="#text">Text</a></td>
 *         <td><code>Tuesday</code>; <code>Tue</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>a</code></td>
 *         <td>Am/pm marker</td>
 *         <td><a href="#text">Text</a></td>
 *         <td><code>PM</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>H</code></td>
 *         <td>Hour in day (0-23)</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>0</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>k</code></td>
 *         <td>Hour in day (1-24)</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>24</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>K</code></td>
 *         <td>Hour in am/pm (0-11)</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>0</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>h</code></td>
 *         <td>Hour in am/pm (1-12)</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>12</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>m</code></td>
 *         <td>Minute in hour</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>30</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>s</code></td>
 *         <td>Second in minute</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>55</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>S</code></td>
 *         <td>Millisecond</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>978</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>z</code></td>
 *         <td>Time zone</td>
 *         <td><a href="#timezone">General time zone</a></td>
 *         <td><code>Pacific Standard Time</code>; <code>PST</code>; <code>GMT-08:00</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>Z</code></td>
 *         <td>Time zone</td>
 *         <td><a href="#rfc822timezone">RFC 822 time zone</a></td>
 *         <td><code>-0800</code></td>
 *     </tr>
 * </table>
 * </blockquote>
 * 
 * <h4>��� ����</h4>
 *
 * ���� ��뿹���� �Ʒ��� ������ �����ڿ� ���ϴ� ��� ������ �����ϰų� format(...) �޼ҵ忡 ���ϴ� ������ �����Ͽ� ����� �� �ִ�
 * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Examples of date and time patterns interpreted in the U.S. locale">
 *     <tr bgcolor="#ccccff">
 *         <th align=left>����</th>
 *         <th align=left>���</th>
 *     </tr>
 *     <tr>
 *         <td><code>"yyyy.MM.dd G 'at' HH:mm:ss z"</code></td>
 *         <td><code>2001.07.04 AD at 12:08:56 PDT</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>"EEE, MMM d, ''yy"</code></td>
 *         <td><code>Wed, Jul 4, '01</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>"h:mm a"</code></td>
 *         <td><code>12:08 PM</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>"hh 'o''clock' a, zzzz"</code></td>
 *         <td><code>12 o'clock PM, Pacific Daylight Time</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>"K:mm a, z"</code></td>
 *         <td><code>0:08 PM, PDT</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>"yyyyy.MMMMM.dd GGG hh:mm aaa"</code></td>
 *         <td><code>02001.July.04 AD 12:08 PM</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>"EEE, d MMM yyyy HH:mm:ss Z"</code></td>
 *         <td><code>Wed, 4 Jul 2001 12:08:56 -0700</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>"yyMMddHHmmssZ"</code></td>
 *         <td><code>010704120856-0700</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>"yyyy-MM-dd'T'HH:mm:ss.SSSZ"</code></td>
 *         <td><code>2001-07-04T12:08:56.235-0700</code></td>
 *     </tr>
 * </table>
 * </blockquote>
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class BomzDateFormat {

	private final String pattern;
	private int patternLength = 0;
	private Pattern[] patterns;
	private Locale locale;
	private TimeZone timeZone;
	
	private static final Map<PatternInfo, BomzDateFormat> cacheMap = new HashMap<PatternInfo, BomzDateFormat>();
	
	private BomzDateFormat(String pattern){
		this.pattern = pattern;
	}
	
	/**
	 * Ư�� ������ �������� ���� ��ü ��û
	 * @return		BomzDateFormat
	 */
	public static final BomzDateFormat getInstance(){
		return getInstance(null, null, null);
	}
	
	/**
	 * ��¥ ��� ������ ������ ��ü ��û
	 * @param pattern		��¥ ����
	 * @return		BomzDateFormat
	 */
	public static final BomzDateFormat getInstance(String pattern){
		return getInstance(pattern, null, null);
	}
	
	/**
	 * ��¥ ��� ���ϰ� ���� ������ ������ ��ü ��û
	 * @param pattern		��¥ ����
	 * @param locale			���� ����
	 * @return		BomzDateFormat
	 */
	public static final BomzDateFormat getInstance(String pattern, Locale locale){
		return getInstance(pattern, locale, null);
	}
	
	/**
	 * ��¥ ��� ���ϰ� ǥ�ؽð��븦 ������ ��ü ��û
	 * @param pattern		��¥ ����
	 * @param timeZone	ǥ�ؽð���
	 * @return		BomzDateFormat
	 */
	public static final BomzDateFormat getInstance(String pattern, TimeZone timeZone){
		return getInstance(pattern, null, timeZone);
	}

	/**
	 * ��¥ ��� ���ϰ� ��������, ǥ�ؽð��븦 ������ ��ü ��û
	 * @param pattern		��¥ ����
	 * @param locale			��������
	 * @param timeZone	ǥ�ؽð���
	 * @return		BomzDateFormat
	 */
	public static final BomzDateFormat getInstance(String pattern, Locale locale, TimeZone timeZone){
		
		if( pattern == null )		throw new NullPointerException("Pattern is null");
		
		if( locale == null )		locale = Locale.getDefault();
		if( timeZone == null )	timeZone = TimeZone.getDefault();
		
		PatternInfo info = new PatternInfo(pattern, locale, timeZone);
		BomzDateFormat format = cacheMap.get(info);
		if( format != null ){
			return format;
		}else{
			format = new BomzDateFormat(pattern);
			format.locale = locale;
			format.timeZone = timeZone;
			format.parsePattern(pattern);
			cacheMap.put(info, format);
			return format;
		}
	}
		
		
	// java.util.Date Ÿ���� java.util.Calendar �������� ����
	private Calendar getCalendarAtLocale(java.util.Date date){
		Calendar result = new GregorianCalendar(this.timeZone, this.locale);
		result.setTime(date);
		return result;
	}
	
	/**
	 * ������ ��¥ �������� �ð� ���� ����
	 * @param date		�ð� ����
	 * @return				������ �������� ����� �ð� ����
	 */	
	public String format(Calendar date){
		if( date == null )		throw new NullPointerException("date value is null");
		return this.getPatternDate(date);
	}
	
	/**
	 * ������ ��¥ �������� �ð� ���� ����.
	 * <code>format(java.util.Calendar)</code> �� ����� ��õ�Ѵ�
	 * @param date		�ð� ����
	 * @return				������ �������� ����� �ð� ����
	 */	
	public String format(java.util.Date date){
		if( date == null )		throw new NullPointerException("date value is null");
		
		return this.getPatternDate(this.getCalendarAtLocale(date));
	}
	
	/**
	 * ������ ��¥ �������� �ð� ���� ����
	 * @param date			�ð� ����
	 * @param pattern		��¥ ����
	 * @return					������ �������� ����� �ð� ����
	 */
	public String format(Calendar date, String pattern){
		if( date == null )		throw new NullPointerException("date value is null");
		if( pattern == null )	throw new NullPointerException("pattern value is null");
		
		
		if( pattern.equals(this.pattern) ){
			// ���� ������ ���
			return this.getPatternDate(date);
		}else{
			// ������ ����Ǿ��� ���
			return getInstance(pattern, this.locale, this.timeZone).format(date, pattern);
		}
		
		
	}
	
	/**
	 * ������ ��¥ �������� �ð� ���� ����.
	 * <code>format(java.util.Calendar, String)</code> �� ����� ��õ�Ѵ�
	 * @param date			�ð� ����
	 * @param pattern		��¥ ����
	 * @return					������ �������� ����� �ð� ����
	 */
	public String format(java.util.Date date, String pattern){
		if( date == null )		throw new NullPointerException("date value is null");
		if( pattern == null )	throw new NullPointerException("pattern value is null");
		
		return this.format( this.getCalendarAtLocale(date), pattern );
	}
	
	// ������ ���� ������ �ð� ������ ����
	private String getPatternDate(Calendar date){
				
		final StringBuilder buffer = new StringBuilder(this.patternLength);
		
		for(final Pattern pt : this.patterns){
			pt.appendPatten(date, buffer);
		}
		
		return buffer.toString();
	}
		
	
	/*
	 * yy , yyyy: �⵵
	 * M , MM : ��
	 * d , dd : �� (�� ����)
	 * D , DD : �� (�� ����)
	 * h , hh : �� (1~12)
	 * K , KK : �� (0~11)
	 * H , HH : ��(0~23)
	 * k , kk : ��(1~24)
	 * m , mm : ��
	 * s , ss : ��
	 * S : �и�������
	 * a : ����/����
	 * w : Week in year
	 * W : Week in month
	 * F : ���� Ƚ�� (�� ����)
	 * E : ���ϸ�
	 * z : General Time Zone (ex:Pacific Standard Time; PST; GMT-08:00)
	 * Z : RFC822 Time Zone (ex:-0800); 
	 * 
	 */
	private void parsePattern(String pattern){
		
		ArrayList<Pattern> patternList = new ArrayList<Pattern>();
		
		int length = pattern.length();
		char charValue;
		try{
			for(int i=0; i < length; i++){
				charValue = pattern.charAt(i);
				switch(charValue){
				case 'y' :		// ��
					this.parsePattern(YearPattern.class, patternList);		break;
				case 'M' : 		// ��
					this.parsePattern(MonthPattern.class, patternList);		break;
				case 'd' :		// �� ���� ��
					this.parsePattern(DayInMonthPattern.class, patternList);		break;
				case 'D' :		// �� ���� ��
					this.parsePattern(DayInYearPattern.class, patternList);		break;
				case 'h' :		// �� (1~12)
					this.parsePattern(Hour12Pattern.class, patternList);		break;
				case 'H' :		// �� (0~23)
					this.parsePattern(Hour24Pattern.class, patternList);		break;
				case 'm' :		// ��
					this.parsePattern(MinutePattern.class, patternList);		break;
				case 's' :		// ��
					this.parsePattern(SecondPattern.class, patternList);		break;
				case 'a' : 		// ���� ����
					this.parsePattern(MeridiemPattern.class, patternList);	break;
				case '\'' :		// ���ڿ�
					i = this.parseString(pattern, i, patternList);		break;
				case 'G' :		// Era designator. AD or BC
					this.parsePattern(EraPattern.class, patternList);	break;
				case 'w' :		// Week In Year
					this.parsePattern(WeekInYearPattern.class, patternList);	break;
				case 'W' :		// Week In Month
					this.parsePattern(WeekInMonthPattern.class, patternList);	break;
				case 'F' :		// �ش� ���� �ش� ���� �ݺ� Ƚ��
					this.parsePattern(DayOfWeekInMonthPattern.class, patternList);	break;
				case 'E' :		// ���� �̸�
					this.parsePattern(DayNameInWeekPattern.class, patternList);	break;
				case 'k' :		// �� (1~24)
					this.parsePattern(Hour24NPattern.class, patternList);	break;
				case 'K' :		// �� (0~11)
					this.parsePattern(Hour12NPattern.class, patternList);	break;
				case 'S' :		// �и�������
					this.parsePattern(MillisecondPattern.class, patternList);	break;
				case 'z' :		// General Time Zone
					this.parsePattern(TimeZoneGeneralPattern.class, patternList);	break;
				case 'Z' :		// RFC Time Zone
					this.parsePattern(TimeZoneRFCPattern.class, patternList);	break;
				default :		// �� �� ���ڿ�
					if( (charValue >= 'a' && charValue <= 'z') || (charValue >= 'A' && charValue <= 'Z') )
						throw new java.lang.IllegalArgumentException("Illegal pattern character '" + charValue + "'");
					i = this.otherString(pattern, i, patternList);
					break;
				}
			}
			
			// ���� �м��� ���������� �̷������ ���
			this.patterns = patternList.toArray(new Pattern[patternList.size()]);
			
			for(int i=0; i < this.patterns.length; i++)
				this.patternLength += this.patterns[i].estimateLength();
			
		}catch(Exception e){
			// ���� �� ��� ���� ��� ����
			System.err.println(e + " (���ǵ��� ���� ��¥ ���� '" + pattern + "')");
		}
		
	}
	
	// ���� ����ǥ�� ������ �ʰ� ���ĺ��� �ƴ� ��� ó��
	private int otherString(String pattern, int i, List<Pattern> patternList){
		int start = i;
		int max = pattern.length();
		char charValue;
		while( true ){
			if( ++i >= max )		break;		// ������ ���� ���
			charValue = pattern.charAt(i);
			if( (charValue >= 'a' && charValue <= 'z') || (charValue >= 'A' && charValue <= 'Z') || (charValue == '\'') )		break;
		}
		
		this.appendStringPattern(patternList, pattern.substring(start, i));
		return --i;
	}
	
	// '\'' �� ���Ͽ� ���ǵ� ���ڿ� �м�
	private int parseString(String pattern, int index, List<Pattern> patternList) throws Exception{
		try{
			int lastIndex = pattern.indexOf("'", index + 1);
			this.appendStringPattern(patternList, pattern.substring(index + 1, lastIndex));
			return lastIndex;
		}catch(Exception e){
			throw new DatePatternException("���� �м� ����", e);
		}
	}
	
	// ���ڿ� ������ ��� �� �� ������ ���ڿ��̾����� Ȯ�� �� ���ڿ��̸� �� ���ϰ� ��ģ��
	private void appendStringPattern(List<Pattern> patternList, String msg){
		int size = patternList.size();
		if( size == 0 ){
			patternList.add(new StringPattern(msg));
		}else{
			Pattern pattern = patternList.get( size - 1);
			if( pattern instanceof StringPattern ){
				// ������ ������ String �� ��� ���� �߰�
				((StringPattern)pattern).append(msg);
			}else{
				// ������ ������ �ٸ� ������ ���
				patternList.add(new StringPattern(msg));
			}
		}
	}
	
	private Pattern parsePattern(Class<? extends Pattern> clazz, List<Pattern> patternList) throws DatePatternException{
		int size = patternList.size();
		Pattern pattern = size == 0 ? null : patternList.get(size-1);
		
		if( pattern == null || pattern.getClass() != clazz){		
			// null �� ���. �Ǵ� ���� ���ϰ� ������ �ٸ� ��� ���� ���� �� ����Ʈ�� �߰�
			try {
				pattern = clazz.newInstance();
				pattern.setLocale(this.locale);
				pattern.setTimeZone(this.timeZone);
				patternList.add(pattern);
			} catch (Exception e) {
				throw new DatePatternException("DatePattern clsss ���� ���� [" + clazz + "]");
			}
			
		}
		
		pattern.repeat();
		
		return pattern;
	}
		
}
