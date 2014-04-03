package com.ghc.custom.functions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;

import com.ghc.ghTester.expressions.EvalUtils;
import com.ghc.ghTester.expressions.Function;
    

public class FTP extends Function 
{
	
	private Function m_fServer = null;
	private Function m_fUsername = null;
	private Function m_fPassword = null;
	private Function m_fCommand = null;
	private Function m_fLocal = null;
	private Function m_fRemote = null;
	
	public FTP( ){
		
	}
	
	public FTP(Function f1, Function f2, Function f3, Function f4, Function f5, Function f6) {
		
		m_fServer = f1;
		m_fUsername = f2;
		m_fPassword = f3;
		m_fCommand = f4;
		m_fLocal = f5;
		m_fRemote = f6;
		
	}
	
	public Object evaluate( Object data ){
            
        FTPClient ftp = new FTPClient();

        String server = m_fServer.evaluateAsString( data );
        String username = m_fUsername.evaluateAsString( data );
        String password = m_fPassword.evaluateAsString( data );
        String command = m_fCommand.evaluateAsString( data );
        String local = m_fLocal.evaluateAsString( data );
        String remote = m_fRemote.evaluateAsString( data );

        if( EvalUtils.isString( server )){
        	server = EvalUtils.getString( server );
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
        
        try
        {
            ftp.connect(server);
            System.out.println("Connected to " + server + ".");

            
            ftp.login(username, password);
            System.out.println("Remote system is " + ftp.getSystemName());

            
            // Use passive mode as default because most of us are
            // behind firewalls these days.
            ftp.enterLocalPassiveMode();
   
            if (command.equalsIgnoreCase("put")) {
            	
            	InputStream input = new FileInputStream(local);
  
            	ftp.storeFile(remote, input);
   
                input.close();
                ftp.logout();
                
                return("Put \"" + local + "\" in \"" + remote + "\".");
            }
            else if(command.equalsIgnoreCase("get")) {
                
            	//OutputStream output = new FileOutputStream(remote);
            	OutputStream output = new FileOutputStream(local);
            	
                //ftp.retrieveFile(local, output);
                ftp.retrieveFile(remote, output);
    
                output.close();
                ftp.logout();
                
                return("Got \"" + remote + "\" put in \"" + local + "\".");
            } else {
	    		return("Error: Command not recognised.");
	    	}
        }
        catch (FTPConnectionClosedException e)
        {
            System.err.println("Server closed connection.");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return "error (check hostname and login details)";
    }
	
	public String getSyntax()
	{
	   return "FTP(hostname, username, password, command, local, remote)";
	}
	
	public Function create(int size, Vector params) {
        return new FTP((Function) params.get(0), (Function) params.get(1), 
        		(Function) params.get(2), (Function) params.get(3),
        		(Function) params.get(4), (Function) params.get(5));
    }
}
   