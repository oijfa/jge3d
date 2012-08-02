package engine.entity;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityIterator implements Iterator<Entity> {
	int index;
	EntityList entity_list;
	ArrayList<Object> key_set;
	
	@SuppressWarnings("unchecked")
	public EntityIterator(EntityList ent_list){
		index = 0;
		entity_list = ent_list;
		try{
			key_set = (ArrayList<Object>) entity_list.getKeySet();
		}catch(ClassCastException e){
			key_set = new ArrayList<Object>();
		}
	}
	
	@Override
	public boolean hasNext() {
		return index < key_set.size() - 1;
	}

	@Override
	public Entity next() {
		Object ret_key = key_set.get(index);
		index += 1;
		return entity_list.getItem(ret_key);
	}

	@Override
	public void remove() {
		Object del_key = key_set.get(index - 1);
		entity_list.removeEntity(del_key);
	}

}
