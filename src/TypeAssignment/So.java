package TypeAssignment;

public class So implements TSub{

	private TSub s1;
	private TSub s2;
	
	public So(TSub s1, TSub s2){
		this.s1 = s1;
		this.s2 = s2;
	}
	
	public TSub getS1(){
		return this.s1;
	}
	
	public TSub getS2(){
		return this.s2;
	}
}
