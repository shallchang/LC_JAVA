import java.util.ArrayList;


public class Context {
    
	private ArrayList<Statement> sts;
	
	
	public Context(ArrayList<Statement> sts){
		this.sts = sts;
	}
	
	public ArrayList<Statement> getContext(){
		return this.sts;
	}
	
	public String tostring(){
		String tmp = "";
		for(Statement states : this.sts){
			tmp += states.tostring() + ", ";
		}
		
		if(tmp.length() > 3) tmp = tmp.substring(0, tmp.length()-2);
		
		return tmp;
	}
}
