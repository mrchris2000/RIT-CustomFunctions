package com.ghc.custom.functions;
import java.util.Scanner;
/**
*****************************************************************************************
*	@name:		ReleaseListPassword.java
*	@author:	jmcotte
* 	@desc:		Clear InUse Indicator for given ID
*	@version:	0.1
* 	@since:		20230126 (0.1) - jmcotte - Initial version
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

import javax.swing.JOptionPane;

/**
*** Data interchange structure ***************************
*	pword[0] = <unused>
*	pword[1] = Password version
*	pword[2] = Encrypted password
*	pword[3] = Decrypted password
*	pword[4] = Full filespec of the password file
*	pword[5] = Full filespec of the PasswordLocation.ini file
*	pword[6] = InUse Indicator
**********************************************************
*/


public class ReleaseListPassword extends Function{
	
	private int size;
	private Vector<Function> params;
 
    public ReleaseListPassword( ) { }
    
    public ReleaseListPassword(int size, Vector<Function> params) {
		this.size = size;
		this.params = params;
    }

	@SuppressWarnings("unchecked")
	public Function create(int size, @SuppressWarnings("rawtypes") Vector params) {
		this.params = params;
		return new ReleaseListPassword(size, params);
	}
	
	public Object evaluate(Object data) {
		String pword[] = new String[7] ;						// Data interchange structure
		pword[1] 	   = "1" ;									// Default password file version is 1
		
		if(size > 0) {
			String pkey = params.get(0).evaluateAsString(data);
			pword[4] = params.get(1).evaluateAsString(data);
			
			try {
				ReleasePasswordInFile( pword, pkey );
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


	public static void ReleasePasswordInFile( String[] pword, String pkey ) throws IOException {
		String row	   = null ;
		String rowOut  = null ;
		int    rowNum  = 0 ;

		JOptionPane.showMessageDialog( null, pword[0] + "," + pword[1] + "," + pword[2] + "," + pword[3] + "," + pword[4] + " - " + pkey, "ERROR", JOptionPane.CLOSED_OPTION ) ;	

		try ( FileInputStream fis = new FileInputStream( pword[4] ); 
//				FileLock lockIn = fis.getChannel().lock() 
						) {
			Scanner scanner = new Scanner(fis);

			try ( FileOutputStream fos = new FileOutputStream( pword[4] + "_tmp" );) { 
	
				while ( scanner.hasNextLine() ) {
					row = scanner.nextLine(); 						// Read lines
		
					if( rowNum > 0) {
						String[] data = row.split(",") ;			// Split row into individual values
						if( data[2].toString().equals(pkey.toString()) ) {
							if ( data.length == 4 ) {
								data[3]  = "" ;							// Mark Indicator as InUse
								rowOut = String.join( ",", data ) ;		// Rejoin input row array for writing
							}
							else {
								rowOut = String.join( ",", data ) + "," ;		// Rejoin input row array for writing
						}	}
						else {
							rowOut = row ;
							rowNum++ ;
						}
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
		
			}
			catch ( NonWritableChannelException e ) {
	    		JOptionPane.showMessageDialog( null, "Exception occured while trying to get a lock on File " + pword[4] + "_tmp", "ERROR", JOptionPane.CLOSED_OPTION ) ;	
				pword[1] = "0" ;									// Version indicator "0" for "File Not Found"
			}	

		}
		catch ( NonWritableChannelException | OverlappingFileLockException | IOException ex) {
    		JOptionPane.showMessageDialog( null, "Exception occured while trying to get a lock on File " + pword[4], "ERROR", JOptionPane.CLOSED_OPTION ) ;	
			pword[1] = "0" ;									// Version indicator "0" for "File Not Found"
        }
		return ;
	}
	
	public static void DisplayInfoMsg( String[] pword ) {
		JOptionPane.showMessageDialog( null, "Password file " + pword[4] + " could not be found!\n\nPlease run the UpdatePassword function.\n\n", "ERROR", JOptionPane.CLOSED_OPTION ) ;	
		return ;
	}
}
