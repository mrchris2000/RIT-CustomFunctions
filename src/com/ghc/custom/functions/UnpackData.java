package com.ghc.custom.functions;

import java.util.Vector;
//import java.text.*;
import com.ghc.ghTester.expressions.Function;

public class UnpackData extends Function{
    
    private Function m_nbr = null;
    private Function m_dpl = null;
    
    public UnpackData(){
    }
    
    public UnpackData(Function f1, Function f2) {
          m_nbr = f1;
          m_dpl = f2;
    }
    
    @SuppressWarnings("rawtypes")
	public Function create(int size, Vector params)
    {
  	  return new UnpackData( (Function)params.get(0),(Function) params.get(1) );
    }
    
    public Object evaluate(Object data) {
    	String s_packedData = m_nbr.evaluateAsString(data);
    	byte[] packedData = s_packedData.getBytes();
    	int decimalPointLocation = Integer.parseInt(m_dpl.evaluateAsString(data));

        String unpackedData = ""; 

        final int negativeSign = 13; 
        for (int currentCharIndex = 0; currentCharIndex < packedData.length; currentCharIndex++) { 
    		byte firstDigit = (byte) ((packedData[currentCharIndex] >>> 4) & 0x0F); 
    		byte secondDigit = (byte) (packedData[currentCharIndex] & 0x0F); 
    		unpackedData += String.valueOf(firstDigit); 
    		if (currentCharIndex == (packedData.length - 1)) {
    			if (secondDigit == negativeSign) { 
    				unpackedData = "-" + unpackedData; 
    			} 
    		} else { 
    			unpackedData += String.valueOf(secondDigit); 
    		} 
    	}

    	if (decimalPointLocation > 0) { 
    		int position = unpackedData.length() - decimalPointLocation; 
    		unpackedData = unpackedData.substring(0, position) + "." + unpackedData.substring(position); 
    	} 
    	return unpackedData; 
    }
}