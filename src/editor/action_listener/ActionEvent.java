package editor.action_listener;

public class ActionEvent {
	private Object source;
	private String action;

	public ActionEvent(Object source) {
		this.source = source;
	}
	
	public ActionEvent(Object source, String action) {
		this.source = source;
		this.action = action;
	}

	public Object getSource() {
		return source;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
}
