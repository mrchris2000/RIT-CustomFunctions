package com.ghc.custom.functions;

import java.util.Base64;
import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class AuthEncode extends Function{
    
    private Function m_user = null;
    private Function m_pwd = null;
    
    public AuthEncode(){
    }
    
    public AuthEncode(Function f1, Function f2) {
          m_user = f1;
          m_pwd = f2;
    }
        
    public Object evaluate(Object data) {
          String t_user = m_user.evaluateAsString(data);
          String t_pwd = m_pwd.evaluateAsString(data);
          String t_text = t_user + ":" + t_pwd;
          
          return "Basic " + Base64.getEncoder().encodeToString(t_text.getBytes());
    }

    @SuppressWarnings("rawtypes")
	public Function create(int size, Vector params)
    {
      	  return new AuthEncode((Function)params.get(0),(Function)params.get(1));
    }

}
