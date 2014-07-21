import java.util.ArrayList;


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
		return "App{ " + this.operator.tostring() + this.operand.tostring()+"}";
	}

	@Override
	public ArrayList<Variable> freeVar() {
		ArrayList<Variable> free = operator.freeVar();
		free.addAll(operand.freeVar());
		return free;
	}

	@Override
	public Term evaluate() {
		if(this.operator instanceof Application){
			System.out.println("eva: " + this.operator.tostring() + " || " + this.operand.tostring());
			return new Application(this.operator.evaluate(), this.operand.evaluate());
		}
		else if(this.operator instanceof Abstraction){
			System.out.println(this.tostring());
			System.out.println("Sub: "+ ((Abstraction)this.operator).getTerm().tostring() +  " "+((Abstraction)this.operator).getName() + " "+this.operand.tostring());
			
			
			return substitution(((Abstraction)this.operator).getTerm(), new Variable(((Abstraction)this.operator).getName()), this.operand.evaluate());
		}
		else if(this.operator instanceof Variable){
			return new Application(this.operator, this.operand.evaluate());
		}
		else{
			throw new IllegalArgumentException("Null data type");
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
			return (new Application(t1, t2)).evaluate();
		}
		else{
			throw new IllegalArgumentException("None type");
		}
		
	}
}
