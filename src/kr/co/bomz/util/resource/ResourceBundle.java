package kr.co.bomz.util.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * 
 * 설정 파일에서 환경설정 값을 가져와 사용할 수 있는 유틸
 * 
 * @author Bomz
 * @since 1.1
 * @version 1.1
 *
 */
public class ResourceBundle {

	private InputStream[] inputStream;
	private Reader[] reader;
		
	private Map<String, String> resourceMap = new HashMap<String, String>();
	
	/**
	 * 		특정 파일명으로 리소스 추가
	 * @param resourceFile		리소스 파일 명
	 */
	public ResourceBundle(String resourceFile){
		this.addResourceFile(resourceFile);
		this.load();
	}
	
	/**
	 * 		java.util.List 타입의 파일명 목록으로 리소스 추가
	 * @param resourceFiles		리소스 파일 명 목록
	 */
	public ResourceBundle(List<String> resourceFiles){
		if( resourceFiles == null )		return;
		
		this.addResourceFile(resourceFiles.toArray(new String[resourceFiles.size()]));
		this.load();
	}
	
	/**
	 * 		파일명 배열로 리소스 추가
	 * @param resourceFiles		리소스 파일 명 목록
	 */
	public ResourceBundle(String[] resourceFiles){
		this.addResourceFile(resourceFiles);
		this.load();
	}
	
	/**
	 * 		특정 파일을 리소스로 사용
	 * @param resourceFile		리소스 파일
	 */
	public ResourceBundle(File resourceFile){
		this.addResourceFile(resourceFile.getPath());
		this.load();
	}
	
	/**
	 * 		특정 파일을 리소스로 사용
	 * @param resourceFiles		리소스 파일 목록
	 */
	public ResourceBundle(File ... resourceFiles){
		if( resourceFiles == null )		return;
		
		int length = resourceFiles.length;
		String[] fileNames = new String[length];
		for(int i=0; i < length; i++)	fileNames[i] = resourceFiles[i].getPath();
		
		this.addResourceFile(fileNames);
		this.load();
	}
	
	/**
	 * 		특정 경로에 있는 모든 파일 중 이름 패턴이 맞는 모든 파일 사용<p>
	 * 		
	 * 		패턴 예제<br>
	 * 			.*-sample.conf		:	파일명이 '-sample' 로 끝나고 확장자가 conf 인 모든 파일<br>
	 * 			.*-.*.conf				:	파일명 중간에 '-' 가 포함되어 있고 확장자가 conf 인 모든 파일<br>
	 * 			project-.*..*			:	파일명이 'project-' 로 시작하는 모든 확장자의 파일<br>
	 * 			[a-c]-sample.*		:	'-sample' 앞부분이 'a' 또는 'b' 또는 'c' 로만 이루어져있는 모든 확장자의 파일<br>
	 * 											예)	aaa-sample.conf (O)<br>
	 * 													bbb-sample.properties (X)<br>
	 * 													abc-sample.conf (X)<br>
	 * 													ads-sample.conf (X)
	 * 			
	 * 
	 * @param resourceDir		파일 경로
	 * @param filePattern		파일 이름 패턴
	 */
	public ResourceBundle(String resourceDir, String filePattern) throws ResourceBundleCreateException{
		if( resourceDir == null || filePattern == null ){
			System.err.println("ResourceBundle 설정 값이 NULL (dir:" + resourceDir + ", filePattern:" + filePattern + ")");
			throw new ResourceBundleCreateException("ResourceBundle 설정 값이 NULL (dir:" + resourceDir + ", filePattern:" + filePattern + ")");
		}
		
		File fileDir = new File(resourceDir);
		if( !fileDir.isDirectory() ){
			System.err.println("잘못된 ResourceBundle 설정 값 (dir:" + resourceDir + ")");
			throw new ResourceBundleCreateException("잘못된 ResourceBundle 설정 값 (dir:" + resourceDir + ")");
		}
		
		ArrayList<String> filePathList = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile(filePattern);
		for(File file : fileDir.listFiles()){
			if( pattern.matcher( file.getName() ).find() ){		// 파일명 패턴 검사
				filePathList.add( file.getPath() );		// 패턴에 맞는 파일일 경우 추가
			}
		}
		
		// 패턴에 맞는 파일을 추가
		this.addResourceFile( filePathList.toArray(new String[filePathList.size()]) );
		
		this.load();
	}
		
