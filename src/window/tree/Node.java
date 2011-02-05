package window.tree;

import java.util.ArrayList;

import de.matthiasmann.twl.model.AbstractTreeTableNode;
import de.matthiasmann.twl.model.TreeTableNode;

public class Node extends AbstractTreeTableNode {
    private Object str0;
    private Object str1;
	private String color;
    
    public Node(TreeTableNode parent, Object str0, Object str1) {
        super(parent);
        nodeInit(str0,str1);
    }
    
    public Node(TreeTableNode parent, Object str0, Object str1, String color) {
        super(parent);
        nodeInit(str0,str1);
        this.color = color;
    }
    
    private void nodeInit(Object str0, Object str1){
    	this.str0 = str0;
        this.str1 = str1;
        setLeaf(true);
    }
    
    public Node changeParent(Model parent){
    	//Get all children
		ArrayList<Node> children = new ArrayList<Node>();
		for(int j = 0; j < this.getNumChildren(); j++){
			children.add((Node) this.getChild(j));
		}
		
		//create the node
		Node n = parent.insert(this.str0, (String) this.str1);
		
		//insert its children
		createChildren(n, children);
		
		return n;
    }
    
    private void createChildren(TreeTableNode parent, ArrayList<Node> subNodes){
    	for(int i = 0; i < subNodes.size(); i++){
    		//Get all children
    		ArrayList<Node> children = new ArrayList<Node>();
    		for(int j = 0; j < subNodes.get(i).getNumChildren(); j++){
    			children.add((Node) subNodes.get(i).getChild(j));
    		}
    		
    		//create the node
    		Node n = ((Node)parent).insert(subNodes.get(i).str0, subNodes.get(i).str1);
    		
    		//insert its children
    		createChildren(n, subNodes.get(i).getChildren());
    	}
    }

    public Object getData(int column) {
        return (column == 0) ? str0 : str1;
    }

    public Node insert(Object str0, Object str1) {
        Node n = new Node(this, str0, str1);
        insertChild(n, getNumChildren());
        setLeaf(false);
        return n;
    }
    
    public Node insert(Object str0, Object str1, String color) {
        Node n = new Node(this, str0, str1, color);
        insertChild(n, getNumChildren());
        setLeaf(false);
        return n;
    }

    public void remove(String name){
    	for(int i=0; i < this.getNumChildren();i++) {
    		if(this.getChild(i).equals(name))
    			this.removeChild(i);
    	}	
    }

    public void removeAll() {
        removeAllChildren();
    }
    
    public ArrayList<Node> getChildren() {
		ArrayList<Node> children = new ArrayList<Node>();
		for(int j = 0; j < this.getNumChildren(); j++){
			children.add((Node) this.getChild(j));
		}
		return children;
	}

	public String getColor() {	return color; }
}
