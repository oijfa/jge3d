package engine.utils;

import java.util.ArrayList;
import java.util.List;

public class StringTree {
	int levels;
	
	public StringTree(){
		levels = 0;
	}
	
	public void addNode(int level, String parent, String value){
		
	}
	
	class Node{
		List<Node> children;
		String value;
		
		public Node(String val){
			value = val;
			children = new ArrayList<Node>();
		}
	}
}
