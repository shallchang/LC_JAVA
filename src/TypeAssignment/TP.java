package TypeAssignment;

public class TP implements Type{
	private Type head;
	private Type tail;
	
	
	public TP(Type head, Type tail){
		this.head = head;
		this.tail = tail;
	}
	
	public Type getHead(){
		return this.head;
	}
	
	public Type getTail(){
		return this.tail;
	}

	@Override
	public String tostring() {
		return "(" + this.head.tostring() + "->" + this.tail.tostring() + ")";
	}

	@Override
	public boolean equals(Type type) {
		if(type instanceof TP){
			return this.equals(type);
		}
		else{
			return false;
		}
	}

}
