package notebook;

public class functionFormat {
    GUI gui;
    public functionFormat() {
        this.gui = new GUI();
    }
    public void wordWrap() {
        if (gui.wordWrapOn == false) {
            gui.textArea.setLineWrap(true);
            gui.textArea.setWrapStyleWord(true);
            gui.wordWrapOn = true;
        } else {
            gui.textArea.setLineWrap(false);
            gui.textArea.setWrapStyleWord(false);
            gui.wordWrapOn = false;
        }
    }
    public static void main(String[] args) {
        new functionFormat();
    }
}
