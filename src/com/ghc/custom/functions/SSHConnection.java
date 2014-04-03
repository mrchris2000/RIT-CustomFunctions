package com.ghc.custom.functions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Vector;

import com.ghc.ghTester.expressions.EvalUtils;
import com.ghc.ghTester.expressions.Function;
import com.ghc.ghTester.expressions.TagStorer;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.configuration.ConfigurationLoader;
import com.sshtools.j2ssh.configuration.SshConnectionProperties;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;

public class SSHConnection extends Function {

	private Function m_fHost;

	private Function m_fPassword;

	private Function m_fUsername;

	private Function m_fCommand;

	private Function m_fStdOut;

	private Function m_fStdErr;

	private static final int TIMEOUT = 30000;

	private static final String FUNCTION_NAME = "SSHConnection";

	/**
	 * Constructor
	 */
	public SSHConnection() {
		
	}

	/**
	 * Constructor
	 */
	public SSHConnection(Function host, Function user, Function pass,
			Function command, Function stdout, Function stderr) {
		m_fHost = host;
		m_fUsername = user;
		m_fPassword = pass;
		m_fCommand = command;
		m_fStdOut = stdout;
		m_fStdErr = stderr;
	}

	protected SSHConnection(Function host, Function user, Function pass,
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
			return new SSHConnection((Function) params.get(0),
					(Function) params.get(1), (Function) params.get(2),
					(Function) params.get(3), (Function) params.get(4), (Function) params.get(5));
		} else {
			return new SSHConnection((Function) params.get(0),
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
			
			ConfigurationLoader.initialize(false);
			// Make a client connection
			SshClient ssh = new SshClient();
			ssh.setSocketTimeout(TIMEOUT);
			SshConnectionProperties properties = new SshConnectionProperties();
			properties.setHost(hostname);
			properties.setPrefPublicKey("ssh-dss");
			// Connect to the host
			ssh.connect(properties, new IgnoreHostKeyVerification());
			// Create a password authentication instance
			PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
			pwd.setUsername(username);
			pwd.setPassword(password);
			// Try the authentication
			int result = ssh.authenticate(pwd);
			// Evaluate the result
			if (result == AuthenticationProtocolState.COMPLETE) {
				// The connection is authenticated we can now do some real work!
				SessionChannelClient session = ssh.openSessionChannel();
				if (!session.requestPseudoTerminal("vt100", 80, 24, 0, 0, ""))
					System.out.println("Failed to allocate a pseudo terminal");
				// if (session.startShell()) {
				BufferedReader is = new BufferedReader(new InputStreamReader(
						session.getInputStream()));
				BufferedReader es = new BufferedReader(new InputStreamReader(
						session.getStderrInputStream()));
				OutputStream os = session.getOutputStream();

				session.executeCommand(command);

				String line = null;
				boolean readAll = false;
				while (!readAll) {
					line = null;
					line = is.readLine();
					if (line == null) {
						readAll = true;
						break;
					}
					stdout = stdout.concat(line) + "\n";
				}

				if (!((TagStorer) data).setTagValue(stdoutTag, stdout)) {
					// writeToConsole(data, 1, "Unable to set tag stdout");
				}

				line = null;
				readAll = false;
				while (!readAll) {
					line = null;
					line = es.readLine();
					if (line == null) {
						readAll = true;
						break;
					}
					stderr = stderr.concat(line) + "\n";
				}
				if (EvalUtils.isString(stderrTag))
					stderrTag = EvalUtils.getString(stderrTag);
				if (!((TagStorer) data).setTagValue(stderrTag, stderr)) {
					// writeToConsole(data, 1, "Unable to set tag stderr");
				}

				is.close();
				os.close();
				session.close();

				// } else {
				// System.out.println("Failed to start the users shell");
				// }
				ssh.disconnect();
				ssh = null;
			}else{
				stdout = "Connection to host: "+hostname+" failed \nPlease check the hostname, userid & password are correct.";
				stderr = "Connection to host: "+hostname+" failed \nPlease check the hostname, userid & password are correct.";
				if (!((TagStorer) data).setTagValue(stdoutTag, stdout)) {
					// writeToConsole(data, 1, "Unable to set tag stdout");
				}
				if (!((TagStorer) data).setTagValue(stderrTag, stderr)) {
					// writeToConsole(data, 1, "Unable to set tag stderr");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stdout;
	}
}
