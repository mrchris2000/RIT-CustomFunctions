package com.ghc.custom.functions;

import com.ghc.ghTester.expressions.*;

import java.util.Vector;

import java.io.FileWriter;
import java.io.IOException;

public class LogPieces extends Function
{
   private Function m_val = null;
   private Function m_len = null;
   private Function m_file = null;

   public LogPieces() {
   }
   
   protected LogPieces(Function f1, Function f2, Function f3)
   {
	   m_val = f1;
	   m_len = f2;
	   m_file = f3;
   }

   public Object evaluate(Object data)
   {
	   String s_val = m_val.evaluateAsString(data);
//	   System.out.println(s_val);
	   int i_len = (int) m_len.evaluate(data);
//	   System.out.println(i_len);
	   String s_file = m_file.evaluateAsString(data);
//	   System.out.println(s_file);
	   
	   int valLen = s_val.length();
		
	   //String to store text that will be output
	   String logText = "";
	   int start = 0;
	   int end = i_len;
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

			   start = start + i_len;
		       end = end + i_len;
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
	   return new LogPieces((Function)params.get(0),
	   							(Function)params.get(1),
	   							(Function)params.get(2));
   }
}
