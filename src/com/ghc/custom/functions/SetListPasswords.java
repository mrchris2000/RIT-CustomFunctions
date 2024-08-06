package com.ghc.custom.functions;

/**
***************************************************************************************************
*	@name:		SetListPasswords 

*	@author:    jmcotte 
*	@desc:      Used to establish and maintain passwords in a list password file.  
*				Passwords are stored in encrypted form.
*	@version:   0.1
*	@since:     20230127 (0.1) - jmcotte - Initial version
***************************************************************************************************
**/


import com.ghc.ghTester.expressions.Function;
import java.awt.* ;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader ;
import java.io.BufferedWriter ;
import java.io.File ;
import java.io.FileWriter ;
import java.io.IOException ;
import java.io.FileReader ;

import java.util.Base64 ;
import java.util.Vector;

import javax.swing.* ;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SetListPasswords extends Function implements ActionListener {

	private Vector<Function> params;
	
	@SuppressWarnings("unused")
	private int size;

	public SetListPasswords(){ }

    public SetListPasswords(int size, Vector<Function> params) {
		this.size = size;
		this.params = params;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Function create( int size, Vector params )
    {
		this.params = params;
    	return new SetListPasswords( size, params ) ;
    }

//	private static final long serialVersionUID = 1L;
	static  String   	pw_Version  = new String( )      ;          // Password version from file
	static  String   	pw_Location = new String( )      ;          // Location of PasswordVault.csv file
	static  String   	pw_LocFile  = new String( )      ;          // Location of PasswordLocation.ini file
	static  String[]	inUse		= new String[100]	 ;
	static  String[] 	pwordKey    = new String[100]    ;          // Array of stored keywords  from password file
	static  String[] 	pwordText   = new String[100]    ;          // Array of stored passwords from password file
	static  String   	tpwordKey   = new String( )      ;          // Stores new password key entered by user
	static  String   	tpwordNew   = new String( )      ;          // Stores new password entered by user
	static  String   	tpwordCurr  = new String( )      ;          // Stores current password entered by user
			JTextField  tfpwordKey  = new JTextField(16) ;          // Field to store password key selected by user
			JTextField  tfpwordCurr = new JTextField(16) ;          // Field to store current password entered by user
			JTextField  tfpwordNew1	= new JTextField(16) ;          // Field to store new password entered by user in baseFrame
			JTextField  tfpwordNew2	= new JTextField(16) ;          // Field to store new password entered by user in winFrame
	static 	JFrame		winFrame    ;                               // Frame of New Password window 
	static 	JFrame		baseFrame   ;                               // Frame of base window (PASSWORD EDITOR   (v0.4))
	static 	int			idx = 0     ;                               // Index position of next avail spot in password array

	JComboBox<String>	cb			= new JComboBox<String>( ) ;    // Picklist field containing list of keys
	JFrame				msgFrame	= new JFrame( )	;               // Frame for info messages to user 

	public Object evaluate( Object data ){
		pw_Location = params.get(0).evaluateAsString(data);

		setListPasswords();
		return pw_Location;
	}
	
/**
*****************************************************************************************
* setPasswords - the main method of the SetPasswords class.  setPasswords launches the  *
* 	initial window of the class, and all subsequent actions are based on this method.   *       
*****************************************************************************************
**/
	public void setListPasswords( ) { 
		pw_Version   = "2" ;                                            // Password version from file; default version is 2
        readPasswordsFromFile( ) ;                                      // Get stored password information
       
        JFrame.setDefaultLookAndFeelDecorated( true ) ;                 // Create and set up a window base frame
        JFrame baseFrame = new JFrame( "     PASSWORD EDITOR                                           |v0.4|" ) ;
        baseFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE ) ; // Turn off X window close button       
        baseFrame.setResizable( false ) ;                               // Turn off window resizing

        JPanel panel1 = new JPanel( ) ;                                 // Create new panel
        panel1.setLayout( new GridLayout( 3, 2, 40, 4 )) ;    		    // Set panel size and row-column spacing

        JLabel label1 = new JLabel( "Select password key:", JLabel.RIGHT ) ;	// Label for password key field
        panel1.add( label1 ) ;                              	  	    // Add label to window grid
         
		panel1.setPreferredSize( new Dimension( 300, 100 )) ;   		// Preferred size of field panel of window
		baseFrame.getContentPane( ).add( panel1 ) ;            			// Define panel inside frame
		baseFrame.pack( ) ;                                     		// Size the frame so it contains all contents
		       
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>( pwordKey ) ;
		cb.setModel( model    ) ;                                 		// Refresh combobox list of password keys
		cb.setEnabled( true   ) ;                                 		// JComboBox is enabled   
		cb.setEditable( false ) ;                                 		// Allow user to edit list of keys      
		
		panel1.add( cb ) ;                                        		// Add a panel called 'cb'              
		panel1.add( new JLabel( "Current Password:", JLabel.RIGHT )) ;	// Add a label for curr password
		panel1.add( tfpwordCurr ) ;                               		// Add a field for curr password
		         
		panel1.add( new JLabel( "New Password:", JLabel.RIGHT )) ;		// Add a label for new password
		panel1.add( tfpwordNew1 ) ;                               		// Add a field for new password
		
		tfpwordCurr.setVisible( true ) ;
		tfpwordNew1.setVisible( true ) ;
              
        baseFrame.add( panel1, BorderLayout.CENTER ) ;                  // Centers window on screen
        baseFrame.pack( ) ;                                             // Size the frame so it contains all contents
        baseFrame.setVisible( true ) ;                                  // Ensures window is visible
        baseFrame.setLocationRelativeTo( null ) ;                       // OPTIONAL: Relocate window relative to a point
        
        // Define new buttons 
        JButton jbtn1 = new JButton( "OK" ) ;                           // Button to choose current selection
        JButton jbtn2 = new JButton( "Exit" ) ;                         // Button to exit the current window
        JButton jbtn3 = new JButton( "New Password" ) ;                 // Button to add a new password.  
                                                                        //   Launches child window.           
        
        jbtn1.addActionListener( this ) ;                               // Spawns action(s) for the "OK" button 
        jbtn2.addActionListener(e -> {                                  // If 'Exit' button is clicked
        	int response = JOptionPane.showConfirmDialog( null, "Save changes to Password Vault?" ) ;

        	if( response == 0 ) 	{									// If user responds with 'Yes' 
           		savePasswordsToFile( ) ;                           		// Save passwords to file
           	}
           	if( response == 2 )		{
           	} else {
            	baseFrame.dispose( ) ;                                  //    Close window
           	}
        }  ) ;
        jbtn3.addActionListener( this ) ;                               // Spawns action(s) for the "New Password" button 
        
        JPanel panel2 = new JPanel( ) ;                                 // Define button panel for window
        panel2.setLayout( new GridLayout( 1, 1 )) ;            			// Set layout to one row
        panel2.add( jbtn1 ) ;                                           // Add button #1: OK                           
        panel2.add( jbtn2 ) ;                                           // Add button #2: Exit 
        panel2.add( jbtn3 ) ;                                           // Add button #3: New Password 
         
        baseFrame.add( panel2, BorderLayout.PAGE_END ) ;                // Locate panel2 at bottom of baseFrame
        baseFrame.pack( ) ;                                             // Size the frame so it contains all contents
        baseFrame.setVisible( true ) ;   
    }

       

/**    
*****************************************************************************************
* Button Controls                                                                       * 
* actionPerformed processes the information broadcast by the actionListener.            *
* Each screen action in the program is processed in this method.                        *
*****************************************************************************************  
**/
	public void actionPerformed( ActionEvent e ) {
		boolean found = false ;                                      	// Flag to indicate key is found or not
		int success   = 0 ;                                             // Degree of success in update 
		String msg ;                                                    // Var for info msg to user
		
		if( e.getActionCommand( ).equals( "OK" )) {                     // If user clicked the OK button
			int i = 0 ;                                                 // Initialize subscript: array position pointer
			tpwordKey    = String.valueOf( cb.getSelectedItem( )) ;   	// Copy password key to string field
			tpwordCurr    = tfpwordCurr.getText( ) ;                    // get string from currPword JTextField
			tpwordNew     = tfpwordNew1.getText( ) ;                    // get string from newPword  JTextField

			while( !found ) {                                           // While found flag is 'False'
				if( pwordKey[i] == tpwordKey ) {                        // If current array key equals selected key 
					found = true ;                                      // Set found flag to true (found)
					String pwordTxt  =  pwordText[i] ;                  // Copy value from String[] to String
				
					if( pwordTxt.equals(tpwordCurr) ) {                 // If current array password equals new password entered
						pwordText[i] =  tpwordNew ;                     // Set current array password to new password entered
					}
					else
						success = 1 ;                                   // Password entered did not match password on file 
				}	  	                                                //     tpwordCurr != pwordText[i]
				i++ ;                                           		// Increment subscript: array pointer
			}  
                          
            if( !found ) {                                      		// If key sought is not found in pwordKeys 
            	msg = "Key " + tpwordKey + "not found" ;       		// Alert message: key not found
                displayMsg( msg ) ;                             		// Display alert message
            } else if( success == 1 ) {                         		// If entered password != array password
            	msg = "Password entered did not match password on file: " + tpwordKey ;
                displayMsg( msg ) ;                             		// Display alert message
            } else {                                            		// Otherwise ...
            	msg = "Password updated for key: " + tpwordKey ;	// Construct message ... password updated successfully
                displayMsg( msg ) ;                             		// Display message
            }

            tfpwordCurr.setText( "" ) ;                         		// Clear the tfpwordCurr textfield in baseFrame
            tfpwordNew1.setText( "" ) ;                         		// Clear the tfpwordNew1 textfield in baseFrame
        }  

        else if( e.getActionCommand( ).equals( "New Password" )) {  	// New Password button was pressed
        	addNewPassword( ) ;                                     	// Call the addNewPassword method
        }
        else if( e.getActionCommand( ).equals( "Add New Password" )) {	// Ad New Password button pressed on window 
        	tpwordKey    = tfpwordKey.getText( ) ;             			// Copy password key to string field in winFrame
        	tpwordNew    = tfpwordNew2.getText( ) ;            			// Copy new password to string field in winFrame
              
           	idx = 0 ;
           	while( pwordKey[idx] != null ) {                        	// While pwordKeys has value
           	if( tpwordKey.equals( pwordKey[idx] ))              		// If new key entered is in pwordKeys list
           		found = true ;                                  		// Flag as found           
               	idx++ ;                                             	// Increment subscript 
           	}  
                  
            if( !found ) {                                          	// If new key not found in pwordKeys 
              	pwordKey[idx] = tpwordKey ;                         	// Add newly-entered key to pwordKeys array
                pwordText[idx] = tpwordNew ;                        	// Add newly-entered password to pwordText array        

                msg = "New key and password added to array" ;      	// Alert user -- update successful
                displayMsg( msg ) ;               
                     
                tfpwordKey.setText( "" ) ;                          	// Clear the textfield
                tfpwordNew2.setText( "" ) ;                         	// Clear the textfield
            }   
            else {
               	msg = "Password key already exists.  Please try again" ;  // Alert user -- update failure
               	displayMsg( msg ) ;               
            }
                  
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>( pwordKey ) ; // This refreshes the cb model
            cb.setModel( model ) ;                                  	// This triggers the refresh
        }
    }    
       
       
       
       
/**
*****************************************************************************************
*  addNewPassword presents the user with a window to add a new password.  The new       *
*     password is then added to the end of the password arrays.                         *
*****************************************************************************************
**/
	public void addNewPassword( ) {
                                                                        // Create and set up a window frame
		JFrame.setDefaultLookAndFeelDecorated( true ) ;                 // Specify window has default look
        JFrame winFrame = new JFrame( "     NEW PASSWORD                  " ) ; // Designate title bar content
        winFrame.setDefaultCloseOperation( 0 ) ;                        // Turn off X window close button       
        winFrame.setResizable( false ) ;                                // Do not allow window to be resized

        JPanel panel10 = new JPanel( ) ;                                // Define the panel to hold the buttons
        panel10.setLayout( new GridLayout( 3, 2, 40, 4 )) ;       		// Designate layout is Grid
        panel10.setPreferredSize( new Dimension( 400, 100 )) ;    		// Window should resize to fit contents 
        winFrame.getContentPane( ).add( panel10 ) ;               		// Place panel10 into frame ( winFrame )
        winFrame.pack( ) ;                                        		// Size the frame so it contains all contents
        
        panel10.add( new JLabel( "Enter new password key:", JLabel.RIGHT )) ; // Paint label
        panel10.add( tfpwordKey ) ;                               		// Field/var for current password
                     
        panel10.add( new JLabel( "Enter new password:", JLabel.RIGHT ));// Paint label
        panel10.add( tfpwordNew2 ) ;                              		// Var for new password in window

        winFrame.add( panel10, BorderLayout.CENTER ) ;                  // Center panel10 in winFrame
        winFrame.pack( ) ;                                              // Size the frame so it contains all contents
        winFrame.setVisible( true ) ;                                   // Ensure the frame is visible
        winFrame.setLocationRelativeTo( null ) ;                        // OPTIONAL: Relocate window relative to a point

        JButton jbtn1 = new JButton( "Add New Password" ) ;             // Define button 
        JButton jbtn2 = new JButton( "Exit" ) ;                         // Define button
        jbtn1.addActionListener( this ) ;                               // Set up and associate action logic for jbtn1 
        jbtn2.setActionCommand( "exit2" ) ;                             // Set action command to unique Exit value     
        
        JPanel panel12 = new JPanel( ) ;                                // Define the panel12 to hold the buttons
        panel12.setLayout( new GridLayout( 1, 1 )) ;                    // Define one row for the button pane
        panel12.add( jbtn1 ) ;                                          // Add button 1 to panel12
        panel12.add( jbtn2 ) ;                                          // Add button 2 to panel12
        
        jbtn2.addActionListener(e -> {                                  // If 'Exit' button is clicked
        	tfpwordKey.setText("");										//	  Clear values in textboxes
        	tfpwordNew2.setText("");
        	winFrame.dispose( ) ;                                       //    Close window
        }  ) ;
       
        winFrame.add( panel12, BorderLayout.PAGE_END ) ;                // Position panel12 at bottom of winFrame
        winFrame.pack( ) ;                                              // Size the frame so it contains both panels
        winFrame.setVisible( true ) ;                                   // Ensure winFrame is visible
        
        return ;
	}
       
       
       


       
/**
*****************************************************************************************
*  getPasswordFileLocation reads the contents of the PasswordLocation.ini file for the  *
*     location of the PasswordVault.csv file.                                           *
*****************************************************************************************
**/
	public static void getPasswordFileLocation( ) {
		pw_LocFile          = System.getProperty( "user.home" ) + "/PasswordLocation.ini" ;  // Default password location filespec

		File directory      = new File(String.valueOf( System.getProperty( "user.home" ) + "/IBM\\PasswordVault" )) ; // Default password file directory
		if(!directory.exists( )){                                                               // If default directory does not exist        
			directory.mkdir( ) ;                                        //    create the directory
		}      
              
		String locInfo = null ;                                         // File location info = empty string
              
		try {                                                           // Open the password file
			BufferedReader csvReader = new BufferedReader( new FileReader( pw_LocFile )) ;
			if((locInfo = csvReader.readLine()) != null) {              // Read data line
				pw_Location = locInfo ;                                 // Save filespec from file
			} else {
				pw_Location = System.getProperty( "user.home" ) + "/IBM/PasswordVault/PasswordVault.csv"; // Default password filespec 
			}
			csvReader.close( ) ;                                        // Close the password file
		}
		catch( IOException e ) {                                        // If the password file not found
			pw_Location = System.getProperty( "user.home" ) + "/IBM/PasswordVault/PasswordVault.csv"; // Default filespec       
		}      
                     
		return ;
    }
       


       
/**
*****************************************************************************************
*  readPasswordsFromFile reads the contents of the PasswordVault.csv file, one line at  *
*     a time, loading the password key into the pwordKeys array and passwords into the  *
*     pwordText array.  The idx variable is left with the number of key/text sets that  *
*     were processed.                                                                   *     
*****************************************************************************************
**/
	public void readPasswordsFromFile( ) {
		String  row                = null ;                             // Accepts row data from password file
        int     rowNum      =  0 ;                                      // Looping construct for reading password file 
              
        try( BufferedReader csvReader = new BufferedReader( new FileReader( pw_Location )))  { // Open the password file
        	while ((row = csvReader.readLine( )) != null) {             // Read lines
        		if( rowNum >= 1) {
        			row = row.replaceAll("^\"|\"$", "" ) ;              // Replace all random chars with nulls
        			String[] data = row.split(",") ;                    // Split row into individual values        
        			
        			pw_Version                 = data[0] ;              // Save version number from file 
        			pwordText[rowNum-1] = decryptPassword( data[1] ) ; 	// Save decrypted password to pwordText array
        			
        			if( data.length == 2 ) {                            // If the password file only contained two elements
        				pwordKey[rowNum-1] = "default" ;                // Set pwordKey = default
        			} else {
        				pwordKey[rowNum-1]  = data[2] ;                 // Save password key to pwordKey array
        			}
        			if( data.length == 4 ) {
        				inUse[rowNum-1] = data[3] ;						// Save InUse Indicator to inUse array
        			} else {
        				inUse[rowNum-1] = "" ;							// Set InUse Indicator as not InUse
        		}	}    		
        		rowNum++ ;                                              // Next row 
        	}
        	csvReader.close( ) ;                                        // Close the password file
        	idx = rowNum - 2 ;                                          // idx is the number of passwords processed  
        } 
        catch( IOException e ) {                                        // If the password file not found
        	String msg = "         Password file not found!\nEmpty password array will be used." ;	// User msg:  File not found
        	displayMsg( msg ) ;                                         // Call displayMsg method
    }	}             

       
       
/**
*****************************************************************************************
* encryptPassword scrambles and encrypts passwords so they can be securely stored       *
*     on disk.                                                                          *   
*****************************************************************************************
**/    
	public String encryptPassword( String pword ) {
		String b64encoded = Base64.getEncoder().encodeToString(pword.getBytes( )) ; // Encrypt the string   
		String reverse    = new StringBuffer(b64encoded).reverse().toString( ) ;	// Reverse the string       
                                                                                                  
		StringBuilder tmp = new StringBuilder( ) ;
		final int OFFSET = 4 ;                                          // Caesar cypher
		for( int i = 0; i < reverse.length(); i++ ) {
			tmp.append((char)(reverse.charAt(i) + OFFSET)) ;                                
		}
		
		pword = tmp.toString( ) ;                                       // Copy encrypted password to pw_Encrypt string 
		
		return( pword ) ;
    }	
             

/**
****************************************************************************************
* decryptPassword - unscrambles and decrypts passwords so they can be used in          *
*	 processes without being exposed on disk.                                          *   
****************************************************************************************
**/    
	public String decryptPassword( String pword ) {
		StringBuilder tmp = new StringBuilder( ) ;
              
		final int OFFSET  = 4 ;                                         // Caesar cypher
		for( int i = 0; i < pword.length( ) ; i++ ) {
			tmp.append((char)( pword.charAt( i ) - OFFSET )) ;
		}
              
		String reversed = new StringBuffer(tmp.toString( )).reverse().toString( ) ; // Reverse the string
		String decryptedPassword = new String(Base64.getDecoder( ).decode( reversed )) ;

		return( decryptedPassword )  ;
    }
    

/**
*****************************************************************************************
*  savePasswordsToFile - presents the user with a file chooser window to select where   *
*  	 the passwords are to be stored.  Once the file has been chosen, the process        *
*    encrypts each password and writes the file version, encrypted password, and     	*
*    password key word to the file.                                                     *
*****************************************************************************************
**/           
	public void savePasswordsToFile( ) {
		File   fileToSave  = null ;                                     // Filespec for output file
		JFrame parentFrame = new JFrame( ) ;
		int    i           = 0 ;                                        // Loop control variable (LCV)
		String encPword    = null ;                                     // Password after encryptPassword( 
		
		try {
			JFileChooser fileChooser = new JFileChooser( ) ;   			// Construct SaveFileDialog
			fileChooser.setDialogTitle("FILE SAVE");                    // Dialog title
			fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES ) ;
			fileChooser.setSelectedFile( new File( pw_Location )) ;    	// Set file location default to pw_Location
                     
			int rv =  fileChooser.showSaveDialog( parentFrame ) ;       // Paint dialog frame
			if( rv == JFileChooser.APPROVE_OPTION) {
				fileToSave = fileChooser.getSelectedFile( ) ;           // Accept user input for pword file location
			}
			pw_Location = fileToSave.getPath( ) ;                       // Save full pword filespec to pw_Location var
                     
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave.getAbsolutePath( ))) ;  // Open the output password file
			writer.write("replyId,Password,Key,InUse\n") ;              // Write column headers  
              
			while( pwordKey[i] != null) {                               // Loop while pwordKey contains a value
				encPword = encryptPassword( pwordText[i] ) ;            // Encrypt the password
				if (pwordKey[i].isEmpty()) {
					writer.write(pw_Version + "," + encPword + "," + pwordKey[i] + "," + inUse[i] + "\n" ) ; // Write the file line
				} else {
					writer.write(pw_Version + "," + encPword + "," + pwordKey[i] + ",\n" ) ; // Write the file line
				}
				i++ ;                                                   // Next record 
			}      
			writer.close( ) ;                                           // Close the password file
		} catch( IOException e ) {
			e.printStackTrace( ) ;
	}	}             
              

       
       
