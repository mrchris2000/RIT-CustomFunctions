package com.ghc.custom.functions;

import com.ghc.ghTester.expressions.*;

import java.util.*;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import org.json.simple.JSONObject;

//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import javax.json.Json;
//import javax.json.JsonArray;
//import javax.json.JsonWriter;

public class FilterArray extends Function
{

//   private Function m_resultSet = null;
//   private Function m_writer = null;

   public FilterArray() {
   }
   
   protected FilterArray(Function f1, Function f2)
   {
//	   m_resultSet = f1;
//	   m_writer = f2;
   }

   public Object evaluate(Object data)
   {
	   String rtn = "";
//	   ResultSet resultSet = (ResultSet) m_resultSet.evaluate(data);
//	   String filePath = m_writer.evaluateAsString(data);
	   
//	   JsonArray value = (JsonArray) m_resultSet.evaluate(data);
//	   JsonWriter writer = null;
//	   try {
//		   writer = Json.createWriter(new FileOutputStream(filePath));
//	   } catch (FileNotFoundException e) {
//		   e.printStackTrace();
//		   rtn = "not ";
//	   }
//	   writer.writeArray(value);
//	   writer.close();
	    	      
//	   FileWriter fileWriter = null;
//	   try {
//		   fileWriter = new FileWriter(filePath);
//	   } catch (IOException e1) {
//		   e1.printStackTrace();
//	   }
//	   
//	   if(resultSet == null || fileWriter == null)
//		   return "didn't work";
//		
//		ResultSetMetaData metadata = null;
//		try {
//			metadata = resultSet.getMetaData();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		int numColumns = 0;
//		try {
//			numColumns = metadata.getColumnCount();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		int numRows = 0;
//		try {
//			while(resultSet.next()) 			//iterate rows
//			{
//				++numRows;
//				JSONObject obj = new JSONObject();		//extends HashMap
//				for (int i = 1; i <= numColumns; ++i) 			//iterate columns
//				{
//					String column_name = metadata.getColumnName(i);
//					obj.put(column_name, resultSet.getObject(column_name));
//				}
//		
//			    fileWriter.write( obj.toJSONString() );
//			    fileWriter.write( "\n" );
//		
//				
//				if(numRows % 1000 == 0)
//					fileWriter.flush();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	   	rtn += "done";
		return rtn;
   }

   @SuppressWarnings("rawtypes")
   public Function create(int size, Vector params) {
	   return new FilterArray( (Function)params.get(0),
			   							(Function)params.get(1));
   }
}
