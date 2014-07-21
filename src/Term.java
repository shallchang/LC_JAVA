import java.util.ArrayList;


public interface Term {
	
	/**
	 *convert a term into string 
	 */
	
	public String tostring();
	
	/**
	 *get all the free variables of a term 
	 */
	
	public ArrayList<Variable> freeVar();
	
	/**
	 *evaluate a term
	 */
	
	public Term evaluate();

}
