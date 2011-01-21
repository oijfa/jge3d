package window.components;

import window.tree.EditFieldCellRenderer;
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

public class Tree extends ScrollPane implements EntityListObserver {
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
    	objectList.registerObserver(this);
    	model = new Model();
        TreeTable t = new TreeTable(model);
        t.setTheme("/table");
        t.registerCellRenderer(SpanString.class, new SpanRenderer());
        t.registerCellRenderer(StringModel.class, new EditFieldCellRenderer());
        t.setDefaultSelectionManager();
        
        model.insert("qwer","1");
        createFolder(model,"qwer","asdf");
        
        setContent(t);
        setTheme("/tableScrollPane");
        //this.update();
    }
    
	@Override
	public void update() {
		model.removeAll();
		this.createEntityListNode();
	}
	
	public void createEntityListNode(){
		Node entityNode;
		for( String key : objectList.getKeySet()){
			Entity ent = objectList.getItem(key);
			entityNode = model.insert(ent.getProperty("name"), ent.getPosition().toString());
			this.createEntityNode(ent, entityNode);
		}
	}
	
	public void createEntityNode(Entity ent, Node entityNode){
		for(String key : ent.getKeySet()){
			Object obj = ent.getProperty(key);
			//EditStringModel esm = new EditStringModel(key, obj.toString(), ent, starter);
			entityNode.insert(key, obj.toString());
		}
	}
	
	@SuppressWarnings("unused")
	private void createFolder(Node node, String parent, String name) {
		Node child;
		for(int i=0;i<node.getNumChildren();i++) {
			child = (Node) node.getChild(i);
			System.out.print(child.getData(0));
			if(child.getData(0) == parent)
				child.insert(name, "");
			else
				createFolder(node,parent,name);
		}
	}
	private void createFolder(Model node, String parent, String name) {
		Node child;
		for(int i=0;i<node.getNumChildren();i++) {
			child = (Node) node.getChild(i);
			if(child.getData(0) == parent)
				child.insert(name, "");
			else
				createFolder(node,parent,name);
		}
	}
    
    public void centerScrollPane() {
        updateScrollbarSizes();
        setScrollPositionX(getMaxScrollPosX()/2);
        setScrollPositionY(getMaxScrollPosY()/2);
    }

	public void init() {	
		model.removeAll();
		this.createEntityListNode();
	}
}