
public class Statement {

	private Term subject;
	private Type predicate;
	
	public Statement(Term subject, Type predicate){
		this.subject = subject;
		this.predicate = predicate;
	}
	
	
	public Term getSubject(){
		return this.subject;
	}
	
	public Type getPredicate(){
		return this.predicate;
	}
	
	
	public String tostring(){
		return this.subject.tostring() + ":" + this.predicate.tostring();
	}
}
