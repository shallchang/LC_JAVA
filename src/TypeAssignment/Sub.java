package TypeAssignment;

public class Sub implements TSub{

	private TVar from;
	private Type to;
	
	public Sub(TVar from, Type to){
		this.from = from;
		this.to = to;
	}
	
	public TVar getFrom(){
		return this.from;
	}
	
	public Type getTo(){
		return this.to;
	}

	
}
