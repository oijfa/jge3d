package Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.vecmath.Vector3f;

import jge3d.physics.Physics;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class Entity extends RigidBody{
	private String name = "";
	private String type = "";
	private int ttl=0;
	private long created_at = 0;
	private boolean transparent = false;
	private float alpha=1.0f;
	private boolean collidable;
	
	//publicly available keys
	protected static String[] keys = {"name","type","positionX","positionY","positionZ","gravityX","gravityY","gravityZ","mass","transparent","alpha","texture_name","collidable","size","TTL"};
	
	protected static int num_entities=0;
	
	public Entity() {
		super();
		num_entities++;
		name="ent" + num_entities;
		created_at = System.nanoTime();
	}
	
	public Entity(String _name) {
		super();
		num_entities++;
		name=_name;
		created_at = System.nanoTime();
	}

	/*	GETTERS	*/
	public String 	getName() 			{return name;}
	public String 	getType() 			{return type;}
	public boolean 	getCollidable() 	{return collidable;}
	public Boolean 	getTransparent() 	{return transparent;}
	public float 	getAlpha() 			{return alpha;}
	public int 		getTTL() 			{return ttl;}
	public Vector3f getGravity() {
		Vector3f v = null;
		getGravity(v);
		return v;
	}
	public Vector3f getPosition(){ 
		Vector3f v = null; 
		getCenterOfMassPosition(v); 
		return v;
	}
	
	public boolean isDead() {
		return ((System.currentTimeMillis() >= (created_at+ttl)) && (ttl != 0) );
	}

	/*	SETTERS	*/
	public void setName(String _name) {
		try {
			name=_name;
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for name, must be String\n");
		}
	}
	public void setType(Object _type) {
		try {
			type=_type.toString();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for type, must be String\n");
		}
	}
	public void setCollidable(boolean b){ collidable = b; }
	public void setPosition(Object p) {
		try {
			Vector3f pos = ((Vector3f) p);
			Transform trans = new Transform();
			float[] f = new float[3];
		
			this.getCenterOfMassTransform(trans);
			trans.setIdentity();
			
			f[0] = pos.x;
			f[1] = pos.y;
			f[2] = pos.z;
			
			trans.origin.set(f);
		} catch (Exception e) {
			System.out.print(p.toString() + "<<Incorrect data type for position, must be Vector3f\n");
			e.printStackTrace();
		}
	}
	public void setGravityX(Object x) {
		try {
			gravity.x = (float)Float.parseFloat(x.toString());
			updatePhysics();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Incorrect data type for gravityX, must be Float\n");
		}
		
	}
	public void setGravityY(Object y) {
		try {
			gravity.y = (float)Float.parseFloat(y.toString());
			updatePhysics();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for gravityY, must be Float\n");
		}
	}
	public void setGravityZ(Object z) {
		try {
			gravity.z = (float)Float.parseFloat(z.toString());
			updatePhysics();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for gravityZ, must be Float\n");
		}
	}
	
	public void setCollidable(Object _collidable) {
		try {
			collidable = Boolean.parseBoolean(_collidable.toString());
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for collidable, must be Boolean\n");
		}
	}
	public void setMass(Object _mass) {
		try {
			mass = Float.parseFloat(_mass.toString());
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for mass, must be Float\n");
		}
	}
	public void setDamping(Object lin_damping, Object ang_damping) {
		try {
			super.setDamping(Float.valueOf(lin_damping.toString()), Float.valueOf(ang_damping.toString()));
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for damping, must be Float,Float\n");
		}
	}
	public void setFriction(Object friction) {
		try {
			setFriction(Float.valueOf(friction.toString()));
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for friction, must be Float\n");
		}
	}

	public void setTTL(Integer _ttl) {
		try {
			ttl=Integer.valueOf(_ttl.toString());
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for TTL, must be Integer(seconds)\n");
		}
	}
	public void setTransparent(Boolean _transparent) {
		try {
			transparent=Boolean.parseBoolean(_transparent.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setAlpha(Float _alpha) {
		try {
			alpha=Float.parseFloat(_alpha.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	//Arbitrary functions
	//Needs replaced.
	public void set(String key, String value) {
		if(key.equals("name"))
			setName(value);
		else if(key.equals("type"))
        	setType(value);
        else if(key.equals("positionX"))
        	setPositionX(Float.valueOf(value));
    	else if(key.equals("positionY"))
    		setPositionY(Float.valueOf(value));
		else if(key.equals("positionZ"))
			setPositionZ(Float.valueOf(value));
        else if(key.equals("gravityX"))
        	setGravityX(Float.valueOf(value));
    	else if(key.equals("gravityY"))
    		setGravityY(Float.valueOf(value));
		else if(key.equals("gravityZ"))
			setGravityZ(Float.valueOf(value));
		else if(key.equals("mass"))
			setMass(Float.valueOf(value));
		else if(key.equals("transparent"))
			setTransparent(Boolean.valueOf(value));
		else if(key.equals("alpha"))
			setAlpha(Float.valueOf(value));
		else if(key.equals("texture_name"))
			setTexture_name(value);
		else if(key.equals("collidable"))		
			setCollidable(Boolean.valueOf(value));
		else if(key.equals("size"))
			setSize(Float.valueOf(value));
		else if(key.equals("TTL"))
			setTTL(Integer.valueOf(value));
		else {
			System.out.print("SSHHIIITTTTT!!!! Entity parsing error [" + key + "," + value + "]\n");
		}
	}
	
	public void deletePhysics() {Physics.getInstance().getDynamicsWorld().removeRigidBody(phys_body);}
	
	public static String[] getKeys() {return keys;}
	
	public String toString() {
		Class<?> c;
		//Field[] allFields = null;
		//String fname;
		String output = new String(); 
		try {
			c = Class.forName("jge3d.Entity");
			output="\tEntity=" + name + "\n";
			for(String key: keys) {
				String method_string = 
					"get" + 
					key.substring(0,1).toUpperCase() + 
					key.substring(1);

				Method m = c.getDeclaredMethod(method_string);
				output = output + "\t\t" + key + "=" + m.invoke(this).toString() + "\n";
			}
			output=output + "\t/Entity\n";
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return output;
	}
}
