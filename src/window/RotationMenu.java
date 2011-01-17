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
	private final Button in;
	private final Button out;
	private Camera cam;
	private static final float LEFT_RIGHT_INC = 0.00000001f;
	private static final float UP_DOWN_INC = 0.00000001f;
	private static final float IN_OUT_INC = 0.1f;
	private HashMap<String, Thread> buttonThreads;
	private ThreadFactory threadStarter;
	private volatile boolean leftAlive, rightAlive, upAlive, downAlive, inAlive, outAlive;
	
	public RotationMenu(EntityList objectList){
		setTitle("Camera Rotation");
		buttonThreads = new HashMap<String, Thread>();
		threadStarter = new ThreadFactory();
		cam = (Camera) objectList.getItem("camera");
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
		in = new Button("Zoom In");
		in.setTheme("in");
		out = new Button("Zoom Out");
		out.setTheme("out");
		
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
		
		final ButtonModel inButtonModel = in.getModel();
		inButtonModel.addStateCallback(new Runnable() {
			@Override
			public void run() {
				if(inButtonModel.isArmed()&&!inButtonModel.isPressed()){
					inAlive = true;
					startThread("in");
				}else if(!inButtonModel.isArmed()&&!inButtonModel.isPressed()){
					inAlive = false;
				}
			}
		});
		
		final ButtonModel outButtonModel = out.getModel();
		outButtonModel.addStateCallback(new Runnable() {
			@Override
			public void run() {
				if(outButtonModel.isArmed()&&!outButtonModel.isPressed()){
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
				cam.setDistance(2.0f);
				cam.setDeclination(0);
				cam.setRotation(0);
			}
		});
		
		//TWL layout to get all the buttons in a cross formation much like a compass.
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
	
	//Thread Starter function that creates 1(uno) thread per Button input.
	public void startThread(String key){
		Runnable threadRun;
		Thread threadStart;
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
			threadStart = buttonThreads.get(key);
			threadStart.start();
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
			threadStart = buttonThreads.get(key);
			threadStart.start();
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
			threadStart = buttonThreads.get(key);
			threadStart.start();
		}else if(key == "in"){
			threadRun = new Runnable(){
				public void run(){
					long previousTime;
					float increment;
					previousTime = System.nanoTime();
					//Increment is now based on how on a coefficient of time.
					while(inAlive){
						increment = (System.nanoTime() - previousTime) * -IN_OUT_INC;
						cam.incrementDistance(increment);
						previousTime = System.nanoTime();
					}
				}
			};
			//add button to threadStarter hash map
			buttonThreads.put(key, threadStarter.newThread(threadRun));
			//grab the newly created thread and start it.
			threadStart = buttonThreads.get(key);
			threadStart.start();
		}else if(key == "out"){
			threadRun = new Runnable(){
				public void run(){
					long previousTime;
					float increment;
					previousTime = System.nanoTime();
					//Increment is now based on how on a coefficient of time.
					while(outAlive){
						increment = (System.nanoTime() - previousTime) * IN_OUT_INC;
						cam.incrementDistance(increment);
						previousTime = System.nanoTime();
					}
				}
			};
			//add button to threadStarter hash map
			buttonThreads.put(key, threadStarter.newThread(threadRun));
			//grab the newly created thread and start it.
			threadStart = buttonThreads.get(key);
			threadStart.start();
		}else{
			System.out.println("Invalid Key");
		}
	}
	
	public void setCameraRef(Camera cam){
		this.cam = cam;
	}
}
