package com.ghc.custom.functions;

import java.io.BufferedReader;
//import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.URL;
//import javax.net.ssl.HttpsURLConnection;
import java.util.Vector;

import com.ghc.ghTester.expressions.Function;

public class ExecSuite extends Function{
    
    private Function m_projectName = null;
    private Function m_suite = null;
    private Function m_environment = null;
    
    public ExecSuite(){
    }
    
    public ExecSuite(Function f1, Function f2, Function f3) {
          m_projectName = f1;
          m_suite = f2;
          m_environment = f3;
    }
        
    public Object evaluate(Object data) {
        String projectName = m_projectName.evaluateAsString(data);
        String suite = m_suite.evaluateAsString(data);
        String environment = m_environment.evaluateAsString(data);
        String cmd = "";
        String url = "";
		Process p;

		try {
			cmd = "runtests -project " + projectName + " -run " + suite + " -environment " + environment + " > c:\\temp\\ExecSuite.txt";
			p = Runtime.getRuntime().exec(cmd);
			
			p.waitFor(); 
			BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
			String line; 
			while((line = reader.readLine()) != null) { 
				System.out.println(line);
			} 
		} catch (IOException e) {
			return e.toString();
		} catch (InterruptedException e) {
			return e.toString();
		}
		return url;
    }

    @SuppressWarnings("rawtypes")
	public Function create(int size, Vector params)
    {
      	  return new ReportExec((Function)params.get(0),(Function)params.get(1));
    }
    
/*	private String doHttpUrlConnectionAction(String desiredUrl, String p_user, String p_projectName)
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
			
			// read the output from the server - 201=successful
//			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//			StringBuilder stringBuilder = new StringBuilder();
//
//			String line = null;
//			while ((line = reader.readLine()) != null)
//			{
//				stringBuilder.append(line + "\n");
//			}
//			return responseCode + "-" + stringBuilder.toString();
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
*/
}
