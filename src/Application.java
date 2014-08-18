/*
 * 
 * Author:Xiaohan Zhang
 * 
 * 
 */

import java.util.ArrayList;

//(\abc.ac(bc))(\xy.x)
public class Application implements Term {
	
	private Term operator;
	private Term operand;
	private IntermediateSub iSub = null;
	
	public Application(Term operator, Term operand){
		this.operator = operator;
		this.operand = operand;
	}
	
	public Term getOperator(){
		return this.operator;
	}
	
	public Term getOperand(){
		return this.operand;
	}
	
	@Override
	public String tostring() {
		String out = "";
		
		if(this.operator instanceof Abstraction){
			if(this.operand instanceof Variable){
				out = "("+this.operator.tostring() + ")" +  this.operand.tostring();
			}
			else if(this.operand instanceof Abstraction){
				out = "("+this.operator.tostring() + ")" + "(" + this.operand.tostring() + ")";
			}
			else{
				out = "("+this.operator.tostring() + ")" + "(" + this.operand.tostring() + ")";
			}
		}
		else if(this.operator instanceof Variable){
			if(this.operand instanceof Variable){
				out = this.operator.tostring()  +  this.operand.tostring();
			}
			else if(this.operand instanceof Abstraction){
				out = this.operator.tostring() + "(" + this.operand.tostring() + ")";
			}
			else{
				out = this.operator.tostring() + "(" + this.operand.tostring() + ")";
			}
		}
		else{
			if(this.operand instanceof Variable){
				out = "("+this.operator.tostring() + ")" + this.operand.tostring();
			}
			else if(this.operand instanceof Abstraction){
				out = "(" +this.operator.tostring() + ")" + "(" + this.operand.tostring() + ")";
			}
			else{
				out = this.operator.tostring() + "(" + this.operand.tostring() + ")";
			}	
		}
		
		if(this.iSub != null) out += iSub.tostring();
		
		return out;
		
	}

	@Override
	public ArrayList<Variable> freeVar() {
		ArrayList<Variable> free = operator.freeVar();
		free.addAll(operand.freeVar());
		return free;
	}

	@Override
	public Term evaluateNormal(boolean exsub) {
		
		
		if(this.iSub != null){
			Term t = this.subs(iSub.getFrom(), iSub.getTo());
			t.setIntermediateSub(null);
			return t;
		}
		
		if(this.operator.getIsub() != null){
			Term opr = this.operator.subs(this.operator.getIsub().getFrom(), this.operator.getIsub().getTo());
			opr.setIntermediateSub(null);
			
			return new Application(opr, this.operand);
		}
		
		if(this.operand.getIsub() != null){
			Term opr = operand.subs(this.operand.getIsub().getFrom(), this.operand.getIsub().getTo());
			return new Application(this.operator, opr);
		}
		
		Term cbn = this.operator.evaluateCbn(exsub);
		
		if(this.operator.equals(cbn)){
			if(cbn instanceof Abstraction){
				if(exsub){
					Term newterm = ((Abstraction)cbn).getTerm();
					
					newterm.setIntermediateSub(new IntermediateSub(new Variable(((Abstraction)cbn).getName()), this.operand));
					
					return newterm;
					
				}
				else{
					Term newterm = substitution(((Abstraction)cbn).getTerm(), new Variable(((Abstraction)cbn).getName()), this.operand);
					return newterm;
				}
				
			}
			else{
				System.out.println(this.operand.tostring());
				return new Application(this.operator, this.operand.evaluateNormal(exsub));
			}
		}
		else{
			return new Application(cbn, this.operand);
		}
	}
	
	
	public Term substitution(Term original, Variable subfrom, Term subto){
		if(original instanceof Variable){
			if(((Variable)original).equals(subfrom)){
				return subto;
			}
			else{
				return original;
			}
		}
		else if(original instanceof Abstraction){
			Variable y = new Variable(((Abstraction)original).getName());
			
			boolean fv = false;
			for(Variable var: subto.freeVar()){
				if(y.equals(var)){
					fv = true;
					break;
				}
			}
			

			if(!fv && !y.equals(subfrom)){
				return new Abstraction(y.getName(), substitution(((Abstraction)original).getTerm(), subfrom, subto));
			}
			else if(y.equals(subfrom)){
				return original;
			}
			else if(fv && !y.equals(subfrom)){
				return new Abstraction('z', substitution(substitution(((Abstraction)original).getTerm(), y , new Variable('z')), subfrom, subto));
			}
			else{
				throw new IllegalArgumentException("not in condition");
			}
		}
		else if(original instanceof Application){
			Application app = (Application)original;
			Term t1 = substitution(app.getOperator(), subfrom, subto);
			Term t2 = substitution(app.getOperand(), subfrom, subto);
			return (new Application(t1, t2));
		}
		else{
			throw new IllegalArgumentException("None type");
		}
		
	}

	@Override
	public boolean equals(Term t) {
		if(t instanceof Application){
	
			return this.operator.equals(((Application)t).getOperator()) && this.operand.equals(((Application)t).getOperand()) && (!(t.containsSub() || this.containsSub()) || (t.containsSub() && this.containsSub()));
		}
		else{
			return false;
		}
	}

