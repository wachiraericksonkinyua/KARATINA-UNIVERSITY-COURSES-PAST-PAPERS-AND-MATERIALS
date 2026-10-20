package notebook;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.text.BadLocationException;

public class GUI implements ActionListener {
    JFrame window;
    JTextArea textArea;
    JScrollPane scrollPane;
    JLabel statusLabel;
    boolean wordWrapOn = false;
    boolean statusBarOn = true;
    boolean modified = false;

    JMenuBar menuBar;
    JMenu fileMenu, editMenu, formatMenu, viewMenu, searchMenu, toolsMenu, documentsMenu, helpMenu, colorMenu;
    JMenuItem newItem, open, save, saveAs, exit;
    JMenuItem undoItem, redoItem, cut, copy, paste, deleteItem, selectAll;
    JMenuItem findItem, replaceItem;
    JCheckBoxMenuItem wordWrap, statusBarItem;
    JMenuItem textColorItem, backgroundColorItem;
    JMenuItem help, about;
    JToolBar toolBar;
    JTabbedPane tabbedPane;
    JPanel topPanel;
    JButton btnNew, btnOpen, btnSave, btnUndo, btnRedo, btnCut, btnCopy, btnPaste, btnFind;

    Functionfile file;
    EditFunctions editFunctions;
    UndoManager undoManager = new UndoManager();

    public static void main(String[] args) {
        new GUI();
    }

