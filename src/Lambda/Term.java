package Lambda;
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
	
	public Term subs(Variable subfrom, Term subto);
	
	public Term mirror();
	
	/**
	 *evaluate a term
	 */
	
	public Term evaluateNormal(boolean exsub);
	
	public Term evaluateCbn(boolean exsub);
	
	public Term evaluateCbv(boolean exsub);
	
	public Term headReduction(boolean exsub);
	
	public Term applicativeOrder(boolean exsub);
	
	public Term rawReduction();
	
	public boolean equals(Term t);

}
