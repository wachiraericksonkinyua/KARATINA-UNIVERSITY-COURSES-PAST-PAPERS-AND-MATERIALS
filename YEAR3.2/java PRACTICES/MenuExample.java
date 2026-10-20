import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MenuExample implements ActionListener {
    JFrame f;
    JMenuBar mb;
    JMenu file, edit, help;
    JMenuItem cut, copy, paste, selectAll, open, save, saveAs;
    JTextArea ta;
    UndoManager undo;
    
    File currentFile = null;
    boolean isModified = false;

    MenuExample() {
        f = new JFrame("Java Text Editor");
        cut = new JMenuItem("Cut");
        copy = new JMenuItem("Copy");
        paste = new JMenuItem("Paste");
        selectAll = new JMenuItem("Select All");
        
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        saveAs = new JMenuItem("Save As");

        // Add Listeners
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        selectAll.addActionListener(this);
        open.addActionListener(this);
        save.addActionListener(this);
        saveAs.addActionListener(this);

        mb = new JMenuBar();
        file = new JMenu("File");
        edit = new JMenu("Edit");
        help = new JMenu("Help");

        file.add(open);
        file.add(save);
        file.add(saveAs);
        
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(selectAll);

        mb.add(file);
        mb.add(edit);
        mb.add(help);

        ta = new JTextArea();
        
        ta.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { isModified = true; updateTitle(); }
            public void removeUpdate(DocumentEvent e) { isModified = true; updateTitle(); }
            public void changedUpdate(DocumentEvent e) { isModified = true; updateTitle(); }
        });

        f.setJMenuBar(mb);
        
        JTabbedPane tp = new JTabbedPane();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(ta), BorderLayout.CENTER);
        tp.addTab("Main", mainPanel);
        
        JPanel visitPanel = new JPanel();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Options");
        root.add(new DefaultMutableTreeNode("Color"));
        root.add(new DefaultMutableTreeNode("Font"));
        root.add(new DefaultMutableTreeNode("Types of Font"));
        JTree tree = new JTree(root);
        visitPanel.add(new JScrollPane(tree));
        tp.addTab("Visit", visitPanel);
        
        JPanel helpPanel = new JPanel(new BorderLayout());
        JTextArea helpTa = new JTextArea("This is a simple text editor.\nUse File menu to open/save files.\nEdit menu for cut/copy/paste.\nRight-click for popup menu.");
        helpTa.setEditable(false);
        helpPanel.add(new JScrollPane(helpTa), BorderLayout.CENTER);
        tp.addTab("Help", helpPanel);
        
        f.add(tp);
        f.setSize(600, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        
        undo = new UndoManager();
        ta.getDocument().addUndoableEditListener(undo);
        
        JPopupMenu popup = new JPopupMenu();
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(e -> ta.copy());
        popup.add(copyItem);
        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.addActionListener(e -> ta.paste());
        popup.add(pasteItem);
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.addActionListener(e -> { if (undo.canUndo()) undo.undo(); });
        popup.add(undoItem);
        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.addActionListener(e -> { if (undo.canRedo()) undo.redo(); });
        popup.add(redoItem);
        
        JMenuItem selectAllItem = new JMenuItem("Select All");
        selectAllItem.addActionListener(e -> ta.selectAll());
        popup.add(selectAllItem);
        
        ta.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    void updateTitle() {
        String name = (currentFile == null) ? "Untitled" : currentFile.getName();
        f.setTitle(name + (isModified ? " (Modified)" : ""));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cut) ta.cut();
        if (e.getSource() == copy) ta.copy();
        if (e.getSource() == paste) ta.paste();
        if (e.getSource() == selectAll) ta.selectAll();
        
        if (e.getSource() == open) openFile();
        if (e.getSource() == save) saveFile(false);
        if (e.getSource() == saveAs) saveFile(true);
    }

    private void openFile() {
        JFileChooser jfc = new JFileChooser();
        if (jfc.showOpenDialog(f) == JFileChooser.APPROVE_OPTION) {
            try {
                currentFile = jfc.getSelectedFile();
                BufferedReader br = new BufferedReader(new FileReader(currentFile));
                ta.read(br, null);
                br.close();
                isModified = false;
                updateTitle();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(f, "Error opening file");
            }
        }
    }

    private void saveFile(boolean forceSaveAs) {
        if (forceSaveAs || currentFile == null) {
            JFileChooser jfc = new JFileChooser();
            if (jfc.showSaveDialog(f) == JFileChooser.APPROVE_OPTION) {
                currentFile = jfc.getSelectedFile();
            } else {
                return;
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(currentFile))) {
            ta.write(bw);
            isModified = false;
            updateTitle();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(f, "Error saving file");
        }
    }

    public static void main(String[] args) {
        new MenuExample();
    }
}
// f.add(mb);f.add(ta);
// f.setJMenuBar(mb);
// f.setLayout(null);
// f.setSize(400,400);
// f.setVisible(true);
// }
// public void actionPerformed(ActionEvent e) {
// if(e.getSource()==cut)
// ta.cut();
// if(e.getSource()==paste)
// ta.paste();
// if(e.getSource()==copy)
// ta.copy();
// if(e.getSource()==selectAll)
// ta.selectAll();
// }
// public static void main(String[] args) {
// new MenuExample();
// }
// }
