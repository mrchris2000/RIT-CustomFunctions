# RIT-CustomFunctions

A library of (mostly working) sample code that illustrates useful custom functions that have been written over the years.
The goal of this repository is to allow anyone to take these as examples and re-use, enhance or otherwise modify them for their own needs.

### How to build?

Important: You will need to add a local maven repository yourself in order to compile the custom functions. This repository simply contains the RIT jar file that is required for build purposes. We cannot distribute this, but if you have a copy of the software you can create the local repository using Maven like so:

mvn deploy:deploy-file -Durl=file:///f:/GitHub/RIT-CustomFunctions/repo/ -Dfile=com.ghc.ghTester_1.1052.0.202303090223.jar -DgroupId=com.ibm.rit -DartifactId=expressions -Dpackaging=jar -Dversion=1.0

On a Windows PC if you run the above command from: C:\Program Files\IBM\IBMIMShared\plugins
Things should work correctly. Obviously you will want to change the output path, and as new versions of RIT are released the actual jar file version number will change, but you get the idea.
If you leave the /repo on the end of the path so that the repo folder is created in the root of the repository the build should 'just work'.

In order to check that it is working simply run:
mvn clean package

In the 'target' folder you will then find a 'CustomFunctions.jar' file which can be dropped into a projects 'Functions' folder and used normally.

### What's included?
At a source level this repository includes everything needed to compile and use these functions and as such it is structured as an Eclipse plugin, the same as you would need to do to write a plugin from scratch.

We will be adding detailed descriptions of functions over time and hopefully pulling in a few community created examples.
Where there is built in functionality in the product the function will marked '(Deprecated)', I am leaving them in here so as not to break projects that may use these functions.

###### FormatDate
The example date formatter provided with the product in the 'Examples' folder.

###### Concat (Deprecated)
Simple string concatenation routine
Note: In most cases you can use %%tag1%%%%tag2%% to concatenate values.

###### FTP
A simple FTP implementation to 'put' or 'get' files from an FTP server.

###### SFTP
A simple SFTP implementatin to 'put' or 'get' files from an SFTP server.

###### FileName
Obtain the name of the nth file in the requested directory.
The files are ordered by their names in ascending ASCII value.
The first file in the directory will have the number 1 (not 0).
The function will return the fully qualified path name for the file.

###### NumberGenerator
The function takes two parameters:
Param1: Result length (ie 6 would result in 000001, 3 would result in 001).
Param2: Optional path to a file to read and store the last used number.

NumberGenerator( 6, c:\\temp\\myFile.txt ) returns: 000001 then: 000002 and increments on subsequent executions.

###### SSHConnection (Deprecated)
Log in to an SSH authenticated terminal session and execute specified commands.
Note: Use 'RunCommand' in preference to this now.

###### Substring
Extract the area of a String between two character positions.

###### ToLower
###### ToUpper
Convert the specified text to Lower or Upper case respectively.

####### XPathUpdate
Changes a value in an XML file on the path to the value passed in. The node to update can be either an element or an attribute. If you want to update an attribute, just pass <code>null</code> for it and the parser will look just for the matching element name. Not intended to solve every XML update problem, but just handle some of the boilerplate in certain cases.

Examples (In all cases the form is - XPathUpdate(%%TAG_CONTAINING_XML%%, xPath, newValue):

Update the value of the 'name' attribute in the 'project' element - 
'/project/@name'

Update the value of the 'dude' attribute in the last element called 'test' - 
'/project/test[last()]/@dude'

Update the value of the  'dude' attribute for all test elements that have an attribute called 'price' whose value is greater than 50.00 - 
'/project/test[@price>50.00]/@dude' 

###### regexReplaceAll (Deprecated)
Replace text matched by the given Regular Expression with the supplied text.