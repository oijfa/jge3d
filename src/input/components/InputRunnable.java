package input.components;

import java.lang.reflect.Method;

public class InputRunnable extends Thread{
	private long previousTime;
	private Method methodToRun;
	private Object objToUse;
	private Float increment;
	private volatile boolean shouldStop;
	
	public InputRunnable(String methodName, Object objToUse, Float inc) throws SecurityException, NoSuchMethodException{
		methodToRun = objToUse.getClass().getMethod(methodName, inc.getClass());
		this.objToUse = objToUse;
		increment = inc;
		shouldStop = false;
	}
	@Override
	public void run() {
		previousTime = System.nanoTime();
		while(!shouldStop){
			float inc = (System.nanoTime() - previousTime) * -increment;
			//System.out.println(inc);
			try {
				methodToRun.invoke(objToUse, inc);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			previousTime = System.nanoTime();
		}
	}
	
	public void end(){
		shouldStop = true;
	}
}
