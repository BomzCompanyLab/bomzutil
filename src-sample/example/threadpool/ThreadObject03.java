package example.threadpool;

import kr.co.bomz.util.threadpool.BomzThread;

public class ThreadObject03 extends BomzThread{

	private String name;
	
	@Override
	protected void execute() throws Exception {
		System.out.println("name is " + name + "."	);
		try{		Thread.sleep(4000);		}catch(Exception e){}
	}
	
	@Override
	protected void constructorParameter(Object ...param) throws Exception{
		this.name = param[0].toString();
	}

}
