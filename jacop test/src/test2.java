
import java.util.ArrayList;
import java.util.HashMap;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.omg.CORBA.PUBLIC_MEMBER;

import JaCoP.core.*; 
import JaCoP.constraints.*; 
import JaCoP.search.*; 
 
public class test2 { 
 
    static test m = new test (); 
 
    
    /**********
     * 
     *FIRST IN FIRST OUT 
     *
     */
    
    public static void main (String[] args) { 
    	
        	Store store = new Store();  // define FD store  
        	store.setID("jj");
        	//ArrayList <Constraint> constraints = new ArrayList<Constraint>();
            ArrayList<IntVar>dVars = new ArrayList<IntVar>();
            
            dVars.add(new IntVar(store, "T1", 0,100));
            dVars.add(new IntVar(store, "T2", 0,100));
            dVars.add(new IntVar(store, "T3", 0,100));
            
            store.impose(new XltY(dVars.get(0), dVars.get(1)));
            store.impose(new XgtC(dVars.get(2), 8));
    	
            IntVar x = dVars.get(0);
            IntVar y = dVars.get(1);
            
            System.out.println(store);
            
            
            store.setLevel(store.level+1);
    
           
            IntVar[] cao = {dVars.get(0), dVars.get(1), dVars.get(2)};
           
            
            Search<IntVar> search = new DepthFirstSearch<IntVar>(); 
            SelectChoicePoint<IntVar> select = 
                new InputOrderSelect<IntVar>(store, cao, 
                                             new IndomainMin<IntVar>()); 
            boolean result = search.labeling(store, select); 
     
            if ( result ){ 
            	System.out.println(store);
             }
    
           
            store.removeLevel(store.level); //reset to the initial time point
			store.setLevel(store.level-1);
			
			System.out.println(store);
			
            
    }

}