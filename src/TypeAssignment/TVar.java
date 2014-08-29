package TypeAssignment;

public class TVar implements Type{

	
	private String name;
	
	
	public TVar(String name){
		this.name = name;
	}
	
	
	public String getName(){
		return this.name;
	}

	
	@Override
	public String tostring() {
		if(this.name.length() <= 1){
			return this.name;
		}
		else{
			return this.name.substring(0, 1) + "->" + (new TVar(this.name.substring(1))).tostring();
		}
	}
	

	@Override
	public boolean equals(Type type) {
		if(type instanceof TVar){
			return this.name == ((TVar) type).getName();
		}
		else{
			return false;
		}
	}
}
