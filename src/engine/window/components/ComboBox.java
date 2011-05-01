package engine.window.components;

import de.matthiasmann.twl.model.ListModel;
import de.matthiasmann.twl.model.SimpleChangableListModel;

public class ComboBox<E> extends de.matthiasmann.twl.ComboBox<Object>{
	private ComboBox<E> combobox = new ComboBox<E>();
	
	public void setLabel() {
		
	}
	
	public void addItem(Object item) {
		ListModel<Object> old_list_model = combobox.getModel();
		
		SimpleChangableListModel<Object> new_list_model = new SimpleChangableListModel<Object>();
		
		new_list_model.addElements(old_list_model);
		new_list_model.addElement(item);
		
		combobox.setModel(new_list_model);
	}
	
	public void removeItem(Object item) {
		ListModel<Object> old_list_model = combobox.getModel();
		
		SimpleChangableListModel<Object> new_list_model = new SimpleChangableListModel<Object>();
		
		new_list_model.addElements(old_list_model);
		
		for(int i=0; i<new_list_model.getNumEntries();i++) {
			if(new_list_model.getEntry(i).equals(item))
				new_list_model.removeElement(i);	
		}
		combobox.setModel(new_list_model);
	}
	
}
