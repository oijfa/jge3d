package window;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.Widget;
import entity.Camera;
import entity.EntityList;

public class RotationMenu extends ResizableFrame {
	private final DialogLayout layout;
	private final Button up;
	private final Button down;
	private final Button right;
	private final Button left;
	private final Button center;
	private final Button zoomIn;
	private final Button zoomOut;
	private EntityList objectList;
	private Camera cam;
	private static final float LEFT_RIGHT_INC = 0.1f;
	private static final float UP_DOWN_INC = 0.1f;
	private static final float ZOOM_INC = 1f;

	
	public RotationMenu(EntityList objectList){
		setTitle("Camera Rotation");
		this.objectList = objectList;
		cam = (Camera) objectList.getItem("camera");
		layout = new DialogLayout();
		up= new Button("Up");
		up.setTheme("up");
		down = new Button("Down");
		down.setTheme("down");
		right = new Button("Right");
		right.setTheme("right");
		left = new Button("Left");
		left.setTheme("left");
		center = new Button("Center");
		center.setTheme("center");
		zoomIn = new Button("+");
		zoomIn.setTheme("zoomin");
		zoomOut = new Button("-");
		zoomOut.setTheme("zoomout");
		
		up.addCallback(new Runnable() {
			@Override
			public void run() {
				cam.incrementDeclination(UP_DOWN_INC);
			}
		});
		down.addCallback(new Runnable() {
			@Override
			public void run() {
				cam.incrementDeclination(-UP_DOWN_INC);
			}
		});
		right.addCallback(new Runnable() {
			@Override
			public void run() {
				cam.incrementRotation(LEFT_RIGHT_INC);
			}
		});
		left.addCallback(new Runnable() {
			@Override
			public void run() {
				cam.incrementRotation(-LEFT_RIGHT_INC);
			}
		});
		zoomIn.addCallback(new Runnable() {
			@Override
			public void run() {
				cam.incrementDistance(-1 * ZOOM_INC);
			}
		});
		zoomOut.addCallback(new Runnable() {
			@Override
			public void run() {
				cam.incrementDistance(ZOOM_INC);
			}
		});
		center.addCallback(new Runnable() {
			@Override
			public void run() {
				cam.setDistance(15.0f);
				cam.setDeclination(0);
				cam.setRotation(0);
			}
		});
		
		Button blank = new Button();
		Button blank2 = new Button();
		Group row1 = layout.createSequentialGroup().addWidget(zoomIn).addGap().addWidget(up).addGap().addWidget(zoomOut);
		Group row2 = layout.createSequentialGroup().addWidget(left).addGap().addWidget(center).addGap().addWidget(right);
		Group row3 = layout.createSequentialGroup().addWidget(blank).addGap().addWidget(down).addGap().addWidget(blank2);
		Group button_hgroup = layout.createParallelGroup()
			.addGroup(row1)
			.addGroup(row2)
			.addGroup(row3);
		
		Group col1 = layout.createSequentialGroup().addWidget(zoomIn).addGap().addWidget(left).addGap().addWidget(blank);
		Group col2 = layout.createSequentialGroup().addWidget(up).addGap().addWidget(center).addGap().addWidget(down);
		Group col3 = layout.createSequentialGroup().addWidget(zoomOut).addGap().addWidget(right).addGap().addWidget(blank2);
		Group button_vgroup = layout.createParallelGroup()
			.addGroup(col1)
			.addGroup(col2)
			.addGroup(col3);
		
	layout.setHorizontalGroup(button_hgroup);
	layout.setVerticalGroup(button_vgroup);

	add(layout);

		
	}
	
	public void setCameraRef(Camera cam){
		this.cam = cam;
	}
}
