package editor;
import java.awt.Color;


public class Block<E extends Number> {
	private Coordinate<E> position;
	private Color base_color;
	//TODO: Textures?
	
	public Coordinate<E> getCoordinate(){return position.clone();}
	public Color getColor(){return new Color(base_color.getRGB());}
	
	public void setPosition(Coordinate<E> position){
		this.position = position;
	}
	
	public void setColor(Color color){
		this.base_color = color;
	}
}
