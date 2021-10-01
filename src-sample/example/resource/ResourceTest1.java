package example.resource;

import java.io.File;
import java.io.FileReader;

import kr.co.bomz.util.resource.ResourceBundle;


public class ResourceTest1 {

	public static void main(String[] args) {
		// 1. java.lang.String Type 
//		ResourceBundle resource = new ResourceBundle("resource_test1.conf");
		
		// 2. java.io.File Type
//		ResourceBundle resource = new ResourceBundle(new File("resource_test1.conf"));
		
		/*
		 * 3. file dir , file name pattern
		 * search file is 
		 * '/resource/my_home.conf' , '/resource/my_name.conf' and '/resource/my_test.conf'
		 */ 
//		ResourceBundle resource = new ResourceBundle("resource", "my_.*.conf");
		
		// 4. java.io.InputStream
//		ResourceBundle resource = null;
//		try{
//			FileInputStream fis = new FileInputStream(new File("resource_test1.conf"));
//			resource = new ResourceBundle(fis);
//		}catch(Exception e){}
		
		// 5. java.io.Reader
		ResourceBundle resource = null;
		try{
			FileReader fr = new FileReader(new File("resource_test1.conf"));
			resource = new ResourceBundle(fr);
		}catch(Exception e){}
		
		
		// print start
		String[] resourceNames = resource.getResourceNames();
		for(String nm : resourceNames){
			System.out.println("Key=" + nm + " , Value=" + resource.getResourceValue(nm));
		}
		
	}

}
