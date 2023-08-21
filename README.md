## Description
This program is a utility that I made to quickly dump everything inside subfolders in the current location in your file system. It will handle recursive and non-recursive copies if for some reason you only want one-level deep copies. Duplicates will also be handled automatically, such that files with names already copied will be copied with a modified name that makes it clear by adding a number in between parenthesis to the end of the filename.

## Requirements
A Unix or Windows system with at least Java 14 installed. Just get Java 17.

## How to use
First, download from the last version "FileDumper.zip" from the "Releases" section of this page.

Unzip the contents whenever you want the program to extract files from subdirectories. 

You'll find 4 scripts. Two of them for Windows, and the other two for Linux. Of those two, one empties only the first subfolder level, and the other recursively empties everything. The scripts will not work unless they are at the same location as the .jar file.

You can either double click on the appropriate script depending on your needs and the system you're running on, or use commands in a terminal or cmd (Windows). The command to execute the program is: 
```
java -jar FileDumper.jar [choice]
```
Where choice can be either 'true' or 'false', depending on whether you want it to be a recursive copy or not, respectively.

After some time, you'll get a small window popup telling you the process was finished. Bear in mind the program does not give you any feedback about what it's doing unless you execute it from a terminal. It will only notify you of either an error (crash) or a finished job. The time this takes varies depending on the depth of the directory tree and the size of the files to copy.

If there are any errors, it will generate a log file which may be useful for debugging if there is some issue in the code. If there were duplicates, it will generate a log file where it will list all the paths of the files which already existed and had to be copied with a modified name.
