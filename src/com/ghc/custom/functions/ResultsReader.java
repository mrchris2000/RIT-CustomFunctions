package com.ghc.custom.functions;

import com.ghc.ghTester.expressions.*;

import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class ResultsReader extends Function
{
   private Function m_resultSet = null;
   private Function m_writer = null;

   public ResultsReader() {
   }
   
   protected ResultsReader(Function f1, Function f2)
   {
	   m_resultSet = f1;
	   m_writer = f2;
   }

   @SuppressWarnings("unchecked")
   public Object evaluate(Object data)
   {
	   String rtn = "";
	   String resultSet = m_resultSet.evaluateAsString(data);
	   System.out.println(resultSet);
	   String filePath = m_writer.evaluateAsString(data);
	   System.out.println(filePath);

       JSONParser parser = new JSONParser();

       try (Reader reader = new FileReader(filePath)) {

           JSONObject jsonObject = (JSONObject) parser.parse(reader);
           System.out.println(jsonObject);

           String name = (String) jsonObject.get("name");
           System.out.println(name);

           long age = (Long) jsonObject.get("age");
           System.out.println(age);

           // loop array
           JSONArray msg = (JSONArray) jsonObject.get("messages");
           Iterator<String> iterator = msg.iterator();
           while (iterator.hasNext()) {
               System.out.println(iterator.next());
           }

       } catch (IOException e) {
           e.printStackTrace();
       } catch (ParseException e) {
           e.printStackTrace();
       }

	   rtn += "done";
	   return rtn;
   }

   @SuppressWarnings("rawtypes")
   public Function create(int size, Vector params) 
   {
	   return new ResultsReader((Function)params.get(0),
	   							(Function)params.get(1));
   }
}
