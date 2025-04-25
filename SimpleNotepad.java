import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SimpleNotepad extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private String currentFile;

    public SimpleNotepad() {
        setTitle("Simple Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        newMenuItem.addActionListener(this);
        openMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        fileChooser = new JFileChooser();

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "New":
                textArea.setText("");
                currentFile = null;
                break;
            case "Open":
                int returnVal = fileChooser.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        StringBuilder text = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            text.append(line).append("\n");
                        }
                        textArea.setText(text.toString());
                        currentFile = file.getAbsolutePath();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case "Save":
                if (currentFile == null) {
                    int saveVal = fileChooser.showSaveDialog(this);
                    if (saveVal == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        saveToFile(file);
                    }
                } else {
                    File file = new File(currentFile);
                    saveToFile(file);
                }
                break;
            case "Exit":
                System.exit(0);
                break;
        }
    }

    private void saveToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(textArea.getText());
            currentFile = file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleNotepad::new);
    }
}
