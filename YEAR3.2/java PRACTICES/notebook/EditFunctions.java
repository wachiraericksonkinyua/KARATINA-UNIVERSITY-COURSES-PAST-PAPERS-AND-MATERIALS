package notebook;

import javax.swing.JTextArea;

public class EditFunctions {
    private final JTextArea textArea;

    public EditFunctions(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void cut() {
        textArea.cut();
    }

    public void copy() {
        textArea.copy();
    }

    public void paste() {
        textArea.paste();
    }

    public void selectAll() {
        textArea.selectAll();
    }
}