	@Override
	public Term evaluateCbn(boolean exsub) {
        if(this.iSub != null) return this.subs(iSub.getFrom(), iSub.getTo());
		
		if(this.operator.getIsub() != null) return new Application(this.operator.subs(this.operator.getIsub().getFrom(), this.operator.getIsub().getTo()), this.operand);
		
		if(this.operand.getIsub() != null) return new Application(this.operator, operand.subs(this.operand.getIsub().getFrom(), this.operand.getIsub().getTo()));
		
		
		Term cbn = this.operator.evaluateCbn(exsub);
		
		if(this.operator.equals(cbn)){
			if(cbn instanceof Abstraction){
				if(exsub){
					Term newterm = ((Abstraction)cbn).getTerm();
					newterm.setIntermediateSub(new IntermediateSub(new Variable(((Abstraction)cbn).getName()), this.operand));
					
					return newterm;
					
				}
				else{
					Term newterm = substitution(((Abstraction)cbn).getTerm(), new Variable(((Abstraction)cbn).getName()), this.operand);
					return newterm;
				}
				
			}
			else{
				return new Application(cbn, operand);
			}
		}
		else{
			return new Application(cbn, this.operand);
		}
	}

	@Override
	public Term evaluateCbv(boolean exsub) {
        if(this.iSub != null) return this.subs(iSub.getFrom(), iSub.getTo());
		
		if(this.operator.getIsub() != null) return new Application(this.operator.subs(this.operator.getIsub().getFrom(), this.operator.getIsub().getTo()), this.operand);
		
		if(this.operand.getIsub() != null) return new Application(this.operator, operand.subs(this.operand.getIsub().getFrom(), this.operand.getIsub().getTo()));
		
		Term cbv = this.operator.evaluateCbv(exsub);
		
		if(this.operator.equals(cbv)){
			if(cbv instanceof Abstraction){
				if(exsub){
					Term newterm = ((Abstraction)cbv).getTerm();
					newterm.setIntermediateSub(new IntermediateSub(new Variable(((Abstraction)cbv).getName()), this.operand));
					return newterm;	
				}
				else{
					if(this.operand.evaluateCbv(exsub).equals(this.operand)){
						return substitution(((Abstraction)cbv).getTerm(), new Variable(((Abstraction)cbv).getName()), this.operand);
					}
					else{
						return new Application(cbv, this.operand.evaluateCbv(exsub));
					}
				}	
			}
			else{
				return new Application(cbv, this.operand.evaluateCbv(exsub));
			}
		}
		else{
			return new Application(cbv, this.operand);
		}			
	}

	@Override
	public Term headReduction(boolean exsub) {
        if(this.iSub != null) return this.subs(iSub.getFrom(), iSub.getTo());
		
		if(this.operator.getIsub() != null) return new Application(this.operator.subs(this.operator.getIsub().getFrom(), this.operator.getIsub().getTo()), this.operand);
		
		if(this.operand.getIsub() != null) return new Application(this.operator, operand.subs(this.operand.getIsub().getFrom(), this.operand.getIsub().getTo()));
		
		Term hr = this.operator.evaluateCbn(exsub);

		if(this.operator.equals(hr)){
			if(hr instanceof Abstraction){
				if(exsub){
					Term newterm = ((Abstraction)hr).getTerm();
					newterm.setIntermediateSub(new IntermediateSub(new Variable(((Abstraction)hr).getName()), this.operand));
					return newterm;
				}
				else{
					Term newterm = substitution(((Abstraction)hr).getTerm(), new Variable(((Abstraction)hr).getName()), this.operand);
					return newterm;
				}
			}
			else{
				return new Application(this.operator, this.operand);
			}
		}
		else{
			return new Application(hr, this.operand);
		}
	}

	@Override
	public Term applicativeOrder(boolean exsub) {
        if(this.iSub != null) return this.subs(iSub.getFrom(), iSub.getTo());
		
		if(this.operator.getIsub() != null) return new Application(this.operator.subs(this.operator.getIsub().getFrom(), this.operator.getIsub().getTo()), this.operand);
		
		if(this.operand.getIsub() != null) return new Application(this.operator, operand.subs(this.operand.getIsub().getFrom(), this.operand.getIsub().getTo()));
		
		Term app = this.operator.applicativeOrder(exsub);


		if(this.operator.equals(app)){
			if(app instanceof Abstraction){
				if(exsub){
					Term newterm = ((Abstraction)app).getTerm();
					newterm.setIntermediateSub(new IntermediateSub(new Variable(((Abstraction)app).getName()), this.operand));
					
					return newterm;
					
				}
				else{
					Term newterm = substitution(((Abstraction)app).getTerm(), new Variable(((Abstraction)app).getName()), this.operand.applicativeOrder(exsub));
					return newterm;
				}
			}
			else{
				return new Application(this.operator, this.operand.applicativeOrder(exsub));
			}
		}
		else{
			return new Application(app, this.operand);
		}
	}

	@Override
	public void setIntermediateSub(IntermediateSub isub) {
		this.iSub = isub;
	}

	@Override
	public Term subs(Variable subfrom, Term subto) {
		return new Application(this.operator.subs(subfrom, subto), this.operand.subs(subfrom, subto));
	}

	@Override
	public IntermediateSub getIsub() {
		return this.iSub;
	}

	@Override
	public Term mirror() {
		Application mirror = new Application(this.operator.mirror(), this.operand.mirror());
		mirror.setIntermediateSub(this.iSub);
		return mirror;
	}

	@Override
	public boolean containsSub() {
		return this.iSub != null || this.operator.containsSub() || this.operand.containsSub();
	}
}
