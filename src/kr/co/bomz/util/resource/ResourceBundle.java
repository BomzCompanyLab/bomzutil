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
 * ���� ���Ͽ��� ȯ�漳�� ���� ������ ����� �� �ִ� ��ƿ
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
	 * 		Ư�� ���ϸ����� ���ҽ� �߰�
	 * @param resourceFile		���ҽ� ���� ��
	 */
	public ResourceBundle(String resourceFile){
		this.addResourceFile(resourceFile);
		this.load();
	}
	
	/**
	 * 		java.util.List Ÿ���� ���ϸ� ������� ���ҽ� �߰�
	 * @param resourceFiles		���ҽ� ���� �� ���
	 */
	public ResourceBundle(List<String> resourceFiles){
		if( resourceFiles == null )		return;
		
		this.addResourceFile(resourceFiles.toArray(new String[resourceFiles.size()]));
		this.load();
	}
	
	/**
	 * 		���ϸ� �迭�� ���ҽ� �߰�
	 * @param resourceFiles		���ҽ� ���� �� ���
	 */
	public ResourceBundle(String[] resourceFiles){
		this.addResourceFile(resourceFiles);
		this.load();
	}
	
	/**
	 * 		Ư�� ������ ���ҽ��� ���
	 * @param resourceFile		���ҽ� ����
	 */
	public ResourceBundle(File resourceFile){
		this.addResourceFile(resourceFile.getPath());
		this.load();
	}
	
	/**
	 * 		Ư�� ������ ���ҽ��� ���
	 * @param resourceFiles		���ҽ� ���� ���
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
	 * 		Ư�� ��ο� �ִ� ��� ���� �� �̸� ������ �´� ��� ���� ���<p>
	 * 		
	 * 		���� ����<br>
	 * 			.*-sample.conf		:	���ϸ��� '-sample' �� ������ Ȯ���ڰ� conf �� ��� ����<br>
	 * 			.*-.*.conf				:	���ϸ� �߰��� '-' �� ���ԵǾ� �ְ� Ȯ���ڰ� conf �� ��� ����<br>
	 * 			project-.*..*			:	���ϸ��� 'project-' �� �����ϴ� ��� Ȯ������ ����<br>
	 * 			[a-c]-sample.*		:	'-sample' �պκ��� 'a' �Ǵ� 'b' �Ǵ� 'c' �θ� �̷�����ִ� ��� Ȯ������ ����<br>
	 * 											��)	aaa-sample.conf (O)<br>
	 * 													bbb-sample.properties (X)<br>
	 * 													abc-sample.conf (X)<br>
	 * 													ads-sample.conf (X)
	 * 			
	 * 
	 * @param resourceDir		���� ���
	 * @param filePattern		���� �̸� ����
	 */
	public ResourceBundle(String resourceDir, String filePattern) throws ResourceBundleCreateException{
		if( resourceDir == null || filePattern == null ){
			System.err.println("ResourceBundle ���� ���� NULL (dir:" + resourceDir + ", filePattern:" + filePattern + ")");
			throw new ResourceBundleCreateException("ResourceBundle ���� ���� NULL (dir:" + resourceDir + ", filePattern:" + filePattern + ")");
		}
		
		File fileDir = new File(resourceDir);
		if( !fileDir.isDirectory() ){
			System.err.println("�߸��� ResourceBundle ���� �� (dir:" + resourceDir + ")");
			throw new ResourceBundleCreateException("�߸��� ResourceBundle ���� �� (dir:" + resourceDir + ")");
		}
		
		ArrayList<String> filePathList = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile(filePattern);
		for(File file : fileDir.listFiles()){
			if( pattern.matcher( file.getName() ).find() ){		// ���ϸ� ���� �˻�
				filePathList.add( file.getPath() );		// ���Ͽ� �´� ������ ��� �߰�
			}
		}
		
		// ���Ͽ� �´� ������ �߰�
		this.addResourceFile( filePathList.toArray(new String[filePathList.size()]) );
		
		this.load();
	}
		
	/**
	 * ���ҽ� ������ InputStream �� ���� ����
	 * @param inputStream
	 */
	public ResourceBundle(InputStream inputStream){
		this.inputStream = new InputStream[]{ inputStream };
		this.load();
	}
	
	/**
	 * �ټ��� ���ҽ� ���� InputStream �� ���� ����
	 * @param inputStreams
	 */
	public ResourceBundle(InputStream ... inputStreams){
		this.inputStream = inputStreams;
		this.load();
	}
	
	/**
	 * ���ҽ� ������ Reader �� ���� ����
	 * @param reader
	 */
	public ResourceBundle(Reader reader){
		this.reader = new Reader[]{ reader };
		this.load();
	}
	
	/**
	 * �ټ��� ���ҽ� ���� Reader �� ���� ����
	 * @param readers
	 */
	public ResourceBundle(Reader ... readers){
		this.reader = readers;
		this.load();
	}
	
	/**
	 * ��û�� ���ҽ� ���� String ���·� ����
	 * @param name		���ҽ� �̸�
	 * @return				���ҽ� ��. ��ϵ��� ���� ���ҽ� �̸��� ��� null ����
	 */
	public String getResourceValue(String name){
		if( name == null )		return null;
		return this.resourceMap.get(name);
	}
	
	/**
	 * ��û�� ���ҽ� ���� String ���·� ����
	 * @param name		���ҽ� �̸�
	 * @return				���ҽ� ��
	 * @throws NullPointerException		��ϵ��� ���� ���ҽ� �̸��� ��� �߻�		
	 */
	public String getResourceStringValue(String name) throws NullPointerException{
		return this._getResourceValue(name);
	}
	
	/**
	 * ��û�� ���ҽ� ���� int ���·� ����
	 * @param name		���ҽ� �̸�
	 * @return				int ������ ��ȯ�� ���ҽ� ��
	 * @throws ResourceFormatException		���ҽ� ���� int ���� �ƴ� ��� �߻�
	 * @throws NullPointerException				��ϵ��� ���� ���ҽ� �̸��� ��� �߻�
	 */
	public int getResourceIntValue(String name) throws ResourceFormatException, NullPointerException{
		String value = this._getResourceValue(name);
		
		try{
			return Integer.parseInt(value);
		}catch(Exception e){
			throw new ResourceFormatException("int ���� �ƴ� ���ҽ� ���� ��û [���̵�=" + name + ", ��=" + value + "]");
		}
	}
	
	/**
	 * ��û�� ���ҽ� ���� long ���·� ����
	 * @param name		���ҽ� �̸�
	 * @return				long ������ ��ȯ�� ���ҽ� ��
	 * @throws ResourceFormatException		���ҽ� ���� long ���� �ƴ� ��� �߻�
	 * @throws NullPointerException				��ϵ��� ���� ���ҽ� �̸��� ��� �߻�
	 */
	public long getResourceLongValue(String name) throws ResourceFormatException, NullPointerException{
		String value = this._getResourceValue(name);
		
		try{
			return Long.parseLong(value);
		}catch(Exception e){
			throw new ResourceFormatException("long ���� �ƴ� ���ҽ� ���� ��û [���̵�=" + name + ", ��=" + value + "]");
		}
	}
	
	/**
	 * ��û�� ���ҽ� ���� double ���·� ����
	 * @param name		���ҽ� �̸�
	 * @return				double ������ ��ȯ�� ���ҽ� ��
	 * @throws ResourceFormatException		���ҽ� ���� double ���� �ƴ� ��� �߻�
	 * @throws NullPointerException				��ϵ��� ���� ���ҽ� �̸��� ��� �߻�
	 */
	public double getResourceDoubleValue(String name) throws ResourceFormatException, NullPointerException{
		String value = this._getResourceValue(name);
		
		try{
			return Double.parseDouble(value);
		}catch(Exception e){
			throw new ResourceFormatException("double ���� �ƴ� ���ҽ� ���� ��û [���̵�=" + name + ", ��=" + value + "]");
		}
	}
	
	/**
	 * ��û�� ���ҽ� ���� float ���·� ����
	 * @param name		���ҽ� �̸�
	 * @return				float ������ ��ȯ�� ���ҽ� ��
	 * @throws ResourceFormatException		���ҽ� ���� float ���� �ƴ� ��� �߻�
	 * @throws NullPointerException				��ϵ��� ���� ���ҽ� �̸��� ��� �߻�
	 */
	public float getResourceFloatValue(String name) throws ResourceFormatException, NullPointerException{
		String value = this._getResourceValue(name);
		
		try{
			return Float.parseFloat(value);
		}catch(Exception e){
			throw new ResourceFormatException("float ���� �ƴ� ���ҽ� ���� ��û [���̵�=" + name + ", ��=" + value + "]");
		}
	}
	
	/**
	 * ���ҽ� ���� String ������ ����
	 * @param name		���ҽ� �̸�
	 * @return				���ҽ� ��
	 * @throws NullPointerException		��ϵ��� ���� ���ҽ��� ��� �߻�
	 */
	private String _getResourceValue(String name) throws NullPointerException{
		if( name == null )		throw new NullPointerException("���ҽ� ��û ���̵� ���� NULL");
		
		String value = this.resourceMap.get(name);
		if( value == null )		throw new NullPointerException("��ϵ��� ���� ���ҽ� ���̵� [" + name + "]");
		
		return value;
	}
	
	/**
	 * ��ϵ� ���ҽ��� �̸� ����� ����
	 * @return		���ҽ� �̸�
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
				System.err.println("���ҽ� ���� �˻� ���� (file:" + resource + ") " + e.getMessage());
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
				System.err.println("���ҽ� ������ �д� �� ����. " + e.getMessage());
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
	
	/*		�������� ���� �м�		*/
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
			System.err.println("���ҽ� ���� �м� ����. " + e.getMessage());
		}

	}
	
}
