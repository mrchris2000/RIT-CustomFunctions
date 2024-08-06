package com.ghc.custom.functions;
// Prepend a string with zeroes until it reaches the desired length
import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class Prepend extends Function{
    
    private Function m_text = null;
    private Function m_len = null;
    
    public Prepend(){
    }
    
    public Prepend(Function f1, Function f2) {
          m_text = f1;
          m_len = f2;
    }
    
    @SuppressWarnings("rawtypes")
    public Function create(int size, Vector params)
    {
  	  return new Prepend( (Function)params.get(0),(Function) params.get(1) );
    }
    
    public Object evaluate(Object data) {
          String t_text = m_text.evaluateAsString(data);
          int t_len = Integer.parseInt(m_len.evaluateAsString(data));
          int c_len = t_text.length();
          
          while (c_len<t_len) {
        	  t_text = "0" + t_text;
        	  c_len = t_text.length();
          }
          
          return t_text.substring(0,t_len);
    }
}