import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

class Main {

    public static void main(String[] args) {

        new TextEditor();

    }
}


class TextEditor extends JFrame implements ActionListener{
    UndoManager m = new UndoManager();
    JTextArea textArea;
    JScrollPane scrollPane;
    JLabel fontLbl;
    JSpinner fontSize;
    JButton fontColor;

    JComboBox fontBox;
    JButton und;

    JButton red;
    JMenuBar menuBar;

    JToolBar footer = new JToolBar();
    JToolBar header = new JToolBar();


    TextEditor() {

        this.m = new UndoManager();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle( "text Editor");
        this.setSize(700, 500);
//        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Times New Roman",Font.PLAIN,20));
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));




        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450,450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        fontLbl = new JLabel("Font: ");

        fontSize = new JSpinner();
        fontSize.setPreferredSize(new Dimension(50,25));
        fontSize.setValue(20);
        fontSize.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                textArea.setFont(new Font(textArea.getFont().getFamily(),Font.PLAIN,(int) fontSize.getValue()));
            }

        });

        fontColor = new JButton("Color");
        fontColor.addActionListener(this);

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        fontBox = new JComboBox(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Times New Roman");

        und = new JButton("undo");
        und.addActionListener(this);
        und.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
//        und.setBackground(Color.black);
//        und.setForeground(Color.WHITE);

        red = new JButton("redo");
        red.addActionListener(this);

        red.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
//        red.setBackground(Color.black);/
//        red.setForeground(Color.WHITE);


        this.header.add(fontLbl);

        this.header.add(fontSize);
        this.header.add(Box.createRigidArea(new Dimension(15, 5)));
        this.header.add(fontColor);
        this.header.add(Box.createRigidArea(new Dimension(15, 5)));
        this.header.add(fontBox);

        this.footer.add(Box.createRigidArea(new Dimension(275, 5)));
        this.footer.add(und);
        this.footer.add(Box.createRigidArea(new Dimension(15, 5)));
        this.footer.add(red);

        menuBar = createMenuBar();

        // Create status bar
        JLabel statusBar = new JLabel("Ready");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        this.setJMenuBar(menuBar);
        this.add(header,BorderLayout.NORTH);
        this.add(scrollPane);
        this.add(footer, BorderLayout.SOUTH);

        this.setVisible(true);

        this.textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent e) {
                TextEditor.this.m.addEdit(e.getEdit());
            }
        });

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);

    }
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();



        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);

        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.setMnemonic(KeyEvent.VK_U);
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        undoItem.addActionListener(this);
        editMenu.add(undoItem);

        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.setMnemonic(KeyEvent.VK_R);
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        redoItem.addActionListener(this);
        editMenu.add(redoItem);


        menuBar.add(editMenu);

        return menuBar;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == und  || e.getActionCommand().equals("Undo")) {
            try {
                TextEditor.this.m.undo();
            } catch (Exception er) {
                handleException("Undo Error", "An error occurred while undoing.");
            }
        }
        if(e.getSource() == red || e.getActionCommand().equals("Redo")){
            try {
                TextEditor.this.m.redo();
            } catch (Exception er) {

                handleException("Redo Error", "An error occurred while redoing.");
            }
        }

        if(e.getSource()== fontColor) {

            Color color = JColorChooser.showDialog(null, "Choose a color", Color.black);

            textArea.setForeground(color);
        }

        if(e.getSource()==fontBox) {
            textArea.setFont(new Font((String)fontBox.getSelectedItem(),Font.PLAIN,textArea.getFont().getSize()));
        }

    }

    private void handleException(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

}