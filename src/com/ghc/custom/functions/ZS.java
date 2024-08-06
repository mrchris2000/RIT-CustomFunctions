package com.ghc.custom.functions;

import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class ZS extends Function{
    
    private Function m_text = null;
    
    public ZS(){
    }
    
    public ZS(Function f1) {
          m_text = f1;
    }
    
    @SuppressWarnings("rawtypes")
	public Function create(int size, Vector params)
    {
  	  return new ZS((Function)params.get(0));
    }
    
    public Object evaluate(Object data) {
          String t_text = m_text.evaluateAsString(data);
          String tr_text = t_text.trim();
          String zs_text = tr_text.replaceFirst("^0+(?!$)", "");
          if(zs_text.contentEquals("")) {
        	  zs_text = "0";
          }
          return zs_text;
    }
}
