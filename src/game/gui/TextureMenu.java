package game.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.swing.ImageIcon;

import org.lwjgl.LWJGLException;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.DynamicImage;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import engine.window.components.Tree;

public class TextureMenu extends ResizableFrame {
	private final Widget preview;
	private final Button add_button, remove_button;
	private final Tree textree;
	private final DialogLayout layout;

	public TextureMenu() {
		setTitle("Texture Editor");

		add_button = new Button("add");
		add_button.setTheme("add");
		remove_button = new Button("remove");
		remove_button.setTheme("remove");
		preview = new Widget();
		// preview.setTheme("preview");
		textree = new Tree(null, null);
		textree.setTheme("textree");

		preview.setBackground(loadImagePreview("resources/themes/widgets.png"));

		layout = new DialogLayout();
		Group button_hgroup = layout
			.createSequentialGroup()
			.addGap()
			.addGroup(
				layout.createParallelGroup(preview, textree, add_button,
					remove_button)).addGap();

		// Group for holding the Vertical alignment of the buttons
		Group button_vgroup = layout.createSequentialGroup().addGap()
			.addWidget(preview).addWidget(textree).addWidget(add_button)
			.addWidget(remove_button).addGap();

		// All Dialog layout groups must have both a HorizontalGroup and
		// VerticalGroup
		// Otherwise "incomplete" exception is thrown and layout is not applied
		layout.setHorizontalGroup(button_hgroup);
		layout.setVerticalGroup(button_vgroup);

		// Make sure to add the layout to the frame
		add(layout);
		// !!! END EXAMPLE !!!//
	}

	public DynamicImage loadImagePreview(String path) {
		try {
			// Create a temporary renderer instance
			LWJGLRenderer render = new LWJGLRenderer();

			// Read image from file
			ImageIcon icon = new ImageIcon(path);
			Image imageicon = icon.getImage();

			// Create empty BufferedImage, sized to Image
			BufferedImage buffImage = new BufferedImage(
				imageicon.getWidth(null), imageicon.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);

			// Draw Image into BufferedImage
			Graphics g = buffImage.getGraphics();
			g.drawImage(imageicon, 0, 0, null);

			int[] data = ((DataBufferInt) buffImage.getRaster().getDataBuffer())
				.getData();
			ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 4);
			bb.order(ByteOrder.LITTLE_ENDIAN);
			bb.asIntBuffer().put(data);

			// Create a TWL dynamic image
			DynamicImage image = render.createDynamicImage(
				imageicon.getWidth(null), imageicon.getHeight(null));

			// Copy contents of bytebuffered image into dynamic image
			image.update(bb, DynamicImage.Format.BGRA);

			// Set the label to the image
			return image;
		} catch ( LWJGLException e ) {
			e.printStackTrace();
		}

		return null;
	}

}
