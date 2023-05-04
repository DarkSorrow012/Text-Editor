import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

public class TextEditor extends JFrame implements ActionListener{

 JTextArea textArea;
 JScrollPane scrollPane;
 JLabel fontLabel;
 JSpinner fontSizeSpinner;
 JButton fontColorButton;
 JComboBox fontBox;
 JLabel lineLabel;
 JLabel wordLabel;
 JLabel letterLabel;
 
 JMenuBar menuBar;
 JMenu fileMenu;
 JMenuItem openItem;
 JMenuItem saveItem;
 JMenuItem exitItem;

 TextEditor(){
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  this.setTitle("Text Editor");
  this.setSize(500, 600);
  this.setLayout(new FlowLayout());
  this.setLocationRelativeTo(null);
  
  textArea = new JTextArea();
  textArea.setLineWrap(true);
  textArea.setWrapStyleWord(true);
  textArea.setFont(new Font("Arial",Font.PLAIN,20));
  
  scrollPane = new JScrollPane(textArea);
  scrollPane.setPreferredSize(new Dimension(450,450));
  scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
  
  fontLabel = new JLabel("Font: ");
  
  fontSizeSpinner = new JSpinner();
  fontSizeSpinner.setPreferredSize(new Dimension(50,25));
  fontSizeSpinner.setValue(20);
  fontSizeSpinner.addChangeListener(new ChangeListener() {

   @Override
   public void stateChanged(ChangeEvent e) {
    
    textArea.setFont(new Font(textArea.getFont().getFamily(),Font.PLAIN,(int) fontSizeSpinner.getValue())); 
   }
   
  });
  
  fontColorButton = new JButton("Color");
  fontColorButton.addActionListener(this);
  
  String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
  
  fontBox = new JComboBox(fonts);
  fontBox.addActionListener(this);
  fontBox.setSelectedItem("Arial");
  
  // ----- menubar -----
  
   menuBar = new JMenuBar();
   fileMenu = new JMenu("File");
   openItem = new JMenuItem("Open");
   saveItem = new JMenuItem("Save");
   exitItem = new JMenuItem("Exit");
   
   openItem.addActionListener(this);
   saveItem.addActionListener(this);
   exitItem.addActionListener(this);
   
   fileMenu.add(openItem);
   fileMenu.add(saveItem);
   fileMenu.add(exitItem);
   menuBar.add(fileMenu);
  
  // ----- /menubar -----
  
  // Initialize line, word, and letter count labels
  lineLabel = new JLabel("Lines: 1");
  wordLabel = new JLabel("Words: 0");
  letterLabel = new JLabel("Letters: 0");
  
  // Add document change listener to count lines, words, and letters
  textArea.getDocument().addDocumentListener(new DocumentListener(){
    public void changedUpdate(DocumentEvent e) {
      updateLabels();
    }
    public void removeUpdate(DocumentEvent e) {
      updateLabels();
    }
    public void insertUpdate(DocumentEvent e) {
      updateLabels();
    }
    
    private void updateLabels(){
      String text = textArea.getText();
      int lineCount = textArea.getLineCount();
      int wordCount = text.isEmpty() ? 0 : text.split("\\s+").length;
      int letterCount = text.length();

      lineLabel.setText("Lines: " + lineCount);
      wordLabel.setText("Words: " + wordCount);
      letterLabel.setText("Letters: " + letterCount);
    }
  });
  
  // Add line, word, and letter count labels to the frame
  this.add(fontLabel);
  this.add(fontSizeSpinner);
  this.add(fontColorButton);
  this.add(fontBox);
  this.add(scrollPane);
  this.add(lineLabel);
  this.add(wordLabel);
  this.add(letterLabel);
  
  this.setJMenuBar(menuBar);
  this.setVisible(true);
 }
 @Override
 public void actionPerformed(ActionEvent e) {
  
  if(e.getSource()==fontColorButton) {
   JColorChooser colorChooser = new JColorChooser();
   
   Color color = colorChooser.showDialog(null, "Choose a color", Color.black);
   
   textArea.setForeground(color);
  }
  
  if(e.getSource()==fontBox) {
   textArea.setFont(new Font((String)fontBox.getSelectedItem(),Font.PLAIN,textArea.getFont().getSize()));
  }
  
  if(e.getSource()==openItem) {
   JFileChooser fileChooser = new JFileChooser();
   fileChooser.setCurrentDirectory(new File("."));
   FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
   fileChooser.setFileFilter(filter);
   
   int response = fileChooser.showOpenDialog(null);
   
   if(response == JFileChooser.APPROVE_OPTION) {
    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
    Scanner fileIn = null;
    
    try {
     fileIn = new Scanner(file);
     if(file.isFile()) {
      while(fileIn.hasNextLine()) {
       String line = fileIn.nextLine()+"\n";
       textArea.append(line);
      }
     }
    } catch (FileNotFoundException e1){
    	e1.printStackTrace();
    }
    finally {
    fileIn.close();
    }
    }
    }

    if(e.getSource()==saveItem) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File("."));
    int response = fileChooser.showSaveDialog(null);

    if(response == JFileChooser.APPROVE_OPTION) {
    File file;
    PrintWriter fileOut = null;

    file = new File(fileChooser.getSelectedFile().getAbsolutePath());
    try {
     fileOut = new PrintWriter(file);
     fileOut.println(textArea.getText());
    } catch (FileNotFoundException e1) {
     e1.printStackTrace();
    }
    finally {
     fileOut.close();
    }

    }
    }

    if(e.getSource()==exitItem) {
    System.exit(0);
    }
    }

    public static void main(String[] args) {
    new TextEditor();
    }
    }