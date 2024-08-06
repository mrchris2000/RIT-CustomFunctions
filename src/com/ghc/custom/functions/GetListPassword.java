package com.ghc.custom.functions;
/**
*****************************************************************************************
*	@name:		GetPassword.java
*	@author:	jmcotte
* 	@desc:		Retrieves an encrypted password from a password 
*				storage file and decrypts it.
*	@version:	0.3
* 	@since:		20191022 (0.1) - jmcotte - Initial version
*****************************************************************************************
*/
import java.util.Vector;
import com.ghc.ghTester.expressions.Function;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.OverlappingFileLockException;
import java.util.Base64;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
*** Data interchange structure ***************************
*	pword[0] = Password key
*	pword[1] = Password version
*	pword[2] = Encrypted password
*	pword[3] = Decrypted password
*	pword[4] = Full filespec of the password file
*	pword[5] = Full filespec of the PasswordLocation.ini file
*	pword[6] = InUse Indicator
**********************************************************
*/


public class GetListPassword extends Function{
	
	private int size;
	private Vector<Function> params;
 
    public GetListPassword( ) { }
    
    public GetListPassword(int size, Vector<Function> params) {
		this.size = size;
		this.params = params;
    }

	@SuppressWarnings("unchecked")
	public Function create(int size, @SuppressWarnings("rawtypes") Vector params) {
		this.params = params;
		return new GetListPassword(size, params);
	}
	
	public Object evaluate(Object data) {
		String pword[] = new String[7] ;						// Data interchange structure
		pword[1] 	   = "1" ;									// Default password file version is 1
		pword[2]       = ""  ;
		
		if(size > 0) {
			pword[4] = params.get(0).evaluateAsString(data);
			
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

		return pword[0] + "," + pword[3] ;
    }


	public static void ReadPasswordFromFile( String[] pword ) throws IOException {
		String row	   = null ;
		String rowOut  = null ;
		int    rowNum  = 0 ;
		
		try ( FileInputStream fis = new FileInputStream( pword[4] ); 
//		    FileLock lockIn = fis.getChannel().lock() 
		    		) {
			Scanner scanner = new Scanner(fis);

			try ( FileOutputStream fos = new FileOutputStream( pword[4] + "_tmp" );) { 

				while ( scanner.hasNextLine() ) {
					row = scanner.nextLine(); 						// Read lines
					if( rowNum > 0) {
						String[] data = row.split(",") ;			// Split row into individual values
						if( pword[2].toString().matches("") ) {
							if ( data.length == 4 ) {
								rowOut = String.join( ",", data ) ;		// Rejoin input row array for writing
							}
							else {
								pword[0] = data[2] ;					// Store the password key
								pword[1] = data[0] ; 					// Store the password version value
								pword[2] = data[1] ;					// Store the password value from file
								rowOut = String.join( ",", data ) + ",X" ;		// Rejoin input row array for writing
							}
						}
						else {
							rowOut = row ;
						}
						rowNum++ ;
					}
					else {
						rowOut = row ;
						rowNum++ ;											// Next row 
					}
					rowOut += "\n" ;
					byte[] strToBytes = rowOut.getBytes() ;
					fos.write(strToBytes) ;				                    // Write out file line 
				}

				fos.close( ) ;												// Close the temp password file

//				lockIn.release() ;
				scanner.close() ;
				fis.close() ;												// Close the password file		
			
				File oldFile = new File( pword[4] ) ; 						
			    if ( oldFile.delete( ) ) { 									// Delete original file version
					File tmpFile = new File( pword[4] + "_tmp" ) ; 	
					tmpFile.renameTo( oldFile ) ;							// Rename temp file to original file
			    } 
			    else {
			    	System.out.println( "Failed to delete the original file" ) ;
			    } 
	
			    if (pword[2] != null ) {							// Password was read from file
					pword[1] = pword[1].replaceAll("^\"|\"$", "") ;	// Remove quotes from retrieved version value
					pword[2] = pword[2].replaceAll("^\"|\"$", "") ;	// Remove quotes from retrieved password value
				}
			
				if( pword[1].equals( "1" )) {						// If password version is 1 ...
					pword[3] = pword[2] ;							//    copy unencrypted password to pword[3]
				} else {  											// otherwise ...
					DecryptPassword( pword ) ;  					//    decrypt the password		
				}
			}
			
			catch ( NonWritableChannelException e ) {
	    		JOptionPane.showMessageDialog( null, "Exception occured while trying to get a lock on File " + pword[4] + "_tmp", "ERROR", JOptionPane.CLOSED_OPTION ) ;	
				pword[1] = "0" ;									// Version indicator "0" for "File Not Found"
			}	
		}

		catch ( NonWritableChannelException | OverlappingFileLockException | IOException ex) {
    		JOptionPane.showMessageDialog( null, "Exception occured while trying to get a lock on File " + pword[4] + "_tmp", "ERROR", JOptionPane.CLOSED_OPTION ) ;	
			pword[1] = "0" ;									// Version indicator "0" for "File Not Found"
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
