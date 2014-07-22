import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


public class GUI extends JFrame implements ActionListener {
	private static JTextArea display;
	private JTextField infield;
	private JTextField maxField;
	private JLabel size;
	private JButton goButton;
	private JButton readButton;
	private JButton exitButton;
	private JScrollPane scrollPane;
	
	
	public GUI(){
		buildGUI();
		setTitle("Theorem Prover");
		pack();
		setVisible(true);
	}
	
	private void buildGUI(){
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		display = new JTextArea(50,70);
		scrollPane = new JScrollPane(display);
		//scrollPane.setVisible(true);
        //scrollPane.setAutoscrolls(true);
        //scrollPane.setPreferredSize(new Dimension(300, 400));
        

		infield = new JTextField(30);
		maxField = new JTextField(2);
		goButton = new JButton("Start");
		readButton = new JButton("Read");
		exitButton = new JButton("Exit");
		readButton.addActionListener(this);
		goButton.addActionListener(this);
		exitButton.addActionListener(this);
		JPanel inputPanel = new JPanel();
		inputPanel.add(new JLabel("input directory"));
		infield.setText("/Users/Shawn/Documents/workspace/Theorem Prover/bin/test.txt");
		maxField.setText("1");
		size = new JLabel("Max Size of Agent:");
		inputPanel.add(infield);
		inputPanel.add(readButton);
		inputPanel.add(goButton);
		inputPanel.add(size);
		inputPanel.add(maxField);
		inputPanel.add(exitButton);
		contentPane.add("Center",scrollPane);
		contentPane.add("South", inputPanel);
	}

	
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == exitButton) {
			System.exit(0);
		}
	}
}