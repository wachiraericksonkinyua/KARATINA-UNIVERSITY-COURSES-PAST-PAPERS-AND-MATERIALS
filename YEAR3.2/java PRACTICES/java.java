import javax.swing.*; // Import all classes from javax.swing package for GUI components
import javax.swing.event.DocumentEvent; // Import DocumentEvent for document change notifications
import javax.swing.event.DocumentListener; // Import DocumentListener interface for listening to document changes
import javax.swing.tree.*; // Import tree classes for JTree component
import javax.swing.undo.UndoManager; // Import UndoManager for managing undo/redo operations
import java.awt.*; // Import all classes from java.awt package for layouts and components
import java.awt.event.*; // Import all event classes from java.awt.event for handling user interactions
import java.io.*; // Import all I/O classes for file operations

public class MenuExample implements ActionListener { // Define the main class that implements ActionListener for menu events
    JFrame f; // Declare JFrame for the main window
    JMenuBar mb; // Declare JMenuBar for the menu bar
    JMenu file, edit, help; // Declare JMenu objects for File, Edit, and Help menus
    JMenuItem cut, copy, paste, selectAll, open, save, saveAs; // Declare JMenuItem objects for various menu items
    JTextArea ta; // Declare JTextArea for the text editing area
    UndoManager undo; // Declare UndoManager for undo/redo functionality
    
    File currentFile = null; // Initialize currentFile to null, tracks the currently opened file
    boolean isModified = false; // Initialize isModified to false, tracks if the document has unsaved changes

