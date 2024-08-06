package com.ghc.custom.functions;

import com.ghc.ghTester.expressions.*;

import java.util.Vector;

//import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.io.FileWriter;
import java.io.IOException;

public class ResultsWriter extends Function
{
   private Function m_resultSet = null;
   private Function m_writer = null;

   public ResultsWriter() {
   }
   
   protected ResultsWriter(Function f1, Function f2)
   {
	   m_resultSet = f1;
	   m_writer = f2;
   }

   @SuppressWarnings("unchecked")
   public Object evaluate(Object data)
   {
	   String rtn = "";
	   String resultSet = m_resultSet.evaluateAsString(data);
//	   String pResultSet = resultSet;
	   System.out.println(resultSet);
	   String filePath = m_writer.evaluateAsString(data);
	   System.out.println(filePath);

	   //parse resultSet
	   //load results to the output obj
	   //write obj to file
	   
       JSONObject obj = new JSONObject();
//       obj.put("name", "google.com");
//       obj.put("age", 90);

       JSONArray list = new JSONArray();
       list.add(resultSet);
//       list.add("msg 2");
//       list.add("msg 3");

       obj.put("values", list);
       
	   //String to store text that will be output
//	   String logText = "";
//	   int start = 0;
//	   int valLen = resultSet.length();
//	   int end = resultSet.length();
	   String writeFail = "";
	   
       try (FileWriter file = new FileWriter(filePath)) {
           file.write(obj.toJSONString());
       } catch (IOException e) {
           e.printStackTrace();
       }
//	   try {
//		   FileWriter file = new FileWriter(filePath, true);
//		   while (valLen>start) {
//			   if (end<valLen) {
//				   logText = resultSet.substring(start, end)+"\r";
//			   } else {
//				   logText = resultSet.substring(start)+"\r";
//			   }
//		   
//			   file.write(logText);
////			   System.out.println("Successfully wrote to the file.");
//
//			   start = start + valLen;
//		       end = end + valLen;
//		   }
//		   file.close();
//		   
//	   } catch (IOException e) {
//		   System.out.println("An error occurred.");
//		   writeFail=e.getStackTrace().toString();
//		   e.printStackTrace();
//	   }
	   
       System.out.print(obj);

	   rtn += "done";
	   return rtn + writeFail;
   }

   @SuppressWarnings("rawtypes")
   public Function create(int size, Vector params) 
   {
	   return new ResultsWriter((Function)params.get(0),
	   							(Function)params.get(1));
   }
}
