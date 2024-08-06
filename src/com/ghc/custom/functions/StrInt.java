package com.ghc.custom.functions;

import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class StrInt extends Function{
    
    private Function m_text = null;
    
    public StrInt(){
    }
    
    public StrInt(Function f1) {
          m_text = f1;
    }
    
    @SuppressWarnings("rawtypes")
	public Function create(int size, Vector params)
    {
  	  return new StrInt((Function)params.get(0));
    }
    
    public Object evaluate(Object data) {
          String t_int = m_text.evaluateAsString(data);
          int i_int = new Integer(t_int).intValue();
          String s_int = String.valueOf(i_int);
          return s_int;
    }
}
