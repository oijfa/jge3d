package editor;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.window.components.VoxelButton;

import java.util.ArrayList;

import de.matthiasmann.twl.Color;
import de.matthiasmann.twl.utils.TintAnimator;
import de.matthiasmann.twl.utils.TintAnimator.TimeSource;

public class Block<E extends Number> extends VoxelButton {
	private Coordinate<E> position;
	private Color base_color;
	private Boolean active = false;

	private ArrayList<ActionListener> action_listeners;

	// TODO: Textures?
	public Block() {
		super();
		setColor(new Color((byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x00));
		active = false;
		action_listeners = new ArrayList<ActionListener>();
		this.addCallback(new Callback(this));
	}

	public Coordinate<E> getCoordinate() {
		return position.clone();
	}

	public Color getColor() {
		if (base_color == null) {
			return new Color((byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF);
		} else {
			return new Color(base_color.toARGB());
		}
	}

	public void setPosition(Coordinate<E> position) {
		this.position = position;
	}

	public void setColor(Color color) {
		super.setTintAnimator(new TintAnimator(new TimeSource() {
			@Override
			public void resetTime() {
			}

			@Override
			public int getTime() {
				return 0;
			}
		}));

		if (color != null) {
			this.base_color = color;
			this.getTintAnimator().setColor(base_color);
			active = true;
		} else {
			this.getTintAnimator().setColor(
				new Color(
					(byte) 0xFF,
					(byte) 0xFF,
					(byte) 0xFF,
					(byte) 0x00
				)
			);
			this.base_color = null;
			active = false;
		}
	}

	public void fireActionEvent() {
		for (ActionListener l : action_listeners) {
			l.actionPerformed(new ActionEvent(this));
		}
	}

	public void addActionListener(ActionListener listener) {
		action_listeners.add(listener);
	}

	private class Callback implements Runnable {
		Block<E> owner;

		public Callback(Block<E> owner) {
			this.owner = owner;
		}

		@Override
		public void run() {
			owner.fireActionEvent();
		}
	}

	public Boolean getActive() {
		return active;
	}
	
	public String toString() {
		String converted_string = new String();
		
		if(base_color != null) {
		converted_string = "\n\t\t<red>" + (base_color.getR() & 0xff) + "</red>" +
				"\n\t\t<green>" + (base_color.getG() & 0xff) + "</green>" +
				"\n\t\t<blue>" + (base_color.getB() & 0xff) + "</blue>\n";
		} else {
			converted_string = "\n\t\tNot set.\n";
		}
		return converted_string;
	}
}
