package com.ghc.custom.functions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class NamedListsToCsv extends Function {

	private int size;
	private Vector<Function> params;

	public NamedListsToCsv() {
	}

	public NamedListsToCsv(int size, Vector<Function> params) {
		this.size = size;
		this.params = params;
	}

	@SuppressWarnings("unchecked")
	public Function create(int size, @SuppressWarnings("rawtypes") Vector params) {
		this.params = params;
		return new NamedListsToCsv(size, params);
	}
	
	private static final String LINEFEED = String.format("%n");
	private static final String SEPARATOR = ",";
	
	private static String quoteCsvField(String str) {
		return String.format("\"%s\"", str.replaceAll("\"", "\"\""));
	}

	private static Object unwrap(Object obj) {
		if (!(obj.getClass().getName().equals("sun.org.mozilla.javascript.internal.NativeJavaObject"))) {
			return obj;
		} else {
			try {
				Method unwrap = obj.getClass().getMethod("unwrap");
				Object wrapped = unwrap.invoke(obj);
				return wrapped;
			} catch (Exception e) {
				System.err.println(e); // Goes to JVM Console in RIT
				return obj;
			}
		}
	}
	
	public Object evaluate(Object data) {
		assert ( size >= 1 ) && ( size % 2 == 0 ) : "Wrong number of parameters";

		LinkedHashMap<String,List<?>> columns = new LinkedHashMap<>( size/2 );
		for (int col=1; col<size; col += 2) {
			String name = params.get(col-1).evaluateAsString(data);
			Object obj = unwrap( params.get(col).evaluate(data) );
			if (!(obj instanceof List))
				throw new RuntimeException(String.format("Parameters must be names and lists alternately." +
						" Type found: %s", obj.getClass().getName()));
			columns.put(name, (List<?>)obj);
		}
		
		int rows = -1;
		for (Entry<String,List<?>> entry : columns.entrySet()) {
			if (rows == -1) 
				rows = entry.getValue().size();
			else 
				if (rows != entry.getValue().size())
					throw new RuntimeException("All lists must be of equal size");
		}

		List<Map.Entry<String,List<?>>> entries = new ArrayList<>( columns.entrySet() );
		Map.Entry<String,List<?>> last = entries.get( entries.size()-1 );
		
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String,List<?>> entry : entries) {
			sb.append( quoteCsvField( entry.getKey() ) );
			sb.append( entry == last ? LINEFEED : SEPARATOR );
		}
		for (int row = 0; row < rows; row++) {
			for (Map.Entry<String,List<?>> entry : entries) {
				sb.append( quoteCsvField( entry.getValue().get(row).toString() ) );
				sb.append( entry == last ? LINEFEED : SEPARATOR );
			}
		}
		
		return sb.toString();
	}
}