import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

///f.(/x.f(xx))(/x.f(xx))
public class GUI extends JFrame implements ActionListener {
	private JButton resetButton;
	private JComboBox<String> strategyList;
	private JComboBox<String> steplists;
	private JButton doButton;
	private JButton nextButton;
	private JButton abortButton;
	private JTextField inputField;
	private JTextArea outputArea;
	private JTextArea typeArea;
	private String strategy = "normal order";
	private String step = "normalize";
	
	private Term intermediateTerm;
	private String output = "";
	private boolean abort = false;
	
	public static void main(String [] args){
		new GUI();
	}
	
	public GUI(){
		//set the JFrame
		setTitle("Lambda term reducer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		
		//set the JPanel
		JPanel topPanel = new JPanel();
		topPanel.setBackground(new Color(248, 213, 131));
		//topPanel.setPreferredSize(new Dimension(600, 40));
		getContentPane().add("North", topPanel);
		
		
        resetButton = new JButton("reset");
		resetButton.addActionListener(this);
		
		
        //the combo boxes
		
		//the reduction strategy combo box
		String[] reductionStrategies = { "normal order", "call-by-name"};

		//Create the combo box, select the item at index 0.
		//Indices start at 0.
		strategyList = new JComboBox<>(reductionStrategies);
		strategyList.setSelectedIndex(0);
		strategyList.addActionListener(this);
		//strategyList.setBounds(200, 20, 80,20);


		//the trace mathod
		String[] steps = { "normalize", "trace", "single step"};

		//Create the combo box, select the item at index 0.
		//Indices start at 0.
		steplists = new JComboBox<>(steps);
		steplists.setSelectedIndex(0);
		steplists.addActionListener(this);
		//strategyList.setBounds(20, 35, 260, 20);

		
		//start button
		
		doButton = new JButton("start");
		doButton.addActionListener(this);
		
		
		topPanel.add(resetButton);
        topPanel.add(strategyList);
        topPanel.add(steplists);
        
        
        //input text field
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(248, 213, 131));
        
        //inputPanel.setPreferredSize(new Dimension(600, 900));
        
        inputField = new JTextField(30);
        inputPanel.add(inputField);
        inputPanel.add(doButton);
        
        getContentPane().add("Center", inputPanel);
        
        
        
        //output scroll pane
        //JScrollPane outputScroll = new JScrollPane();
        
        
        
        //output text area
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setPreferredSize(new Dimension(1100, 500));
        outputPanel.setBackground(new Color(248, 213, 131));
        outputArea = new JTextArea(20,40);
        outputArea.setBackground(new Color(248, 213, 131));
        outputArea.setLineWrap(true);
        
        outputArea.setBorder(BorderFactory.createEmptyBorder(10,20,10,10));
        
        
        
        outputPanel.add(BorderLayout.LINE_START, outputArea);
        
        
        
        //next and abort buttons in the middle
        
        Box bv = Box.createVerticalBox();
        
        nextButton = new JButton("next");
        nextButton.addActionListener(this);
        nextButton.setEnabled(false);
        
        bv.add(nextButton);
        
        abortButton = new JButton("abort");
        abortButton.setEnabled(false);
        abortButton.addActionListener(this);
        
        
        bv.add(abortButton);
        bv.setBackground(new Color(2, 213, 131));
        
        outputPanel.add(BorderLayout.CENTER, bv);
        
        
        
        typeArea = new JTextArea(20,40);
        typeArea.setBackground(new Color(248, 213, 131));
        typeArea.setLineWrap(true);
        
        typeArea.setBorder(BorderFactory.createEmptyBorder(10,20,10,10));
        
        //outputPanel.add(typeArea);
        outputPanel.add(BorderLayout.LINE_END, typeArea);
        
        
        
        getContentPane().add("South", outputPanel);
        
        
		pack();
		setVisible(true);
	}
	
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == resetButton){
			inputField.setText("");
		}
		if(e.getSource() == strategyList)
		{
			strategy = (String)strategyList.getSelectedItem();
		}
		if(e.getSource() == steplists)
		{
			step = (String)steplists.getSelectedItem();
		}
		if(e.getSource() == abortButton)
		{
			abort = true;
		}
		if(e.getSource() == doButton){
			output = "";
			abort = false;
			outputArea.setText(output);
			
			
			nextButton.setEnabled(true);
			abortButton.setEnabled(true);
			
			
			LC lc = new LC();
			
			String input = inputField.getText();
	        Term newTerm = lc.toTerm(input);
	        
	        System.out.println("Before");
	        
	        if(strategy.equals("normal order")){
	        	switch (step) {
				case "normalize":
					output += newTerm.tostring()+"\n";
					
					intermediateTerm = newTerm;
					
					while(true){
						if(abort){
							break;
						}
						Term tmp = intermediateTerm; 
				        intermediateTerm = intermediateTerm.evaluateNormal();
				        if(tmp.equals(intermediateTerm)){
				        	output += "Reduced to:" + intermediateTerm.tostring();
					        outputArea.setText(output);
					        nextButton.setEnabled(false);
					        abortButton.setEnabled(false);
				        	
				        	break;
				        }
				        else{
				        	output += "==> " + intermediateTerm.tostring()+"\n";
				        	outputArea.setText(output);
				        }	
					}
					break;
				case "single step":
					if(!abort){
						output += newTerm.tostring()+"\n";
						
						intermediateTerm = newTerm.evaluateNormal();
						output += "==> " + intermediateTerm.tostring()+"\n";
			        	outputArea.setText(output);
					}
					break;

				default:
					break;
				}
	        }
	        else if(strategy.equals("call-by-name")){
	        	
	        	
	        	
	        }
	        else{
	        	
	        	
	        }
		}
		if(e.getSource() == nextButton){
			Term tmp = intermediateTerm;
			intermediateTerm = intermediateTerm.evaluateNormal();
			
			
			
			if(tmp.equals(intermediateTerm)){
				output += "Reduced to:" + intermediateTerm.tostring();
		        outputArea.setText(output);
		        nextButton.setEnabled(false);
		        abortButton.setEnabled(false);
			}
			else{
				output += "==> " + intermediateTerm.tostring()+"\n";
	        	outputArea.setText(output);
	        	
			}
		}
		
		
	}
	
}