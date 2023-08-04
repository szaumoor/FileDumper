## Description
This program is an utility that I made to quickly dump everything inside subfolders in the current location in your file system.
It will handle recursive and non-recursive copies if for some reason you only want one-level deep copies.
Duplicates will also be handled automatically, such that files with names already copied will be copied with a modified name
that makes it clear by adding a number in between parenthesis to the end of the filename.

## Requirements
A Unix or Windows system with at least Java 14 installed. Just get Java 17.

## How to use
You can either use commands or use the appropriate script depending on your needs and the system you're running on.
The command to execute the program is: 'java -jar FileDumper.jar' followed by either 'true' or 'false' depending on
whether you want it to be a recursive copy or not, respectively.