    MenuExample() { // Constructor for MenuExample class
        f = new JFrame("Java Text Editor"); // Create a new JFrame with title "Java Text Editor"
        
        // Initialize Menu Items
        cut = new JMenuItem("Cut"); // Create Cut menu item
        copy = new JMenuItem("Copy"); // Create Copy menu item
        paste = new JMenuItem("Paste"); // Create Paste menu item
        selectAll = new JMenuItem("Select All"); // Create Select All menu item
        
        open = new JMenuItem("Open"); // Create Open menu item
        save = new JMenuItem("Save"); // Create Save menu item
        saveAs = new JMenuItem("Save As"); // Create Save As menu item

        // Add Listeners
        cut.addActionListener(this); // Add action listener to Cut menu item
        copy.addActionListener(this); // Add action listener to Copy menu item
        paste.addActionListener(this); // Add action listener to Paste menu item
        selectAll.addActionListener(this); // Add action listener to Select All menu item
        open.addActionListener(this); // Add action listener to Open menu item
        save.addActionListener(this); // Add action listener to Save menu item
        saveAs.addActionListener(this); // Add action listener to Save As menu item

        mb = new JMenuBar(); // Create a new JMenuBar
        file = new JMenu("File"); // Create File menu
        edit = new JMenu("Edit"); // Create Edit menu
        help = new JMenu("Help"); // Create Help menu

        // Assemble Menus
        file.add(open); // Add Open item to File menu
        file.add(save); // Add Save item to File menu
        file.add(saveAs); // Add Save As item to File menu
        
        edit.add(cut); // Add Cut item to Edit menu
        edit.add(copy); // Add Copy item to Edit menu
        edit.add(paste); // Add Paste item to Edit menu
        edit.add(selectAll); // Add Select All item to Edit menu

        mb.add(file); // Add File menu to menu bar
        mb.add(edit); // Add Edit menu to menu bar
        mb.add(help); // Add Help menu to menu bar

        ta = new JTextArea(); // Create a new JTextArea for text input
        
        // --- CHANGE DETECTION LOGIC ---
        ta.getDocument().addDocumentListener(new DocumentListener() { // Add a document listener to detect changes
            public void insertUpdate(DocumentEvent e) { isModified = true; updateTitle(); } // On insert, mark as modified and update title
            public void removeUpdate(DocumentEvent e) { isModified = true; updateTitle(); } // On remove, mark as modified and update title
            public void changedUpdate(DocumentEvent e) { isModified = true; updateTitle(); } // On change, mark as modified and update title
        });

        f.setJMenuBar(mb); // Set the menu bar on the frame
        
        // Create JTabbedPane
        JTabbedPane tp = new JTabbedPane(); // Create a new JTabbedPane for tabbed interface
        
        // Main tab with text area
        JPanel mainPanel = new JPanel(new BorderLayout()); // Create main panel with BorderLayout
        mainPanel.add(new JScrollPane(ta), BorderLayout.CENTER); // Add text area in scroll pane to main panel
        tp.addTab("Main", mainPanel); // Add Main tab to tabbed pane
        
        // Visit tab with JTree
        JPanel visitPanel = new JPanel(); // Create visit panel
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Options"); // Create root node for tree
        root.add(new DefaultMutableTreeNode("Color")); // Add Color node
        root.add(new DefaultMutableTreeNode("Font")); // Add Font node
        root.add(new DefaultMutableTreeNode("Types of Font")); // Add Types of Font node
        JTree tree = new JTree(root); // Create JTree with root
        visitPanel.add(new JScrollPane(tree)); // Add tree in scroll pane to visit panel
        tp.addTab("Visit", visitPanel); // Add Visit tab to tabbed pane
        
        // Help tab
        JPanel helpPanel = new JPanel(new BorderLayout()); // Create help panel with BorderLayout
        JTextArea helpTa = new JTextArea("This is a simple text editor.\nUse File menu to open/save files.\nEdit menu for cut/copy/paste.\nRight-click for popup menu."); // Create help text area
        helpTa.setEditable(false); // Make help text area non-editable
        helpPanel.add(new JScrollPane(helpTa), BorderLayout.CENTER); // Add help text area in scroll pane
        tp.addTab("Help", helpPanel); // Add Help tab to tabbed pane
        
        f.add(tp); // Add tabbed pane to frame
        f.setSize(600, 600); // Set frame size to 600x600 pixels
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation to exit application
        f.setVisible(true); // Make the frame visible
        
        // Add undo manager
        undo = new UndoManager(); // Create a new UndoManager instance
        ta.getDocument().addUndoableEditListener(undo); // Add undoable edit listener to document for undo/redo
        
        // Create popup menu
        JPopupMenu popup = new JPopupMenu(); // Create a new JPopupMenu for right-click context menu
        JMenuItem copyItem = new JMenuItem("Copy"); // Create Copy item for popup
        copyItem.addActionListener(e -> ta.copy()); // Add action listener to copy text
        popup.add(copyItem); // Add Copy item to popup menu
        JMenuItem pasteItem = new JMenuItem("Paste"); // Create Paste item for popup
        pasteItem.addActionListener(e -> ta.paste()); // Add action listener to paste text
        popup.add(pasteItem); // Add Paste item to popup menu
        JMenuItem undoItem = new JMenuItem("Undo"); // Create Undo item for popup
        undoItem.addActionListener(e -> { if (undo.canUndo()) undo.undo(); }); // Add action listener to undo if possible
        popup.add(undoItem); // Add Undo item to popup menu
        JMenuItem redoItem = new JMenuItem("Redo"); // Create Redo item for popup
        redoItem.addActionListener(e -> { if (undo.canRedo()) undo.redo(); }); // Add action listener to redo if possible
        popup.add(redoItem); // Add Redo item to popup menu
        // Another feature, say select all
        JMenuItem selectAllItem = new JMenuItem("Select All"); // Create Select All item for popup
        selectAllItem.addActionListener(e -> ta.selectAll()); // Add action listener to select all text
        popup.add(selectAllItem); // Add Select All item to popup menu
        // Add mouse listener for popup
        ta.addMouseListener(new MouseAdapter() { // Add mouse listener to text area for popup trigger
            public void mousePressed(MouseEvent e) { // Handle mouse press event
                if (e.isPopupTrigger()) { // Check if event is a popup trigger
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

    // Updates the window title to show if work is unsaved
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
                return; // User cancelled
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

// edit.add(cut);edit.add(copy);edit.add(paste);edit.add(selectAll); edit.add(Save) edit.add(Save);
// mb.add(file);mb.add(edit);mb.add(help);
// ta=new JTextArea();
// ta.setBounds(5,5,360,320);
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
