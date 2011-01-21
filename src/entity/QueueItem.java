package entity;

public class QueueItem {
	private Entity ent;
	private int action;
	
	public static final int REMOVE = 0;
	public static final int ADD = 1;
	
	public QueueItem(Entity ent, int action) {
		this.ent = ent;
		this.action = action;
	}
	public void setEnt(Entity ent){
		this.ent= ent;
	}
	public Entity getEnt(){
		return this.ent;
	}
	
	public void setAction(int act){
		action = act;
	}
	public int getAction(){
		return action;
	}
}
