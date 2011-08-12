package editor.window;

import java.util.ArrayList;

import de.matthiasmann.twl.FileSelector;
import de.matthiasmann.twl.FileTable.Entry;
import de.matthiasmann.twl.ResizableFrame;
import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;

public class FileMenu  extends ResizableFrame implements ActionListener {
	private FileSelector file_selector;
	private ArrayList<ActionListener> action_listeners;

	public FileMenu() {
		action_listeners = new ArrayList<ActionListener>();
		setTitle("Layer Menu");

		file_selector = new FileSelector();
		file_selector.setTheme("filechooser");
		//TODO: need a backing class to handle this
		//file_selector.addActionListener(this);

		add(file_selector);
		
		setSize(300, 150);
	}

	public String getSelection() {
		Entry entries[] = file_selector.getFileTable().getSelection();
		return entries[0].getPath();
	}

	public void addActionListener(ActionListener listener) {
		action_listeners.add(listener);
	}

	private void fireActionEvent() {
		for (ActionListener ae : action_listeners) {
			ae.actionPerformed(new ActionEvent(this));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		fireActionEvent();
	}
}
