package Lambda;

import java.util.ArrayList;

public class XSub implements Term{

	private Term term;
	private Variable from;
	private Term to;


	public XSub(Term term, Variable from, Term to){
		this.term = term;
		this.from = from;
		this.to = to;
	}

	public Term getTerm(){
		return this.term;
	}

	public Variable getFrom(){
		return this.from;
	}

	public Term getTo(){
		return this.to;
	}

	@Override
	public String tostring(){
		if(this.term instanceof Variable){
			return this.term.tostring()+ "<"+this.from.tostring()+":="+this.to.tostring()+">";
		}
		else{
			return "(" + this.term.tostring()+ ")" + "<"+this.from.tostring()+":="+this.to.tostring()+">";
		}

	}

	@Override
	public ArrayList<Variable> freeVar() {
		ArrayList<Variable> freeM = term.freeVar();
		freeM.remove(from);
		freeM.addAll(to.freeVar());
		return freeM;
	}

	@Override
	public Term subs(Variable subfrom, Term subto) {
		return null;
	}

	@Override
	public Term mirror() {
		return this;
	}


	@Override
	public Term evaluateNormal(boolean exsub) {
		/*
		boolean b = false;

		if(term instanceof Variable){
			if(term.equals(this.from)) return this.to;
			else return this.term;
		}
		else if(this.term instanceof Abstraction){
			for(Variable var: this.term.freeVar()){
				if(var.equals(this.from)){
					b = true;
					break;
				}
			}

			if(b){
				return new Abstraction(((Abstraction) term).getName(), new XSub(((Abstraction) term).getTerm(), from, to));
			}
			else{
				return this.term;
			}
		}
		else if(this.term instanceof Application){
			for(Variable var: this.term.freeVar()){
				if(var.equals(this.from)){
					b = true;
					break;
				}
			}

			if(b){

				XSub sub1 = new XSub(((Application) term).getOperator(), from, to);
				XSub sub2 = new XSub(((Application) term).getOperand(), from, to);

				return new Application(sub1, sub2);
			}
			else{
				return this.term;
			}
		}
		else{
			for(Variable var: this.term.freeVar()){
				if(var.equals(this.from)){
					b = true;
					break;
				}
			}
			if(b){
				return new XSub(this.term.evaluateNormal(exsub), from, to);
			}
			else{
				return this.term;
			}

		}
		*/
		if(term instanceof Variable){
			if(term.equals(this.from)) return this.to;
			else return this.term;
		}
		else if(this.term instanceof Abstraction){
			return new Abstraction(((Abstraction) term).getName(), new XSub(((Abstraction) term).getTerm(), from, to));

		}
		else if(this.term instanceof Application){
			XSub sub1 = new XSub(((Application) term).getOperator(), from, to);
			XSub sub2 = new XSub(((Application) term).getOperand(), from, to);

			return new Application(sub1, sub2);

		}
		else{
			return new XSub(this.term.evaluateNormal(exsub), from, to);
		}
	}

	@Override
	public Term evaluateCbn(boolean exsub) {
		boolean b = false;

		if(term instanceof Variable){
			if(term.equals(this.from)) return this.to;
			else return this.term;
		}
		else if(this.term instanceof Abstraction){
			for(Variable var: this.term.freeVar()){
				if(var.equals(this.from)){
					b = true;
					break;
				}
			}

			if(b){
				return new Abstraction(((Abstraction) term).getName(), new XSub(((Abstraction) term).getTerm(), from, to));
			}
			else{
				return this.term;
			}
		}
		else if(this.term instanceof Application){
			for(Variable var: this.term.freeVar()){
				if(var.equals(this.from)){
					b = true;
					break;
				}
			}

			if(b){

				XSub sub1 = new XSub(((Application) term).getOperator(), from, to);
				XSub sub2 = new XSub(((Application) term).getOperand(), from, to);

				return new Application(sub1, sub2);
			}
			else{
				return this.term;
			}
		}
		else{
			for(Variable var: this.term.freeVar()){
				if(var.equals(this.from)){
					b = true;
					break;
				}
			}
			if(b){
				return new XSub(this.term.evaluateNormal(exsub), from, to);
			}
			else{
				return this.term;
			}

		}
	}

