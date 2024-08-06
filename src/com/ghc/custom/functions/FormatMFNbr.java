package com.ghc.custom.functions;

import java.util.Vector;
//import java.util.Arrays;
//import java.util.List;
//import java.util.ListIterator;

import com.ghc.ghTester.expressions.Function;

public class FormatMFNbr extends Function{
    
    private Function m_nbr = null;
    private Function m_fmt = null;
    
    public FormatMFNbr(){
    }
    
    public FormatMFNbr(Function f1, Function f2) {
          m_nbr = f1;
          m_fmt = f2;
    }
    
    @SuppressWarnings("rawtypes")
	public Function create(int size, Vector params)
    {
  	  return new FormatMFNbr( (Function)params.get(0),(Function) params.get(1) );
    }
    
    public Object evaluate(Object data) {
          String t_nbr = m_nbr.evaluateAsString(data);
          t_nbr = t_nbr.replace("-", "");
          System.out.println("32 t_nbr=" + t_nbr);

          String t_fmt = m_fmt.evaluateAsString(data);
          System.out.println("35 t_fmt=" + t_fmt);
          double d_nbr = Double.parseDouble(m_nbr.evaluateAsString(data));
          System.out.println("37 d_nbr=" + d_nbr);
          
          int l_nbr = t_nbr.length() - 1;
          System.out.println("40 l_nbr=" + l_nbr);
          int l_fmt = t_fmt.length() - 1;
          System.out.println("42 l_fmt=" + l_fmt);
          int rc_fmt = l_fmt;
          System.out.println("44 rc_fmt=" + rc_fmt);
          
          for (int c_nbr = l_nbr; c_nbr >= 0; c_nbr--) {
              System.out.println("47 c_nbr=" + c_nbr);
        	  for (int c_fmt = rc_fmt; c_fmt >= 0; c_fmt--) {
                  System.out.println("49 c_fmt=" + c_fmt);
                  System.out.println("50 t_fmt.substring(c_fmt, c_fmt + 1)=" + t_fmt.substring(c_fmt, c_fmt + 1));
        		  if (t_fmt.substring(c_fmt, c_fmt + 1).matches("[9Z]")) { // || t_fmt.substring(c_fmt, c_fmt + 1) == "Z") {
			          System.out.println("52 c_fmt=" + c_fmt);
    				  if (c_fmt == 0) {
        				  t_fmt = t_nbr.substring(c_nbr, c_nbr + 1) + t_fmt.substring(c_fmt + 1);
        		          System.out.println("55 t_fmt=" + t_fmt);
        				  rc_fmt = c_fmt - 1;
        		          System.out.println("57 rc_fmt=" + rc_fmt);
        				  break;
        			  }
        			  else {
        				  t_fmt = t_fmt.substring(0, c_fmt) + t_nbr.substring(c_nbr, c_nbr + 1) + t_fmt.substring(c_fmt + 1);
        		          System.out.println("62 t_fmt=" + t_fmt);
        				  rc_fmt = c_fmt - 1;
        		          System.out.println("64 rc_fmt=" + rc_fmt);
        				  break;
        			  }
        		  }
        	  }
          }
          System.out.println("70 rc_fmt=" + rc_fmt);
          System.out.println("71 t_fmt=" + t_fmt);
          
    	  for (int c_fmt = rc_fmt; c_fmt >= 0; c_fmt--) {
              System.out.println("74 c_fmt=" + c_fmt);
              System.out.println("75 char=" + t_fmt.substring(c_fmt, c_fmt + 1));

    		  switch (t_fmt.substring(c_fmt, c_fmt + 1)) {
    		  	case "9":  
      			  if (c_fmt == 0) {
    				  t_fmt = "0" + t_fmt.substring(c_fmt + 1);
    		          System.out.println("81 t_fmt=" + t_fmt);
    				  break;
    			  }
    			  else {
    				  t_fmt = t_fmt.substring(0, c_fmt) + "0" + t_fmt.substring(c_fmt + 1);
    		          System.out.println("86 t_fmt=" + t_fmt);
    				  break;
    			  }
                case "Z": 
        			  if (c_fmt == 0) {
        				  t_fmt = " " + t_fmt.substring(c_fmt + 1);
        		          System.out.println("92 t_fmt=" + t_fmt);
        				  break;
        			  }
        			  else {
        				  t_fmt = t_fmt.substring(0, c_fmt) + " " + t_fmt.substring(c_fmt + 1);
        		          System.out.println("97 t_fmt=" + t_fmt);
        				  break;
        			  }
    		  }
    	  }

    	  //fix hanging commas
    	  t_fmt = t_fmt.replace(" ,", "  ");
          System.out.println("105 t_fmt=" + t_fmt);
    	  
    	  //fix negative sign, if present
    	  if (d_nbr >= 0) {
    		  t_fmt = t_fmt.replace("-", " ");
              System.out.println("110 t_fmt=" + t_fmt);
    	  }
    	  
    	  //suppress leading spaces while allowing for a leading negative sign
    	  if (t_fmt.substring(0, 1).contains("-")) {
    		  int i = 1;
    		  while (i < t_fmt.length() && Character.isWhitespace(t_fmt.charAt(i))) {
    			  i++;
    		  }
    		  t_fmt = "-" + t_fmt.substring(i);
              System.out.println("120 t_fmt=" + t_fmt);
    	  }
    	  else {
        	  int i = 0;
    		  while (i < t_fmt.length() && Character.isWhitespace(t_fmt.charAt(i))) {
    			  i++;
    		  }
    		  t_fmt = t_fmt.substring(i);
              System.out.println("128 t_fmt=" + t_fmt);
    	  }

          return t_fmt;
    }
}