import java.util.ArrayList;



public class Variable implements Term{

	private char name;
	
	public Variable(char name){
		this.name = name;
	}
	
	public char getName(){
		return this.name;
	}
	
	@Override
	public String tostring() {
		return Character.toString(this.name);
	}

	
	@Override
	public ArrayList<Variable> freeVar() {
		ArrayList<Variable> freev = new ArrayList<>();
		freev.add(this);
		return freev;
	}

	@Override
	public boolean equals(Object obj) {
        if(this.name == ((Variable)obj).name){
        	return true;
        }
       return false;		
	}

	@Override
	public Term evaluateNormal() {
		return this;	
	} 
	

	public boolean equals(Term t){
		if(t instanceof Variable){
			return this.name == ((Variable)t).getName();
		}
		else{
			return false;
		}
	}
	
}