	/**
	 * 리소스 파일의 InputStream 을 직접 설정
	 * @param inputStream
	 */
	public ResourceBundle(InputStream inputStream){
		this.inputStream = new InputStream[]{ inputStream };
		this.load();
	}
	
	/**
	 * 다수의 리소스 파일 InputStream 을 직접 설정
	 * @param inputStreams
	 */
	public ResourceBundle(InputStream ... inputStreams){
		this.inputStream = inputStreams;
		this.load();
	}
	
	/**
	 * 리소스 파일의 Reader 를 직접 설정
	 * @param reader
	 */
	public ResourceBundle(Reader reader){
		this.reader = new Reader[]{ reader };
		this.load();
	}
	
	/**
	 * 다수의 리소스 파일 Reader 를 직접 설정
	 * @param readers
	 */
	public ResourceBundle(Reader ... readers){
		this.reader = readers;
		this.load();
	}
	
	/**
	 * 요청한 리소스 값을 String 형태로 리턴
	 * @param name		리소스 이름
	 * @return				리소스 값. 등록되지 않은 리소스 이름일 경우 null 리턴
	 */
	public String getResourceValue(String name){
		if( name == null )		return null;
		return this.resourceMap.get(name);
	}
	
	/**
	 * 요청한 리소스 값을 String 형태로 리턴
	 * @param name		리소스 이름
	 * @return				리소스 값
	 * @throws NullPointerException		등록되지 않은 리소스 이름일 경우 발생		
	 */
	public String getResourceStringValue(String name) throws NullPointerException{
		return this._getResourceValue(name);
	}
	
	/**
	 * 요청한 리소스 값을 int 형태로 리턴
	 * @param name		리소스 이름
	 * @return				int 형으로 변환한 리소스 값
	 * @throws ResourceFormatException		리소스 값이 int 형이 아닐 경우 발생
	 * @throws NullPointerException				등록되지 않은 리소스 이름일 경우 발생
	 */
	public int getResourceIntValue(String name) throws ResourceFormatException, NullPointerException{
		String value = this._getResourceValue(name);
		
		try{
			return Integer.parseInt(value);
		}catch(Exception e){
			throw new ResourceFormatException("int 형이 아닌 리소스 값을 요청 [아이디=" + name + ", 값=" + value + "]");
		}
	}
	
	/**
	 * 요청한 리소스 값을 long 형태로 리턴
	 * @param name		리소스 이름
	 * @return				long 형으로 변환한 리소스 값
	 * @throws ResourceFormatException		리소스 값이 long 형이 아닐 경우 발생
	 * @throws NullPointerException				등록되지 않은 리소스 이름일 경우 발생
	 */
	public long getResourceLongValue(String name) throws ResourceFormatException, NullPointerException{
		String value = this._getResourceValue(name);
		
		try{
			return Long.parseLong(value);
		}catch(Exception e){
			throw new ResourceFormatException("long 형이 아닌 리소스 값을 요청 [아이디=" + name + ", 값=" + value + "]");
		}
	}
	
	/**
	 * 요청한 리소스 값을 double 형태로 리턴
	 * @param name		리소스 이름
	 * @return				double 형으로 변환한 리소스 값
	 * @throws ResourceFormatException		리소스 값이 double 형이 아닐 경우 발생
	 * @throws NullPointerException				등록되지 않은 리소스 이름일 경우 발생
	 */
	public double getResourceDoubleValue(String name) throws ResourceFormatException, NullPointerException{
		String value = this._getResourceValue(name);
		
		try{
			return Double.parseDouble(value);
		}catch(Exception e){
			throw new ResourceFormatException("double 형이 아닌 리소스 값을 요청 [아이디=" + name + ", 값=" + value + "]");
		}
	}
	
