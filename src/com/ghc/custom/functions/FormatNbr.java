package com.ghc.custom.functions;

import java.util.Vector;
import java.text.*;
import com.ghc.ghTester.expressions.Function;

public class FormatNbr extends Function{
    
    private Function m_nbr = null;
    private Function m_fmt = null;
    
    public FormatNbr(){
    }
    
    public FormatNbr(Function f1, Function f2) {
          m_nbr = f1;
          m_fmt = f2;
    }
    
    @SuppressWarnings("rawtypes")
	public Function create(int size, Vector params)
    {
  	  return new FormatNbr( (Function)params.get(0),(Function) params.get(1) );
    }
    
    public Object evaluate(Object data) {
          String t_fmt = m_fmt.evaluateAsString(data);
          double d_nbr = Double.parseDouble(m_nbr.evaluateAsString(data));
          
          DecimalFormat myFormatter = new DecimalFormat(t_fmt);
          String result = myFormatter.format(d_nbr);

          return result;
    }
}