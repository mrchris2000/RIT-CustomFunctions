package com.ghc.custom.functions;

import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class regexReplaceAll extends Function {
	
	
	/**
	 * This function will return a regular expression.
	 */
	private Function m_fRegex = null;
	
	/**
	 * This function will return what the regex matches will be replaced with.
	 */
	private Function m_fReplaceWith = null;
	
	/**
	 * This function will return the text the regex will search.
	 */
	private Function m_fText = null;
	
	
	public regexReplaceAll(){
		
		//regex = A regular expression, anything that matches it will be replaced
		//replaceWith = Matching regex patterns are replaced with this
		//text = String that the regex searches		
	}
	
	public regexReplaceAll(Function f1, Function f2, Function f3){
		
		m_fRegex = f1;
	    m_fReplaceWith = f2;
	    m_fText = f3;
	}
	
	public Object evaluate( Object data ){
		
		String regex = m_fRegex.evaluateAsString( data );
		String replaceWith = m_fReplaceWith.evaluateAsString( data );
		String text = m_fText.evaluateAsString( data );

		
		//String to store text that will be output
		String outputText = "";
		
		//And as if by magic, Java happens...		
		outputText = text.replaceAll(regex, replaceWith);
				
		return outputText;
	}
	
	public Function create(int size, Vector params) {
        return new regexReplaceAll((Function) params.get(0),(Function) params.get(1), (Function) params.get(2));
    }
}
