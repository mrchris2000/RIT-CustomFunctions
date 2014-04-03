package com.ghc.custom.functions;

import com.ghc.ghTester.expressions.*;

import java.io.File;
import java.util.*;

/**
 * This is an implementation of a function, to obtain the name of the nth file in the requested directory.
 *
 * The files are ordered by their names in ascending ASCII value.
 *
 * The function takes two parameters.
 *
 *     fileName(dirPath, nthFile)
 *
 * the first file in the directory will have the number 1 (not 0).
 *
 * The function will return the fully qualified path name for the file.
 *
 * To have the files processed in a specific order, their names will need
 * to be amended to include a number at the beginning.
 *
 * e.g.
 *
 * 001_process_this_file_first.xml
 * 002_process_this_file_second.xml
 * 003_process_this_file_third.xml
 * 004_process_this_file_fourth.xml
 *
 */
public class FileName extends Function
{
   /**
    * This function will return the directory that the files are in.
    */
   private Function m_fDir = null;

   /**
    * This function will return the file number in the directory.
    */
   private Function m_fFileNo = null;

   /**
    * Constructor used to register this function.
    */
   public FileName()
   {

   }

   /**
    * Constructor used to create an instance of this function when an
    * expression is parsed.
    *
    * @param f1 - function to return the directory
    * @param f2 - function to return the file number
    *
    */
   protected FileName( Function f1, Function f2)
   {
      m_fDir = f1;
      m_fFileNo = f2;
   }


   /**
    * Called to evaluate the function.  In this case we evaluate the functions
    * that return the date and the format of the date and use them to return
    * a date in the standard format.
    *
    * @param data - this should be passed to all evaluate calls
    */
   public Object evaluate(Object data)
   {
   	  String fileName = "";

      String dirName = m_fDir.evaluateAsString(data);
      String fileNoStr = m_fFileNo.evaluateAsString(data);

      // When evaluating an expression, a string is only considered
      // to be a string when it is surrounded by double-qoutes.  We
      // need to remove these before further processing.
      if( EvalUtils.isString(dirName) )
      {
         dirName = EvalUtils.getString(dirName);
      }
      if( EvalUtils.isString(fileNoStr) )
      {
         fileNoStr = EvalUtils.getString(fileNoStr);
      }

      File directory = new File(dirName);
      int fileNo = Integer.parseInt(fileNoStr);
      int fileIndex = fileNo-1;

      // Obtain all the file names in the directory
      File[] allFiles = directory.listFiles();

      // Now sort the files alphanumerically.
      SortedSet sortedFileNames = new TreeSet();
      for (int i=0, n=allFiles.length; i<n; i++) {
      	 if (!allFiles[i].isDirectory()) {
        	sortedFileNames.add(allFiles[i].getAbsolutePath());
      	 }
      }
      String[] allFileNames = new String[sortedFileNames.size()];
      allFileNames = (String[]) sortedFileNames.toArray(allFileNames);

      // Now get the nth (-1) file name.
      if (fileIndex < allFileNames.length) {
      	 fileName = allFileNames[fileIndex];
      }

      return "\"" + fileName + "\"";
   }

   /**
    * This function is called when an expression is being evaluated and
    * a new instance of this function needs to be created.
    *
    * @param size number of params
    * @param params a vector of Function objects, which are the parameters
    */
   public Function create( int size, Vector params )
   {
      return new FileName( (Function)params.get( 0 ),
                             (Function)params.get( 1 ));
   }
}
