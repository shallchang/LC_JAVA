import java.util.Scanner;


public class LC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LC lc  = new LC();
		System.out.print("Please input a term: ");
		Scanner sc = new Scanner(System.in);
	    String input = sc.nextLine();
        Term newTerm = lc.toTerm(input);
        int n = 50;
        while(n-- > 0){
        	newTerm = newTerm.evaluate();
        }
        System.out.println(newTerm.tostring());
        System.out.println("Reducted to:" + newTerm.evaluate().tostring());
        
	}
	
	
	public Term toTerm(String input){
		if(input.startsWith("/")){
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
			return new Variable(input.charAt(0));
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
			return new Abstraction(input.charAt(0), toAbs(input.substring(1)));	
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
			
			if(last(tmp[0]).equals(")") && head(tail(exp)).equals("/")){			
				return new String[]{toExp(tmp[0]), tmp[1]};
			}
			else{
				return new String[]{tmp[0], tmp[1]};
			}
		}
		else if(head(tail(exp)).equals("/") && last(init(exp)).equals(")")){
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
	
		//System.out.println("Begin: " + left + " " + right);
		
		if(n==1 && last(left).equals("(")){
			//System.out.println(1);
			//System.out.println("split new: " + new_left + " " + new_right);
			return new String[]{new_left, right};
		}
		else if(last(left).equals("(")){
			//System.out.println(2);
			//System.out.println("split new: " + new_left + " " + new_right);
			return split(new_left, new_right, n-1);
		}
		else if(last(left).equals(")")){
			//System.out.println(3);
			return split(new_left, new_right, n+1);
		}
		else{
			//System.out.println(4);
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
	
	
	
	
	
	
	
	
	
	
	
}
