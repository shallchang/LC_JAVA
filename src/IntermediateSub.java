
public class IntermediateSub {

	private Variable from;
	private Term to;
	
	public IntermediateSub(Variable from, Term to){
		this.from = from;
		this.to = to;
		
	}
	
	public Variable getFrom(){
		return this.from;
	}
	
	public Term getTo(){
		return this.to;
	}
	
	
	public String tostring(){
		return "[" + to.tostring() + "/" + from.tostring() + "]";
	}
	
}