    public GUI() {
        setLookAndFeel();
        createWindow();
        createMenuBar();
        createToolBar();
        createTabbedPane();
        createTextArea();
        createFileMenu();
        createEditMenu();
        createFormatMenu();
        createViewMenu();
        createSearchMenu();
        createToolsMenu();
        createDocumentsMenu();
        createColorMenu();
        createHelpMenu();
        createStatusBar();

        file = new Functionfile(this);
        editFunctions = new EditFunctions(textArea);

        window.setVisible(true);
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    public void createWindow() {
        window = new JFrame("Untitled - Notepad");
        window.setSize(900, 620);
        window.setMinimumSize(new Dimension(650, 450));
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.setLayout(new BorderLayout());

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (confirmSaveChanges()) {
                    System.exit(0);
                }
            }
        });
    }

    public void createTextArea() {
        textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setBackground(new Color(30, 30, 30));
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.WHITE);
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                markModified();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                markModified();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                markModified();
            }
        });

        textArea.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));

        textArea.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                updateStatusBar();
            }
        });

        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        window.add(scrollPane, BorderLayout.CENTER);
    }

    public void createToolBar() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(6, 6, 4, 6));
        toolBar.setBackground(new Color(35, 35, 35));

        btnNew = createToolButton("✚", "New", "New file");
        btnOpen = createToolButton("📂", "Open", "Open file");
        btnSave = createToolButton("💾", "Save", "Save file");
        btnUndo = createToolButton("↶", "Undo", "Undo");
        btnRedo = createToolButton("↷", "Redo", "Redo");
        btnCut = createToolButton("✂", "Cut", "Cut");
        btnCopy = createToolButton("📄", "Copy", "Copy");
        btnPaste = createToolButton("📋", "Paste", "Paste");
        btnFind = createToolButton("🔍", "Find", "Find text");

        toolBar.add(btnNew);
        toolBar.add(btnOpen);
        toolBar.add(btnSave);
        toolBar.addSeparator(new Dimension(10, 0));
        toolBar.add(btnUndo);
        toolBar.add(btnRedo);
        toolBar.addSeparator(new Dimension(10, 0));
        toolBar.add(btnCut);
        toolBar.add(btnCopy);
        toolBar.add(btnPaste);
        toolBar.addSeparator(new Dimension(10, 0));
        toolBar.add(btnFind);

        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(40, 40, 40));
        topPanel.add(toolBar, BorderLayout.NORTH);
        window.add(topPanel, BorderLayout.NORTH);
    }

    private JButton createToolButton(String label, String command, String tooltip) {
        JButton button = new JButton(label);
        button.setActionCommand(command);
        button.addActionListener(this);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(55, 55, 55));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        button.setToolTipText(tooltip);
        return button;
    }

    public void createTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(35, 35, 35));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        tabbedPane.addTab("*Unsaved Document 1", new JPanel());
        tabbedPane.setPreferredSize(new Dimension(10, 40));

        if (topPanel != null) {
            topPanel.add(tabbedPane, BorderLayout.SOUTH);
        } else {
            window.add(tabbedPane, BorderLayout.NORTH);
        }
    }

    public void createMenuBar() {
        menuBar = new JMenuBar();
        menuBar.setBackground(new Color(40, 40, 40));
        menuBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        window.setJMenuBar(menuBar);

        fileMenu = createMenu("File");
        editMenu = createMenu("Edit");
        formatMenu = createMenu("Format");
        viewMenu = createMenu("View");
        searchMenu = createMenu("Search");
        toolsMenu = createMenu("Tools");
        documentsMenu = createMenu("Documents");
        helpMenu = createMenu("Help");
        colorMenu = createMenu("Color");

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(viewMenu);
        menuBar.add(searchMenu);
        menuBar.add(toolsMenu);
        menuBar.add(documentsMenu);
        menuBar.add(helpMenu);
        menuBar.add(colorMenu);
    }

    public void createFileMenu() {
        newItem = createMenuItem("New", "New");
        open = createMenuItem("Open", "Open");
        save = createMenuItem("Save", "Save");
        saveAs = createMenuItem("Save As", "Save As");
        exit = createMenuItem("Exit", "Exit");

        fileMenu.add(newItem);
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.addSeparator();
        fileMenu.add(exit);
    }

    public void createSearchMenu() {
        findItem = createMenuItem("Find", "Find");
        replaceItem = createMenuItem("Replace", "Replace");
        searchMenu.add(findItem);
        searchMenu.add(replaceItem);
    }

    public void createToolsMenu() {
        // Tools menu currently empty; can be extended later
    }

    public void createDocumentsMenu() {
        // Documents menu currently empty; can be extended later
    }

    private JMenu createMenu(String title) {
        JMenu menu = new JMenu(title);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return menu;
    }

    public void createEditMenu() {
        undoItem = createMenuItem("Undo", "Undo");
        redoItem = createMenuItem("Redo", "Redo");
        cut = createMenuItem("Cut", "Cut");
        copy = createMenuItem("Copy", "Copy");
        paste = createMenuItem("Paste", "Paste");
        deleteItem = createMenuItem("Delete", "Delete");
        selectAll = createMenuItem("Select All", "Select All");

        undoItem.setEnabled(false);
        redoItem.setEnabled(false);

        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.addSeparator();
        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.add(deleteItem);
        editMenu.addSeparator();
        editMenu.add(selectAll);
    }

    public void createFormatMenu() {
        wordWrap = new JCheckBoxMenuItem("Word Wrap", false);
        wordWrap.setActionCommand("Word Wrap");
        wordWrap.addActionListener(this);

        JMenu fontMenu = new JMenu("Font");
        fontMenu.add(createMenuItem("Consolas", "Font-Consolas"));
        fontMenu.add(createMenuItem("Serif", "Font-Serif"));
        fontMenu.add(createMenuItem("Monospaced", "Font-Monospaced"));

        JMenu sizeMenu = new JMenu("Font Size");
        sizeMenu.add(createMenuItem("12", "Size-12"));
        sizeMenu.add(createMenuItem("14", "Size-14"));
        sizeMenu.add(createMenuItem("16", "Size-16"));
        sizeMenu.add(createMenuItem("18", "Size-18"));
        sizeMenu.add(createMenuItem("20", "Size-20"));

        formatMenu.add(wordWrap);
        formatMenu.addSeparator();
        formatMenu.add(fontMenu);
        formatMenu.add(sizeMenu);
    }

    public void createViewMenu() {
        statusBarItem = new JCheckBoxMenuItem("Status Bar", true);
        statusBarItem.setActionCommand("Status Bar");
        statusBarItem.addActionListener(this);
        viewMenu.add(statusBarItem);
    }

    public void createColorMenu() {
        textColorItem = createMenuItem("Text Color", "Text Color");
        backgroundColorItem = createMenuItem("Background Color", "Background Color");
        colorMenu.add(textColorItem);
        colorMenu.add(backgroundColorItem);
    }

    public void createHelpMenu() {
        help = createMenuItem("Help", "Help");
        about = createMenuItem("About", "About");

        helpMenu.add(help);
        helpMenu.addSeparator();
        helpMenu.add(about);
    }

    public void createStatusBar() {
        statusLabel = new JLabel("Plain Text    Spaces: 4    Ln 1, Col 1    INS");
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(30, 30, 30));
        statusLabel.setForeground(new Color(200, 200, 200));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        window.add(statusLabel, BorderLayout.SOUTH);
    }

    private JMenuItem createMenuItem(String title, String command) {
        JMenuItem item = new JMenuItem(title);
        item.setActionCommand(command);
        item.addActionListener(this);
        return item;
    }

    private void markModified() {
        if (!modified) {
            modified = true;
            updateWindowTitle();
        }
        updateUndoRedo();
    }

    public void setModified(boolean modified) {
        this.modified = modified;
        updateWindowTitle();
    }

    public void updateWindowTitle() {
        String title = (file != null && file.hasCurrentFile()) ? file.getCurrentFileName() : "Untitled";
        if (modified) {
            title += " *";
        }
        window.setTitle(title);
    }

    private boolean confirmSaveChanges() {
        if (!modified) {
            return true;
        }

        int option = JOptionPane.showConfirmDialog(window,
                "You have unsaved changes. Do you want to save before continuing?",
                "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
            return false;
        }

        if (option == JOptionPane.YES_OPTION) {
            return file.savefile();
        }

        return true;
    }

    private void updateUndoRedo() {
        undoItem.setEnabled(undoManager.canUndo());
        redoItem.setEnabled(undoManager.canRedo());
    }

    private void updateStatusBar() {
        try {
            int position = textArea.getCaretPosition();
            int line = textArea.getLineOfOffset(position) + 1;
            int column = position - textArea.getLineStartOffset(line - 1) + 1;
            String text = textArea.getText();
            int words = text.trim().isEmpty() ? 0 : text.trim().split("\\s+").length;
            int length = text.length();
            statusLabel.setText(String.format("Ln %d, Col %d    Words: %d    Length: %d", line, column, words, length));
        } catch (BadLocationException ex) {
            statusLabel.setText("Ln 1, Col 1    Words: 0    Length: 0");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "New":
                if (confirmSaveChanges()) {
                    file.newfile();
                    setModified(false);
                }
                break;
            case "Open":
                if (confirmSaveChanges()) {
                    file.openfile();
                    setModified(false);
                }
                break;
            case "Save":
                if (file.savefile()) {
                    setModified(false);
                }
                break;
            case "Save As":
                if (file.saveasfile()) {
                    setModified(false);
                }
                break;
            case "Exit":
                if (confirmSaveChanges()) {
                    System.exit(0);
                }
                break;
            case "Undo":
                try {
                    if (undoManager.canUndo()) {
                        undoManager.undo();
                    }
                } catch (CannotUndoException ignored) {
                }
                updateUndoRedo();
                break;
            case "Redo":
                try {
                    if (undoManager.canRedo()) {
                        undoManager.redo();
                    }
                } catch (CannotRedoException ignored) {
                }
                updateUndoRedo();
                break;
            case "Cut":
                editFunctions.cut();
                break;
            case "Copy":
                editFunctions.copy();
                break;
            case "Paste":
                editFunctions.paste();
                break;
            case "Delete":
                textArea.replaceSelection("");
                break;
            case "Select All":
                editFunctions.selectAll();
                break;
            case "Word Wrap":
                wordWrapOn = !wordWrapOn;
                textArea.setLineWrap(wordWrapOn);
                textArea.setWrapStyleWord(wordWrapOn);
                wordWrap.setSelected(wordWrapOn);
                scrollPane.setHorizontalScrollBarPolicy(wordWrapOn ? JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                        : JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                break;
            case "Font-Consolas":
                textArea.setFont(new Font("Consolas", Font.PLAIN, textArea.getFont().getSize()));
                break;
            case "Font-Serif":
                textArea.setFont(new Font("Serif", Font.PLAIN, textArea.getFont().getSize()));
                break;
            case "Font-Monospaced":
                textArea.setFont(new Font("Monospaced", Font.PLAIN, textArea.getFont().getSize()));
                break;
            case "Size-12":
                textArea.setFont(textArea.getFont().deriveFont(12f));
                break;
            case "Size-14":
                textArea.setFont(textArea.getFont().deriveFont(14f));
                break;
            case "Size-16":
                textArea.setFont(textArea.getFont().deriveFont(16f));
                break;
            case "Size-18":
                textArea.setFont(textArea.getFont().deriveFont(18f));
                break;
            case "Size-20":
                textArea.setFont(textArea.getFont().deriveFont(20f));
                break;
            case "Status Bar":
                statusBarOn = !statusBarOn;
                statusLabel.setVisible(statusBarOn);
                statusBarItem.setSelected(statusBarOn);
                break;
            case "Text Color":
                Color textColor = JColorChooser.showDialog(window, "Choose Text Color", textArea.getForeground());
                if (textColor != null) {
                    textArea.setForeground(textColor);
                    textArea.setCaretColor(textColor);
                }
                break;
            case "Background Color":
                Color backgroundColor = JColorChooser.showDialog(window, "Choose Background Color",
                        textArea.getBackground());
                if (backgroundColor != null) {
                    textArea.setBackground(backgroundColor);
                }
                break;
            case "Find":
                findText();
                break;
            case "Replace":
                replaceText();
                break;
            case "Help":
                JOptionPane.showMessageDialog(window,
                        "Simple Notepad Application\n\n" + "Features:\n" + "- Create, Open, Save, and Save As files\n"
                                + "- Undo / Redo / Cut / Copy / Paste / Delete / Select All\n"
                                + "- Toggle Word Wrap and choose font family / size\n"
                                + "- Change text and background colors\n" + "- Show or hide the status bar\n",
                        "Help", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "About":
                JOptionPane.showMessageDialog(window,
                        "Notepad UI Demo\n" + "Built with Java Swing\n" + "Designed to match a modern notepad layout.",
                        "About Notepad", JOptionPane.INFORMATION_MESSAGE);
                break;
            default:
                break;
        }

        updateStatusBar();
    }

    private void findText() {
        String query = JOptionPane.showInputDialog(window, "Find:", "Find", JOptionPane.PLAIN_MESSAGE);
        if (query == null || query.isEmpty()) {
            return;
        }
        String content = textArea.getText();
        int index = content.indexOf(query, textArea.getCaretPosition());
        if (index < 0) {
            index = content.indexOf(query);
        }
        if (index >= 0) {
            textArea.select(index, index + query.length());
            textArea.requestFocus();
        } else {
            JOptionPane.showMessageDialog(window, "Text not found.", "Find", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void replaceText() {
        javax.swing.JTextField findField = new javax.swing.JTextField();
        javax.swing.JTextField replaceField = new javax.swing.JTextField();
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.add(findField, BorderLayout.NORTH);
        panel.add(replaceField, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(window, panel, "Replace", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        String query = findField.getText();
        String replacement = replaceField.getText();
        if (query.isEmpty()) {
            return;
        }
        String content = textArea.getText();
        int index = content.indexOf(query, textArea.getCaretPosition());
        if (index < 0) {
            index = content.indexOf(query);
        }
        if (index >= 0) {
            textArea.select(index, index + query.length());
            textArea.replaceSelection(replacement);
        } else {
            JOptionPane.showMessageDialog(window, "Text not found.", "Replace", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
