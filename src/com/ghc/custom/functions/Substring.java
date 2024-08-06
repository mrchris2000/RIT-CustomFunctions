package com.ghc.custom.functions;

import java.util.Vector;

import com.ghc.ghTester.expressions.EvalUtils;
import com.ghc.ghTester.expressions.Function;

public class Substring extends Function{

	/**
	  * This function will return whether to trim from the front or back (0 = front).
	  */
	private Function m_fStart = null;
	
	/**
	  * This function will return how many chars to trim off.
	  */
	private Function m_fEnd = null;
	
	/**
	  * This function will return the text to be trimmed.
	  */
	private Function m_fText = null;
	
	
	public Substring(){
	
	}
	
	public Substring( Function f1, Function f2, Function f3){
		
		m_fStart = f1;
		m_fEnd = f2;
		m_fText = f3;
				
	}
	
	/**
	  * Called to evaluate the function.  In this case we evaluate the functions
	  * that return the date and the format of the date and use them to return
	  * a date in the standard format.
	  *
	  * @param data - this should be passed to all evaluate calls
	  */
	public Object evaluate( Object data ){
		
		//Read in values from constructor and convert data types when necessary
		String s = m_fStart.evaluateAsString( data );
		int start = (new Integer(s).intValue());
		
		String e = m_fEnd.evaluateAsString( data );
		int end = (new Integer(e).intValue());
		
		String trimText = m_fText.evaluateAsString( data );
		
		//Trim quotation marks off of trimText variable if necessary
		if( EvalUtils.isString( trimText )){
			trimText = EvalUtils.getString( trimText );
	    }
		
		//String to store text that will be output
		String outputText = "";
			
		outputText = trimText.substring(start, end);
					
		return outputText;		
	}
	
	/**
	  * This method is called when an expression is being evaluated and
	  * a new instance of this function needs to be created.
	  *
	  * @param size number of params
	  * @param params a vector of Function objects, which are the parameters
	  */
	public Function create(int size, Vector params) {
        return new Substring((Function) params.get(0),(Function) params.get(1), (Function) params.get(2));
    }

}

