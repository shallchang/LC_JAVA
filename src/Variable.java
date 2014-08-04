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
			if(iSub.getFrom().getName() == this.name){
				return iSub.getTo();
			}
		}
		
		this.iSub = null;
		
		return this;	
	} 
	

	@Override
	public boolean equals(Term t){
		if(t instanceof Variable){
			return this.name == ((Variable)t).getName() && (!(t.containsSub() || this.containsSub()) || (t.containsSub() && this.containsSub()));
		}
		else{
			return false;
		}
	}

	@Override
	public Term evaluateCbn(boolean exsub) {
		if(this.iSub != null){
			if(iSub.getFrom().getName() == this.name){
				return iSub.getTo();
			}
		}
		
		this.iSub = null;
		
		return this;
	}

	@Override
	public Term evaluateCbv(boolean exsub) {
		if(this.iSub != null){
			if(iSub.getFrom().getName() == this.name){
				return iSub.getTo();
			}
		}
		
		this.iSub = null;
		
		return this;
	}

	@Override
	public Term headReduction(boolean exsub) {
		if(this.iSub != null){
			if(iSub.getFrom().getName() == this.name){
				return iSub.getTo();
			}
		}
		
		this.iSub = null;
		
		return this;
	}

	
	@Override
	public Term applicativeOrder(boolean exsub) {
		if(this.iSub != null){
			if(iSub.getFrom().getName() == this.name){
				return iSub.getTo();
			}
		}
		
		this.iSub = null;
		
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
		
		this.iSub = null;
		return this;
	}

	@Override
	public IntermediateSub getIsub() {
		return this.iSub;
	}

	@Override
	public Term mirror() {
		Variable mirror = new Variable(name);
		mirror.setIntermediateSub(this.iSub);
		return mirror;
	}

	@Override
	public boolean containsSub() {
		return this.iSub != null;
	}
}
