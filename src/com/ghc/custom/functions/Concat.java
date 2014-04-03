package com.ghc.custom.functions;

import java.util.Vector;
import com.ghc.ghTester.expressions.EvalUtils;
import com.ghc.ghTester.expressions.Function;

public class Concat extends Function{

	private Function m_fNum1;
	
	private Function m_fNum2;
	
	public Concat(){
				
	}
	
	public Concat( Function f1, Function f2){
		
		m_fNum1 = f1;
		m_fNum2 = f2;
		
	}
	
	//Simple String concatenation
	public Object evaluate(Object data) {
		
		String text1 = m_fNum1.evaluateAsString(data);
		
		String text2 = m_fNum2.evaluateAsString(data);

		//Trim quotation marks off of text variable if necessary
		if( EvalUtils.isString( text1 )){
			text1 = EvalUtils.getString( text1 );
	    }
		if( EvalUtils.isString( text2 )){
			text2 = EvalUtils.getString( text2 );
	    }
		
		String outputText = text1+text2;
				
		return outputText;
	}
	
	public Function create(int size, Vector params) {
        return new Concat((Function) params.get(0),(Function) params.get(1));
    }	

}
