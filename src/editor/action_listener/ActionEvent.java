package editor.action_listener;

public class ActionEvent {
	Object source;

	public ActionEvent(Object source) {
		this.source = source;
	}

	public Object getSource() {
		return source;
	}
}
