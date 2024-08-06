package com.ghc.custom.functions;

import java.util.Base64;
import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class Base64Encode extends Function{
    
    private Function m_text = null;
    
    public Base64Encode(){
    }
    
    public Base64Encode(Function f1) {
          m_text = f1;
    }
    
    @SuppressWarnings("rawtypes")
	public Function create(int size, Vector params)
    {
  	  return new Base64Encode((Function)params.get(0));
    }
    
    public Object evaluate(Object data) {
          String t_text = m_text.evaluateAsString(data);
          
          return Base64.getEncoder().encodeToString(t_text.getBytes());
    }
}
