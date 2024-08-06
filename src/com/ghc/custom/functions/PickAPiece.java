package com.ghc.custom.functions;

import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class PickAPiece extends Function{

	/**
	  * This function will return whether to trim from the front or back (0 = front).
	  */
	private Function m_fCnt = null;
	
	/**
	  * This function will return how many chars to trim off.
	  */
	private Function m_fItm = null;
	
	/**
	  * This function will return the text to be trimmed.
	  */
	private Function m_fText = null;
	
	
	public PickAPiece(){
	
	}
	
	public PickAPiece( Function f1, Function f2, Function f3 ){
		
		m_fCnt = f1;
		m_fItm = f2;
		m_fText = f3;
				
	}
	
	/**
	  * Called to evaluate the function.  In this case we evaluate the functions
	  * that 
	  *
	  * @param data - this should be passed to all evaluate calls
	  */
	public Object evaluate( Object data ){
		
		//Read in values from constructor and convert data types when necessary
		String trimText = m_fText.evaluateAsString( data );
		
		String s = m_fCnt.evaluateAsString( data );
		String st = s.trim();
		int Cnt = new Integer(st).intValue();
		int itmLen = trimText.length() / Cnt;
		
		String e = m_fItm.evaluateAsString( data );
		String et = e.trim();
		int itm = new Integer(et).intValue();
		
		int start = itmLen * (itm - 1);
		int end = itmLen * itm;
		
		//String to store text that will be output
		String outputText = "";
		
		if (start>trimText.length() || end>trimText.length()) {
			outputText = "";
		}
		else {
			outputText = trimText.substring(start, end);
		}			
		return outputText;
	}
	
	@SuppressWarnings("rawtypes")
	public Function create(int size, Vector params) {
        return new PickAPiece((Function) params.get(0),(Function) params.get(1), (Function) params.get(2));
    }

}

