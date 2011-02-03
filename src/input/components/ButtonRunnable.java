package input.components;

import window.RotationMenu;

import de.matthiasmann.twl.model.ButtonModel;

public class ButtonRunnable implements Runnable{
	private final RotationMenu objToUse;
	private final String buttonName;
	private final ButtonModel buttonModel;
	
	public ButtonRunnable(RotationMenu objToUse, String buttonName, ButtonModel buttonModel) throws SecurityException, NoSuchMethodException{
		this.objToUse = objToUse;
		this.buttonName = buttonName;
		this.buttonModel = buttonModel;
	}
	
	@Override
	public void run() {
		if(buttonModel.isArmed()&&buttonModel.isPressed()){
			try {
				objToUse.startThread(buttonName);
			} catch (Exception e) {
				System.out.println("Couldn't start inputrunnable - " + buttonName);
				e.printStackTrace();
			}
		}else if(!buttonModel.isArmed()&&!buttonModel.isPressed()){
			try {
				objToUse.stopThread(buttonName);
			} catch (Exception e) {
				System.out.println("Couldn't stop inputrunnable - " + buttonName);
				e.printStackTrace();
			}
		}
	}
}
