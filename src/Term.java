import java.util.ArrayList;


public interface Term {
	
	/**
	 *convert a term into string 
	 */
	
	public String tostring();
	
	
	public IntermediateSub getIsub();
	
	public void setIntermediateSub(IntermediateSub isub);
	/**
	 *get all the free variables of a term 
	 */
	
	public ArrayList<Variable> freeVar();
	
	
	
	
	public Term subs(Variable subfrom, Term subto);
	
	public Term mirror();
	
	public boolean containsSub();
	/**
	 *evaluate a term
	 */
	
	public Term evaluateNormal(boolean exsub);
	
	public Term evaluateCbn(boolean exsub);
	
	public Term evaluateCbv(boolean exsub);
	
	public Term headReduction(boolean exsub);
	
	public Term applicativeOrder(boolean exsub);
	
	public boolean equals(Term t);

}
