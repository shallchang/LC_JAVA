package TypeAssignment;

public class PPC {

	
	private String counter;
	private Context context;
	private Type predicate;
	
	public PPC(Context context, Type predicate, String counter){
		this.context = context;
		this.predicate = predicate;
		this.counter = counter;
	}
	
	public Context getSubject(){
		return this.context;
	}
	
	public Type getPredicate(){
		return this.predicate;
	}
	
	public String getCounter(){
		return this.counter;
	}

}
