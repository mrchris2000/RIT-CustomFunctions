package com.ghc.custom.functions;

import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class Len extends Function{
    
    private Function m_text = null;
    
    public Len(){
    }
    
    public Len(Function f1) {
          m_text = f1;
    }
    
    @SuppressWarnings("rawtypes")
	public Function create(int size, Vector params)
    {
  	  return new Len((Function)params.get(0));
    }
    
    public Object evaluate(Object data) {
          String t_text = m_text.evaluateAsString(data);
          return t_text.length();
    }
}
