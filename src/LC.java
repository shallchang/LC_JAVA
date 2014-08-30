import java.util.ArrayList;
import java.util.Scanner;
import Lambda.*;
import TypeAssignment.*;

public class LC {
	
	/**
	 * @param args
	 */
	
	public static void main(String[] args){
		LC lc  = new LC();
		System.out.print("Please input a term: ");
		Scanner sc = new Scanner(System.in);
	    String input = sc.nextLine();
        Term newTerm = lc.toTerm(input);
        
        System.out.println(newTerm.tostring());
        
        while(!newTerm.evaluateNormal(true).equals(newTerm)){
        	newTerm = newTerm.evaluateNormal(true);
        	System.out.println(newTerm.tostring());
        }
        
        
       /*
        PPC ppc = lc.ppc(newTerm, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		
		if(ppc == null){
			System.out.println("untypable");
		}
		else{
			System.out.println(ppc.getSubject().tostring() + " |- " + ppc.getPredicate().tostring() + "\n");
		}
		*/
        
        
	}
	
	
	public Term toTerm(String input){
		if(input.startsWith("\\")){
			return toAbs(tail(input));
		}
		else if(input.length() == 1){
			return toVar(input);
		}
		else{
			return toApp(input);
		}
	}
	
	public Term toVar(String input){
		if(input.length() == 1){
			//System.out.println("New var: " + input);
			return new Variable(input);
		}
		else{
			throw new IllegalArgumentException("Invalid variable");
		}
	}
	
	public Term toAbs(String input){
		//System.out.println("input: " + input);
		if(input.length() >0 && input.charAt(0) == '.'){
			if(tail(tail(input)).length() == 0){
				return toVar(tail(input));
			}
			else{
				return toApp(tail(input));
			}
		}
		else{
			return new Abstraction(input.substring(0, 1), toAbs(input.substring(1)));	
		}
	}
	
	public Term toApp(String input){
		String[] tmp = div(input);
		//System.out.println("div: " + tmp[0] +" " + tmp[1]);
		return new Application(toTerm(tmp[0]), toTerm(tmp[1]));
	}
	
	
	public String[] div(String exp){
		if(last(exp).equals(")")){
			//System.out.println("Div 3");
			String[] tmp = split(init(exp), "", 1);
			
			if(last(tmp[0]).equals(")") && head(tail(exp)).equals("\\")){			
				return new String[]{toExp(tmp[0]), tmp[1]};
			}
			else{
				return new String[]{tmp[0], tmp[1]};
			}
		}
		else if(head(tail(exp)).equals("\\") && last(init(exp)).equals(")")){
			//System.out.println("Div 1");
				
			return new String[]{toExp(init(exp)), last(exp)};
		}
	    else{
			//System.out.println("Div 2");
			
			return new String[]{init(exp), last(exp)};
		}
	}
	
	public String toExp(String exp){
		String[] tmp = split(init(exp), "", 1);
	    
		if(this.last(exp).equals(")") && tmp[0].equals("")){
			return tmp[1];
		}
		else{
			return exp;
		}
	}
	
	public String[] split(String left, String right, int n){
		String new_left = init(left);
		String new_right = last(left) + right;
		
		if(n==1 && last(left).equals("(")){
			return new String[]{new_left, right};
		}
		else if(last(left).equals("(")){
			return split(new_left, new_right, n-1);
		}
		else if(last(left).equals(")")){
			return split(new_left, new_right, n+1);
		}
		else{
			return split(new_left, new_right, n);
		}
	}
	
	public String last(String input){
		if(input.length() == 0 || input.length() == 1){
			return input;
		}
		else{
			return input.substring(input.length()-1);
		}
	}
	
	public String init(String input){
		if(input.length() ==0 || input.length() == 1){
			return "";
		}
		else if(input.length() == 2){
			return input.substring(0,1);
		}
		else{
			return input.substring(0, input.length()-1);
		}	
	}
	
	public String head(String input){
		if(input.length() ==0 || input.length() == 1){
			return input;
		}
		else{
			return input.substring(0,1);
		}
	}
	
