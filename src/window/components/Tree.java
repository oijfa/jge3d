package window.components;

import window.tree.EditFieldCellRenderer;
import window.tree.Model;
import window.tree.Node;
import window.tree.SpanRenderer;
import window.tree.SpanString;
import monitoring.Observer;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TreeTable;
import de.matthiasmann.twl.model.StringModel;
import de.matthiasmann.twl.model.TreeTableNode;
import entity.Entity;
import entity.EntityList;

public class Tree extends ScrollPane implements Observer {
    //private MyNode dynamicNode;
    int state;
    EntityList objectList;
    private Model model;
    
    public Tree() {
    	treeInit(null);
    }
    
    public Tree(EntityList objectList){
    	treeInit(objectList);
    }
    private void treeInit(EntityList objectList){
    	model = new Model();
    	if( objectList != null ){
	    	this.objectList = objectList;
	    	objectList.registerObserver(this);
	    	model.removeAll();
	    	this.createEntityListNode();
    	}
    	
        TreeTable t = new TreeTable(model);
        t.setTheme("/table");
        t.registerCellRenderer(SpanString.class, new SpanRenderer());
        t.registerCellRenderer(StringModel.class, new EditFieldCellRenderer());
        t.setDefaultSelectionManager();
        
        model.insert("qwer","1");
        createSubNode(model,"qwer","asdf");
        
        setContent(t);
        setTheme("/tableScrollPane");
    }
    
	@Override
	public void update(Object _name) {
		String name = (String)_name;
		Node n = findChildNode(name, model);
		if( n == null ){
			model.insert(name, "");
			n = findChildNode(name, model);
		}
		n.removeAll();
		createEntityNode(objectList.getItem(name), n);
		//model.removeAll();
		//this.createEntityListNode();
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
		String t = (String) ent.getProperty("name");
		if( !t.equals("camera") ){
			int i = 0;
		}
		for(String key : ent.getKeySet()){
			Object obj = ent.getProperty(key);
			//EditStringModel esm = new EditStringModel(key, obj.toString(), ent, starter);
			
			entityNode.insert(key, obj.toString());
		}
	}
	
	private void createSubNode(TreeTableNode node, String parent, String name) {
		Node insertTo = findChildNode(parent, node);
		if( insertTo != null ){
			insertTo.insert(name, "Value?");
		}
	}
    
    public void centerScrollPane() {
        updateScrollbarSizes();
        setScrollPositionX(getMaxScrollPosX()/2);
        setScrollPositionY(getMaxScrollPosY()/2);
    }
	
	private Node findChildNode(String nodeName, TreeTableNode findIn){
		Node child;
		//Loop through all children
		for(int i=0;i<findIn.getNumChildren();i++) {
			child = (Node) findIn.getChild(i);
			
			//If the first column has what we're looking for
			System.out.println(child.getData(0) + "==" +nodeName);
			if(child.getData(0).equals(nodeName)){
				//return it
				return child;
			}else{
				//else look through the child's children
				Node temp = findChildNode(nodeName, child);
				if( temp != null ){
					return temp;
				}
			}
		}
		return null;
	}
}