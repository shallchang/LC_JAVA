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
		
		if(this.operator instanceof Abstraction){
			if(this.operand instanceof Variable){
				return "("+this.operator.tostring() + ")" +  this.operand.tostring();
			}
			else if(this.operand instanceof Abstraction){
				return "("+this.operator.tostring() + ")" + "(" + this.operand.tostring() + ")";
			}
			else{
				return "("+this.operator.tostring() + ")" + this.operand.tostring();
			}
		}
		else if(this.operator instanceof Variable){
			if(this.operand instanceof Variable){
				return "("+this.operator.tostring()  +  this.operand.tostring() + ")";
			}
			else if(this.operand instanceof Abstraction){
				return this.operator.tostring() + "(" + this.operand.tostring() + ")";
			}
			else{
				return this.operator.tostring() + this.operand.tostring();
			}
		}
		else{
			if(this.operand instanceof Variable){
				return "("+this.operator.tostring() + ")" + this.operand.tostring();
			}
			else if(this.operand instanceof Abstraction){
				return "(" +this.operator.tostring() + ")" + "(" + this.operand.tostring() + ")";
			}
			else{
				return this.operator.tostring() + this.operand.tostring();
			}	
		}
		
	}

	@Override
	public ArrayList<Variable> freeVar() {
		ArrayList<Variable> free = operator.freeVar();
		free.addAll(operand.freeVar());
		return free;
	}

	@Override
	public Term evaluateNormal() {
		Term cbn = this.operator.evaluateCbn();
		
		
		if(this.operator.equals(cbn)){
			if(cbn instanceof Abstraction){
				Term newterm = substitution(((Abstraction)cbn).getTerm(), new Variable(((Abstraction)cbn).getName()), this.operand);
				return newterm;
			}
			else{
				return new Application(this.operator, this.operand.evaluateNormal());
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
			return this.operator.equals(((Application)t).getOperator()) && this.operand.equals(((Application)t).getOperand());
		}
		else{
			return false;
		}
	}

	@Override
	public Term evaluateCbn() {
		Term cbn = this.operator.evaluateCbn();
		
		if(this.operator.equals(cbn)){
			if(cbn instanceof Abstraction){
				Term newterm = substitution(((Abstraction)cbn).getTerm(), new Variable(((Abstraction)cbn).getName()), this.operand);
				return newterm;
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
	public Term evaluateCbv() {
		Term cbv = this.operator.evaluateCbv();
		
		if(this.operator.equals(cbv)){
			if(cbv instanceof Abstraction){
				Term newterm = substitution(((Abstraction)cbv).getTerm(), new Variable(((Abstraction)cbv).getName()), this.operand.evaluateCbv());
				return newterm;
			}
			else{
				return new Application(cbv, this.operand.evaluateCbv());
			}
			
		}
		else{
			return new Application(cbv, this.operand);
		}			
	}

	@Override
	public Term headReduction() {
		return new Application(this.operator.headReduction(), this.operand);
	}
}
