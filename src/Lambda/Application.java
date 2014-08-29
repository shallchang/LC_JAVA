package Lambda;
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
			else if(this.operand instanceof XSub){
				out = "("+this.operator.tostring() + ")"+ "(" + this.operand.tostring() + ")";
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
			else if(this.operand instanceof XSub){
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
			else if(this.operand instanceof XSub){
				out = "("+this.operator.tostring() + ")"+ "(" + this.operand.tostring() + ")";
			}
			else{
				out = this.operator.tostring() + "(" + this.operand.tostring() + ")";
			}	
		}
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
		if(this.operator instanceof Abstraction){  //the operator is an abstraction, so the substitution could take place
			if(exsub){ //use explicit substitution, return a new XSub
				return new XSub(((Abstraction) this.operator).getTerm(), new Variable(((Abstraction) this.operator).getName()), this.operand);
			}
			else{ // disabled explicit substitution, do the substitution directly
				return substitution(((Abstraction) this.operator).getTerm(), new Variable(((Abstraction) this.operator).getName()), this.operand);
			}
		}
		else{
			if(this.operator.evaluateNormal(exsub).equals(this.operator)){
				return new Application(this.operator, this.operand.evaluateNormal(exsub));
			}
			else{
				return new Application(this.operator.evaluateNormal(exsub), this.operand);
			}
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
	
			return this.operator.equals(((Application)t).getOperator()) && this.operand.equals(((Application)t).getOperand());
		}
		else{
			return false;
		}
	}

	@Override
	public Term evaluateCbn(boolean exsub) {
		Term cbn = this.operator.evaluateCbn(exsub);
		
		if(this.operator.equals(cbn)){
			if(cbn instanceof Abstraction){  //the operator is an abstraction, so the substitution could take place
				if(exsub){ //use explicit substitution, return a new XSub
					return new XSub(((Abstraction) cbn).getTerm(), new Variable(((Abstraction) cbn).getName()), this.operand);
				}
				else{ // disabled explicit substitution, do the substitution directly
					return substitution(((Abstraction)cbn).getTerm(), new Variable(((Abstraction)cbn).getName()), this.operand);
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
		Term cbv = this.operator.evaluateCbv(exsub);
		
		if(this.operator.equals(cbv)){
			if(cbv instanceof Abstraction){
				if(exsub){
					if(this.operand.evaluateCbv(exsub).equals(this.operand)){
						return new XSub(((Abstraction) cbv).getTerm(), new Variable(((Abstraction) cbv).getName()), this.operand);
					}
					else{
						return new Application(cbv, this.operand.evaluateCbv(exsub));
					}
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
		Term hr = this.operator.evaluateCbn(exsub);

		if(this.operator.equals(hr)){
			if(hr instanceof Abstraction){
				if(exsub){
					return new XSub(((Abstraction) hr).getTerm(), new Variable(((Abstraction) hr).getName()), this.operand);
				}
				else{
					return substitution(((Abstraction)hr).getTerm(), new Variable(((Abstraction)hr).getName()), this.operand);
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
		Term app = this.operator.applicativeOrder(exsub);

		if(this.operator.equals(app)){
			if(app instanceof Abstraction){
				if(exsub){
					return new XSub(((Abstraction) app).getTerm(), new Variable(((Abstraction) app).getName()), this.operand);	
				}
				else{
					return substitution(((Abstraction)app).getTerm(), new Variable(((Abstraction)app).getName()), this.operand.applicativeOrder(exsub));
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
	public Term subs(Variable subfrom, Term subto) {
		return new Application(this.operator.subs(subfrom, subto), this.operand.subs(subfrom, subto));
	}

	@Override
	public Term mirror() {
		Application mirror = new Application(this.operator.mirror(), this.operand.mirror());
		return mirror;
	}

	@Override
	public Term rawReduction() {
		Term raw = this.operator.rawReduction();
		
		if(raw.equals(this.operator)){
			return new Application(raw, this.operand.rawReduction());
		}
		return new Application(raw, this.operand);
		
	}
}
