package com.ghc.custom.functions;

import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class Scrutinize extends Function{
	//Return values:
	// 0 - text1 = text2
	// 1 - text1 != text2
	// 2 - comparison not performed 
    
    private Function m_text1 = null;
    private Function m_text2 = null;
    
    public Scrutinize(){
    }
    
    protected Scrutinize(Function f1, Function f2) {
          m_text1 = f1;
          m_text2 = f2;
    }
    
    @SuppressWarnings("rawtypes")
	public Function create(int size, Vector params)
    {
  	  return new Scrutinize((Function)params.get(0),(Function)params.get(1));
    }
    
    public Object evaluate(Object data) {
          String t_text1 = m_text1.evaluateAsString(data);
//        System.out.println("32 '" + t_text1 + "'");
          String t_text2 = m_text2.evaluateAsString(data);
//        System.out.println("34 '" + t_text2 + "'");
          
          int rtn; 
          String carat = "^";
 //       System.out.println("37 " + (t_text1.compareTo(carat)));
 //       System.out.println("38 " + (t_text2.compareTo(carat)));
 //       System.out.println("39 " + (t_text1==t_text2));
          if((t_text1.compareTo(carat))==0 || (t_text2.compareTo(carat)==0)) {
        	  System.out.println("41");
        	  rtn = 2;
          } else {
        	  if(t_text1.compareTo(t_text2)==0) {
//	        	  System.out.println("45");
        		  rtn = 0;
        	  } else {
        		  rtn = 1;        	  
        	  }
          }
          
          return rtn;
    }
}
