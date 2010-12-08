package controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Queue;

import render.Renderer;

public class Controller {
	// the game always runs (except when it doesn't)
	private static boolean isRunning = true;
	private static Controller uniqueInstance = new Controller();
	private Queue<Command> controller_queue = new LinkedList<Command>();
	private static Controller controller;
	private Renderer renderer;
	private long frames = 0;

	public static void main(String[] args) {
		controller = new Controller();
		controller.start();
	}

	public Controller() {
		// controller_thread.start();
		// input_thread.start();
		// physics_thread.start();
	}

	private void start() {
		renderer = new Renderer();
		render_thread.start();
	}

	public void run_queue() {
		Command commandToRunCommand;
		try {
			for (int i = 0; i < controller_queue.size(); i++) {
				commandToRunCommand = controller_queue.poll();
				Method methodToInvoke = commandToRunCommand
						.getClassInstance()
						.getClass()
						.getDeclaredMethod(
								commandToRunCommand.getMethodToInvoke());
				methodToInvoke.invoke(commandToRunCommand.getClassInstance());
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		++frames;
	}

	public void monitor() {
		// if(LevelView.getInstance().getLoadLevel()) {
		// level.load();
		// System.out.println("You loaded the level\n");
		// }

		// Check if textureList has been altered since last frame
		// if(TextureList.getInstance().hasChanged()) {
		// try {
		// TextureList.getInstance().loadQueuedTexture();
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (LWJGLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

		// Check to make sure none of the entities are marked as dead
		// EntityList.getInstance().pruneEntities();

		// Update the world's physical layout
		// Physics.getInstance().clientUpdate();

		// Camera check versus player position
		// Camera.getInstance().moveToPlayerLocation(player);

		/*
		 * if(Controller.getInstance().hasQueuedItems()) {
		 * //Controller.getInstance().run_queue(); }
		 */

		// Draw world
		// Renderer.getInstance().draw();

		// FPSView.getInstance().updateFPS();

		// Here's the idea. Branch out, come back together. Input run twice for
		// every 1 render/physics run.
		// The functions we call in the thread will go, and then join back. We
		// wait for them to do so, run
		// our entity checks and process the queue, then throw out the thread
		// branches again.

		// Start the Render thread going
		// Start the Physics thread going
		// Start the Input thread going
		// Wait for the input thread to rejoin
		// Start it again
		// wait for the input thread to rejoin
		// wait for both the physics and render threads to rejoin

		// run_queue();

		// try {
		// check_entities();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (LWJGLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	// Create the Controller Listening thread
	Thread controller_thread = new Thread() {
		public void run() {
			while (isRunning) {
				Controller.getInstance().monitor();
			}
		}
	};

	// Create the Input Listening thread
	Thread input_thread = new Thread() {
		public void run() {
			while (isRunning) {
				// read keyboard and mouse
				// Input.getInstance().updateInput();
			}
		}
	};

	// Create the Physics Listening thread
	Thread physics_thread = new Thread() {
		public void run() {
			while (isRunning) {
				// Update the physics world
				// Physics.getInstance().clientUpdate();

				// Rejoin the controller thread
			}
		}
	};
	// };

	// Create the vidya thread
	Thread render_thread = new Thread() {
		public void run() {
			renderer.createRenderer();
			while (isRunning) {
				renderer.draw();
			}
		}
	};

	public static Controller getInstance() {
		return uniqueInstance;
	}

	public void priorityRun(Object classInstance, String methodToInvoke) {

	}

	public void enqueue(Object classInstance, String methodToInvoke) {
		controller_queue.add(new Command(classInstance, methodToInvoke));
	}

	public Boolean hasQueuedItems() {
		if (controller_queue.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	// private void check_entities() throws InterruptedException,
	// FileNotFoundException, LWJGLException, IOException {
	// if(EntityList.getInstance().getChanged().size() != 0)
	// {
	// EntityList.getInstance().getChanged().removeAll(EntityList.getInstance().getChanged());
	// //update the entity table if necessary
	// EntityComboBox.getInstance().update();
	//
	// //notify the renderer if a level entity changed
	// render_thread.wait();
	// Renderer.getInstance().makeLevelList();
	// render_thread.notify();
	// }
	// }

	public long getFrames() {
		return frames;
	}

	public void resetFrames() {
		frames = 0;
	}

	public static void quit() {
		isRunning = false;
	}

	public static boolean getRunning() {
		return isRunning;
	}
}
