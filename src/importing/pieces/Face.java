package importing.pieces;

import java.util.ArrayList;

public class Face {
	private ArrayList<Integer> pref;
	private ArrayList<Integer> nref;
	private int material = 0;

	Face()
	{
		pref = new ArrayList<Integer>();
		nref = new ArrayList<Integer>();
	}
	
	Face(ArrayList<Integer> _pref, ArrayList<Integer> _nref, int _material) {
		pref = _pref;
		nref = _nref;
		material = _material;
	}
	
	public ArrayList<Integer> getPointRefs() {
		return pref;
	}
	
	public ArrayList<Integer> getNormalRefs() {
		return nref;
	}

	public int getMaterial() {
		return material;
	}
	
	public void addPoint(int reference)
	{
		pref.add(reference);
	}
	
	public void addNormal(int reference)
	{
		nref.add(reference);
	}
	
	public void setMaterial(int reference)
	{
		material = reference;
	}
	
}
