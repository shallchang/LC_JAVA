import java.util.ArrayList;



public class Variable implements Term{

	private char name;
	private IntermediateSub iSub = null;
	
	public Variable(char name){
		this.name = name;
	}
	
	public char getName(){
		return this.name;
	}
	
	@Override
	public String tostring() {
		if(this.iSub == null) return Character.toString(this.name);
		else return Character.toString(this.name)+this.iSub.tostring();
	}

	
	@Override
	public ArrayList<Variable> freeVar() {
		ArrayList<Variable> freev = new ArrayList<>();
		freev.add(this);
		return freev;
	}

	@Override
	public Term evaluateNormal(boolean exsub) {
		if(this.iSub != null){
			if(iSub.getFrom().equals(this)){
				return iSub.getTo();
			}
		}
		
		return this;	
	} 
	

	@Override
	public boolean equals(Term t){
		if(t instanceof Variable){
			return this.name == ((Variable)t).getName();
		}
		else{
			return false;
		}
	}

	@Override
	public Term evaluateCbn(boolean exsub) {
		if(this.iSub != null){
			if(iSub.getFrom().equals(this)){
				return iSub.getTo();
			}
		}
		
		return this;
	}

	@Override
	public Term evaluateCbv(boolean exsub) {
		if(this.iSub != null){
			if(iSub.getFrom().equals(this)){
				return iSub.getTo();
			}
		}
		
		return this;
	}

	@Override
	public Term headReduction(boolean exsub) {
		if(this.iSub != null){
			if(iSub.getFrom().equals(this)){
				return iSub.getTo();
			}
		}
		
		return this;
	}

	
	@Override
	public Term applicativeOrder(boolean exsub) {
		if(this.iSub != null){
			if(iSub.getFrom().equals(this)){
				return iSub.getTo();
			}
		}
		
		return this;
	}

	@Override
	public void setIntermediateSub(IntermediateSub isub) {
		this.iSub = isub;
	}

	@Override
	public Term subs(Variable subfrom, Term subto) {
		if(this.equals(subfrom)){
			return subto.mirror();
		}
		return this;
	}

	@Override
	public IntermediateSub getIsub() {
		return this.iSub;
	}

	@Override
	public Term mirror() {
		return new Variable(name);
	}
}
