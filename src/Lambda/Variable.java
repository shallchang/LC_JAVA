package Lambda;
import java.util.ArrayList;



public class Variable implements Term{

	private String name;
	
	public Variable(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	@Override
	public String tostring() {
		return this.name;
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
			return this.name.equals(((Variable)t).getName());
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
		Variable mirror = new Variable(this.name);
		return mirror;
	}

	@Override
	public Term rawReduction() {
		return this;
	}
}