	/**
	 * 요청한 리소스 값을 float 형태로 리턴
	 * @param name		리소스 이름
	 * @return				float 형으로 변환한 리소스 값
	 * @throws ResourceFormatException		리소스 값이 float 형이 아닐 경우 발생
	 * @throws NullPointerException				등록되지 않은 리소스 이름일 경우 발생
	 */
	public float getResourceFloatValue(String name) throws ResourceFormatException, NullPointerException{
		String value = this._getResourceValue(name);
		
		try{
			return Float.parseFloat(value);
		}catch(Exception e){
			throw new ResourceFormatException("float 형이 아닌 리소스 값을 요청 [아이디=" + name + ", 값=" + value + "]");
		}
	}
	
	/**
	 * 리소스 값을 String 형으로 리턴
	 * @param name		리소스 이름
	 * @return				리소스 값
	 * @throws NullPointerException		등록되지 않은 리소스일 경우 발생
	 */
	private String _getResourceValue(String name) throws NullPointerException{
		if( name == null )		throw new NullPointerException("리소스 요청 아이디 값이 NULL");
		
		String value = this.resourceMap.get(name);
		if( value == null )		throw new NullPointerException("등록되지 않은 리소스 아이디 [" + name + "]");
		
		return value;
	}
	
	/**
	 * 등록된 리소스의 이름 목록을 리턴
	 * @return		리소스 이름
	 */
	public String[] getResourceNames(){
		return this.resourceMap.keySet().toArray(new String[this.resourceMap.size()]);
	}
	
	private void addResourceFile(String ... resourceFile){
		ArrayList<FileReader> fileReaderList = new ArrayList<FileReader>(resourceFile.length);
		
		for(String resource : resourceFile){
			
			try {
				fileReaderList.add( new FileReader(resource) );
			} catch (FileNotFoundException e) {
				System.err.println("리소스 파일 검색 실패 (file:" + resource + ") " + e.getMessage());
			}
			
		}
		
		int length = fileReaderList.size();
		this.reader = new Reader[ length ];
		
		for(int i=0; i < length; i++)	this.reader[i] = new BufferedReader( fileReaderList.get(i) );
	}
	
	private void load(){
		this.parsing(
				this.readResourceFile().toString().split("\n")
			);
	}
	
	private StringBuilder readResourceFile(){
		int size;
		char[] readerData = null;
		byte[] streamData = null;
		
		int length;
		if( this.inputStream != null ){
			streamData = new byte[100];
			length = this.inputStream.length;
		}else{
			readerData = new char[100];
			length = this.reader.length;
		}
		
		StringBuilder buffer = new StringBuilder();

		for(int i=0; i < length; i++){
			try {
				while( true ){
				
					size = (this.inputStream != null ) ? this.inputStream[i].read(streamData) : this.reader[i].read(readerData);
					if( size == -1 || size == 0 )		break;
					
					if( this.inputStream != null )
						buffer.append( new String( streamData, 0, size) );
					else
						buffer.append( new String(readerData, 0, size) );
					
				}
				
			} catch (Exception e) {
				System.err.println("리소스 파일을 읽던 중 오류. " + e.getMessage());
			}finally{
				if( this.inputStream != null ){
					try{		this.inputStream[i].close();		}catch(Exception e){}
					this.inputStream[i] = null;
				}
				
				if( this.reader != null ){
					try{		this.reader[i].close();		}catch(Exception e){}
					this.reader[i] = null;
				}
				
				buffer.append("\n");
			}
		
		}
		
		streamData = null;
		readerData = null;
		
		return buffer;
	}
	
	/*		설정파일 내용 분석		*/
	private void parsing(String[] messages){
		
		try{
			int startPosition;
			String resourceName, resourceValue;
			for(String message : messages){
				startPosition = message.indexOf("=");
				if( startPosition <= 0 )	continue;
				
				resourceName = message.substring(0, startPosition).trim();
				if(resourceName.length() <= 0 || resourceName.charAt(0) == '#' )		continue;
				
				resourceValue = message.substring(startPosition+1).trim();
				
				if( resourceName == null || resourceValue == null )				return;
				if( resourceName.equals("") || resourceValue.equals("") )		return;

				if( !this.resourceMap.containsKey(resourceName) ){
					this.resourceMap.put(resourceName, resourceValue);
				}
			}
		}catch(Exception e){
			System.err.println("리소스 파일 분석 오류. " + e.getMessage());
		}

	}
	
}
