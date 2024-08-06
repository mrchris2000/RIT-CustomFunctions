package com.ghc.custom.functions;

import com.ghc.ghTester.expressions.*;

import java.io.File;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.io.IOException;

public class MoveFilebu extends Function
{

   private Function m_from = null;
   private Function m_to = null;
   private Function m_opt = null;

   public MoveFilebu() {}

   protected MoveFilebu( Function f1, Function f2, Function f3)
   {
      m_from = f1;
      m_to = f2;
      m_opt = f3;
   }

   public Object evaluate(Object data)
   {
   	  String success = "";
   	  boolean addCRLF = false;

      String fileFrom = m_from.evaluateAsString(data);
      String fileTo = m_to.evaluateAsString(data);
      String moveOpt = m_opt.evaluateAsString(data);

      if( EvalUtils.isString(fileFrom) ) {
    	  fileFrom = EvalUtils.getString(fileFrom);
      }
      if( EvalUtils.isString(fileTo) ) {
    	  fileTo = EvalUtils.getString(fileTo);
      }
      if( EvalUtils.isString(moveOpt) ) {
    	  moveOpt = EvalUtils.getString(moveOpt);
      }

      File fFile = new File(fileFrom);
      File tFile = new File(fileTo);
      
      if (moveOpt.equals("M")) {
    	  if (Files.exists(tFile.toPath())) {
    		  tFile.delete();
    	  }
    	  if(fFile.renameTo (new File(fileTo))) {
	          // if file copied successfully then delete the original file
	          fFile.delete();
	          success = "M";
	      } else {
	          success = "F";
	      }
      }
      else if (moveOpt.equals("A")) {
          try {
		    InputStream is = null;
		    OutputStream os = null;
		    try {
		        if (Files.exists(tFile.toPath())) {
		        	addCRLF = true;
		        }
		        is = new FileInputStream(fFile);
		        os = new FileOutputStream(tFile, true);
		        if (addCRLF) {
		        	os.write(10);
		        }
		        byte[] buffer = new byte[1024];
		        int length;
		        while ((length = is.read(buffer)) > 0) {
		            os.write(buffer, 0, length);
		        }
		    } finally {
		        is.close();
		        fFile.delete();
		        os.flush();
		        os.close();
		    }
	        success = "A"; 	  
          } catch (IOException e) {
			e.printStackTrace();
			success = "F";
          }
      }

      return success;
   }
   
   @SuppressWarnings("rawtypes")
   public Function create( int size, Vector params )
   {
      return new MoveFilebu( (Function)params.get( 0 ),
                             (Function)params.get( 1 ),
                             (Function)params.get( 2 ));
   }
}