/**
*****************************************************************************************
*     displayMsg presents informational messages to the user.                           *
*****************************************************************************************
**/
	public void displayMsg( String msg ) {
		JOptionPane.showMessageDialog( msgFrame, msg, 
			"Processing Information", JOptionPane.INFORMATION_MESSAGE ) ;           
	}


	public void clearVars( ) {
		pw_Version  = ""; //new String( )      ;          // Password version from file
		pw_Location = ""; //new String( )      ;          // Location of PasswordVault.csv file
		pw_LocFile  = ""; //new String( )      ;          // Location of PasswordLocation.ini file
		inUse		= new String[100]	 ;
		pwordKey    = new String[100]    ;          // Array of stored keywords  from password file
		pwordText   = new String[100]    ;          // Array of stored passwords from password file
		tpwordKey   = ""; //new String( )      ;          // Stores new password key entered by user
		tpwordNew   = ""; //new String( )      ;          // Stores new password entered by user
		tpwordCurr  = ""; //new String( )      ;          // Stores current password entered by user
		tfpwordKey.setText(""); //new JTextField(16) ;          // Field to store password key selected by user
		tfpwordCurr.setText(""); //new JTextField(16) ;          // Field to store current password entered by user
		tfpwordNew1.setText(""); //new JTextField(16) ;          // Field to store new password entered by user in baseFrame
		tfpwordNew2.setText(""); //new JTextField(16) ;          // Field to store new password entered by user in winFrame

		cb.removeAll(); //new JComboBox<String>( ) ;    // Picklist field containing list of keys
	}
}