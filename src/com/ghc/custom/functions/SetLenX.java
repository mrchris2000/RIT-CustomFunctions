package com.ghc.custom.functions;

import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class SetLenX extends Function{
    
    private Function m_text = null;
    private Function m_char = null;
    private Function m_len = null;
    
    public SetLenX(){
    }
    
    public SetLenX(Function f1, Function f2, Function f3) {
          m_text = f1;
          m_char = f2;
          m_len = f3;
    }
    
    @SuppressWarnings("rawtypes")
    public Function create(int size, Vector params)
    {
  	  return new SetLenX( (Function)params.get(0),(Function)params.get(1),(Function)params.get(2) );
    }
    
    public Object evaluate(Object data) {
          String t_text = m_text.evaluateAsString(data);
          String t_char = m_char.evaluateAsString(data);
          if (t_char.length()<1) {
        	  t_char=" ";
          }
          int t_len = Integer.parseInt(m_len.evaluateAsString(data));
          int c_len = t_text.length();
          
          while (c_len<t_len) {
        	  t_text += t_char;
        	  c_len = t_text.length();
          }
          
          return t_text.substring(0,t_len);
    }
}