package example.threadpool;

import kr.co.bomz.util.threadpool.BomzThread;

public class ThreadObject01 extends BomzThread{

	private String msg;
	private int number;
	
	@Override
	protected void execute() throws Exception {
		System.out.println(
				( msg == null ? "no name " : msg ) +
				number +
				" thread call"
			);
	}

	@Override
	protected void executeParameter(Object ... param) throws Exception{
		if( param.length == 1 ){
			this.number = (Integer)param[0];
		}else if( param.length == 2 ){
			this.msg = param[0].toString();
			this.number = (Integer)param[1];
		}
	}
	
	@Override
	protected void close(){
		this.msg = null;
		this.number = 0;
	}
}
