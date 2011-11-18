package engine.window.components;

import de.matthiasmann.twl.AnimationState;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.TextWidget;
import de.matthiasmann.twl.ThemeInfo;
import de.matthiasmann.twl.model.ButtonModel;
import de.matthiasmann.twl.model.SimpleButtonModel;
import de.matthiasmann.twl.renderer.AnimationState.StateKey;
import de.matthiasmann.twl.utils.TextUtil;

public class VoxelButton extends TextWidget {
	    public static final StateKey STATE_ARMED = StateKey.get("armed");
    public static final StateKey STATE_PRESSED = StateKey.get("pressed");
    public static final StateKey STATE_SELECTED = StateKey.get("selected");

    private final Runnable stateChangedCB;
    private ButtonModel model;
    private String themeText;
    private String text;
    private int mouseButton;

    public VoxelButton() {
        this(null, false, null);
    }

    public VoxelButton(ButtonModel model) {
        this(null, false, model);
    }

    /**
     * Creates a Button with a shared animation state
     *
     * @param animState the animation state to share, can be null
     */
    public VoxelButton(AnimationState animState) {
        this(animState, false, null);
    }

    /**
     * Creates a Button with a shared or inherited animation state
     *
     * @param animState the animation state to share or inherit, can be null
     * @param inherit true if the animation state should be inherited false for sharing
     */
    public VoxelButton(AnimationState animState, boolean inherit) {
        this(animState, inherit, null);
    }

    public VoxelButton(String text) {
        this(null, false, null);
        setText(text);
    }

    /**
     * Creates a Button with a shared animation state
     *
     * @param animState the animation state to share, can be null
     * @param model the button behavior model, if null a SimpleButtonModel is created
     */
    public VoxelButton(AnimationState animState, ButtonModel model) {
        this(animState, false, model);
    }

    /**
     * Creates a Button with a shared or inherited animation state
     *
     * @param animState the animation state to share or inherit, can be null
     * @param inherit true if the animation state should be inherited false for sharing
     * @param model the button behavior model, if null a SimpleButtonModel is created
     */
    public VoxelButton(AnimationState animState, boolean inherit, ButtonModel model) {
        super(animState, inherit);
        this.mouseButton = Event.MOUSE_LBUTTON;
        this.stateChangedCB = new Runnable() {
            public void run() {
                modelStateChanged();
            }
        };
        if(model == null) {
            model = new SimpleButtonModel();
        }
        setModel(model);
        setCanAcceptKeyboardFocus(true);
    }

    public ButtonModel getModel() {
        return model;
    }

    public void setModel(ButtonModel model) {
        if(model == null) {
            throw new NullPointerException("model");
        }
        boolean isConnected = getGUI() != null;
        if(this.model != null) {
            if(isConnected) {
                this.model.disconnect();
            }
            this.model.removeStateCallback(stateChangedCB);
        }
        this.model = model;
        this.model.addStateCallback(stateChangedCB);
        if(isConnected) {
            this.model.connect();
        }
        modelStateChanged();
        AnimationState as = getAnimationState();
        as.dontAnimate(STATE_ARMED);
        as.dontAnimate(STATE_PRESSED);
        as.dontAnimate(STATE_HOVER);
        as.dontAnimate(STATE_SELECTED);
    }

    @Override
    protected void widgetDisabled() {
        disarm();
    }

    @Override
    public void setEnabled(boolean enabled) {
        model.setEnabled(enabled);
    }

    public void addCallback(Runnable callback) {
        model.addActionCallback(callback);
    }

    public void removeCallback(Runnable callback) {
        model.removeActionCallback(callback);
    }

    public boolean hasCallbacks() {
        return model.hasActionCallbacks();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        updateText();
    }

    public int getMouseButton() {
        return mouseButton;
    }

    @Override
    protected void applyTheme(ThemeInfo themeInfo) {
        super.applyTheme(themeInfo);
        applyThemeButton(themeInfo);
    }

    protected void applyThemeButton(ThemeInfo themeInfo) {
        themeText = themeInfo.getParameterValue("text", false, String.class);
        updateText();
    }

    @Override
    protected void afterAddToGUI(GUI gui) {
        super.afterAddToGUI(gui);
        if(model != null) {
            model.connect();
        }
    }

    @Override
    protected void beforeRemoveFromGUI(GUI gui) {
        if(model != null) {
            model.disconnect();
        }
        super.beforeRemoveFromGUI(gui);
    }

    @Override
    public int getMinWidth() {
        return Math.max(super.getMinWidth(), getPreferredWidth());
    }

    @Override
    public int getMinHeight() {
        return Math.max(super.getMinHeight(), getPreferredHeight());
    }

    protected final void doCallback() {
        getModel().fireActionCallback();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if(!visible) {
            disarm();
        }
    }

    protected void disarm() {
        // disarm first to not fire a callback
        model.setHover(false);
        model.setArmed(false);
        model.setPressed(false);
    }

    void modelStateChanged() {
        super.setEnabled(model.isEnabled());
        AnimationState as = getAnimationState();
        as.setAnimationState(STATE_SELECTED, model.isSelected());
        as.setAnimationState(STATE_HOVER, model.isHover());
        as.setAnimationState(STATE_ARMED, model.isArmed());
        as.setAnimationState(STATE_PRESSED, model.isPressed());
    }

    void updateText() {
        if(text == null) {
            super.setCharSequence(TextUtil.notNull(themeText));
        } else {
            super.setCharSequence(text);
        }
        invalidateLayout();
    }

    @Override
    protected boolean handleEvent(Event evt) {
        if(evt.isMouseEvent()) {
            boolean hover = (evt.getType() != Event.Type.MOUSE_EXITED) && isMouseInside(evt);
            model.setHover(hover);
            model.setArmed(hover && model.isPressed());
        }
        if(!model.isEnabled()) {
            // don't process event for a disabled button (except hover above)
            return false;
        }
        switch (evt.getType()) {
        case MOUSE_BTNDOWN:
        	mouseButton = evt.getMouseButton();
            model.setPressed(true);
            model.setArmed(true);
            break;
        case MOUSE_BTNUP:
        	mouseButton = 0;
            model.setPressed(false);
            model.setArmed(false);
            break;
        case KEY_PRESSED:
            switch (evt.getKeyCode()) {
            case Event.KEY_RETURN:
            case Event.KEY_SPACE:
                if(!evt.isKeyRepeated()) {
                    model.setPressed(true);
                    model.setArmed(true);
                }
                return true;
            }
            break;
        case KEY_RELEASED:
            switch (evt.getKeyCode()) {
            case Event.KEY_RETURN:
            case Event.KEY_SPACE:
                model.setPressed(false);
                model.setArmed(false);
                return true;
            }
            break;
        case POPUP_OPENED:
            model.setHover(false);
            break;
        case MOUSE_WHEEL:
            // ignore mouse wheel
            return false;
        }
        // eat all mouse events
        return evt.isMouseEvent();
    }
}