package input.components;

public class ThreadFactory {
	public ThreadFactory(){}
	
	public Thread newThread(Runnable runThread){
		return new Thread(runThread);
	}
}
