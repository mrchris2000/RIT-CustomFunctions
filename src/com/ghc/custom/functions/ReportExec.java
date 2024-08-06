package com.ghc.custom.functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class ReportExec extends Function{
    
    private Function m_user = null;
    private Function m_projectName = null;
    
    public ReportExec(){
    }
    
    public ReportExec(Function f1, Function f2) {
          m_user = f1;
          m_projectName = f2;
    }
        
    public Object evaluate(Object data) {
          String t_user = m_user.evaluateAsString(data);
          String t_projectName = m_projectName.evaluateAsString(data);
          
  		try
  		{
  			String myUrl = "https://eftaf-eapi.us.bank-dns.com/api/Frameworkusers";
  			String results = doHttpUrlConnectionAction(myUrl, t_user, t_projectName);
  			return results;
  		}
  		catch (Exception e)
  		{
  			return e.toString();
  		}

    }

    @SuppressWarnings("rawtypes")
	public Function create(int size, Vector params)
    {
      	  return new ReportExec((Function)params.get(0),(Function)params.get(1));
    }
    
	private String doHttpUrlConnectionAction(String desiredUrl, String p_user, String p_projectName)
			throws Exception
	{
		URL url = null;
		BufferedReader reader = null;

		try
		{
			// create the HttpURLConnection
			url = new URL(desiredUrl);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod("POST");

			connection.setDoOutput(true);
			String jsonInputString = "{\"prefferedID\":\"" + p_user + "\"," +
									"\"projectname\":\"" + p_projectName + "\"," +
									"\"frameworkType\":\"RTW\"," + 
									"\"programmingLanguage\":\"HATS\"," +
									"\"mainFrame\":true}";

			try(OutputStream os = connection.getOutputStream()) {
			    byte[] input = jsonInputString.getBytes("utf-8");
			    os.write(input, 0, input.length);
			}
			// give it 15 seconds to respond
			connection.setReadTimeout(15*1000);
			connection.connect();

			int responseCode = connection.getResponseCode();
			
			return Integer.toString(responseCode);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			// close the reader;
			if (reader != null)
			{
				try
				{
					reader.close();
				}
				catch (IOException ioe)
				{
					ioe.printStackTrace();
				}
			}
		}
	}

}
