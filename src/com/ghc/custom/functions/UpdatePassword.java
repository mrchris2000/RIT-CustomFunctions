package com.ghc.custom.functions;
/**
*****************************************************************************************
*	@name:		UpdatePassword 
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
//import java.util.Arrays;
import java.util.Base64;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import javax.swing.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
*** Data interchange structure ***************************
*	pword[0] = *** DO NOT USE ***
*	pword[1] = Password version
*	pword[2] = Encrypted password
*	pword[3] = Decrypted password
*	pword[4] = Full filespec of the password file
*	pword[5] = Full filespec of the PasswordLocation.ini file
**********************************************************
*/


public class UpdatePassword extends Function{
      
    public UpdatePassword(){ }
      
    @SuppressWarnings("rawtypes")
	public Function create( int size, Vector params )
    {
   	  return new UpdatePassword( ) ;
    }
      
    public Object evaluate(Object data) {
		String pword[] = new String[6] ;						// Data interchange structure
		pword[1] 	   = "1" ;									// Default version is 1
		 
		try {													// Update password file
			ReadPasswordFromFile( pword ) ;
			try {
				ChangeUI( pword ) ;
			} catch (IOException e) {
				e.printStackTrace( ) ;
			}
		} catch (IOException e) {
			e.printStackTrace( ) ;
		}

		return 0 ;
    }
	  
	
	public static void ReadPasswordFromFile( String[] pword ) throws IOException {
		String row	   = null ; 
		int    rowNum  = 0 ;

		GetPasswordFileLocation( pword ) ;						// Check PasswordLoc.ini file
		
		try( BufferedReader csvReader = new BufferedReader( new FileReader( pword[4] )))  { // Open the password file
			while ((row = csvReader.readLine()) != null) {		// Read lines
				if( rowNum == 1) {
					String[] data = row.split(",") ;			// Split row into individual values
					pword[1] = data[0] ; 						// Store the password version value
					pword[2] = data[1] ;						// Store the password value
				}
				else {
					rowNum++ ;									// Next row 
				}	}
			csvReader.close( ) ;								// Close the password file

			if (pword[2] != null ) {
				pword[1] = pword[1].replaceAll("^\"|\"$", "") ;	// Remove quotes from retrieved version value
				pword[2] = pword[2].replaceAll("^\"|\"$", "") ;	// Remove quotes from retrieved password value
			}
		
			if( pword[1].equals( "1" )) {						// If password version is 1 ...
				pword[3] = pword[2] ;							//    copy unencrypted password to pword[2]
			} else {  											// otherwise ...
				DecryptPassword( pword ) ;  					//    decrypt the password
			}
		} 
		catch( IOException e ) {								// If the password file not found
			pword[1] = "0" ;									// Version indicator "0" for "File Not Found"
		}
			
		return ;
	}
	
	
	public static void GetPasswordFileLocation( String[] pword ) {
		pword[5] 		= System.getProperty( "user.home" ) + "\\PasswordLocation.ini" ;
																			// Default password location filespec
		File directory	= new File(String.valueOf( System.getProperty( "user.home" ) + "\\IBM\\PasswordVault" )) ;	
																			// Default password file dir
		String locInfo 	= null ; 

	if(!directory.exists( )){									// If default directory does not exist		
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
	
	public static void EncryptPassword(String[] pword) {
		String b64encoded = Base64.getEncoder().encodeToString(pword[3].getBytes());	
		String reverse = new StringBuffer(b64encoded).reverse().toString();		// Reverse the string
		
		StringBuilder tmp = new StringBuilder( ) ;
		final int OFFSET = 4 ;								// Caesar cypher
		for( int i = 0; i < reverse.length(); i++ ) {
			tmp.append((char)(reverse.charAt(i) + OFFSET)) ;					
		}

		pword[2] = tmp.toString( ) ;						// Copy encrypted pword to pword structure 
		
		return ;
	}
	
	
	public static void DecryptPassword(String[] pword) {
		StringBuilder tmp = new StringBuilder();
		final int OFFSET = 4 ;								// Caesar cypher
		for( int i = 0; i < pword[2].length(); i++ ) {
			tmp.append((char)(pword[2].charAt(i) - OFFSET));
		}
		
		String reversed = new StringBuffer(tmp.toString()).reverse().toString(); // Reverse the string
		String password = new String(Base64.getDecoder().decode(reversed)) ;
		pword[3] = password ;
		
		return ;
	}

	
	public static void SavePasswordToFile(String[] pword) throws IOException {
		File   fileToSave  = null ;
		JFrame parentFrame = new JFrame( ) ;
		
		JFileChooser fileChooser = new JFileChooser( ) ;
		fileChooser.setSelectedFile( new File( pword[4] )) ;		// Set file location default to pword[4]
		fileChooser.setDialogTitle("File Save                                v0.3");
		
		int rv = fileChooser.showSaveDialog(parentFrame) ;
		if( rv == JFileChooser.APPROVE_OPTION) {
			fileToSave = fileChooser.getSelectedFile( ) ;
		}
		
		pword[4] = fileToSave.getPath( ) ;					// Save full pword filespec to pword structure

		EncryptPassword( pword ) ;
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave.getAbsolutePath()));
		writer.write("\"replyId\",\"Password\"\n") ; 
		writer.write("\"2\",\"" + pword[2] + "\"") ;		// Write file line with version & encrypted password 
		writer.close();

		// Update the PasswordLocation.ini file
		try (FileWriter fw = new FileWriter( pword[5] ) ;
			BufferedWriter bw = new BufferedWriter( fw )) {
				bw.write( pword[4] ) ;
			} catch (IOException e) {
				System.err.format("IOException: %s%n", e);
			}
		
		return ;
	}
	
	
	// Main interface for updating the password in the PasswordVault.csv file
	public static void ChangeUI(String[] pword) throws IOException {
		JTextField currPassword = new JTextField(8);		// Stores the current password entered by the user
		JTextField newPassword  = new JTextField(8);		// Stores the new password entered by the user
		int writeToFile = 0 ;
		
		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel( "Current Password: " )) ;
		myPanel.add( currPassword ) ;
		myPanel.add(Box.createHorizontalStrut(15)) ; 		// a spacer
		myPanel.add(new JLabel( "New Password: " )) ;
		myPanel.add( newPassword ) ;

		int result =  JOptionPane.showConfirmDialog(null, myPanel, "MAINFRAME PASSWORDS:  Please Enter Values Below                 v0.3", JOptionPane.OK_CANCEL_OPTION) ;
		if (result == JOptionPane.OK_OPTION) {				// If user clicked/pressed OK
			int dialogButton = JOptionPane.YES_NO_OPTION ;
			
			if( !pword[1].equals( "0" )) {					// If a password file was found and password retrieved
				if( currPassword.getText().compareTo(pword[3]) == 0 ) {		// User-entered password matches password from file 
					writeToFile = 1 ;
				}	
				else {
					JOptionPane.showMessageDialog( null, "The password you entered does not match the password on file!", "WARNING", JOptionPane.CLOSED_OPTION ) ;
			}	}
			else {
				writeToFile = 1 ;
			}
					
			if( writeToFile != 0 ) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Save new password to password file?", "File Save", dialogButton);

				if (dialogResult == 0) { 
					pword[3] = newPassword.getText() ;
					SavePasswordToFile( pword ) ;
		}	}	}

		return ;
	}	
	
}
