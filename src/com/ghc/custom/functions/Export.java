package com.ghc.custom.functions;

import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

//import com.ghc.ghTester.resources.*;

public class Export extends Function{
    
    private Function m_text = null;
    
    public Export(){
    }
    
    public Export(Function f1) {
          m_text = f1;
    }
    
    @SuppressWarnings("rawtypes")
	public Function create(int size, Vector params)
    {
  	  return new Export((Function)params.get(0));
    }
    
    public Object evaluate(Object data) {
          String t_text = m_text.evaluateAsString(data);
          return t_text.length();
    }
}
