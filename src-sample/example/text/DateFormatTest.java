package example.text;

import java.util.Calendar;

import kr.co.bomz.util.text.BomzDateFormat;

public class DateFormatTest {

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();

		BomzDateFormat dateFormat = BomzDateFormat.getInstance("[yyyy.MM.dd a hh:mm:ss]");
		
		System.out.println( dateFormat.format(cal) );
		
		System.out.println( dateFormat.format(cal, "yy-MM-dd'T'HH:mm:ss.SSSZ") );
		
		System.out.println( dateFormat.format(cal) );
	}

}
