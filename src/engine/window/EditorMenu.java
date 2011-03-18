package engine.window;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.swing.ImageIcon;

import org.lwjgl.LWJGLException;

import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.renderer.DynamicImage;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;

public class EditorMenu extends ResizableFrame {
	private final Label preview;

	public EditorMenu() {
		setTitle("Texture Editor");
		preview = new Label();
		preview.setBackground(loadImagePreview("resources/themes/widgets.png"));
		add(preview);
		
		//tree
		
		//add
		
		//remove
	}
	
	public DynamicImage loadImagePreview(String path) {
		try {
			//Create a temporary renderer instance
			LWJGLRenderer render;
			
				render = new LWJGLRenderer();
			
			//Read image from file
			ImageIcon icon = new ImageIcon(path);
		    Image imageicon = icon.getImage();
			
		    // Create empty BufferedImage, sized to Image
		    BufferedImage buffImage = new BufferedImage(
		    	imageicon.getWidth(null), 
		    	imageicon.getHeight(null), 
		        BufferedImage.TYPE_INT_ARGB
		    );

		    // Draw Image into BufferedImage
		    Graphics g = buffImage.getGraphics();
		    g.drawImage(imageicon, 0, 0, null);
		    
			int[] data = ((DataBufferInt)buffImage.getRaster().getDataBuffer()).getData(); 
			ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 4); 
			bb.order(ByteOrder.LITTLE_ENDIAN); 
			bb.asIntBuffer().put(data); 
			
			//Create a TWL dynamic image
			DynamicImage image = render.createDynamicImage(
				imageicon.getWidth(null),
				imageicon.getHeight(null)
			);
			
			//Copy contents of bytebuffered image into dynamic image
			image.update(bb,DynamicImage.Format.RGBA);
			
			//Set the label to the image
			return image;
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
