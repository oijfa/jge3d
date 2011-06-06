package editor.window;

import de.matthiasmann.twl.ResizableFrame;
import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.window.components.ComboBox;

public class LayerMenu extends ResizableFrame implements ActionListener {
	private ComboBox<Integer> layer_cb;
	
	public LayerMenu() {
		setTitle("Layer Menu");
		
		layer_cb = new ComboBox<Integer>();
		
		layer_cb.setTheme("layer_cb");
		
		add(layer_cb);
	}

	public void populateLayers(Integer num_layers) {
		layer_cb.removeAllItems();
		for(int i=1; i <= num_layers; i++) {
			layer_cb.addItem(i);
		}
	}
	

	public void addCellListener(ActionListener listener){
	    layer_cb.addActionListener(listener);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(((ComboBox<Integer>) e.getSource()).getSelected());
	}
}
