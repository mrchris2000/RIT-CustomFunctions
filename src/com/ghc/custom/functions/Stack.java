package com.ghc.custom.functions;

import com.ghc.ghTester.expressions.*;
import java.util.Vector;

public class Stack extends Function
{
   private Function m_tag = null;
   private Function m_new = null;

   public Stack() {
   }
   
   protected Stack(Function f1, Function f2)
   {
	   m_tag = f1;
	   m_new = f2;
   }

   public Object evaluate(Object data)
   {
	   String s_tag = m_tag.evaluateAsString(data);
//	   System.out.println(s_tag);
	   
	   String s_new = m_new.evaluateAsString(data);
//	   System.out.println(s_new);
	   
	   int tagLen = s_tag.length();
	   String newtag = "";
	   
	   if (tagLen>0) {
		   newtag = s_tag + "\r" + s_new;
	   } else {
		   newtag = s_new;
	   }
  
	   return newtag;
   }

   @SuppressWarnings("rawtypes")
   public Function create(int size, Vector params) 
   {
	   return new Stack((Function)params.get(0),
	   							(Function)params.get(1));
   }
}
