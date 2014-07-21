import java.util.ArrayList;

import JaCoP.core.*; 
import JaCoP.constraints.*; 
import JaCoP.search.*; 
//yang b she jing le
public class test { 
 
    static test m = new test (); 
 
    
    /**********
     * 
     *FIRST IN FIRST OUT 
     *
     */
    
    public static void main (String[] args) { 
    
    	Store store = new Store();  // define FD store  
    	
    	IntVar T1 = new IntVar(store, "T1", 0,100); 
    	IntVar T2 = new IntVar(store, "T2", 0,100);
        
        store.impose(new XltY(T1, T2));

        store = new Store();
        
        System.out.println("SIZE: "+store.size());
        
        IntVar[] cao = {T1, T2};
       
        
        Search<IntVar> search = new DepthFirstSearch<IntVar>(); 
        SelectChoicePoint<IntVar> select = 
            new InputOrderSelect<IntVar>(store, cao, 
                                         new IndomainMin<IntVar>()); 
        boolean result = search.labeling(store, select); 
 
        if ( result ) 
            System.out.println("Solution: " + cao[0]+", "+cao[1]);
         }
}