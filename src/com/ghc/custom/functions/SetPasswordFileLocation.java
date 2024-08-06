package com.ghc.custom.functions;
/**
*****************************************************************************************
*	@name:		SetPasswordFileLocation 
*	@author:	jmcotte	
*	@desc:		Used to establish and maintain mainframe passwords
*				in the password file.  Passwords are stored in 
*				encrypted form.
*	@version:	0.3
* 	@since:		20191022 (0.1) - jmcotte - Initial version
* 				20191124 (0.2) - jmcotte - Added PasswordLocation.ini functionality
* 				20210611 (0.3) - jmcotte - Altered password location file handling
*****************************************************************************************
**/
import com.ghc.ghTester.expressions.Function;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import javax.swing.*;
import javax.swing.JFileChooser;

/**
*** Data interchange structure ***************************
*	pword[0] = *** DO NOT USE ***
*	pword[1] = 
*	pword[2] = 
*	pword[3] = 
*	pword[4] = Full filespec of the password file
*	pword[5] = Full filespec of the PasswordLocation.ini file
**********************************************************
*/


public class SetPasswordFileLocation extends Function{
      
    public SetPasswordFileLocation(){ }
      
    @SuppressWarnings("rawtypes")
	public Function create(int size, Vector params)
    {
   	  return new SetPasswordFileLocation( ) ;
    }
      
    public Object evaluate(Object data) {
		String pword[] = new String[6] ;						// Data interchange structure
		 
		try {													// Update password file
			GetPasswordFileLocation( pword ) ;						// Check PasswordLoc.ini file

			SetFileLocation( pword ) ;
		} catch (IOException e) {
			e.printStackTrace( ) ;
		}

		return 0 ;
    }
	
    
	public static void GetPasswordFileLocation( String[] pword ) {
		pword[5] 		= System.getProperty( "user.home" ) + "\\PasswordLocation.ini" ;
																			// Default password location filespec
		File directory	= new File(String.valueOf( System.getProperty( "user.home" ) + "\\IBM\\PasswordVault" )) ;	
																			// Default password file dir
		String locInfo 	= null ; 

		if(!directory.exists( )){								// If default directory does not exist		
			directory.mkdir( ) ;								//    create the directory
		}
		
		try {													// Open the password file
			BufferedReader csvReader = new BufferedReader( new FileReader( pword[5] )) ;
			if((locInfo = csvReader.readLine()) != null) {		// Read data line
				pword[4] = locInfo ;							// Save filespec from file
			} else {
				pword[4] = System.getProperty( "user.home" ) + "\\IBM\\PasswordVault\\PasswordVault.csv"; // Default password filespec 
			}
			csvReader.close( ) ;								// Close the password file
		}
		catch( IOException e ) {								// If the password file not found
			pword[4] = System.getProperty( "user.home" ) + "\\IBM\\PasswordVault\\PasswordVault.csv";	// Default filespec 	
		}	
		
		return ;
	}
	
	
	public static void SetFileLocation(String[] pword) throws IOException {
		File   fileToSave  = null ;
		JFrame parentFrame = new JFrame( ) ;
		
		JFileChooser fileChooser = new JFileChooser( ) ;
		fileChooser.setSelectedFile( new File( pword[4] )) ;		// Set file location default to pword[4]
		fileChooser.setDialogTitle("File Save                                v0.1");
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
		
		int rv = fileChooser.showSaveDialog(parentFrame) ;
		if( rv == JFileChooser.APPROVE_OPTION) {
			fileToSave = fileChooser.getSelectedFile( ) ;
			pword[4] = fileToSave.getPath( ) ;					// Save full pword filespec to pword structure

			// Update the PasswordLocation.ini file
			try (FileWriter fw = new FileWriter( pword[5] ) ;
				BufferedWriter bw = new BufferedWriter( fw )) {
				bw.write( pword[4] ) ;
				System.out.println("##### written ######");
			} catch (IOException e) {
				System.err.format("IOException: %s%n", e);
				System.out.println("##### error ######");
			}
		}
		
		return ;
	}
	
}
