package com.ghc.custom.functions;

import com.ghc.ghTester.expressions.Function;

import java.io.BufferedReader;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Vector;

/*
 * Java Program read a User Info text file in multiple ways.
 * This program demonstrates how you can use FileReader,
 * BufferedReader, and Scanner to read a text file,
 * along with newer utility methods added in JDK 7 and 8. 
 * 1 - reading a text file using FileReader
 * 2 - reading a text file using BufferedReader
 * 3 - reading a text file using Scanner
 * 4 - reading a text file using Stream
 * 5 - filtering empty lines from a file
 * 6 - reading a text file as a String
 * 7 - reading whole file in a List
 * 8 - reading a text file into an array
 */
public class ReadUser extends Function{
	
	// file path & name
	private Function m_id = null;
	
	// file read option
	private Function m_opt = null;

	public ReadUser(){
		
	}
	
	public ReadUser( Function f1, Function f2 ){		
		m_id = f1;
		m_opt = f2;
	}
	
	@SuppressWarnings("rawtypes")
    public Function create( int size, Vector params )
    {
  	  return new ReadUser( (Function)params.get(0),(Function) params.get(1) );
    }

    public Object evaluate(Object data) {
        String id = m_id.evaluateAsString(data);
        String RV = "";
        int opt = Integer.parseInt(m_opt.evaluateAsString(data));

        switch (opt) {
        	case 1:
        		// Example 1 - reading a text file using FileReader in Java
			    RV = readTextFileUsingFileReader(id);
			    break;
			    
        	case 2:
			    // Example 2 - reading a text file in Java using BufferedReader
        		RV = readFileLineByLine(id);
        		break;

        	case 3:
        		// Example 3 - reading a text file in Java using Scanner
        		RV = readTextFileUsingScanner(id);
        		break;
        		
        	case 4:
			    // Example 4 - reading a text file using Stream in Java 8
			    try {
					Files.lines(Paths.get(id)).forEach(System.out::println);
					RV = Files.lines(Paths.get(id)).collect(Collectors.joining());
				} catch (IOException e) {
					e.printStackTrace();
				}
			    break;
	    
        	case 5:
        		// Example 5 - filtering empty lines from a file in Java 8
			    try {
					RV = Files.lines(new File(id).toPath())
					.map(s -> s.trim()) 
					.filter(s -> !s.isEmpty())
					.collect(Collectors.joining());
					//.forEach(System.out::println)
					;
				} catch (IOException e) {
					e.printStackTrace();
				}
			    break;
			    
        	case 6:
        		// Example 6 - reading a text file as String in Java
			    RV = readFileAsString(id);
			    break;
		
        	case 7:
			    // Example 7 - reading whole file in a List
			    List<String> lines = readFileInList(id);
			    RV = lines.toString();
//			    System.out.println("Total number of lines in file: " + lines.size());
			    break;
			    
        	case 8:
			    // Example 8 - reading a text file in java into an array
			    String[] arrayOfString = readFileIntoArray(id);
//			    RV = readFileIntoArray(id);
			    for(String line: arrayOfString){
//			    System.out.println(line);
			    RV += line + "\n";
			    }
			    break;
			            		
        	default:
        		// Invalid option selected
        		RV = "Option = " + opt + "; Select options 1-9";
        		break;
        		
        }
	    
//        return id + " = " + RV;
        return RV;
}

public static String readTextFileUsingFileReader(String fileName) {
	String fileText = "";
	try {
      FileReader textFileReader = new FileReader(fileName);
      char[] buffer = new char[8096];
      int numberOfCharsRead = textFileReader.read(buffer);
      while (numberOfCharsRead != -1) {
//        System.out.println(String.valueOf(buffer, 0, numberOfCharsRead));
        fileText += String.valueOf(buffer, 0, numberOfCharsRead);
        numberOfCharsRead = textFileReader.read(buffer);
      }
      textFileReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return fileText;
  }

  public static String readTextFileUsingScanner(String fileName) {
	  String fileText = "";
    try {
      Scanner sc = new Scanner(new File(fileName));
      while (sc.hasNext()) {
        String str = sc.nextLine();
		  fileText += str;
//        System.out.println(str);
      }
      sc.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
	  return fileText;
  }

  public static String readFileAsString(String fileName) {
    String data = "";
    try {
      data = new String(Files.readAllBytes(Paths.get(fileName)));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return data;
  }

  public static List<String> readFileInList(String fileName) {
    List<String> lines = Collections.emptyList();
    try {
      lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
//      for(String line: lines) {
//          System.out.println(line);
//      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return lines;
  }

  public static String[] readFileIntoArray(String fileName) {
    List<String> list = readFileInList(fileName);
    return list.toArray(new String[list.size()]);

  }

  public static String readFileLineByLine(String fileName) {
	  String fileText = "";
	  try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
		  String line = br.readLine();
		  while (line != null) {
//			  System.out.println(line);
			  fileText += line;
			  line = br.readLine();
		  }
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
	  return fileText;
  }

}
