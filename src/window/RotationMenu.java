package window;

import input.components.ThreadFactory;

import java.util.HashMap;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.model.ButtonModel;
import entity.Camera;
import entity.EntityList;

public class RotationMenu extends ResizableFrame {
	private final DialogLayout layout;
	private final Button up;
	private final Button down;
	private final Button right;
	private final Button left;
	private final Button center;
	private Camera cam;
	private static final float LEFT_RIGHT_INC = 0.00000001f;
	private static final float UP_DOWN_INC = 0.1f;
	private HashMap<String, Thread> buttonThreads;
	private ThreadFactory threadStarter;
	
	public RotationMenu(EntityList objectList){
		setTitle("Camera Rotation");
		buttonThreads = new HashMap<String, Thread>();
		threadStarter = new ThreadFactory();
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
		
		//Create a model for reflecting the state of the button
		final ButtonModel leftButtonModel = left.getModel();
		
		leftButtonModel.addStateCallback(new Runnable() { 
		   @Override 
		   public void run() { 
			   if(leftButtonModel.isArmed()&&leftButtonModel.isPressed()){
				   System.out.println("pressed");
				   startThread("left");
			   }else if (!leftButtonModel.isArmed()&&!leftButtonModel.isPressed()){
				   System.out.println("released");
				   endThread("left");
			   }
		   } 
		}); 
		
		/*
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
				if(right.getModel().isHover()) {
					System.out.print("poopopopppooooppoooppop\n");
					cam.incrementRotation(LEFT_RIGHT_INC);
				}
			}
		});
		left.addCallback(new Runnable() {
			@Override
			public void run() {
				cam.incrementRotation(-LEFT_RIGHT_INC);
			}
		});
		*/
		center.addCallback(new Runnable() {
			@Override
			public void run() {
				cam.setDistance(15.0f);
				cam.setDeclination(0);
				cam.setRotation(0);
			}
		});
		
		
		Group row1 = layout.createSequentialGroup().addGap().addWidget(up).addGap();
		Group row2 = layout.createSequentialGroup().addWidget(left).addGap().addWidget(center).addGap().addWidget(right);
		Group row3 = layout.createSequentialGroup().addGap().addWidget(down).addGap();
		Group button_hgroup = layout.createParallelGroup()
			.addGroup(row1)
			.addGroup(row2)
			.addGroup(row3);
		
		Group col1 = layout.createSequentialGroup().addGap().addWidget(left).addGap();
		Group col2 = layout.createSequentialGroup().addWidget(up).addGap().addWidget(center).addGap().addWidget(down);
		Group col3 = layout.createSequentialGroup().addGap().addWidget(right).addGap();
		Group button_vgroup = layout.createParallelGroup()
			.addGroup(col1)
			.addGroup(col2)
			.addGroup(col3);
		
	layout.setHorizontalGroup(button_hgroup);
	layout.setVerticalGroup(button_vgroup);
	add(layout);		
	}
	
	public void startThread(String key){
		Runnable threadRun;
		Thread threadStart;
		if(key == "left"){
			threadRun = new Runnable(){
				public void run(){
					while(true){
						cam.incrementRotation(-LEFT_RIGHT_INC);
						System.out.print("DicksEverywhere!\n");
					}
				}
			};
			buttonThreads.put(key, threadStarter.newThread(threadRun));
			threadStart = buttonThreads.get(key);
			threadStart.start();
		}else if(key == "right"){
			threadRun = new Runnable(){
				public void run(){
					
				}
			};
			buttonThreads.put(key, threadStarter.newThread(threadRun));
		}else if(key == "up"){
			threadRun = new Runnable(){
				public void run(){
					
				}
			};
			buttonThreads.put(key, threadStarter.newThread(threadRun));
		}else if(key == "right"){
			threadRun = new Runnable(){
				public void run(){
					
				}
			};
			buttonThreads.put(key, threadStarter.newThread(threadRun));
		}else{
			System.out.println("Invalid Key");
		}
	}
	
	public void endThread(String key){
		Thread toDie;
		toDie = buttonThreads.get(key);
		toDie = null;
	}
	
	
	public void setCameraRef(Camera cam){
		this.cam = cam;
	}
}
