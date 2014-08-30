package Lambda;
/*
 * 
 * Author:Xiaohan Zhang
 * 
 * 
 */

import java.util.ArrayList;

//(\abc.ac(bc))(\xy.x)
//(\a.a)(\b.b)((\x.x)(\y.(\z.z)w))
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
			else if(fv && !y.equals(subfrom)){ //alpha-conversion
				String name = ((Abstraction)original).getName();
				
				if(name.length() > 1){ //not only a character, but with a number following it which means already alpha-converted. y1
					int number = Integer.parseInt(name.substring(1))+1;
					String newname = name.substring(0, 1)+number;
					return new Abstraction(newname, substitution(substitution(((Abstraction)original).getTerm(), y , new Variable(newname)), subfrom, subto));	
				}
				else{ //only a character
					String newname = name+1;

					return new Abstraction(newname, substitution(substitution(((Abstraction)original).getTerm(), y , new Variable(newname)), subfrom, subto));
				}
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
		else if(original instanceof XSub){
			Term term = substitution(((XSub) original).getTerm(), subfrom, subto);
			return new XSub(term, ((XSub) original).getFrom(), ((XSub) original).getTo());
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
		if(this.operator instanceof Abstraction){  //the operator is an abstraction, so the substitution could take place
			if(exsub){ //use explicit substitution, return a new XSub
				return new XSub(((Abstraction) this.operator).getTerm(), new Variable(((Abstraction) this.operator).getName()), this.operand);
			}
			else{ // disabled explicit substitution, do the substitution directly
				return substitution(((Abstraction)this.operator).getTerm(), new Variable(((Abstraction)this.operator).getName()), this.operand);
			}
		}
		else{
			if(this.operator.evaluateCbn(exsub).equals(this.operator)){
				return new Application(this.operator, this.operand);
			}
			else{
				return new Application(this.operator.evaluateCbn(exsub), this.operand);
			}
		}
	}

	@Override
	public Term evaluateCbv(boolean exsub) {
		if(this.operator instanceof Abstraction){  //the operator is an abstraction, so the substitution could take place
			if(exsub){ //use explicit substitution, return a new XSub
				if(this.operand.evaluateCbv(exsub).equals(this.operand)){
					return new XSub(((Abstraction) this.operator).getTerm(), new Variable(((Abstraction) this.operator).getName()), this.operand);
				}
				else{
					return new Application(this.operator, this.operand.evaluateCbv(exsub));
				}
			}
			else{ // disabled explicit substitution, do the substitution directly
				if(this.operand.evaluateCbv(exsub).equals(this.operand)){
					return substitution(((Abstraction)this.operator).getTerm(), new Variable(((Abstraction)this.operator).getName()), this.operand);
				}
				else{
					return new Application(this.operator, this.operand.evaluateCbv(exsub));
				}
			}
		}
		else{
			if(this.operator.evaluateCbv(exsub).equals(this.operator)){
				return new Application(this.operator, this.operand.evaluateCbv(exsub));
			}
			else{
				return new Application(this.operator.evaluateCbv(exsub), this.operand);
			}
		}			
	}

	@Override
	public Term headReduction(boolean exsub) {
		if(this.operator instanceof Abstraction){  //the operator is an abstraction, so the substitution could take place
			if(exsub){ //use explicit substitution, return a new XSub
				return new XSub(((Abstraction) this.operator).getTerm(), new Variable(((Abstraction) this.operator).getName()), this.operand);
			}
			else{ // disabled explicit substitution, do the substitution directly
				return substitution(((Abstraction)this.operator).getTerm(), new Variable(((Abstraction)this.operator).getName()), this.operand);
			}
		}
		else{
			if(this.operator.headReduction(exsub).equals(this.operator)){
				return new Application(this.operator, this.operand);
			}
			else{
				return new Application(this.operator.headReduction(exsub), this.operand);
			}
		}
	}

	@Override
	public Term applicativeOrder(boolean exsub) {
		if(this.operator instanceof Abstraction){  //the operator is an abstraction, so the substitution could take place
			if(this.operator.applicativeOrder(exsub).equals(this.operator)){
				if(this.operand.applicativeOrder(exsub).equals(this.operand)){
					if(exsub){ //use explicit substitution, return a new XSub
						return new XSub(((Abstraction) this.operator).getTerm(), new Variable(((Abstraction) this.operator).getName()), this.operand);
					}
					else{ // disabled explicit substitution, do the substitution directly
						return substitution(((Abstraction)this.operator).getTerm(), new Variable(((Abstraction)this.operator).getName()), this.operand);
					}
				}
				else{
					return new Application(this.operator, this.operand.applicativeOrder(exsub));
				}
			}
			else{
				return new Application(this.operator.applicativeOrder(exsub), this.operand);
			}
		}
		else{
			if(this.operand.applicativeOrder(exsub).equals(this.operand)){
				return new Application(this.operator.applicativeOrder(exsub), this.operand);
			}
			else{
				return new Application(this.operator, this.operand.applicativeOrder(exsub));
			}
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
