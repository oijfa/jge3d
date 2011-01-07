package window.components;

import window.tree.EditFieldCellRenderer;
import window.tree.EditStringModel;
import window.tree.Model;
import window.tree.Node;
import window.tree.SpanRenderer;
import window.tree.SpanString;
import monitoring.EntityListObserver;

import de.matthiasmann.twl.ScrollPane;

import de.matthiasmann.twl.TreeTable;

import de.matthiasmann.twl.model.StringModel;

import entity.Entity;
import entity.EntityList;

public class Tree extends ScrollPane implements Runnable, EntityListObserver {
    //private MyNode dynamicNode;
    int state;
    Node subNode;
    EntityList objectList;
    private Model model;
    public Tree() {
        Model m = new Model();
        
        Node a = m.insert("A", "1");
        a.insert("Aa", "2");
        a.insert("Ab", "3");
        a.insert("EditField", "Not anymore");
        Node b = m.insert("B", "4");
        b.insert("hurrtest", "lawl");
        m.insert(new SpanString("This is a very long string which will span into the next column.", 2), "Not visible");
        m.insert("This is a very long string which will be clipped.", "This is visible");

        
        TreeTable t = new TreeTable(m);
        t.setTheme("/table");
        t.registerCellRenderer(SpanString.class, new SpanRenderer());
        t.registerCellRenderer(StringModel.class, new EditFieldCellRenderer());
        t.setDefaultSelectionManager();

        setContent(t);
        setTheme("/tableScrollPane");
    }
    
    public Tree(EntityList objectList){
    	this.objectList = objectList;
    	//System.out.println("Tree Constructor");
    	model = new Model();
        TreeTable t = new TreeTable(model);
        t.setTheme("/table");
        t.registerCellRenderer(SpanString.class, new SpanRenderer());
        t.registerCellRenderer(StringModel.class, new EditFieldCellRenderer());
        t.setDefaultSelectionManager();

        setContent(t);
        setTheme("/tableScrollPane");
        this.update(this);
    }
    
	@Override
	public void update(Object starter) {
		if(starter != this){
			model.removeAll();
			this.entityListNode(starter);
			System.out.println("Tree Ran");
		}
	}
	
	public void entityListNode(Object starter){
		Node entityNode;
		for( String key : objectList.getKeySet()){
			Entity ent = objectList.getItem(key);
			entityNode = model.insert(ent.getProperty("name"), ent.getPosition().toString());
			System.out.println(ent.getProperty("name"));
			this.entityNode(ent, entityNode, starter);
		}
	}
	public void entityNode(Entity ent, Node entityNode, Object starter){
		for(String key : ent.getKeys()){
			Object obj = ent.getProperty(key);
			EditStringModel esm = new EditStringModel(key, obj.toString(), ent, starter);
			entityNode.insert(key, esm);
		}
	}
    public void run() {
    	
    }
    
    public void centerScrollPane() {
        updateScrollbarSizes();
        setScrollPositionX(getMaxScrollPosX()/2);
        setScrollPositionY(getMaxScrollPosY()/2);
    }
}