package window;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.textarea.HTMLTextAreaModel;

public class MainMenu extends ResizableFrame {
    private final HTMLTextAreaModel textAreaModel;
    private final TextArea textArea;
    private final EditField editField;
    private final ScrollPane scrollPane;

    public MainMenu() {
        setTitle("TEST");

        this.textAreaModel = new HTMLTextAreaModel();
        this.textArea = new TextArea(textAreaModel);
        this.editField = new EditField();

        scrollPane = new ScrollPane(textArea);
        scrollPane.setFixed(ScrollPane.Fixed.HORIZONTAL);

        DialogLayout layout = new DialogLayout();
        layout.setTheme("content");
        layout.setHorizontalGroup(layout.createParallelGroup(scrollPane, editField));
        layout.setVerticalGroup(layout.createSequentialGroup(scrollPane, editField));

        add(layout);
    }
}
