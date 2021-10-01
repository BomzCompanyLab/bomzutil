package example.threadpool;

import kr.co.bomz.util.threadpool.BomzThread;

public class ThreadObject02 extends BomzThread{

	private String name;
	private String address;
	
	@Override
	protected void execute() throws Exception {
		System.out.println(
				"name=" + name + " , address=" + address + "."
			);
	}
	
	@Override
	protected void constructorParameter(Object ...param) throws Exception{
		this.name = param[0].toString();
	}

	@Override
	protected void executeParameter(Object ... param) throws Exception{
		this.address = param[0].toString();
	}
	
	@Override
	protected void close(){
		this.address = null;
	}
	
}
