package com.ghc.custom.functions;

import com.ghc.ghTester.expressions.Function;

import java.util.Vector;

public class ToUpper extends Function
{
   /**
    * This function will return the log level that we wish to imply.
    */
   private Function m_fValue = null;

   /**
    * Constructor used to register this function.
    */
   public  ToUpper()
   {

   }

   /**
    * Constructor used to create an instance of this function when an
    * expression is parsed.
    *
    * @param f1 - function to return the password string
    *
    */
   public ToUpper( Function f1)
   {
       m_fValue = f1;
   }


   /**
    * Evaluate method is used to do the meat of the work.
    * This will always return 'done' to the caller regardless of success or failure
    *
    *
    * @param data - this should be passed to all evaluate calls
    */
   public synchronized Object evaluate( Object data )
   {
      String value = m_fValue.evaluateAsString( data );
      
      return value.toUpperCase();
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
      return new ToUpper( (Function)params.get( 0 ) );
   }
}