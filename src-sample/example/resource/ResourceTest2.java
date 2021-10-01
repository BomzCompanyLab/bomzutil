package example.resource;

import kr.co.bomz.util.resource.ResourceBundle;

public class ResourceTest2 {

	public static void main(String[] args) {

		ResourceBundle resource = new ResourceBundle("resource", "format.conf");
		
		// get resources
		String stringFormat1 = resource.getResourceValue("string_format");
		String stringFormat2 = resource.getResourceStringValue("string_format");
		int intFormat = resource.getResourceIntValue("int_format");
		long longFormat = resource.getResourceLongValue("long_format");
		double doubleFormat = resource.getResourceDoubleValue("double_format");
		float floatFormat = resource.getResourceFloatValue("float_format");
		
		// print start
		System.out.println("stringFormat1=" + stringFormat1);
		System.out.println("stringFormat2=" + stringFormat2);
		System.out.println("intFormat=" + intFormat);
		System.out.println("longFormat=" + longFormat);
		System.out.println("doubleFormat=" + doubleFormat);
		System.out.println("floatFormat=" + floatFormat);
		
		// exception test
		resource.getResourceIntValue("string_format");		// resource value format exception
	}

}
