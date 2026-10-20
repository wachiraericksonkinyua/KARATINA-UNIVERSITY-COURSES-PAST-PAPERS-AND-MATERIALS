package notebook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Functionfile {
    GUI gui;
    private File currentFile;

    public Functionfile(GUI gui) {
        this.gui = gui;
    }

    public void newfile() {
        gui.textArea.setText("");
        currentFile = null;
        gui.updateWindowTitle();
        gui.setModified(false);
    }

    public void openfile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open");
        int result = chooser.showOpenDialog(gui.window);

        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile();
            gui.setModified(false);
            gui.updateWindowTitle();

            try (BufferedReader br = new BufferedReader(new FileReader(currentFile))) {
                gui.textArea.setText("");
                String line;
                while ((line = br.readLine()) != null) {
                    gui.textArea.append(line + "\n");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(gui.window, "Could not open file:\n" + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean savefile() {
        if (currentFile == null) {
            return saveasfile();
        }
        return writeToFile(currentFile);
    }

    public boolean saveasfile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save As");
        int result = chooser.showSaveDialog(gui.window);

        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile();
            boolean saved = writeToFile(currentFile);
            if (saved) {
                gui.updateWindowTitle();
            }
            return saved;
        }
        return false;
    }

    private boolean writeToFile(File file) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(gui.textArea.getText());
            gui.setModified(false);
            gui.updateWindowTitle();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(gui.window, "Could not save file:\n" + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean hasCurrentFile() {
        return currentFile != null;
    }

    public String getCurrentFileName() {
        return currentFile != null ? currentFile.getName() : "Untitled";
    }
}
