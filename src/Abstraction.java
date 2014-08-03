import java.util.ArrayList;


public class Abstraction implements Term{

	private char name;
	private Term toTerm;
	private IntermediateSub iSub = null;
	
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
		if(this.iSub == null) return "\\" + this.name + "." + toTerm.tostring();
		else return "\\" + this.name + "."+ toTerm.tostring() + this.iSub.tostring();
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
	public Term evaluateNormal(boolean exsub) {
		if(this.iSub != null){
			return new Abstraction(this.name, this.toTerm.subs(iSub.getFrom(), iSub.getTo()));
		}
		
		System.out.println("not sub");
		return new Abstraction(this.name, toTerm.evaluateNormal(exsub));
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
	public Term evaluateCbn(boolean exsub) {
		return this;
	}

	
	@Override
	public Term evaluateCbv(boolean exsub) {
		return this;
	}
	

	@Override
	public Term headReduction(boolean exsub) {
		return new Abstraction(this.name, toTerm.headReduction(exsub));
	}

	@Override
	public Term applicativeOrder(boolean exsub) {
		return new Abstraction(this.name, toTerm.applicativeOrder(exsub));
	}

	@Override
	public void setIntermediateSub(IntermediateSub isub) {
		this.iSub = isub;
	}

	@Override
	public Term subs(Variable subfrom, Term subto) {
		return new Abstraction(this.name, this.toTerm.subs(subfrom, subto));
	}

	@Override
	public IntermediateSub getIsub() {
		return this.iSub;
	}

	@Override
	public Term mirror() {
		return new Abstraction(name, toTerm.mirror());
	}
}
