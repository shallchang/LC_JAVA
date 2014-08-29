package Lambda;
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
	public Term evaluateNormal(boolean exsub) {
		return this;	
	} 
	

	@Override
	public boolean equals(Term t){
		if(t instanceof Variable){
			return this.name == ((Variable)t).getName();
		}
	    return false;
	}

	@Override
	public Term evaluateCbn(boolean exsub) {
		return this;
	}

	@Override
	public Term evaluateCbv(boolean exsub) {
		return this;
	}

	@Override
	public Term headReduction(boolean exsub) {
		return this;
	}

	
	@Override
	public Term applicativeOrder(boolean exsub) {
		return this;
	}


	@Override
	public Term subs(Variable subfrom, Term subto) {
		if(this.equals(subfrom)){
			return subto.mirror();
		}
		return this;
	}

	@Override
	public Term mirror() {
		Variable mirror = new Variable(name);
		return mirror;
	}

	@Override
	public Term rawReduction() {
		return this;
	}
}
