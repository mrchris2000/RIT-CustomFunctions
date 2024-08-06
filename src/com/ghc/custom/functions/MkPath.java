package com.ghc.custom.functions;

import java.io.File;
import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class MkPath extends Function{
    
    private Function m_pth = null;
    
    public MkPath(){
    }
    
    public MkPath(Function f1) {
          m_pth = f1;
    }
    
    @SuppressWarnings("rawtypes")
    public Function create(int size, Vector params)
    {
  	  return new MkPath( (Function)params.get(0) );
    }
    
    public Object evaluate(Object data) {
          String t_pth = m_pth.evaluateAsString(data);
          
          File file = new File(t_pth);
          file.mkdirs();
          
          return (file.exists());
    }
}