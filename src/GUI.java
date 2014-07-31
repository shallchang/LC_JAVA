import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//\f.(\x.f(xx))(\x.f(xx))
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
	private String type = "";
	
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
		String[] reductionStrategies = { "normal order", "call-by-name", "call-by-value", "head reduction"};

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
        
        
        
        outputArea = new JTextArea(20,40);
        outputArea.setBackground(new Color(248, 213, 131));
        outputArea.setLineWrap(true);
        outputArea.setBorder(BorderFactory.createEmptyBorder(10,20,10,10));
        
        
        //output scroll pane
        JScrollPane outputScroll = new JScrollPane(outputArea);
        
       
        //output text area
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setPreferredSize(new Dimension(1100, 500));
        outputPanel.setBackground(new Color(248, 213, 131));
        
        
        outputPanel.add(BorderLayout.LINE_START, outputScroll);
        
        
        
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
        
        
        //type assignment output scroll pane
        JScrollPane typeoutScroll = new JScrollPane(typeArea);
        
        
        outputPanel.add(BorderLayout.LINE_END, typeoutScroll);
        
        
        
        getContentPane().add("South", outputPanel);
        
        
		pack();
		setVisible(true);
	}
	
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == resetButton){
			inputField.setText("");
			outputArea.setText("");
			typeArea.setText("");
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
			nextButton.setEnabled(false);
			abortButton.setEnabled(false);
		}
		if(e.getSource() == doButton){
			output = "";
			type = "";
			abort = false;
			outputArea.setText(output);
			typeArea.setText(type);
			
			
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
					
					//type assignment
					PPC ppc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
					
					type += "Lambda term: " + newTerm.tostring()+ "\n";
					type += "Context created: " + ppc.getSubject().tostring() + "\n";
			        type += "Type assigned:" + ppc.getPredicate().tostring() + "\n";
					
			        typeArea.setText(type);
			        
			        
			        //reduction
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
						
						PPC pc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
						
						type += "Lambda term: " + newTerm.tostring()+ "\n";
						type += "Context created: " + pc.getSubject().tostring() + "\n";
				        type += "Type assigned:" + pc.getPredicate().tostring() + "\n";
				        type += "------------------------------------------------------\n";
				        
				        typeArea.setText(type);
						
				        
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
	        	switch (step) {
				case "normalize":
					System.out.println("cbn");
					
					output += newTerm.tostring()+"\n";
					
					//type assignment
					PPC ppc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
					
					type += "Lambda term: " + newTerm.tostring()+ "\n";
					type += "Context created: " + ppc.getSubject().tostring() + "\n";
			        type += "Type assigned:" + ppc.getPredicate().tostring() + "\n";
					
			        typeArea.setText(type);
			        
			        
			        //reduction
					intermediateTerm = newTerm;
					
					while(true){
						if(abort){
							break;
						}
						Term tmp = intermediateTerm; 
				        intermediateTerm = intermediateTerm.evaluateCbn();
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
						
						PPC pc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
						
						type += "Lambda term: " + newTerm.tostring()+ "\n";
						type += "Context created: " + pc.getSubject().tostring() + "\n";
				        type += "Type assigned:" + pc.getPredicate().tostring() + "\n";
				        type += "------------------------------------------------------\n";
				        
				        typeArea.setText(type);
						
				        
						intermediateTerm = newTerm.evaluateCbn();
						output += "==> " + intermediateTerm.tostring()+"\n";
			        	outputArea.setText(output);
			        	
					}
					break;

				default:
					break;
				}
	        }
	        else if(strategy.equals("call-by-value")){
	        	switch (step) {
				case "normalize":
					output += newTerm.tostring()+"\n";
					
					//type assignment
					PPC ppc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
					
					type += "Lambda term: " + newTerm.tostring()+ "\n";
					type += "Context created: " + ppc.getSubject().tostring() + "\n";
			        type += "Type assigned:" + ppc.getPredicate().tostring() + "\n";
					
			        typeArea.setText(type);
			        
			        
			        //reduction
					intermediateTerm = newTerm;
					
					while(true){
						if(abort){
							break;
						}
						Term tmp = intermediateTerm; 
				        intermediateTerm = intermediateTerm.evaluateCbv();
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
						
						PPC pc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
						
						type += "Lambda term: " + newTerm.tostring()+ "\n";
						type += "Context created: " + pc.getSubject().tostring() + "\n";
				        type += "Type assigned:" + pc.getPredicate().tostring() + "\n";
				        type += "------------------------------------------------------\n";
				        
				        typeArea.setText(type);
						
				        
						intermediateTerm = newTerm.evaluateCbv();
						output += "==> " + intermediateTerm.tostring()+"\n";
			        	outputArea.setText(output);
			        	
					}
					break;

				default:
					break;
				}
	        }
	        else if(strategy.equals("head reduction")){
	        	switch (step) {
				case "normalize":
					System.out.println("head");
					
					output += newTerm.tostring()+"\n";
					
					//type assignment
					PPC ppc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
					
					type += "Lambda term: " + newTerm.tostring()+ "\n";
					type += "Context created: " + ppc.getSubject().tostring() + "\n";
			        type += "Type assigned:" + ppc.getPredicate().tostring() + "\n";
					
			        typeArea.setText(type);
			        
			        
			        //reduction
					intermediateTerm = newTerm;
					
					while(true){
						if(abort){
							break;
						}
						Term tmp = intermediateTerm; 
				        intermediateTerm = intermediateTerm.headReduction();
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
						
						PPC pc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
						
						type += "Lambda term: " + newTerm.tostring()+ "\n";
						type += "Context created: " + pc.getSubject().tostring() + "\n";
				        type += "Type assigned:" + pc.getPredicate().tostring() + "\n";
				        type += "------------------------------------------------------\n";
				        
				        typeArea.setText(type);
						
				        
						intermediateTerm = newTerm.headReduction();
						output += "==> " + intermediateTerm.tostring()+"\n";
			        	outputArea.setText(output);
			        	
					}
					break;

				default:
					break;
				}
	        }
		}
		if(e.getSource() == nextButton){
			LC lc = new LC();
			
			Term tmp = intermediateTerm;
			
			
			PPC pc = lc.ppc(tmp, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			
			type += "Lambda term: " + tmp.tostring()+ "\n";
			type += "Context created: " + pc.getSubject().tostring() + "\n";
	        type += "Type assigned:" + pc.getPredicate().tostring() + "\n";
			type += "------------------------------------------------------\n";
	        
	        typeArea.setText(type);
	        

	        switch (strategy) {
	        case "normal order":
	        	intermediateTerm = intermediateTerm.evaluateNormal();
	        	break;
	        case "call-by-name":
	        	intermediateTerm = intermediateTerm.evaluateCbn();
	        	break;
	        case "call-by-value":
	        	intermediateTerm = intermediateTerm.evaluateCbv();
	        	break;
	        case "head reduction":
	        	intermediateTerm = intermediateTerm.headReduction();
	        	break;
	        }


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