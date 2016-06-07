package com.ghc.custom.functions;

import java.util.Vector;

import com.ghc.ghTester.expressions.EvalUtils;
import com.ghc.ghTester.expressions.Function;
import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.configuration.ConfigurationLoader;

public class SFTP extends Function{	
	
	private Function m_fHostname = null;	
	private Function m_fUsername = null;
	private Function m_fPassword = null;
	private Function m_fCommand = null;
	private Function m_fLocal = null;
	private Function m_fRemote = null;	
	
	public SFTP() {
		
	}
	
	public SFTP(Function f1, Function f2, Function f3, Function f4, Function f5, Function f6 ){
		
		m_fHostname = f1;
		m_fUsername = f2;
		m_fPassword = f3;
		m_fCommand = f4;
		m_fLocal = f5;
		m_fRemote = f6;
		
	}
	
	public Object evaluate( Object data ){
		try {

			ConfigurationLoader.initialize(false);

			//Inputs
		    String hostname = m_fHostname.evaluateAsString( data );
		    String username = m_fUsername.evaluateAsString( data );
		    String password = m_fPassword.evaluateAsString( data );
		    String command = m_fCommand.evaluateAsString( data );
		    String local = m_fLocal.evaluateAsString( data );
		    String remote = m_fRemote.evaluateAsString( data );
		    
		    if( EvalUtils.isString( hostname )){
		    	hostname = EvalUtils.getString( hostname );
		    }
		    if( EvalUtils.isString( username )){
		    	username = EvalUtils.getString( username );
		    }
		    if( EvalUtils.isString( password )){
		    	password = EvalUtils.getString( password );
		    }
		    if( EvalUtils.isString( command )){
		    	command = EvalUtils.getString( command );
		    }
		    if( EvalUtils.isString( local )){
		    	local = EvalUtils.getString( local );
		    }
		    if( EvalUtils.isString( remote )){
		    	remote = EvalUtils.getString( remote );
		    }		    
		    
		    //return(hostname + username + password +command+file+directory);
		    // Make a client connection
		    SshClient ssh = new SshClient();
		    
		    // Connect to the host
		    ssh.connect(hostname);
			      
		    // Create a password authentication instance
		    PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
			
		    //Enter hostname
		    pwd.setUsername(username);
			
		    //Enter password
		    pwd.setPassword(password);
			      
		    // Try the authentication
		    int result = ssh.authenticate(pwd);
			      
		    // Evaluate the result
		    if (result == AuthenticationProtocolState.COMPLETE) {
		    	
		    	// The connection is authenticated we can now do some real work!
		    	SftpClient sftp = ssh.openSftpClient();
		    	
		    	if(command.equalsIgnoreCase("put")){
		    		
		    		// Change directory
			    	sftp.cd(remote);		    			    	
			    	
			    	//Write file
			    	sftp.put(local);
			    	
			    	// Quit
			    	sftp.quit();
			    	ssh.disconnect();
			    	
			    	return("Put \"" + local + "\" in \"" + remote + "\".");
		    	}
		    	else if(command.equalsIgnoreCase("get")){
		    		
		    		// Change directory
			    	//sftp.cd(remote);		    			    	
			    	
			    	//Write file
			    	sftp.get(remote, local);
			    	
			    	// Quit
			    	sftp.quit();
			    	ssh.disconnect();
			    	
			    	return("Got \"" + remote + "\" put in \"" + local + "\".");
		    	}
		    	else {
		    		// Quit
			    	sftp.quit();
			    	ssh.disconnect();
			    	
		    		return("Error: Command not recognised.");
		    	}
		    }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return("error (check hostname and login details)");
	}
	
	public Function create(int size, Vector params) {
        return new SFTP((Function) params.get(0), (Function) params.get(1), 
        		(Function) params.get(2), (Function) params.get(3),
        		(Function) params.get(4), (Function) params.get(5));
    }
}

