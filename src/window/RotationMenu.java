package window;

import input.components.ThreadFactory;
import java.util.HashMap;
import java.util.Random;

import javax.vecmath.Vector3f;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.model.ButtonModel;
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

	private static final float ZOOM_INC = 0.01f;
	private static final float LEFT_RIGHT_INC = 0.00000001f;
	private static final float UP_DOWN_INC = 0.00000001f;
	private HashMap<String, Thread> buttonThreads;
	private ThreadFactory threadStarter;
	private volatile boolean leftAlive, rightAlive, upAlive, downAlive, inAlive, outAlive;
	
	
	public RotationMenu(){
		setTitle("Camera Rotation");
		buttonThreads = new HashMap<String, Thread>();
		threadStarter = new ThreadFactory();
		leftAlive = false;
		rightAlive = false;
		upAlive = false;
		downAlive = false;
		inAlive = false;
		outAlive = false;
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
		
		//Create a models for reflecting the state of the buttons
		final ButtonModel leftButtonModel = left.getModel();
		leftButtonModel.addStateCallback(new Runnable() { 
		   @Override 
		   public void run() { 
			   if(leftButtonModel.isArmed()&&leftButtonModel.isPressed()){
				   leftAlive = true;
				   startThread("left");
			   }else if (!leftButtonModel.isArmed()&&!leftButtonModel.isPressed()){
				   leftAlive = false;
			   }
		   } 
		}); 
		
		final ButtonModel rightButtonModel = right.getModel();
		rightButtonModel.addStateCallback(new Runnable() { 
		   @Override 
		   public void run() { 
			   if(rightButtonModel.isArmed()&&rightButtonModel.isPressed()){
				   rightAlive = true;
				   startThread("right");
			   }else if (!rightButtonModel.isArmed()&&!rightButtonModel.isPressed()){
				   rightAlive = false;
			   }
		   } 
		}); 
		
		final ButtonModel upButtonModel = up.getModel();
		upButtonModel.addStateCallback(new Runnable() { 
		   @Override 
		   public void run() { 
			   if(upButtonModel.isArmed()&&upButtonModel.isPressed()){
				   upAlive = true;
				   startThread("up");
			   }else if (!upButtonModel.isArmed()&&!upButtonModel.isPressed()){
				   upAlive = false;
			   }
		   } 
		}); 
		
		final ButtonModel downButtonModel = down.getModel();
		downButtonModel.addStateCallback(new Runnable() { 
		   @Override 
		   public void run() { 
			   if(downButtonModel.isArmed()&&downButtonModel.isPressed()){
				   downAlive = true;
				   startThread("down");
			   }else if (!downButtonModel.isArmed()&&!downButtonModel.isPressed()){
				   downAlive = false;
			   }
		   } 
		}); 
		
		final ButtonModel inButtonModel = zoomIn.getModel();
		inButtonModel.addStateCallback(new Runnable() {
			@Override
			public void run() {
				if(inButtonModel.isArmed()&&inButtonModel.isPressed()){
					inAlive = true;
					startThread("in");
				}else if(!inButtonModel.isArmed()&&!inButtonModel.isPressed()){
					inAlive = false;
				}
			}
		});
		
		final ButtonModel outButtonModel = zoomOut.getModel();
		outButtonModel.addStateCallback(new Runnable() {
			@Override
			public void run() {
				if(outButtonModel.isArmed()&&outButtonModel.isPressed()){
					outAlive = true;
					startThread("out");
				}else if(!outButtonModel.isArmed()&&!outButtonModel.isPressed()){
					outAlive = false;
				}
			}
		});


		//Center doesn't need continuous input capabilities, sad little center button :(
		center.addCallback(new Runnable() {
			@Override
			public void run() {
				cam.setDeclination(0);
				cam.setRotation(0);
			}
		});
		
		explode.addCallback(new Runnable() {
			@Override
			public void run() {
				for(String key:objectList.getKeySet()) {
					Entity ent = objectList.getItem(key);
					if(!key.equals(Camera.CAMERA_NAME)) {
						Random rand = new Random();
						Vector3f force = new Vector3f(
							rand.nextFloat()*1000,
							rand.nextFloat()*1000,
							rand.nextFloat()*1000
						);
						ent.applyImpulse(force, ent.getPosition());
					}
				}
			}
		});
		
		align.addCallback(new Runnable() {
			@Override
			public void run() {
				int pos_x=0;
				for(String key:objectList.getKeySet()) {
					Entity ent = objectList.getItem(key);
					if(!key.equals(Camera.CAMERA_NAME)) {
						pos_x += 2;
						ent.setPosition(new Vector3f(pos_x,0,0));
					}
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
	
	//Thread Starter function that creates 1(uno) thread per Button input.
	public void startThread(String key){
		Runnable threadRun;
		//if statements checks to see which button's thread needs to be invoked.
		if(key == "left"){
			threadRun = new Runnable(){
				public void run(){
					long previousTime;
					float increment;
					previousTime = System.nanoTime();
					//Increment is now based on how on a coefficient of time.
					while(leftAlive){
						increment = (System.nanoTime() - previousTime) * -LEFT_RIGHT_INC;
						cam.incrementRotation(increment);	
						previousTime = System.nanoTime();
					}
					return;
				}
			};
			//add button to threadStarter hash map
			buttonThreads.put(key, threadStarter.newThread(threadRun));
			//grab the newly created thread and start it.
			buttonThreads.get(key).start();
		}else if(key == "right"){
			threadRun = new Runnable(){
				public void run(){
					long previousTime;
					float increment;
					previousTime = System.nanoTime();
					//Increment is now based on how on a coefficient of time.
					while(rightAlive){
						increment = (System.nanoTime() - previousTime) * LEFT_RIGHT_INC;
						cam.incrementRotation(increment);
						previousTime = System.nanoTime();
					}
				}
			};
			//add button to threadStarter hash map
			buttonThreads.put(key, threadStarter.newThread(threadRun));
			//grab the newly created thread and start it.
			buttonThreads.get(key).start();
		}else if(key == "up"){
			threadRun = new Runnable(){
				public void run(){
					long previousTime;
					float increment;
					previousTime = System.nanoTime();
					//Increment is now based on how on a coefficient of time.
					while(upAlive){
						increment = (System.nanoTime() - previousTime) * UP_DOWN_INC;
						cam.incrementDeclination(increment);
						previousTime = System.nanoTime();
					}
				}
			};
			//add button to threadStarter hash map
			buttonThreads.put(key, threadStarter.newThread(threadRun));
			//grab the newly created thread and start it.
			buttonThreads.get(key).start();
		}else if(key == "down"){
			threadRun = new Runnable(){
				public void run(){
					long previousTime;
					float increment;
					previousTime = System.nanoTime();
					//Increment is now based on how on a coefficient of time.
					while(downAlive){
						increment = (System.nanoTime() - previousTime) * -UP_DOWN_INC;
						cam.incrementDeclination(increment);
						previousTime = System.nanoTime();
					}
				}
			};
			//add button to threadStarter hash map
			buttonThreads.put(key, threadStarter.newThread(threadRun));
			//grab the newly created thread and start it.
			buttonThreads.get(key).start();
		}else if(key == "in"){
			threadRun = new Runnable(){
				public void run(){
					long previousTime;
					float increment;
					previousTime = System.nanoTime()/1000000;
					//Increment is now based on how on a coefficient of time.
					while(inAlive){
						increment = (System.nanoTime()/1000000 - previousTime) * -ZOOM_INC;
						cam.incrementDistance(increment);
						previousTime = System.nanoTime()/1000000;
					}
				}
			};
			//add button to threadStarter hash map
			buttonThreads.put(key, threadStarter.newThread(threadRun));
			//grab the newly created thread and start it.
			buttonThreads.get(key).start();
		}else if(key == "out"){
			threadRun = new Runnable(){
				public void run(){
					long previousTime;
					float increment;
					previousTime = System.nanoTime()/1000000;
					//Increment is now based on how on a coefficient of time.
					while(outAlive){
						increment = (System.nanoTime()/1000000 - previousTime) * ZOOM_INC;
						cam.incrementDistance(increment);
						previousTime = System.nanoTime()/1000000;
					}
				}
			};
			//add button to threadStarter hash map
			buttonThreads.put(key, threadStarter.newThread(threadRun));
			//grab the newly created thread and start it.
			buttonThreads.get(key).start();
		}else{
			System.out.println("Invalid Key");
		}
	}
	
	public void setCameraRef(Camera cam){
		this.cam = cam;
	}
	
	public void setObjectList(EntityList objectList){
		this.objectList = objectList;
	}
}
