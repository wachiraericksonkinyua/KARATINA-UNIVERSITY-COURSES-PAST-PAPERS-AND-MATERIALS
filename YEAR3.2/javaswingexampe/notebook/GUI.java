package notebook;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
// Import necessary Swing components for UI
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane; // For creating tabbed interface
import javax.swing.JPanel; // For creating panels
import javax.swing.JTree; // For note tree view
import javax.swing.tree.DefaultTreeModel; // For tree model
import javax.swing.tree.DefaultMutableTreeNode; // For tree nodes
import javax.swing.JSplitPane; // For split pane to divide main area
import javax.swing.JButton; // For formatting toolbar buttons
import java.awt.BorderLayout; // For layout management
import java.awt.FlowLayout; // For button layout
import javax.swing.KeyStroke; // For keyboard shortcuts
import javax.swing.SwingConstants; // For component alignment

public class GUI implements ActionListener {
    JFrame window;
    JTextArea textArea;
    JScrollPane scrollPane;
    boolean wordWrapOn = false;// Boolean flag to track word wrap state
    JMenuBar menuBar;
    JMenu fileMenu, editMenu, formatMenu, viewMenu, helpMenu, color;
    JMenuItem newItem, open, save, saveAs, exit;// File menu items
    JMenuItem cut, copy, paste, selectAll;// Edit menu items - basic operations
    // Edit menu items - advanced operations
    JMenuItem undoItem, redoItem, clearItem, clearFormattingItem; // Undo/Redo/Clear operations
    JMenuItem findItem, findNextItem, findPrevItem, replaceItem, goToItem, findReplaceItem; 
    // View menu items 
    JCheckBoxMenuItem wordWrap, statusBarItem; // View options
    JMenuItem zoomInItem, zoomOutItem; // Zoom menu items
    // File and Edit function utilities
    JMenuItem about, help;
    Functionfile file;
    EditFunctions editFunctions;
    JTabbedPane formattingTabs;
    JTree noteTree;// Node for tree root
    DefaultMutableTreeNode rootNode;
    JPanel statusBar;// Status bar to show line/column info
    // Current zoom level (100 = normal)
    int zoomLevel = 100;
    public static void main(String[] args) {
        new GUI();
    }
    public GUI(){        
        createWindow();// Create the main window frame
        createTextArea();
        createMenuBar();
        createFileMenu();
        createEditMenu();
        createFormatMenu();
        createViewMenu();
        // Populate Help menu with help and about
        createHelpMenu();
        createFormattingTabs();
        createNotesTree();
        
        file = new Functionfile(this);// Initialize file handling utility
        // Initialize edit functions utility
        editFunctions = new EditFunctions(textArea);
        // Add Find/Replace dialog functionality
        addFindReplaceDialog();
        window.setVisible(true);// Make the window visible to user
    }
    public void createWindow(){
        // Initialize the JFrame window with title
        window = new JFrame("Notepad++ Clone");
        window.setSize(1000,700);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Exit program when window is closed
        window.setLayout(new BorderLayout());
        // Create and add status bar at bottom
        statusBar = new JPanel();
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        window.add(statusBar, BorderLayout.SOUTH);
    }
    public void createTextArea(){
        textArea = new JTextArea();
        textArea.setTabSize(4);
        // Create scroll pane to enable scrolling for text area
        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        window.add(scrollPane, BorderLayout.CENTER);// Add scroll pane to window center area
    }
    public void createMenuBar(){
        menuBar = new JMenuBar();
        window.setJMenuBar(menuBar);
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        formatMenu = new JMenu("Format");
        viewMenu = new JMenu("View");
        helpMenu = new JMenu("Help");
        color = new JMenu("Color");

        // Add all menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        menuBar.add(color);
    }
    public void createFileMenu(){
        // Create "New" menu item for creating new documents
        newItem = new JMenuItem("New");
        newItem.addActionListener(this);
        newItem.setActionCommand("New");
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        // Create "Open" menu item to open existing files
        open = new JMenuItem("Open");
        open.addActionListener(this);
        open.setActionCommand("Open");
        // Add keyboard shortcut Ctrl+O for Open
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

        // Create "Save" menu item to save current document
        save = new JMenuItem("Save");
        save.addActionListener(this);
        save.setActionCommand("Save");
        // Add keyboard shortcut Ctrl+S for Save
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        // Create "Save As" menu item to save document with new name
        saveAs = new JMenuItem("Save As");
        saveAs.addActionListener(this);
        saveAs.setActionCommand("Save As");
        // Add keyboard shortcut Ctrl+Shift+S for Save As
        saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        
        // Create "Exit" menu item to close application
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        exit.setActionCommand("Exit");
        // Add keyboard shortcut Alt+F4 for Exit
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));

        // Add all file menu items to File menu
        fileMenu.add(newItem);
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(saveAs);
        // Add separator line in menu for visual organization
        fileMenu.addSeparator();
        fileMenu.add(exit);
    }
    public void createEditMenu(){
        // Create "Undo" menu item to reverse last action
        undoItem = new JMenuItem("Undo");
        undoItem.addActionListener(this);
        undoItem.setActionCommand("Undo");
        // Add keyboard shortcut Ctrl+Z for Undo
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        
        // Create "Redo" menu item to redo last undone action
        redoItem = new JMenuItem("Redo");
        redoItem.addActionListener(this);
        redoItem.setActionCommand("Redo");
        // Add keyboard shortcut Ctrl+Y for Redo
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        
        editMenu.addSeparator();
        
        // Create "Cut" menu item to cut selected text
        cut = new JMenuItem("Cut");
        cut.addActionListener(this);
        cut.setActionCommand("Cut");
        // Add keyboard shortcut Ctrl+X for Cut
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        
        // Create "Copy" menu item to copy selected text
        copy = new JMenuItem("Copy");
        copy.addActionListener(this);
        copy.setActionCommand("Copy");
        // Add keyboard shortcut Ctrl+C for Copy
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        
        // Create "Paste" menu item to paste text from clipboard
        paste = new JMenuItem("Paste");
        paste.addActionListener(this);
        paste.setActionCommand("Paste");
        // Add keyboard shortcut Ctrl+V for Paste
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        
        // Add separator for visual organization
        editMenu.addSeparator();
        
        // Create "Select All" menu item to select entire document
        selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(this);
        selectAll.setActionCommand("Select All");
        // Add keyboard shortcut Ctrl+A for Select All
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        
        // Add separator for visual organization
        editMenu.addSeparator();
        
        // Create "Find" menu item to search text
        findItem = new JMenuItem("Find");
        findItem.addActionListener(this);
        findItem.setActionCommand("Find");
        // Add keyboard shortcut Ctrl+F for Find
        findItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        
        // Create "Find Next" menu item to find next occurrence
        findNextItem = new JMenuItem("Find Next");
        findNextItem.addActionListener(this);
        findNextItem.setActionCommand("Find Next");
        // Add keyboard shortcut F3 for Find Next
        findNextItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        
        // Create "Find Previous" menu item to find previous occurrence
        findPrevItem = new JMenuItem("Find Previous");
        findPrevItem.addActionListener(this);
        findPrevItem.setActionCommand("Find Previous");
        // Add keyboard shortcut Shift+F3 for Find Previous
        findPrevItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, ActionEvent.SHIFT_MASK));
        
        // Create "Replace" menu item to find and replace text
        replaceItem = new JMenuItem("Replace");
        replaceItem.addActionListener(this);
        replaceItem.setActionCommand("Replace");
        // Add keyboard shortcut Ctrl+H for Replace
        replaceItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        
        // Add separator for visual organization
        editMenu.addSeparator();
        
        // Create "Go To" menu item to jump to specific line
        goToItem = new JMenuItem("Go To Line");
        goToItem.addActionListener(this);
        goToItem.setActionCommand("Go To");
        // Add keyboard shortcut Ctrl+G for Go To
        goToItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        
        // Add separator for visual organization
        editMenu.addSeparator();
        
        // Create "Clear" menu item to clear all text
        clearItem = new JMenuItem("Clear");
        clearItem.addActionListener(this);
        clearItem.setActionCommand("Clear");
        
        // Create "Clear Formatting" menu item to remove all formatting
        clearFormattingItem = new JMenuItem("Clear Formatting");
        clearFormattingItem.addActionListener(this);
        clearFormattingItem.setActionCommand("Clear Formatting");
        // Add keyboard shortcut Ctrl+M for Clear Formatting
        clearFormattingItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
        
        // Add all items to Edit menu
        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.add(selectAll);
        editMenu.add(findItem);
        editMenu.add(findNextItem);
        editMenu.add(findPrevItem);
        editMenu.add(replaceItem);
        editMenu.add(goToItem);
        editMenu.add(clearItem);
        editMenu.add(clearFormattingItem);
    }

    public void createFormatMenu(){
        // Create "Bold" menu item to make text bold
        JMenuItem boldItem = new JMenuItem("Bold");
        boldItem.addActionListener(this);
        boldItem.setActionCommand("Bold");
        // Add keyboard shortcut Ctrl+B for Bold
        boldItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        
        // Create "Italic" menu item to make text italic
        JMenuItem italicItem = new JMenuItem("Italic");
        italicItem.addActionListener(this);
        italicItem.setActionCommand("Italic");
        // Add keyboard shortcut Ctrl+I for Italic
        italicItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        
        // Create "Underline" menu item to underline text
        JMenuItem underlineItem = new JMenuItem("Underline");
        underlineItem.addActionListener(this);
        underlineItem.setActionCommand("Underline");
        // Add keyboard shortcut Ctrl+U for Underline
        underlineItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
        
        // Create "Strikethrough" menu item to strike through text
        JMenuItem strikethroughItem = new JMenuItem("Strikethrough");
        strikethroughItem.addActionListener(this);
        strikethroughItem.setActionCommand("Strikethrough");
        // Add keyboard shortcut Ctrl+Shift+X for Strikethrough
        strikethroughItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        
        // Add separator for visual organization
        formatMenu.addSeparator();
        
        // Create "H1 Heading" menu item for heading format
        JMenuItem h1Item = new JMenuItem("H1 Heading");
        h1Item.addActionListener(this);
        h1Item.setActionCommand("H1");
        
        // Create "H2 Heading" menu item for subheading format
        JMenuItem h2Item = new JMenuItem("H2 Heading");
        h2Item.addActionListener(this);
        h2Item.setActionCommand("H2");
        
        // Create "H3 Heading" menu item for smaller heading
        JMenuItem h3Item = new JMenuItem("H3 Heading");
        h3Item.addActionListener(this);
        h3Item.setActionCommand("H3");
        
        // Add separator for visual organization
        formatMenu.addSeparator();
        
        // Create "Bullet List" menu item for bulleted list
        JMenuItem bulletItem = new JMenuItem("Bullet List");
        bulletItem.addActionListener(this);
        bulletItem.setActionCommand("Bullet");
        // Add keyboard shortcut Ctrl+Shift+B for Bullet List
        bulletItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        
        // Create "Numbered List" menu item for numbered list
        JMenuItem numberedItem = new JMenuItem("Numbered List");
        numberedItem.addActionListener(this);
        numberedItem.setActionCommand("Numbered");
        
        // Add separator for visual organization
        formatMenu.addSeparator();
        
        // Create "Insert Link" menu item to add hyperlinks
        JMenuItem linkItem = new JMenuItem("Insert Link");
        linkItem.addActionListener(this);
        linkItem.setActionCommand("Link");
        // Add keyboard shortcut Ctrl+K for Insert Link
        linkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK));
        
        // Create "Insert Table" menu item to add tables
        JMenuItem tableItem = new JMenuItem("Insert Table");
        tableItem.addActionListener(this);
        tableItem.setActionCommand("Table");
        
        // Add separator for visual organization
        formatMenu.addSeparator();
        
        // Create Word Wrap checkbox menu item
        wordWrap = new JCheckBoxMenuItem("Word Wrap", false);
        wordWrap.addActionListener(this);
        wordWrap.setActionCommand("Word Wrap");
        
        // Add all format menu items to Format menu
        formatMenu.add(boldItem);
        formatMenu.add(italicItem);
        formatMenu.add(underlineItem);
        formatMenu.add(strikethroughItem);
        formatMenu.add(h1Item);
        formatMenu.add(h2Item);
        formatMenu.add(h3Item);
        formatMenu.add(bulletItem);
        formatMenu.add(numberedItem);
        formatMenu.add(linkItem);
        formatMenu.add(tableItem);
        formatMenu.add(wordWrap);
    }

    public void createViewMenu(){
        // Create "Zoom In" menu item to increase text size
        zoomInItem = new JMenuItem("Zoom In");
        zoomInItem.addActionListener(this);
        zoomInItem.setActionCommand("Zoom In");
        // Add keyboard shortcut Ctrl+Plus for Zoom In
        zoomInItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.CTRL_MASK));
        
        // Create "Zoom Out" menu item to decrease text size
        zoomOutItem = new JMenuItem("Zoom Out");
        zoomOutItem.addActionListener(this);
        zoomOutItem.setActionCommand("Zoom Out");
        // Add keyboard shortcut Ctrl+Minus for Zoom Out
        zoomOutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
        
        // Create "Reset Zoom" menu item to reset text size to default (100%)
        JMenuItem resetZoomItem = new JMenuItem("Reset Zoom");
        resetZoomItem.addActionListener(this);
        resetZoomItem.setActionCommand("Reset Zoom");
        // Add keyboard shortcut Ctrl+0 for Reset Zoom
        resetZoomItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.CTRL_MASK));
        
        // Add separator for visual organization
        viewMenu.addSeparator();
        
        // Create Status Bar checkbox menu item to toggle status bar visibility
        statusBarItem = new JCheckBoxMenuItem("Status Bar", true);
        statusBarItem.addActionListener(this);
        statusBarItem.setActionCommand("Status Bar");
        
        // Add all view menu items to View menu
        viewMenu.add(zoomInItem);
        viewMenu.add(zoomOutItem);
        viewMenu.add(resetZoomItem);
        viewMenu.add(statusBarItem);
    }

    public void createHelpMenu(){
        // Create "About" menu item showing application version and info
        about = new JMenuItem("About");
        about.addActionListener(this);
        about.setActionCommand("About");
        
        // Create "Help" menu item to show help documentation
        help = new JMenuItem("Help");
        help.addActionListener(this);
        help.setActionCommand("Help");
        // Add keyboard shortcut F1 for Help
        help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        
        // Add all help menu items to Help menu
        helpMenu.add(help);
        helpMenu.addSeparator();
        helpMenu.add(about);
    }
    
    public void createFormattingTabs(){
        // Create tabbed pane for formatting features
        formattingTabs = new JTabbedPane();
        
        // Create Basic Formatting tab
        JPanel basicTab = new JPanel();
        basicTab.setLayout(new FlowLayout());
        
        // Create buttons for basic formatting
        JButton boldBtn = new JButton("Bold");
        boldBtn.addActionListener(e -> applyFormatting("Bold"));
        JButton italicBtn = new JButton("Italic");
        italicBtn.addActionListener(e -> applyFormatting("Italic"));
        JButton underlineBtn = new JButton("Underline");
        underlineBtn.addActionListener(e -> applyFormatting("Underline"));
        JButton strikethroughBtn = new JButton("Strikethrough");
        strikethroughBtn.addActionListener(e -> applyFormatting("Strikethrough"));
        
        // Add buttons to basic tab
        basicTab.add(boldBtn);
        basicTab.add(italicBtn);
        basicTab.add(underlineBtn);
        basicTab.add(strikethroughBtn);
        
        // Create Headings tab
        JPanel headingsTab = new JPanel();
        headingsTab.setLayout(new FlowLayout());
        
        // Create buttons for headings
        JButton h1Btn = new JButton("H1");
        h1Btn.addActionListener(e -> applyFormatting("H1"));
        JButton h2Btn = new JButton("H2");
        h2Btn.addActionListener(e -> applyFormatting("H2"));
        JButton h3Btn = new JButton("H3");
        h3Btn.addActionListener(e -> applyFormatting("H3"));
        
        // Add buttons to headings tab
        headingsTab.add(h1Btn);
        headingsTab.add(h2Btn);
        headingsTab.add(h3Btn);
        
        // Create Lists tab
        JPanel listsTab = new JPanel();
        listsTab.setLayout(new FlowLayout());
        
        // Create buttons for lists
        JButton bulletBtn = new JButton("Bullet List");
        bulletBtn.addActionListener(e -> applyFormatting("Bullet"));
        JButton numberedBtn = new JButton("Numbered List");
        numberedBtn.addActionListener(e -> applyFormatting("Numbered"));
        
        // Add buttons to lists tab
        listsTab.add(bulletBtn);
        listsTab.add(numberedBtn);
        
        // Create Links and Tables tab
        JPanel linksTab = new JPanel();
        linksTab.setLayout(new FlowLayout());
        
        // Create buttons for links and tables
        JButton linkBtn = new JButton("Insert Link");
        linkBtn.addActionListener(e -> applyFormatting("Link"));
        JButton tableBtn = new JButton("Insert Table");
        tableBtn.addActionListener(e -> applyFormatting("Table"));
        
        // Add buttons to links tab
        linksTab.add(linkBtn);
        linksTab.add(tableBtn);
        
        // Add all tabs to the tabbed pane
        formattingTabs.addTab("Basic", basicTab);
        formattingTabs.addTab("Headings", headingsTab);
        formattingTabs.addTab("Lists", listsTab);
        formattingTabs.addTab("Links & Tables", linksTab);
        
        // Add formatting tabs to window north area
        window.add(formattingTabs, BorderLayout.NORTH);
    }
    
    public void createNotesTree(){
        // Create root node for the notes tree
        rootNode = new DefaultMutableTreeNode("Notes");
        
        // Create sample note categories
        DefaultMutableTreeNode personalNode = new DefaultMutableTreeNode("Personal");
        DefaultMutableTreeNode workNode = new DefaultMutableTreeNode("Work");
        DefaultMutableTreeNode ideasNode = new DefaultMutableTreeNode("Ideas");
        
        // Add sample notes to categories
        personalNode.add(new DefaultMutableTreeNode("Shopping List"));
        personalNode.add(new DefaultMutableTreeNode("Reminders"));
        workNode.add(new DefaultMutableTreeNode("Meeting Notes"));
        workNode.add(new DefaultMutableTreeNode("Project Ideas"));
        ideasNode.add(new DefaultMutableTreeNode("App Features"));
        ideasNode.add(new DefaultMutableTreeNode("Future Plans"));
        
        // Add categories to root
        rootNode.add(personalNode);
        rootNode.add(workNode);
        rootNode.add(ideasNode);
        
        // Create tree model with root node
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        
        // Create the JTree component
        noteTree = new JTree(treeModel);
        
        // Create scroll pane for the tree
        JScrollPane treeScrollPane = new JScrollPane(noteTree);
        treeScrollPane.setBorder(BorderFactory.createTitledBorder("Notes Tree"));
        
        // Create split pane to divide main area between text area and notes tree
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, treeScrollPane);
        splitPane.setDividerLocation(700); // Set initial divider position
        splitPane.setResizeWeight(0.8); // Give more space to text area
        
        // Replace the center component with the split pane
        window.add(splitPane, BorderLayout.CENTER);
    }
    
    public void addFindReplaceDialog(){
        // Initialize find/replace functionality (placeholder for now)
        // This would typically create a dialog for find/replace operations
    }
    
    public void applyFormatting(String formatType){
        // Apply the specified formatting to selected text or current position
        String selectedText = textArea.getSelectedText();
        if (selectedText == null) {
            // If no text selected, insert formatting markers at cursor
            int caretPos = textArea.getCaretPosition();
            String marker = getFormattingMarker(formatType);
            textArea.insert(marker, caretPos);
        } else {
            // If text selected, wrap it with formatting markers
            String startMarker = getFormattingStartMarker(formatType);
            String endMarker = getFormattingEndMarker(formatType);
            int start = textArea.getSelectionStart();
            int end = textArea.getSelectionEnd();
            textArea.replaceRange(startMarker + selectedText + endMarker, start, end);
        }
    }
    
    private String getFormattingMarker(String formatType){
        // Return appropriate formatting marker based on type
        switch (formatType) {
            case "Bold": return "**bold text**";
            case "Italic": return "*italic text*";
            case "Underline": return "<u>underlined text</u>";
            case "Strikethrough": return "~~strikethrough text~~";
            case "H1": return "# Heading 1\n";
            case "H2": return "## Heading 2\n";
            case "H3": return "### Heading 3\n";
            case "Bullet": return "- Bullet item\n";
            case "Numbered": return "1. Numbered item\n";
            case "Link": return "[link text](url)";
            case "Table": return "| Header 1 | Header 2 |\n|----------|----------|\n| Cell 1   | Cell 2   |\n";
            default: return "";
        }
    }
    
    private String getFormattingStartMarker(String formatType){
        // Return start marker for wrapping selected text
        switch (formatType) {
            case "Bold": return "**";
            case "Italic": return "*";
            case "Underline": return "<u>";
            case "Strikethrough": return "~~";
            default: return "";
        }
    }
    
    private String getFormattingEndMarker(String formatType){
        // Return end marker for wrapping selected text
        switch (formatType) {
            case "Bold": return "**";
            case "Italic": return "*";
            case "Underline": return "</u>";
            case "Strikethrough": return "~~";
            default: return "";
        }
    }
    
    // Find and Replace functionality methods
    public void showFindDialog(){
        // Show find dialog - placeholder implementation
        String searchText = JOptionPane.showInputDialog(window, "Find:", "Find", JOptionPane.QUESTION_MESSAGE);
        if (searchText != null && !searchText.isEmpty()) {
            findText(searchText, textArea.getCaretPosition());
        }
    }
    
    public void findNext(){
        // Find next occurrence - placeholder implementation
        JOptionPane.showMessageDialog(window, "Find Next functionality not yet implemented", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void findPrevious(){
        // Find previous occurrence - placeholder implementation
        JOptionPane.showMessageDialog(window, "Find Previous functionality not yet implemented", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showReplaceDialog(){
        // Show replace dialog - placeholder implementation
        JOptionPane.showMessageDialog(window, "Replace functionality not yet implemented", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showGoToDialog(){
        // Show go to line dialog
        String lineNumber = JOptionPane.showInputDialog(window, "Go to line:", "Go To Line", JOptionPane.QUESTION_MESSAGE);
        if (lineNumber != null && !lineNumber.isEmpty()) {
            try {
                int line = Integer.parseInt(lineNumber) - 1; // Convert to 0-based
                // Simple implementation - count lines to find position
                String text = textArea.getText();
                int pos = 0;
                int currentLine = 0;
                while (currentLine < line && pos < text.length()) {
                    if (text.charAt(pos) == '\n') {
                        currentLine++;
                    }
                    pos++;
                }
                textArea.setCaretPosition(Math.min(pos, text.length()));
                textArea.requestFocus();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(window, "Invalid line number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void clearFormatting(){
        // Clear formatting - placeholder implementation
        JOptionPane.showMessageDialog(window, "Clear Formatting functionality not yet implemented", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Zoom functionality methods
    public void zoomIn(){
        // Increase zoom level
        if (zoomLevel < 300) { // Max zoom 300%
            zoomLevel += 25;
            applyZoom();
        }
    }
    
    public void zoomOut(){
        // Decrease zoom level
        if (zoomLevel > 50) { // Min zoom 50%
            zoomLevel -= 25;
            applyZoom();
        }
    }
    
    public void resetZoom(){
        // Reset zoom to 100%
        zoomLevel = 100;
        applyZoom();
    }
    
    private void applyZoom(){
        // Apply zoom level to text area font
        java.awt.Font currentFont = textArea.getFont();
        float newSize = (zoomLevel / 100.0f) * 12.0f; // Base size 12pt
        textArea.setFont(currentFont.deriveFont(newSize));
        // Update status bar if visible
        updateStatusBar();
    }
    
    // Status bar functionality
    public void toggleStatusBar(){
        // Toggle status bar visibility
        statusBar.setVisible(!statusBar.isVisible());
        window.revalidate();
    }
    
    private void updateStatusBar(){
        // Update status bar with current line/column info
        if (statusBar.isVisible()) {
            try {
                int caretPos = textArea.getCaretPosition();
                String text = textArea.getText();
                int line = 1;
                int column = 1;
                
                for (int i = 0; i < caretPos && i < text.length(); i++) {
                    if (text.charAt(i) == '\n') {
                        line++;
                        column = 1;
                    } else {
                        column++;
                    }
                }
                
                statusBar.removeAll();
                statusBar.add(new javax.swing.JLabel("Line: " + line + "  Column: " + column + "  Zoom: " + zoomLevel + "%"));
                statusBar.revalidate();
                statusBar.repaint();
            } catch (Exception e) {
                // Ignore errors in status bar update
            }
        }
    }
    
    // Helper method for find functionality
    private void findText(String searchText, int startPos){
        // Simple find implementation
        String text = textArea.getText().toLowerCase();
        String search = searchText.toLowerCase();
        int foundPos = text.indexOf(search, startPos);
        if (foundPos != -1) {
            textArea.setCaretPosition(foundPos);
            textArea.select(foundPos, foundPos + searchText.length());
            textArea.requestFocus();
        } else {
            JOptionPane.showMessageDialog(window, "Text not found: " + searchText, "Find", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent e) {
        // TODO: Implement action handling
        String command = e.getActionCommand();

        switch (command) {
            // File Menu
            case "New": file.newfile(); break;
            case "Open": file.openfile(); break;
            case "Save": file.savefile(); break;
            case "Save As": file.saveasfile(); break;
            case "Exit": System.exit(0); break;
            
            // Edit Menu - Basic operations
            case "Undo": editFunctions.undo(); break;
            case "Redo": editFunctions.redo(); break;
            case "Cut": editFunctions.cut(); break;
            case "Copy": editFunctions.copy(); break;
            case "Paste": editFunctions.paste(); break;
            case "Select All": editFunctions.selectAll(); break;
            
            // Edit Menu - Find and Replace operations
            case "Find": showFindDialog(); break;
            case "Find Next": findNext(); break;
            case "Find Previous": findPrevious(); break;
            case "Replace": showReplaceDialog(); break;
            case "Go To": showGoToDialog(); break;
            
            // Edit Menu - Clear operations
            case "Clear": textArea.setText(""); break;
            case "Clear Formatting": clearFormatting(); break;
            
            // Format Menu - Text formatting
            case "Bold": applyFormatting("Bold"); break;
            case "Italic": applyFormatting("Italic"); break;
            case "Underline": applyFormatting("Underline"); break;
            case "Strikethrough": applyFormatting("Strikethrough"); break;
            case "H1": applyFormatting("H1"); break;
            case "H2": applyFormatting("H2"); break;
            case "H3": applyFormatting("H3"); break;
            case "Bullet": applyFormatting("Bullet"); break;
            case "Numbered": applyFormatting("Numbered"); break;
            case "Link": applyFormatting("Link"); break;
            case "Table": applyFormatting("Table"); break;
            
            // Format Menu - Word Wrap
            case "Word Wrap": 
                if (wordWrapOn == false) {
                    wordWrapOn = true;
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    wordWrap.setSelected(true);
                } else {
                    wordWrapOn = false;
                    textArea.setLineWrap(false);
                    textArea.setWrapStyleWord(false);
                    wordWrap.setSelected(false);
                }
                break;
            
            // View Menu - Zoom operations
            case "Zoom In": zoomIn(); break;
            case "Zoom Out": zoomOut(); break;
            case "Reset Zoom": resetZoom(); break;
            case "Status Bar": toggleStatusBar(); break;
            
            // Help Menu
            case "Help":
                JOptionPane.showMessageDialog(window, 
                    "Enhanced Notepad++ Clone Application\n\n" +
                    "Features:\n" +
                    "- Create, Open, Save, and Save As files\n" +
                    "- Cut, Copy, Paste, Undo, Redo text operations\n" +
                    "- Find, Find Next, Find Previous, Replace text\n" +
                    "- Go To Line functionality\n" +
                    "- Clear and Clear Formatting options\n" +
                    "- Text formatting: Bold, Italic, Underline, Strikethrough\n" +
                    "- Headings: H1, H2, H3\n" +
                    "- Lists: Bullet and Numbered lists\n" +
                    "- Insert Links and Tables\n" +
                    "- Word Wrap toggle\n" +
                    "- Zoom In/Out and Status Bar\n" +
                    "- Notes Tree for organization\n" +
                    "- Formatting toolbar with tabs", 
                    "Help", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "About":
                JOptionPane.showMessageDialog(window, 
                    "Enhanced Notepad++ Clone\n" +
                    "Version 2.0\n" +
                    "A feature-rich text editor built with Java Swing\n" +
                    "Includes Notepad++ style features and more", 
                    "About Notepad++ Clone", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }


}
