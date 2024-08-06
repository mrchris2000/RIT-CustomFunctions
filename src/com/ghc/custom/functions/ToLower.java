package com.ghc.custom.functions;

import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class ToLower extends Function{
      
      private Function m_id = null;
      
      public ToLower(){
      }
      
      // not sure this is needed
      protected ToLower(Function f1) {
            m_id = f1;
      }
      
      public Function create( int size, Vector params )
      {
    	  return new ToLower( (Function)params.get( 0 ) );
      }
      
      public Object evaluate(Object data) {
            String id = m_id.evaluateAsString(data);
            return id.toLowerCase();
      }
}
