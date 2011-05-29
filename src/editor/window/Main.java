package editor.window;

import engine.controller.Controller;

public class Main extends Controller {
	private static final long serialVersionUID = 1L;

	@Override
	public void initialize() {
		
		createCamera();
		
		//Adding windows back in (pretty fucked)
		//while(!renderer.isInitialized()) {}
		
		try {
			renderer.getWindow().addWindow(new GridWindow(5));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