	@Override
	public Term evaluateCbv(boolean exsub) {
		boolean b = false;

		if(term instanceof Variable){
			if(term.equals(this.from)) return this.to;
			else return this.term;
		}
		else if(this.term instanceof Abstraction){
			for(Variable var: this.term.freeVar()){
				if(var.equals(this.from)){
					b = true;
					break;
				}
			}

			if(b){
				return new Abstraction(((Abstraction) term).getName(), new XSub(((Abstraction) term).getTerm(), from, to));
			}
			else{
				return this.term;
			}
		}
		else if(this.term instanceof Application){
			for(Variable var: this.term.freeVar()){
				if(var.equals(this.from)){
					b = true;
					break;
				}
			}

			if(b){

				XSub sub1 = new XSub(((Application) term).getOperator(), from, to);
				XSub sub2 = new XSub(((Application) term).getOperand(), from, to);

				return new Application(sub1, sub2);
			}
			else{
				return this.term;
			}
		}
		else{
			for(Variable var: this.term.freeVar()){
				if(var.equals(this.from)){
					b = true;
					break;
				}
			}
			if(b){
				return new XSub(this.term.evaluateNormal(exsub), from, to);
			}
			else{
				return this.term;
			}

		}
	}

	@Override
	public Term headReduction(boolean exsub) {
		boolean b = false;

		if(term instanceof Variable){
			if(term.equals(this.from)) return this.to;
			else return this.term;
		}
		else if(this.term instanceof Abstraction){
			for(Variable var: this.term.freeVar()){
				if(var.equals(this.from)){
					b = true;
					break;
				}
			}

			if(b){
				return new Abstraction(((Abstraction) term).getName(), new XSub(((Abstraction) term).getTerm(), from, to));
			}
			else{
				return this.term;
			}
		}
		else if(this.term instanceof Application){
			for(Variable var: this.term.freeVar()){
				if(var.equals(this.from)){
					b = true;
					break;
				}
			}

			if(b){

				XSub sub1 = new XSub(((Application) term).getOperator(), from, to);
				XSub sub2 = new XSub(((Application) term).getOperand(), from, to);

				return new Application(sub1, sub2);
			}
			else{
				return this.term;
			}
		}
		else{
			for(Variable var: this.term.freeVar()){
				if(var.equals(this.from)){
					b = true;
					break;
				}
			}
			if(b){
				return new XSub(this.term.evaluateNormal(exsub), from, to);
			}
			else{
				return this.term;
			}

		}
	}

	@Override
	public Term applicativeOrder(boolean exsub) {
		boolean b = false;

		if(term instanceof Variable){
			if(term.equals(this.from)) return this.to;
			else return this.term;
		}
		else if(this.term instanceof Abstraction){
			for(Variable var: this.term.freeVar()){
				if(var.equals(this.from)){
					b = true;
					break;
				}
			}

			if(b){
				return new Abstraction(((Abstraction) term).getName(), new XSub(((Abstraction) term).getTerm(), from, to));
			}
			else{
				return this.term;
			}
		}
		else if(this.term instanceof Application){
			for(Variable var: this.term.freeVar()){
				if(var.equals(this.from)){
					b = true;
					break;
				}
			}

			if(b){
				XSub sub1 = new XSub(((Application) term).getOperator(), from, to);
				XSub sub2 = new XSub(((Application) term).getOperand(), from, to);

				return new Application(sub1, sub2);
			}
			else{
				return this.term;
			}
		}
		else{
			for(Variable var: this.term.freeVar()){
				if(var.equals(this.from)){
					b = true;
					break;
				}
			}
			if(b){
				return new XSub(this.term.evaluateNormal(exsub), from, to);
			}
			else{
				return this.term;
			}

		}
	}

	@Override
	public boolean equals(Term t) {
		if(t instanceof XSub){
			return this.term.equals(((XSub) t).getTerm()) && this.from.equals(((XSub) t).getFrom()) && this.to.equals(((XSub) t).getTo());
		}
		return false;
	}

	@Override
	public Term rawReduction() {
		if(term instanceof Variable){
			if(term.equals(this.from)) return this.to;
			else return this.term;
		}
		else if(term instanceof Abstraction){
			return new Abstraction(((Abstraction) term).getName(), new XSub(((Abstraction) term).getTerm(), from, to));
		}
		else if(term instanceof Application){
			XSub sub1 = new XSub(((Application) term).getOperator(), from, to);
			XSub sub2 = new XSub(((Application) term).getOperand(), from, to);

			return new Application(sub1, sub2);
		}
		else{
			return this;
		}
	}

}
