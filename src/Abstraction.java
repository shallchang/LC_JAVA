import java.util.ArrayList;
import java.util.Iterator;


public class Abstraction implements Term{

	private char name;
	private Term toTerm;
	
	public Abstraction(char name, Term toTerm){
		this.name = name;
		this.toTerm = toTerm;
	}
	
	public Term getTerm(){
		return this.toTerm;
	}
	
	public char getName(){
		return this.name;
	}
	
	@Override
	public String tostring() {
		return "(/" + this.name + "."+ toTerm.tostring()+")";
	}

	@Override
	public ArrayList<Variable> freeVar() {
		
		ArrayList<Variable> free = toTerm.freeVar();
	
		ArrayList<Variable> toRemove = new ArrayList<>();
		
		for (Variable var : free) {
			if(var.getName() == this.name){
				toRemove.add(var);
			}
		}
		free.removeAll(toRemove);
		
		return free;
	}

	@Override
	public Term evaluate() {
		return new Abstraction(this.name, toTerm.evaluate());
	}
}
