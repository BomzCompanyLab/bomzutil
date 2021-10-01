package example.resource;

import java.util.ArrayList;
import java.util.List;

import kr.co.bomz.util.resource.ResourceBundle;


public class ResourceTest3 {

	public static void main(String[] args) {
		// 1. java.lang.String Multi Type 
		List<String> resourceFileList = new ArrayList<String>();
		resourceFileList.add("resource_test1.conf");
		resourceFileList.add("resource/di_pattern.conf");
		ResourceBundle resource = new ResourceBundle(resourceFileList);
		
		// 2. java.lang.String Multi Type 
//		ResourceBundle resource = new ResourceBundle(new String[]{"resource_test1.conf", "resource/di_pattern.conf"});
		
		// 3. java.io.File Multi Type
//		ResourceBundle resource = new ResourceBundle(new File("resource_test1.conf"), new File("resource/di_pattern.conf"));
		
		// 4. java.io.InputStream Multi Type
//		ResourceBundle resource = null;
//		try{
//			FileInputStream fis1 = new FileInputStream(new File("resource_test1.conf"));
//			FileInputStream fis2 = new FileInputStream(new File("resource/di_pattern.conf"));
//			resource = new ResourceBundle(fis1, fis2);
//		}catch(Exception e){}
		
		// 5. java.io.Reader Multi Type
//		ResourceBundle resource = null;
//		try{
//			FileReader fr1 = new FileReader(new File("resource_test1.conf"));
//			FileReader fr2 = new FileReader(new File("resource/di_pattern.conf"));
//			resource = new ResourceBundle(fr1, fr2);
//		}catch(Exception e){}
		
		
		// print start
		String[] resourceNames = resource.getResourceNames();
		for(String nm : resourceNames){
			System.out.println("Key=" + nm + " , Value=" + resource.getResourceValue(nm));
		}
		
	}

}
