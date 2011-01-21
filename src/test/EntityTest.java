package test;

import java.util.Random;

import javax.vecmath.Vector3f;

import physics.Physics;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.linearmath.DefaultMotionState;

import entity.Entity;

public class EntityTest extends Thread{
	static Entity testEnt;
	static Physics phy;
	@SuppressWarnings("unused")
	private String name;
	static Random rand;
	EntityTest(String name){
		super();
		this.name = name;
	}
	public static void main(String args[]) throws InterruptedException{
		phy = new Physics();
		rand = new Random(1000);
		CollisionShape boxShape = new BoxShape(new Vector3f(1, 1, 1));
		testEnt = new Entity(0, new DefaultMotionState(), boxShape, true);
		phy.addEntity(testEnt);
		
		EntityTest thread1 = new EntityTest("a");
		EntityTest thread2 = new EntityTest("b");
		
		thread1.start();
		thread2.start();
		
		for(int i = 0; i < 20000000; i++){
			phy.clientUpdate();
		}

	}
	
	public void run(){
		for(int i = 0; i < 10000; i++){
			testEnt.setPosition(new Vector3f(rand.nextInt(100),rand.nextInt(100),rand.nextInt(100)));
			testEnt.setMotionState(new DefaultMotionState());
		}
	}
}
