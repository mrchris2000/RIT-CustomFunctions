package com.ghc.custom.functions;
// Returns a single space
import java.util.Vector;
import com.ghc.ghTester.expressions.Function;

public class Space extends Function{
 
    public Space( ) { }

	@SuppressWarnings("rawtypes")
	public Function create( int size, Vector params ) {
		return new Space( ) ;
	}
      
	public Object evaluate(Object data) {
		return " ";
    }
	  
}
