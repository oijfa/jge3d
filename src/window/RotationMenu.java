package window;

import input.components.ButtonRunnable;
import input.components.InputRunnable;

import java.util.HashMap;

import javax.vecmath.Vector3f;

import controller.Config;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.DialogLayout.Group;
import entity.Camera;
import entity.Entity;
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
	Button explode = new Button();
	Button align = new Button();
	private Camera cam;
	private EntityList objectList;

	private static final double ZOOM_INC = 0.00000001f;
	//private static final double ZOOM_INC = 0.0000001f;
	private static final double LEFT_RIGHT_INC = 0.00000001f;
	private static final double UP_DOWN_INC = -0.00000001f;
	private HashMap<String, InputRunnable> buttonThreads;
	
	static boolean linearShow = false;
	
	public void setCameraRef(Camera cam){
		this.cam = cam;
		try {
			createThreads();
			//Create a models for reflecting the state of the buttons
			left.getModel().addStateCallback(new ButtonRunnable(this, "Left", left.getModel())); 
			right.getModel().addStateCallback(new ButtonRunnable(this, "Right", right.getModel()));
			up.getModel().addStateCallback(new ButtonRunnable(this, "Up", up.getModel()));
			down.getModel().addStateCallback(new ButtonRunnable(this, "Down", down.getModel()));
			right.getModel().addStateCallback(new ButtonRunnable(this, "Right", right.getModel()));
			zoomIn.getModel().addStateCallback(new ButtonRunnable(this, "zoomIn", zoomIn.getModel()));
			zoomOut.getModel().addStateCallback(new ButtonRunnable(this, "zoomOut", zoomOut.getModel()));
		} catch (Exception e) {
			System.out.println("Failed to create RotationMenu threads");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void setObjectList(EntityList objectList){
		this.objectList = objectList;
	}
	
	public RotationMenu() throws SecurityException, NoSuchMethodException{
		
		setTitle("Camera Rotation");
		
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
		explode.setTheme("explode");
		align.setTheme("align");

		center.addCallback(new Runnable() {
			@Override
			public void run() {
				cam.setDeclination(0.0);
				cam.setRotation(0.0);
			}
		});
		
		explode.addCallback(new Runnable() {
			@Override
			public void run() {
				for(String key:objectList.getKeySet()) {
					if(!key.equals(Camera.CAMERA_NAME)) {
						//Reset position of all other objects
						for(String name: objectList.getKeySet()) {
							//For everything but the camera do the following
							if(!name.equals(Camera.CAMERA_NAME)) {
								//Stop the movement
								objectList.getItem(name).setDamping(1.0f,1.0f);
								objectList.getItem(name).activate();
								objectList.getItem(name).setAngularFactor(0.0f, new Vector3f(0,0,0));
								
								//Move the object back to its original position
								objectList.getItem(name).setAngularIdentity();
								objectList.getItem(name).setPosition(Config.getPosition(name));
							}
						}
					}
				}
			}
		});
		
		align.addCallback(new Runnable() {
			@Override
			public void run() {
				for(String name: objectList.getKeySet()) {
					//For everything but the camera do the following
					if(!name.equals(Camera.CAMERA_NAME)) {
						//Stop the movement
						objectList.getItem(name).setDamping(1.0f,1.0f);
						objectList.getItem(name).activate();
						objectList.getItem(name).setAngularFactor(0.0f, new Vector3f(0,0,0));
						
						//Move the object back to its original position
						objectList.getItem(name).setAngularIdentity();
						objectList.getItem(name).setPosition(Config.getPosition(name));
					}
				}
				
				
				if(!linearShow){
					cam.focusOn(Config.getLineupFocus());
					cam.setDistance(9.0f);
					linearShow = true;
				}else{
					cam.focusOn(Config.getFullAssemblyFocus());
					linearShow = false;
					//System.out.println(Config.getFullAssemblyFocus().getProperty("name"));
				}
			}
		});
		
		Group row1 = layout.createSequentialGroup().addWidget(zoomIn).addGap().addWidget(up).addGap().addWidget(zoomOut);
		Group row2 = layout.createSequentialGroup().addWidget(left).addGap().addWidget(center).addGap().addWidget(right);
		Group row3 = layout.createSequentialGroup().addWidget(explode).addGap().addWidget(down).addGap().addWidget(align);
		Group button_hgroup = layout.createParallelGroup()
			.addGroup(row1)
			.addGroup(row2)
			.addGroup(row3);
		
		Group col1 = layout.createSequentialGroup().addWidget(zoomIn).addGap().addWidget(left).addGap().addWidget(explode);
		Group col2 = layout.createSequentialGroup().addWidget(up).addGap().addWidget(center).addGap().addWidget(down);
		Group col3 = layout.createSequentialGroup().addWidget(zoomOut).addGap().addWidget(right).addGap().addWidget(align);
		Group button_vgroup = layout.createParallelGroup()
			.addGroup(col1)
			.addGroup(col2)
			.addGroup(col3);

		layout.setHorizontalGroup(button_hgroup);
		layout.setVerticalGroup(button_vgroup);
	
		add(layout);	
	}
	
	public synchronized void createThreads() throws Exception{
		buttonThreads = new HashMap<String, InputRunnable>();
		resetThread("Left");
		resetThread("Right");
		resetThread("Up");
		resetThread("Down");
		resetThread("zoomIn");
		resetThread("zoomOut");
	}
	
	public synchronized void startThread(String key) throws SecurityException, NoSuchMethodException{
		if(buttonThreads.get(key) != null){
			buttonPressed();
			resetThread(key);
			buttonThreads.get(key).start();
		}else{
			resetThread(key);
		}
	}
	
	public synchronized  void stopThread(String key) throws SecurityException, NoSuchMethodException {
		buttonThreads.get(key).end();
	}
	
	public synchronized InputRunnable resetThread(String key) throws SecurityException, NoSuchMethodException{
		if( buttonThreads.get(key) != null && buttonThreads.get(key).isAlive() ){
			buttonThreads.get(key).end();
		}
		if(key.equals("Left")){
			buttonThreads.put("Left", new InputRunnable("incrementRotation",cam, -LEFT_RIGHT_INC));
		}else if(key.equals("Right")){
			buttonThreads.put("Right", new InputRunnable("incrementRotation",cam, LEFT_RIGHT_INC));
		}else if(key.equals("Up")){
			buttonThreads.put("Up", new InputRunnable("incrementDeclination",cam, UP_DOWN_INC));
		}else if(key.equals("Down")){
			buttonThreads.put("Down", new InputRunnable("incrementDeclination",cam, -UP_DOWN_INC));
		}else if(key.equals("zoomIn")){
			buttonThreads.put("zoomIn", new InputRunnable("incrementDistance",cam, ZOOM_INC));
		}else if(key.equals("zoomOut")){
			buttonThreads.put("zoomOut", new InputRunnable("incrementDistance",cam, -ZOOM_INC));
		}
		return buttonThreads.get(key);
	}
	
	public synchronized void buttonPressed() {
		Entity ent = ((Camera)objectList.getItem(Camera.CAMERA_NAME)).getFocus();
		ent.setAngularFactor(0, new Vector3f(0,0,0));
	}
}
