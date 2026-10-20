package notebook;

import javax.swing.JTextArea;

public class EditFunctions {
    JTextArea textArea;

    public EditFunctions(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void cut() {
        String selectedText = textArea.getSelectedText();
        if (selectedText != null) {
            textArea.replaceSelection("");
        }
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
    
    public void undo() {
        // Undo functionality - placeholder implementation
        // JTextArea doesn't have built-in undo/redo, would need UndoManager
        // For now, just show message
        javax.swing.JOptionPane.showMessageDialog(null, "Undo functionality requires UndoManager implementation", "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void redo() {
        // Redo functionality - placeholder implementation
        // JTextArea doesn't have built-in undo/redo, would need UndoManager
        // For now, just show message
        javax.swing.JOptionPane.showMessageDialog(null, "Redo functionality requires UndoManager implementation", "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
}
