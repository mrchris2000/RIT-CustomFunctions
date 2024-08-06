package com.ghc.custom.functions;
// Returns a string of spaces of the desired length
import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class Spaces extends Function{
    
    private Function m_len = null;
    
    public Spaces(){
    }
    
    public Spaces(Function f1) {
          m_len = f1;
    }
    
    @SuppressWarnings("rawtypes")
	public Function create(int size, Vector params)
    {
  	  return new Spaces((Function)params.get(0));
    }
    
    public Object evaluate(Object data) {
          Integer i_len = Integer.parseInt(m_len.evaluateAsString(data));
          StringBuffer outputBuffer = new StringBuffer(i_len);
          for (int i = 0; i < i_len; i++){
             outputBuffer.append(" ");
          }
          return outputBuffer.toString();
    }
}
