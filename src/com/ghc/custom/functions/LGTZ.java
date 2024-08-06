package com.ghc.custom.functions;

import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class LGTZ extends Function{
    
    private Function m_text = null;
    
    public LGTZ(){
    }
    
    public LGTZ(Function f1) {
          m_text = f1;
    }
    
    @SuppressWarnings("rawtypes")
	public Function create(int size, Vector params)
    {
  	  return new LGTZ((Function)params.get(0));
    }
    
    public Object evaluate(Object data) {
          String t_text = m_text.evaluateAsString(data);
          boolean t_rtn = false;
          if(t_text.length() > 0) {
        	  t_rtn = true;
          }
          
          return t_rtn;
    }
}
