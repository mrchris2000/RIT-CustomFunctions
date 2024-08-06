package com.ghc.custom.functions;

import com.ghc.ghTester.expressions.*;

import java.util.Vector;


public class SendMail extends Function
{
   private Function m_Recipients = null;
   private Function m_Subject = null;
   private Function m_Message = null;

   public SendMail() {
   }
   
   protected SendMail(Function f1, Function f2, Function f3)
   {
	   m_Recipients = f1;
	   m_Subject = f2;
	   m_Message = f3;
   }

   public Object evaluate(Object data)
   { 
       String sRecipients = m_Recipients.evaluateAsString(data);
       String sSubject = m_Subject.evaluateAsString(data);
       String sMessage = m_Message.evaluateAsString(data);

       return sRecipients + sSubject + sMessage;
   }
   @SuppressWarnings("rawtypes")
   public Function create(int size, Vector params) 
   {
	   return new SendMail((Function)params.get(0),
	   							(Function)params.get(1),
	   							(Function)params.get(2));
   }
}
