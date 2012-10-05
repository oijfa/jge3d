package topdownshooter;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import org.lwjgl.opengl.Display;

import engine.Engine;
import engine.entity.*;

import engine.render.Model;
import engine.render.Shader;
import engine.render.model_pieces.Texture;
import engine.render.primitives.Box;
import engine.render.ubos.Light;
import engine.render.ubos.Lights;
import engine.render.ubos.Material;
import engine.stars.Stars;

public class Main {
	private Engine engine;
	
	private Player player;
	/*
	private Actor model;
	private Entity model2;
	private Entity model3;
	private Entity model4;
	private Entity model5;
	*/
	private Lights lights;
    private Light light;
    private Light light2;
	
	private Camera camera;
	
	//private LightMenu lightmenu;
	//private MaterialMenu materialmenu;
	private static boolean multiThreaded = true;
	
	public static void main(String args[]) {
		Main m = new Main();
		if(multiThreaded == true)
			m.runMultiThread();
		else
			m.runSingleThread();
	}

	public Main() {
		engine = new Engine();
			
		//Create a camera
		camera = engine.addCamera(1f, false, "box2");
		camera.setDistance(30f);
		addUBOsToDefaultShader();
		
		//Create the player
		player = new Player(
			"player", 
			10.0f, 
			0.5f, 
			(Model) engine.resource_manager.getResource("box","models"), 
			(Shader) engine.resource_manager.getResource("topdowndefault","shaders")
		);
		player = (Player) engine.addActor(player);
		player.setProperty(Entity.POSITION,new Vector3f(0, 0, 0));
		((Model)player.getProperty(Entity.MODEL)).setTexture(
			(Texture) engine.resource_manager.getResource("test1","textures")
		);
		((Model)player.getProperty(Entity.MODEL)).getCollisionShape().setLocalScaling(new Vector3f(5,5,5));

		camera.focusOn(player);
				
		//Make sure the player doesn't wander off the lefthand side of the screen
		Box left_limit = new Box(
			0,
			true,
			new Vector3f(1,10000,100)
			,(Shader) engine.resource_manager.getResource("topdowndefault","shaders")
		);
		left_limit.setProperty(Entity.NAME, "left_limit");
		left_limit.setProperty(Entity.POSITION, camera.getRayTo(0, Display.getHeight()/2));
		((Model)left_limit.getProperty(Entity.MODEL)).setTexture(
			(Texture) engine.resource_manager.getResource("test1","textures")
		);
		engine.addEntity(left_limit);
		
		//Make sure the player doesn't wander off the righthand side of the screen
		Box right_limit = new Box(
			0,
			true,
			new Vector3f(1,10000,100)
			,(Shader) engine.resource_manager.getResource("topdowndefault","shaders")
		);
		right_limit.setProperty(Entity.NAME, "right_limit");
		right_limit.setProperty(Entity.POSITION, camera.getRayTo(Display.getWidth(), Display.getHeight()/2));
		((Model)left_limit.getProperty(Entity.MODEL)).setTexture(
			(Texture) engine.resource_manager.getResource("test2","textures")
		);
		engine.addEntity(right_limit);
		
		for(int i=0;i<10;i++) {
			Box shitbox = new Box(
				1,
				true,
				new Vector3f(1,1,1)
				,(Shader) engine.resource_manager.getResource("topdowndefault","shaders")
			);
			shitbox.setProperty(Entity.NAME, "shitbox" + i);
			shitbox.setProperty(Entity.POSITION, new Vector3f(-5,i,-5));
			shitbox.setProperty(Entity.GRAVITY,new Vector3f(0, -1, 0));
			((Model)shitbox.getProperty(Entity.MODEL)).setTexture(
				(Texture) engine.resource_manager.getResource("test"+((i%2)+1),"textures")
			);
			engine.addEntity(shitbox);
		}
		
		//Make some parallax stars
		Stars stars = new Stars(engine,1000,10000,200,5,400);
		stars.getEntity().applyImpulse(new Vector3f(0,-100,0), new Vector3f(0,0,0));
		((Model)stars.getEntity().getProperty(Entity.MODEL)).setTexture(
			(Texture) engine.resource_manager.getResource("test1","textures")
		);
		
		//player.setScale(new Vector3f(1,1,1));
		//player.setFallSpeed(1);
		/*
		//Heat seeking box (if box makes contact with player then model5 explodes)
		model = engine.addActor("model1", 1.0f, "test", "default");
		model.setProperty(Entity.NAME, "model1");
		model.setProperty("position", new Vector3f(10, 15, 0));
		engine.addAIRoutine("model1", "followPlayer");
		model.addCollisionFunctions("explodeOnPlayerContact");
		model.setProperty("gravity", new Vector3f(0,0,0));

		//Box that moves to trigger box that launches pyramid
		model2 = engine.addEntity("model2", 1f, true, "export", "default");
		model2.setProperty("position",new Vector3f(-6, 0, 0));
		model2.applyImpulse(new Vector3f(2,0,0), new Vector3f(0,0,0));		

		//Trigger box that launches pyramid
		model3 = engine.addEntity("model3", 1f, false, "singlebox", "default");
		model3.setProperty("position",new Vector3f(0, 0, 0));
		//model3.getModel().setTransparent();
		model3.addCollisionFunctions("explode");

		//Pyramid that flies off when touched by model2
		model4 = engine.addEntity("model4", 1f, true, "test", "default");
		model4.setProperty(Entity.NAME, "model4");
		model4.setProperty("position",new Vector3f(0, 0, -6));
		
		//Just a legoman (explodes if missle collides with player)
		model5 = engine.addEntity("model5", 1f, true, "legoman", "default");
		model5.setProperty(Entity.NAME, "model5");
		model5.setProperty("position",new Vector3f(10, 0, 0));
		//model5.setScale(new Vector3f(0.1f,0.1f,0.1f));
		model5.setProperty("gravity",new Vector3f(0,0,0));
		*/
		/*
		//Test armadillo
		Entity armadillo = engine.addEntity("armadillo", 0f, true, "armadillo", "default");
		armadillo.setProperty(Entity.NAME, "armadillo");
		armadillo.setProperty("position",new Vector3f(0, 0, 0));
		armadillo.setScale(new Vector3f(0.01f,0.01f,0.01f));
		*/
		/*
		String name_to_use = "bunny";
		Entity test = engine.addEntity(name_to_use, 1f, true, name_to_use, "default");
		test.setProperty(Entity.NAME, name_to_use);
		test.setProperty("position",new Vector3f(0, 30, 0));
		test.setAngularFactor(0, new Vector3f(0,1,0));
		test.setScale(new Vector3f(100.00f,100.00f,100.00f));
		*/
		
		
		/*
		lightmenu = new LightMenu();
		lightmenu.setTheme("lightmenu");
		engine.addWindow(lightmenu, 400, 400);
		lightmenu.setName("Light Menu");
		materialmenu = new MaterialMenu();
		materialmenu.setTheme("materialmenu");
		engine.addWindow(materialmenu, 400, 400);
		materialmenu.setName("Material Menu");
		
		lightmenu.setLight(light);
		materialmenu.setMaterial(test.getModel().getMesh(0).getMaterial());
		*/
	}	
	