	public String tail(String input){
		if(input.length() ==0 || input.length() == 1){
			return "";
		}
		else{
			return input.substring(1);
		}
	}
	
	public Type typeSubs(TSub ts, Type type){
		if(ts instanceof Sub){
			if(type instanceof TVar){
				if(((Sub) ts).getFrom().equals(type)){
					return ((Sub) ts).getTo();
				}
				else{
					return type;
				}
			}
			else if(type instanceof TP){
				return new TP(typeSubs(ts, ((TP) type).getHead()), typeSubs(ts, ((TP) type).getTail()));
			}
			else{
				throw new IllegalArgumentException("Null type");
			}
		}
		else if(ts instanceof So){
			return So((So)ts, type);
		}
		else{
			throw new IllegalArgumentException("Null type");
		}
	}
	
	public Type So(So so, Type type){
		return typeSubs(so.getS1(), typeSubs(so.getS2(), type));
	}
	
	
	public Context contextSubs(TSub ts, Context context){
		ArrayList<Statement> tmp = new ArrayList<>();

		for(Statement statement: context.getContext()){
			tmp.add(new Statement(statement.getSubject(), typeSubs(ts, statement.getPredicate())));
		}

		return new Context(tmp);

	}
	
	
	public TSub unify(Type first, Type second){
		System.out.println(first.tostring() + second.tostring());
		
		if(first instanceof TVar){
			if(second instanceof TVar){
				return new Sub((TVar)first, second);
			}
			else{
				if(!occur(first, ((TP) second).getHead()) && !occur(first, ((TP)second).getTail())){
					System.out.println("fk");
					return new Sub((TVar)first, second);
				}
				else{
					return null;
				}
			}
		}
		else if(first instanceof TP){
			if(second instanceof TVar){
				return unify(second, first);
			}
			else{
	            TSub s1 = unify(((TP) first).getHead(), ((TP) second).getHead());
	            TSub s2 = unify(typeSubs(s1, ((TP) first).getTail()), typeSubs(s1, ((TP) second).getTail()));
	            return new So(s1, s2);
	     
			}
		}
		else{
			throw new IllegalArgumentException("Null type");
		}
		
	}
	
	
	public TSub unifyContext(Context first, Context second){
		
		if(first.getContext().size() == 0){
			return new Sub(new TVar("A"), new TVar("A"));
		}
		
		Statement hd = first.getContext().get(0);

		ArrayList<Statement> tl = first.getContext();
		tl.remove(0);
		
		
		Statement target = search(hd.getSubject(), second);
		if(!target.getPredicate().equals(new TVar(""))){
			TSub s1 = unify(hd.getPredicate(), target.getPredicate());
			
			if(s1 == null) return null;
			
			TSub s2 = unifyContext(contextSubs(s1, new Context(tl)), contextSubs(s1, second));
          
			return new So(s2, s1);

		}
		
		else{
			return unifyContext(new Context(tl), second);	
		}
	}
	
	
	public Statement search(Term term, Context context){
		for(Statement sts: context.getContext()){
			if(sts.getSubject().equals(term)){
				return sts;
			}
		}
		return new Statement(new Variable(""), new TVar(""));
	}
	
	
	public boolean contains(Term term, Context context){
		for(Statement sts: context.getContext()){
			if(sts.getSubject().equals(term)){
				return true;
			}
		}
		return false;
	}
	
	
	
