package com.ghc.custom.functions;

import com.ghc.ghTester.expressions.*;

import java.util.Vector;

import java.io.FileWriter;
import java.io.IOException;

public class ScreenLog extends Function
{
   private Function m_val = null;
   private Function m_file = null;

   public ScreenLog() {
   }
   
   protected ScreenLog(Function f1, Function f2)
   {
	   m_val = f1;
	   m_file = f2;
   }

   public Object evaluate(Object data)
   {
	   String s_val = m_val.evaluateAsString(data);
//	   System.out.println(s_val);
	   
	   String s_file = m_file.evaluateAsString(data);
//	   System.out.println(s_file);
	   
	   int valLen = s_val.length();
		
	   String logText = "";
	   int start = 0;
	   int end = 80;
	   String writeFail = "";

	   try {
		   FileWriter file = new FileWriter(s_file, true);
		   while (valLen>start) {
			   if (end<valLen) {
				   logText = s_val.substring(start, end)+"\r";
			   } else {
				   logText = s_val.substring(start)+"\r";
			   }
		   
			   file.write(logText);
//			   System.out.println("Successfully wrote to the file.");

			   start = start + 80;
		       end = end + 80;
		   }
		   file.close();
	   } catch (IOException e) {
		   System.out.println("An error occurred.");
		   writeFail=e.getStackTrace().toString();
		   e.printStackTrace();
	   }
  
	   return writeFail;
   }

   @SuppressWarnings("rawtypes")
   public Function create(int size, Vector params) 
   {
	   return new ScreenLog((Function)params.get(0),
	   							(Function)params.get(1));
   }
}
