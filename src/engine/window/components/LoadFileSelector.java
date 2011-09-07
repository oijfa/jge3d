package engine.window.components;

import de.matthiasmann.twl.FileSelector;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.PopupWindow;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.FileSystemModel;
import de.matthiasmann.twl.model.FileSystemModel.FileFilter;
import de.matthiasmann.twl.model.JavaFileSystemModel;
import java.io.File;
import java.util.prefs.Preferences;

public class LoadFileSelector {

    public interface Callback {
        public void fileSelected(File file);
        public void canceled();
    }

    final Callback callback;
    final FileSelector fileSelector;
    final PopupWindow popupWindow;
    final ExtFilter file_selector;

    public LoadFileSelector(Widget owner, Preferences prefs, String prefsKey,
            String description, Callback callback, String... extensions ) {
        this.callback = callback;

        if(callback == null) {
            throw new NullPointerException("callback");
        }
        
        file_selector = new ExtFilter(extensions);

        FileSelector.NamedFileFilter filter = new FileSelector.NamedFileFilter(description,file_selector);

        fileSelector = new FileSelector(prefs, prefsKey);
        fileSelector.setFileSystemModel(JavaFileSystemModel.getInstance());
        fileSelector.setAllowMultiSelection(false);
        fileSelector.addCallback(new CB());
        fileSelector.addFileFilter(FileSelector.AllFilesFilter);
        fileSelector.addFileFilter(filter);
        fileSelector.setFileFilter(filter);

        popupWindow = new PopupWindow(owner);
        popupWindow.setTheme("fileselector-popup");
        popupWindow.add(fileSelector);
    }

    public void openPopup() {
        if(popupWindow.openPopup()) {
            GUI gui = popupWindow.getGUI();
            popupWindow.setSize(gui.getWidth()*4/5, gui.getHeight()*4/5);
            popupWindow.setPosition(
                    (gui.getWidth() - popupWindow.getWidth())/2,
                    (gui.getHeight() - popupWindow.getHeight())/2);
        }
    }

    class CB implements FileSelector.Callback {
        public void filesSelected(Object[] files) {
            if(files.length == 1 && (files[0] instanceof File)) {
                popupWindow.closePopup();
                callback.fileSelected((File)files[0]);
            }
        }

        public void canceled() {
            popupWindow.closePopup();
            callback.canceled();
        }
    }
    
    public static class ExtFilter implements FileFilter {
	    private final String[] extensions;
	    public ExtFilter(String ... extensions) {
	    	this.extensions = extensions;
	    }
	    public boolean accept(FileSystemModel fsm, Object file) {
		    String name = fsm.getName(file).toLowerCase();
		    for(String extension : extensions) {
			    if(name.endsWith(extension)) {
			    	return true;
			    }
		    }
		    return false;
	    }
    }
}