	//This function is just for testing; we'll need to set this stuff at the map level
	public void addUBOsToDefaultShader() {
		lights = new Lights();
		
		Shader shader = (Shader)engine.resource_manager.getResource("topdowndefault", "shaders");
		light = new Light(
			new Vector4f(10.0f,5.0f,0.0f,1.0f),
			new Vector4f(5.0f,5.0f,1.0f,255.0f),
			new Vector4f(5.0f,5.0f,1.0f,255.0f),
			new Vector4f(5.0f,5.0f,1.0f,255.0f),
			0.1f,
			1.0f,
			0.1f,
			new Vector3f(-1.0f,-1.0f,0.0f),
			1.0f,
			0.1f
		);
        light2 = new Light(
			new Vector4f(-10.0f,5.0f,0.0f,1.0f),
			new Vector4f(1.0f,5.0f,5.0f,255.0f),
			new Vector4f(1.0f,5.0f,5.0f,255.0f),
			new Vector4f(1.0f,5.0f,5.0f,255.0f),
			0.1f,
			1.0f,
			0.1f,
			new Vector3f(1.0f,-1.0f,0.0f),
			1.0f,
			1.0f
		);
        lights.add(light);
        lights.add(light2);
        
        shader.addUBO(lights);
    	shader.addUBO(camera.getMVPmatrix());
    	shader.addUBO(new Material());
	}

	public void runMultiThread() {
		engine.run();
	}
	
	public void runSingleThread() {
		while(!Display.isCloseRequested()){
			engine.renderOnce();
			engine.physicsOnce();
		}
	}
}
