import java.util.ArrayList;


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
		return "\\" + this.name + "."+ toTerm.tostring();
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
	public Term evaluateNormal() {
		return new Abstraction(this.name, toTerm.evaluateNormal());
	}

	@Override
	public boolean equals(Term t) {
		if(t instanceof Abstraction){
			return this.name == ((Abstraction)t).getName() && this.toTerm.equals(((Abstraction)t).getTerm());
		}
		else{
			return false;
		}
		
	}

	
	@Override
	public Term evaluateCbn() {
		return this;
	}

	
	@Override
	public Term evaluateCbv() {
		return this;
	}
	

	@Override
	public Term headReduction() {
		return new Abstraction(this.name, toTerm.headReduction());
	}
}
