package com.ghc.custom.functions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Vector;

import com.ghc.ghTester.expressions.EvalUtils;
import com.ghc.ghTester.expressions.Function;
import com.ghc.ghTester.expressions.TagStorer;
import de.mud.*;
import de.mud.telnet.*;

public class TelnetConnection extends Function {

	private Function m_fHost;

	private Function m_fPassword;

	private Function m_fUsername;

	private Function m_fCommand;

	private Function m_fStdOut;

	private Function m_fStdErr;

	private static final int TIMEOUT = 30000;

	private static final String FUNCTION_NAME = "TelnetConnection";

	/**
	 * Constructor
	 */
	public TelnetConnection() {
	}

	/**
	 * Constructor
	 */
	public TelnetConnection(Function host, Function user, Function pass,
			Function command, Function stdout, Function stderr) {
		m_fHost = host;
		m_fUsername = user;
		m_fPassword = pass;
		m_fCommand = command;
		m_fStdOut = stdout;
		m_fStdErr = stderr;
	}

	public TelnetConnection(Function host, Function user, Function pass,
			Function command) {
		m_fHost = host;
		m_fUsername = user;
		m_fPassword = pass;
		m_fCommand = command;
	}

	/**
	 * 
	 * @see com.ghc.expr.Function#getSyntax()
	 */
	public String getSyntax() {
		return FUNCTION_NAME
				+ "(Host, Username, Password, Command[, StdOutTag, StdErrTag])";
	}

	/**
	 * 
	 * @see com.ghc.expr.Function#create(int, java.util.Vector)
	 */
	public Function create(int size, Vector params) {
		if (params.size() > 4) {
			return new TelnetConnection((Function) params.get(0),
					(Function) params.get(1), (Function) params.get(2),
					(Function) params.get(3), (Function) params.get(4), (Function) params.get(5));
		} else {
			return new TelnetConnection((Function) params.get(0),
					(Function) params.get(1), (Function) params.get(2),
					(Function) params.get(3));
		}
	}

	/**
	 * @see com.ghc.ghTester.function#evaluate(java.lang.Object)
	 */
	public Object evaluate(Object data) {
		String hostname = m_fHost.evaluateAsString(data);
		String username = m_fUsername.evaluateAsString(data);
		String password = m_fPassword.evaluateAsString(data);
		String command = m_fCommand.evaluateAsString(data);
		String stdoutTag = "";
		String stderrTag = "";
		try{
			stdoutTag = m_fStdOut.evaluateAsString(data);
			stderrTag = m_fStdErr.evaluateAsString(data);
		}catch(Exception e){
			e.printStackTrace();
		}
	
		System.out.println("Tags:"+stdoutTag+":"+stderrTag);

		// System.out.println("stdoutTag:"+stdoutTag);
		// System.out.println("stderrTag:"+stderrTag);
		String stdout = "";
		String stderr = "";

		if (command.charAt(0) == '"') {
			command = command.substring(1, command.length() - 1);
		}

		try {
			if (EvalUtils.isString(stderrTag))
				stderrTag = EvalUtils.getString(stderrTag);
			if (EvalUtils.isString(stdoutTag))
				stdoutTag = EvalUtils.getString(stdoutTag);
			
			  TelnetWrapper telnet = new TelnetWrapper();
			  try {
			      telnet.connect(hostname, 23);
			      System.out.println("Connected");
			      telnet.login(username, password);
			      //System.out.println("Logged in");
			      telnet.setPrompt(hostname);
			      //System.out.println("Prompt");
			      telnet.waitfor(hostname);
			      //System.out.println("Term type");
			      //telnet.send("dumb");
			      //System.out.println("Send");
			      stdout = telnet.send(command);
			      System.out.println("Command sent:"+stdout);
			    } catch(java.io.IOException e) {
			      e.printStackTrace();
			      stderr = e.getMessage();
			    }
			    if (!((TagStorer) data).setTagValue(stdoutTag, stdout)) {
					// writeToConsole(data, 1, "Unable to set tag stdout");
				}
				if (!((TagStorer) data).setTagValue(stderrTag, stderr)) {
					// writeToConsole(data, 1, "Unable to set tag stderr");
				}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return stdout;
	}
}