	public PPC ppc(Term term, String counter){
		if(term instanceof Variable){
			ArrayList<Statement> sts = new ArrayList<>();
			Type type = new TVar(counter.substring(0, 1));
			sts.add(new Statement(term, type));
			
			return new PPC(new Context(sts), type, counter.substring(1));
			
		}
		else if(term instanceof Abstraction){
			Variable xv = new Variable(((Abstraction) term).getName());
			
			PPC receiver = ppc(((Abstraction) term).getTerm(), counter);
			
			if(receiver != null){
				if(contains(xv, receiver.getSubject())){
					Type searchType = search(xv, receiver.getSubject()).getPredicate();
					ArrayList<Statement> original = receiver.getSubject().getContext();
					
					ArrayList<Statement> remove = new ArrayList<>();
	
					for(Statement s: original){
						if(s.getSubject().equals(xv)) remove.add(s);
					}
					
					for(Statement rm: remove){
						original.remove(rm);
					}
					
					TP tp = new TP(searchType, receiver.getPredicate());
					
					return new PPC(new Context(original), tp, receiver.getCounter());
					
				}
				else{
					System.out.println("HERE");
					TVar f = new TVar(receiver.getCounter().substring(0, 1));
					
					return new PPC(receiver.getSubject(), new TP(f, receiver.getPredicate()) , receiver.getCounter().substring(1));
					
				}
			}	
			else {
				return null;
			}
		}
		else if(term instanceof Application){
			TVar f = new TVar(counter.substring(0,1));
			
			PPC receiver1 = ppc(((Application)term).getOperator(), counter.substring(1));
			
			if(receiver1 == null) return null;
			
			PPC receiver2 = ppc(((Application)term).getOperand(), receiver1.getCounter());
			
			if(receiver2 == null) return null;
			
			
			System.out.println(receiver1.getSubject().tostring());
			System.out.println(receiver2.getSubject().tostring());
			
			
			TSub s1 = unify(receiver1.getPredicate(), new TP(receiver2.getPredicate(), f));
			
			if(s1 == null) return null;
			
			TSub s2 = unifyContext(contextSubs(s1, receiver1.getSubject()), contextSubs(s1, receiver2.getSubject()));
			
			if(s2 == null) return null;
			
			
			ArrayList<Statement> tmp = receiver1.getSubject().getContext();
			tmp.addAll(receiver2.getSubject().getContext());
			Context union = new Context(tmp);
			
			
			return new PPC(contextSubs(s2, contextSubs(s1, union)), typeSubs(s2, typeSubs(s1, f)), receiver2.getCounter());	
		}
		else{//it is an explicit substitution term as M<x:=N>
			//ppc M, saved in container
            PPC receiverM = ppc(((XSub)term).getTerm(), counter);
			
			if(receiverM == null) return null;
			
			//ppc N, saved in container
			PPC receiverN = ppc(((XSub)term).getTo(), receiverM.getCounter());
			
			if(receiverN == null) return null;
			
			
			//type get the type of x, if x is untyped, type it to a fresh type
		    Type c = null;
			
			
			for(Variable var: ((XSub)term).getTerm().freeVar()){
				if(var.equals(((XSub)term).getFrom())){
					c = search(var, receiverM.getSubject()).getPredicate();
					break;
				}
			}
			
			if(c == null){
				c = new TVar(receiverN.getCounter().substring(0,1));
			}
			
			// s1 and s2
            TSub s1 = unify(receiverN.getPredicate(), c);
			
			if(s1 == null) return null;
			
			TSub s2 = unifyContext(contextSubs(s1, receiverM.getSubject()), contextSubs(s1, receiverN.getSubject()));
			
			if(s2 == null) return null;
			
			//unione Gamma_1 and Gamma_2
			ArrayList<Statement> tmp = receiverM.getSubject().getContext();
			tmp.addAll(receiverN.getSubject().getContext());
			Context union = new Context(tmp);
			
			//return S2 0 S1 <union, A>
			return new PPC(contextSubs(s2, contextSubs(s1, union)), typeSubs(s2, typeSubs(s1, receiverM.getPredicate())), receiverN.getCounter().substring(1));	
		}
		
	}
	
	public boolean occur(Type first, Type second){
		if(first instanceof TVar){
			if(second instanceof TP){
				return occur(first, ((TP) second).getHead()) || occur(first, ((TP) second).getTail());
			}
			else{
				if(((TVar) first).getName().length() == 1){
					return ((TVar) second).getName().contains(((TVar) first).getName());
				}
				else{
					throw new IllegalArgumentException("not 1");
				}	
			}
		}
		else{
			return false;
		}
	}
}
