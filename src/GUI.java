import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
	private JCheckBox exsubBox;
	private String strategy = "normal order";
	private String step = "trace";
	
	private Term intermediateTerm;
	private String output = "";
	private boolean abort = false;
	private boolean exsub = false;
	private String type = "";
	private Term threadTerm;
	
	
	public static void main(String [] args){
		new GUI();
	}
	
	public GUI(){
		//set the JFrame
		setTitle("Lambda Term Reducer");
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
		String[] reductionStrategies = { "normal order", "call-by-name", "call-by-value", "head reduction", "applicative order"};

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
		steplists.setSelectedIndex(1);
		steplists.addActionListener(this);
		
		exsubBox = new JCheckBox("explicit substitution");
		exsubBox.addActionListener(this);
		

		
		//start button
		
		doButton = new JButton("start");
		doButton.addActionListener(this);
		
		
		topPanel.add(resetButton);
        topPanel.add(strategyList);
        topPanel.add(steplists);
        topPanel.add(exsubBox);
        
        
        //input text field
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(248, 213, 131));
        
        //inputPanel.setPreferredSize(new Dimension(600, 900));
        
        inputField = new JTextField(37);
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
	
	
	public Context mergeSts(Context context){
		ArrayList<Statement> tmp = new ArrayList<>();


		
		for (int i = 0; i < context.getContext().size(); i++) {
			for(int j = i+1; j < context.getContext().size(); j++){
				if(context.getContext().get(i).getSubject().equals(context.getContext().get(j).getSubject())){
					tmp.add(context.getContext().get(j));
				}
			}
		}
		
		
		context.getContext().removeAll(tmp);

		return context;
	}

	private class RunnableThread extends Thread {

		public RunnableThread() {
			
		}
		
		
		public void run() {
			
			LC lc = new LC();
			
			output += threadTerm.tostring()+"\n";
			
			//type assignment
			PPC ppc = lc.ppc(threadTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			
			if(ppc == null){
				type += "untypable\n";
			}
			else{
				type += mergeSts(ppc.getSubject()).tostring() + " |- " + threadTerm.tostring() + " : " + ppc.getPredicate().tostring() + "\n";
			}
			
	        typeArea.setText(type);
	        
	        //reduction
			intermediateTerm = threadTerm;
			
			while(true){
				if(abort){
					break;
				}
				
				try{
	    			Thread.sleep((long)(Math.random()*100));
	    		}catch(InterruptedException e){
	    		}
				
				Term tmp = intermediateTerm.mirror(); 

				System.out.println("tmp: " + tmp.tostring());
				
				switch (strategy) {
				case "normal order":
					intermediateTerm = intermediateTerm.evaluateNormal(exsub);
					break;
				case "call-by-name":
					intermediateTerm = intermediateTerm.evaluateCbn(exsub);
					break;
				case "call-by-value":
					intermediateTerm = intermediateTerm.evaluateCbv(exsub);
					break;
				case "head reduction":
					intermediateTerm = intermediateTerm.headReduction(exsub);
					break;
				case "applicative order":
					intermediateTerm = intermediateTerm.applicativeOrder(exsub);
					break;
				}
				
				System.out.println("tmp after: " + tmp.tostring());
				
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
		}
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
	    			
	    			if(ppc == null){
	    				type += "untypable\n";
	    			}
	    			else{
	    				type += ppc.getSubject().tostring() + " |- " + newTerm.tostring() + " : " + ppc.getPredicate().tostring() + "\n";
	    			}
	    			
	    	        typeArea.setText(type);
	    	        
	    	        //reduction
	    			intermediateTerm = newTerm;
	    			int reductionCounter = 0;
	    			
	    			while(true){
	    	
	    				Term tmp = intermediateTerm; 

	    				intermediateTerm = intermediateTerm.evaluateNormal(exsub);
	    				
	    				if(tmp.equals(intermediateTerm)){
	    		        	output += "Reduced to:" + intermediateTerm.tostring() + "\n";
	    		        	
	    		        	if(reductionCounter > 1) output += "Performed " + reductionCounter + " beta-reductions";
	    		        	else output += "Performed " + reductionCounter + " beta-reduction";
	    			        outputArea.setText(output);
	    			        nextButton.setEnabled(false);
	    			        abortButton.setEnabled(false);
	    		        	
	    		        	break;
	    		        }
	    				
	    				if(!intermediateTerm.containsSub()) reductionCounter++;
	    				
	    			}
	        	    break;
				case "trace":
					this.threadTerm = newTerm;

					RunnableThread thread = new RunnableThread();
					thread.start();
	
					break;
				case "single step":
					if(!abort){
						output += newTerm.tostring()+"\n";
						
						PPC pc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
						
						if(pc == null){
							type += "untypable\n";
						}
						else{
							type += pc.getSubject().tostring() + " |- " + newTerm.tostring() + " : " + pc.getPredicate().tostring() + "\n";
						}
				        
				        typeArea.setText(type);
						
				        
						intermediateTerm = newTerm.evaluateNormal(exsub);
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
	        		output += newTerm.tostring()+"\n";
	    			
	    			//type assignment
	    			PPC ppc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	    			
	    			if(ppc == null){
	    				type += "untypable\n";
	    			}
	    			else{
	    				type += ppc.getSubject().tostring() + " |- " + newTerm.tostring() + " : " + ppc.getPredicate().tostring() + "\n";
	    			}
	    			
	    	        typeArea.setText(type);
	    	        
	    	        //reduction
	    			intermediateTerm = newTerm;
	    			int reductionCounter = 0;
	    			
	    			while(true){
	    	
	    				Term tmp = intermediateTerm; 

	    				intermediateTerm = intermediateTerm.evaluateCbn(exsub);
	    				
	    				if(tmp.equals(intermediateTerm)){
	    		        	output += "Reduced to:" + intermediateTerm.tostring() + "\n";
	    		        	
	    		        	if(reductionCounter > 1) output += "Performed " + reductionCounter + " beta-reductions";
	    		        	else output += "Performed " + reductionCounter + " beta-reduction";
	    			        outputArea.setText(output);
	    			        nextButton.setEnabled(false);
	    			        abortButton.setEnabled(false);
	    		        	
	    		        	break;
	    		        }
	    				
	    				reductionCounter++;
	    			}
	        	    break;
				case "trace":
					this.threadTerm = newTerm;

					RunnableThread thread = new RunnableThread();
					thread.start();
					
					break;
				case "single step":
					if(!abort){
						output += newTerm.tostring()+"\n";
						
						PPC pc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
						
						if(pc == null){
							type += "untypable\n";
						}
						else{
							type += pc.getSubject().tostring() + " |- " + newTerm.tostring() + " : " + pc.getPredicate().tostring() + "\n";
						}
				        
				        typeArea.setText(type);
						
				        
						intermediateTerm = newTerm.evaluateCbn(exsub);
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
	    			
	    			if(ppc == null){
	    				type += "untypable\n";
	    			}
	    			else{
	    				type += ppc.getSubject().tostring() + " |- " + newTerm.tostring() + " : " + ppc.getPredicate().tostring() + "\n";
	    			}
	    			
	    	        typeArea.setText(type);
	    	        
	    	        //reduction
	    			intermediateTerm = newTerm;
	    			int reductionCounter = 0;
	    			
	    			while(true){
	    	
	    				Term tmp = intermediateTerm; 

	    				intermediateTerm = intermediateTerm.evaluateCbv(exsub);
	    				
	    				if(tmp.equals(intermediateTerm)){
	    		        	output += "Reduced to:" + intermediateTerm.tostring() + "\n";
	    		        	
	    		        	if(reductionCounter > 1) output += "Performed " + reductionCounter + " beta-reductions";
	    		        	else output += "Performed " + reductionCounter + " beta-reduction";
	    			        outputArea.setText(output);
	    			        nextButton.setEnabled(false);
	    			        abortButton.setEnabled(false);
	    		        	
	    		        	break;
	    		        }
	    				
	    				reductionCounter++;
	    			}
	        	    break;
				case "trace":
					this.threadTerm = newTerm;

					RunnableThread thread = new RunnableThread();
					thread.start();
					
					break;
				case "single step":
					if(!abort){
						output += newTerm.tostring()+"\n";
						
						PPC pc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
						
						if(pc == null){
							type += "untypable\n";
						}
						else{
							type += pc.getSubject().tostring() + " |- " + newTerm.tostring() + " : " + pc.getPredicate().tostring() + "\n";
						}
				        
				        typeArea.setText(type);
						
				        
						intermediateTerm = newTerm.evaluateCbv(exsub);
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
	        		output += newTerm.tostring()+"\n";
	    			
	    			//type assignment
	    			PPC ppc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	    			
	    			if(ppc == null){
	    				type += "untypable\n";
	    			}
	    			else{
	    				type += ppc.getSubject().tostring() + " |- " + newTerm.tostring() + " : " + ppc.getPredicate().tostring() + "\n";
	    			}
	    			
	    	        typeArea.setText(type);
	    	        
	    	        //reduction
	    			intermediateTerm = newTerm;
	    			int reductionCounter = 0;
	    			
	    			while(true){
	    	
	    				Term tmp = intermediateTerm; 

	    				intermediateTerm = intermediateTerm.headReduction(exsub);
	    				
	    				if(tmp.equals(intermediateTerm)){
	    		        	output += "Reduced to:" + intermediateTerm.tostring() + "\n";
	    		        	
	    		        	if(reductionCounter > 1) output += "Performed " + reductionCounter + " beta-reductions";
	    		        	else output += "Performed " + reductionCounter + " beta-reduction";
	    			        outputArea.setText(output);
	    			        nextButton.setEnabled(false);
	    			        abortButton.setEnabled(false);
	    		        	
	    		        	break;
	    		        }
	    				
	    				reductionCounter++;
	    			}
	        	    break;
				case "trace":
					
					this.threadTerm = newTerm;

					RunnableThread thread = new RunnableThread();
					thread.start();
					
					break;
				case "single step":
					if(!abort){
						output += newTerm.tostring()+"\n";
						
						PPC pc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
						
						if(pc == null){
							type += "untypable\n";
						}
						else{
							type += pc.getSubject().tostring() + " |- " + newTerm.tostring() + " : " + pc.getPredicate().tostring() + "\n";
						}
				        
				        typeArea.setText(type);
						
				        
						intermediateTerm = newTerm.headReduction(exsub);
						output += "==> " + intermediateTerm.tostring()+"\n";
			        	outputArea.setText(output);
			        	
					}
					break;

				default:
					break;
				}
	        }
	        else if(strategy.equals("applicative order")){
	        	switch (step) {
	        	case "normalize":
	        		output += newTerm.tostring()+"\n";
	    			
	    			//type assignment
	    			PPC ppc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	    			
	    			if(ppc == null){
	    				type += "untypable\n";
	    			}
	    			else{
	    				type += ppc.getSubject().tostring() + " |- " + newTerm.tostring() + " : " + ppc.getPredicate().tostring() + "\n";
	    			}
	    			
	    	        typeArea.setText(type);
	    	        
	    	        //reduction
	    			intermediateTerm = newTerm;
	    			int reductionCounter = 0;
	    			
	    			while(true){
	    	
	    				Term tmp = intermediateTerm; 

	    				intermediateTerm = intermediateTerm.applicativeOrder(exsub);
	    				
	    				if(tmp.equals(intermediateTerm)){
	    		        	output += "Reduced to:" + intermediateTerm.tostring() + "\n";
	    		        	
	    		        	if(reductionCounter > 1) output += "Performed " + reductionCounter + " beta-reductions";
	    		        	else output += "Performed " + reductionCounter + " beta-reduction";
	    			        outputArea.setText(output);
	    			        nextButton.setEnabled(false);
	    			        abortButton.setEnabled(false);
	    		        	
	    		        	break;
	    		        }
	    				
	    				reductionCounter++;
	    			}
	        	    break;
				case "trace":
					
					this.threadTerm = newTerm;

					RunnableThread thread = new RunnableThread();
					thread.start();
					
					break;
				case "single step":
					if(!abort){
						output += newTerm.tostring()+"\n";
						
						PPC pc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
						
						if(pc == null){
							type += "untypable\n";
						}
						else{
							type += pc.getSubject().tostring() + " |- " + newTerm.tostring() + " : " + pc.getPredicate().tostring() + "\n";
						}
				        
				        typeArea.setText(type);
						
				        
						intermediateTerm = newTerm.applicativeOrder(exsub);
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
			
			if(pc == null){
				type += "untypable\n";
			}
			else{
				type += pc.getSubject().tostring() + " |- " + tmp.tostring() + " : " + pc.getPredicate().tostring() + "\n";
			}
			
	        
	        typeArea.setText(type);
	        

	        switch (strategy) {
	        case "normal order":
	        	intermediateTerm = intermediateTerm.evaluateNormal(exsub);
	        	break;
	        case "call-by-name":
	        	intermediateTerm = intermediateTerm.evaluateCbn(exsub);
	        	break;
	        case "call-by-value":
	        	intermediateTerm = intermediateTerm.evaluateCbv(exsub);
	        	break;
	        case "head reduction":
	        	intermediateTerm = intermediateTerm.headReduction(exsub);
	        	break;
	        case "applicative order":
	        	intermediateTerm = intermediateTerm.applicativeOrder(exsub);
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
		if(e.getSource() == exsubBox){
			if(exsubBox.isSelected()){
				exsub = true;
			}
			else{
				exsub = false;
			}
		}
	}	
}