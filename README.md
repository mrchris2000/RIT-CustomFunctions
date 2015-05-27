RIT-CustomFunctions
===================

A library of (mosly working) sample code that illustrates useful custom functions that have been written over the years.
The goal of this repository is to allow anyone to take these as examples and re-use, enhance or otherwise modify them for their own needs.

## How to build?

Important: You will need to add a local maven repository yourself in order to compile the custom functions. This repository simply contains the RIT jar file that is required for build purposes. We cannot distribute this, but if you have a copy of the software you can create the local repository using Maven like so:

mvn deploy:deploy-file -Durl=file:///d:/OneDrive/GitHub/RIT-CustomFunctions/repo/ -Dfile=com.ghc.ghTester_1.870.0.v20150219_1811.jar -DgroupId=com.ibm.rit -DartifactId=expressions -Dpackaging=jar -Dversion=1.0

On a Windows PC if you run the above command from: C:\Program Files\IBM\IBMIMShared\plugins
Things should work correctly. Obviously you will want to change the output path, and as new versions of RIT are released the actual jar file version number will change, but you get the idea.
If you leave the /repo on the end of the path so that the repo folder is created in the root of the repository the build should 'just work'.

In order to check that it is working simply run:
mvn clean package

In the 'target' folder you will then find a 'CustomFunctions.jar' file which can be dropped into a projects 'Functions' folder and used normally.

## What's included?
At a source level this repository includes everything needed to compile and use these functions and as such it is structured as an Eclipse plugin, the same as you would need to do to write a plugin from scratch.

We will be adding detailed descriptions of functions over time and hopefully pulling in a few community created examples.

- [FormatDate]
- [Concat]
- [FTP]
- [SFTP]
- [FileName]
- [NumberGenerator]
- [SSHConnection]
- [Substring]
- [TelnetConnection]
- [ToLower]
- [ToUpper]
- [XPathUpdate]
- [regexReplaceAll]
