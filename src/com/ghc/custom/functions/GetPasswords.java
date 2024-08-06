package com.ghc.custom.functions;
/**
*****************************************************************************************
*	@name:		GetPassword.java
*	@author:	jmcotte
* 	@desc:		Retrieves an encrypted password from a password 
*				storage file and decrypts it.
*	@version:	0.3
* 	@since:		20191022 (0.1) - jmcotte - Initial version
*  				20191124 (0.2) - jmcotte - Added PasswordLocation.ini functionality
*  				20210611 (0.3) - jmcotte - Altered password location file handling
*****************************************************************************************
*/
import java.util.Vector;
import com.ghc.ghTester.expressions.Function;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import javax.swing.JOptionPane;

/**
*** Data interchange structure ***************************
*	pword[0] = <unused>
*	pword[1] = Password version
*	pword[2] = Encrypted password
*	pword[3] = Decrypted password
*	pword[4] = Full filespec of the password file
*	pword[5] = Full filespec of the PasswordLocation.ini file
**********************************************************
*/


public class GetPasswords extends Function{
	
	private int size;
	private Vector<Function> params;
 
    public GetPasswords( ) { }
    
    public GetPasswords(int size, Vector<Function> params) {
		this.size = size;
		this.params = params;
    }

	@SuppressWarnings("unchecked")
	public Function create(int size, @SuppressWarnings("rawtypes") Vector params) {
		this.params = params;
		return new GetPasswords(size, params);
	}
	
	public Object evaluate(Object data) {
		String pword[] = new String[6] ;						// Data interchange structure
		pword[1] 	   = "1" ;									// Default password file version is 1
		
		if(size > 0) {
			String pkey = params.get(0).evaluateAsString(data);

			try {
				ReadPasswordFromFile( pword, pkey );				
			} 
			catch (IOException e) {
				e.printStackTrace( ) ;
			}
		}
		else {
			try {
				ReadPasswordFromFile( pword );
			} 
			catch (IOException e) {
				e.printStackTrace( ) ;
			}
		}

		if( pword[1].equals( "0" )) {							// No password file found
			DisplayInfoMsg( pword ) ;
		}

		return pword[3] ;
    }
	  

	public static void ReadPasswordFromFile( String[] pword ) throws IOException {
		String row	   = "" ; 
		int    rowNum  = 0 ;

		GetPasswordFileLocation( pword ) ;						// Check PasswordLoc.ini file
		
		try( BufferedReader csvReader = new BufferedReader( new FileReader( pword[4] ))) {  // Open the password file
			while ((row = csvReader.readLine()) != null) {		// Read lines
				if( rowNum == 1) {
					String[] data = row.split(",") ;			// Split row into individual values
					pword[1] = data[0] ; 						// Store the password version value
					pword[2] = data[1] ;						// Store the password value from file
				}
				else {
					rowNum++ ;									// Next row 
			}	}
			csvReader.close( ) ;								// Close the password file

			if (pword[2] != null ) {							// Password was read from file
				pword[1] = pword[1].replaceAll("^\"|\"$", "") ;	// Remove quotes from retrieved version value
				pword[2] = pword[2].replaceAll("^\"|\"$", "") ;	// Remove quotes from retrieved password value
			}
		
			if( pword[1].equals( "1" )) {						// If password version is 1 ...
				pword[3] = pword[2] ;							//    copy unencrypted password to pword[3]
			} else {  											// otherwise ...
				DecryptPassword( pword ) ;  					//    decrypt the password
		} 	}
		catch( IOException e ) {								// If the password file not found
			pword[1] = "0" ;									// Version indicator "0" for "File Not Found"
		}			
		return ;
	}


	public static void ReadPasswordFromFile( String[] pword, String pkey ) throws IOException {
		String row	   = null ; 
		int    rowNum  = 0 ;

		GetPasswordFileLocation( pword ) ;						// Check PasswordLoc.ini file
		
		try( BufferedReader csvReader = new BufferedReader( new FileReader( pword[4] ))) {  // Open the password file
			while ((row = csvReader.readLine()) != null) {		// Read lines
				if( rowNum > 0) {
					String[] data = row.split(",") ;			// Split row into individual values
					if( data[2].toString().equals(pkey.toString()) ) {
						pword[1] = data[0] ; 					// Store the password version value
						pword[2] = data[1] ;					// Store the password value from file
					}
					else {
						rowNum++ ;
					}
				}
				else {
					rowNum++ ;									// Next row 
				}	
			}
			csvReader.close( ) ;								// Close the password file

			if (pword[2] != null ) {							// Password was read from file
				pword[1] = pword[1].replaceAll("^\"|\"$", "") ;	// Remove quotes from retrieved version value
				pword[2] = pword[2].replaceAll("^\"|\"$", "") ;	// Remove quotes from retrieved password value
			}
		
			if( pword[1].equals( "1" )) {						// If password version is 1 ...
				pword[3] = pword[2] ;							//    copy unencrypted password to pword[3]
			} else {  											// otherwise ...
				DecryptPassword( pword ) ;  					//    decrypt the password
		} 	}
		catch( IOException e ) {								// If the password file not found
			pword[1] = "0" ;									// Version indicator "0" for "File Not Found"
		}			
		return ;
	}

	public static void GetPasswordFileLocation( String[] pword ) {
		pword[5] 		= System.getProperty( "user.home" ) + "\\PasswordLocation.ini" ;			
																// Default password location filespec
		String locInfo 	= "" ;									// Clear the var 
		
		try {													// Open the password file
			BufferedReader csvReader = new BufferedReader( new FileReader( pword[5] )) ;
			if((locInfo  = csvReader.readLine()) != null) {		// Read data line
				pword[4] = locInfo ;							// Save filespec from file
			} else {
				pword[4] = System.getProperty( "user.home" ) + "\\IBM\\PasswordVault\\PasswordVault.csv" ; // Save default filespec 
			}
			csvReader.close( ) ;								// Close the password file
		}
		catch( IOException e ) {								// If the password file not found
			pword[4] = System.getProperty( "user.home" ) + "\\IBM\\PasswordVault\\PasswordVault.csv";	// Save default filespec 
		}
		return ;
	}
		
	public static void DecryptPassword( String[] pword ) {
		StringBuilder tmp = new StringBuilder();
		final int OFFSET = 4 ;													// Caesar decypher
		for( int i = 0; i < pword[2].length(); i++ ) {
			tmp.append((char)(pword[2].charAt(i) - OFFSET));
		}
		
		String reversed = new StringBuffer(tmp.toString()).reverse().toString(); // Reverse the string
		String password = new String(Base64.getDecoder().decode(reversed)) ;
		pword[3] = password ;									// Copy unencrypted password to pword[3]
		
		return ;
	}
	
	public static void DisplayInfoMsg( String[] pword ) {
		JOptionPane.showMessageDialog( null, "Password file " + pword[4] + " could not be found!\n\nPlease run the UpdatePassword function.\n\n", "ERROR", JOptionPane.CLOSED_OPTION ) ;	
		return ;
	}	
}
