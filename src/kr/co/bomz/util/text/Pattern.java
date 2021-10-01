package kr.co.bomz.util.text;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * ��, ��, ��, ��, ��, �� �� ���Ͽ� ���� �⺻ �߻� Ŭ����
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public abstract class Pattern{
	/**
	 *  ������ ���� �ݺ� Ƚ��.
	 */
	protected int repeat = 0;
	
	/**
	 * ������ ���� �ݺ� Ƚ�� ����
	 */
	protected void repeat(){
		this.repeat++;
	}
	
	/**
	 * ������ ���� �ݺ� Ƚ�� ����
	 * @return		������ ���� �ݺ� Ƚ��
	 */
	protected int getRepeat(){
		return this.repeat;
	}
		
	/**
	 * ��Ʈ���� �ڸ���
	 * @param value		��
	 * @return				���� �ڸ���. ��) ���� 1994 �� ��� 4 ����
	 */
	protected int intLength(int value){
		if( value <= 9 )						return 1;
		else if( value <= 99 )				return 2;
		else if( value <= 999 )			return 3;
		else if( value <= 9999 )			return 4;
		else if( value <= 99999 )		return 5;
		else		return Integer.toString(value).length();
	}
	
	/**
	 * ������ �ڸ����� �°� �� �ڸ��� ä���
	 * @param size		������ �ڸ���
	 * @param buffer		ǥ���� ������
	 */
	protected void appendEmptyData(int size, StringBuilder buffer){
		if( size <= 0 )		return;
		switch( size ){
		case 1 :		buffer.append("0");		break;
		case 2 :		buffer.append( "00");		break;
		case 3 :		buffer.append( "000");		break;
		case 4 :		buffer.append( "0000");		break;
		case 5 :		buffer.append( "00000");		break;
		case 6 :		buffer.append( "000000");		break;
		case 7 :		buffer.append( "0000000");		break;
		case 8 :		buffer.append( "00000000");		break;
		default :
			for(int i=0; i < size; i++){
				buffer.append("0");
			}
		}
	}
	
	/**
	 * ǥ���� ��¥ ���� ����
	 * @return
	 */
	abstract int getCalendarType();
		
	/**
	 * ���Ͽ� �´� ��¥ ������ �߰�
	 * @param date
	 * @param buffer
	 */
	void appendPatten(Calendar date, StringBuilder buffer){
		
		int value = date.get( this.getCalendarType() );
		
		int intLength = this.intLength(value);
		if( intLength < this.repeat )		this.appendEmptyData(this.repeat - intLength, buffer);
		
		buffer.append( value );
	}
	
	/**
	 * ���� ���� ����
	 * @param locale		��������
	 */
	void setLocale(Locale locale){}
	
	/**
	 * ǥ�� �ð��� ����
	 * @param timeZone		ǥ�� �ð���
	 */
	void setTimeZone(TimeZone timeZone){}
	
	/**
	 * ���ڿ� ���� ����
	 * @return		���ڿ� ���� ����
	 */
	abstract int estimateLength();
}
