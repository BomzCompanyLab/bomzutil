package example.text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kr.co.bomz.util.text.BomzDateFormat;

public class SpeedTest {

	private final String[] patterns = new String[]{
			"[yyyy-MM-dd kk:mm:ss]",
			"'Y'yy'M'MM'D'dd 'H'hh'm'mm's'ss",
			"G kk:mm:ss a"
	};
	
	private int length = 950000;
	
	public static void main(String[] args) {
		SpeedTest speedTest = new SpeedTest();
		
		int[] formats = new int[]{0, 2};		// 0:java , 1:apache , 2:Bomz
		
		System.out.println("nm\t\tJava\t\tApache\tBomz");
		
		for(int i=0; i < 10; i++){
			System.out.print( i );		// nm
			for(int id : formats)		speedTest.run(id);
		}
		
		
		
	}
	
	private void run(int cursor){
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		
		long start = System.currentTimeMillis();		// start time
		
		for(int i=0; i < length; i++){
			for(String pattern : patterns){
				switch( cursor ){
				case 0 :		javaDateFormat(date, pattern);		break;
//				case 1 :		apacheDateFormat(cal, pattern);		break;
				default :		bomzDateFormat(cal, pattern);	break;
				}
			}
		}
		
		System.out.print( "\t\t" + (System.currentTimeMillis() - start) );
		if( cursor == 2 )		System.out.println();
		
	}

	// java package. java.text.SimpleDateFormat
	private void javaDateFormat(Date date, String pattern){
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.format(date);
	}
	
//	// apache commons lang3
//	private void apacheDateFormat(Calendar cal, String pattern){
//		FastDateFormat format = FastDateFormat.getInstance(pattern);
//		format.format(cal);
//	}
	
	// bomz project utility
	private void bomzDateFormat(Calendar cal, String pattern){
		BomzDateFormat format = BomzDateFormat.getInstance(pattern);
		format.format(cal);
	}
}
