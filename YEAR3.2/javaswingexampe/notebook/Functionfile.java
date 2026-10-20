package notebook;
import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class Functionfile {
    GUI gui;
    String fileName;
    String fileAddress;

    public Functionfile(GUI gui) {
        this.gui = gui;
    }

    public void newfile() {
        gui.textArea.setText("");
        gui.window.setTitle("New");
        fileName = null;
        fileAddress = null;
    }

    public void openfile() {
        FileDialog fd = new FileDialog(gui.window, "Open", FileDialog.LOAD);
        fd.setVisible(true);

        if (fd.getFile() != null) {
            fileName = fd.getFile();
            fileAddress = fd.getDirectory();
            gui.window.setTitle(fileName);
            

            try {
                BufferedReader br = new BufferedReader(new FileReader(fileAddress + fileName));
                gui.textArea.setText("");
                String line;
                while ((line = br.readLine()) != null) {
                    gui.textArea.append(line + "\n");
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void savefile() {
        if (fileName == null) {
            saveasfile();
        } else {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(fileAddress + fileName));
                bw.write(gui.textArea.getText());
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveasfile() {
        FileDialog fd = new FileDialog(gui.window, "Save As", FileDialog.SAVE);
        fd.setVisible(true);

        if (fd.getFile() != null) {
            fileName = fd.getFile();
            fileAddress = fd.getDirectory();
            gui.window.setTitle(fileName);

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(fileAddress + fileName));
                bw.write(gui.textArea.getText());
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
