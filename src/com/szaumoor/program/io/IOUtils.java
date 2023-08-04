package com.szaumoor.program.io;

import com.szaumoor.program.exceptions.UnexpectedPathException;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.szaumoor.program.io.FileDumper.currentLocation;
import static java.lang.Integer.parseInt;

/**
 * Singleton class with IO utility methods
 */
public enum IOUtils {
    ;
    public static final String fileSeparator = System.getProperty("file.separator");
    public static final String lineBreak = System.getProperty("line.separator");
    public static final String duplicateRegex = "(\\(\\d+\\))"; // any String which is a digit(s) between parenthesis
    public static final Pattern pattern = Pattern.compile(duplicateRegex);
    public static Matcher matcher;
    /**
     * Record that holds data of a pattern match. Namely, it stores whether there was a match, and what the
     * match was in String form.
     *
     * @param matched Whether the match happened or not.
     * @param getMatch What the match is in String form.
     */
    private record Match(Boolean matched, String getMatch){}

    /**
     * Returns the list of files present if the passed File is not null and a valid folder.
     *
     * @param location Location to check and extract other files from.
     * @return Array of files present in the argument location
     * @throws IOException thrown if there are any errors logging
     */
    static File[] getFiles(final File location) throws IOException {
        fileNullCheck(location);
        final File[] folders = location.listFiles();
        fileListNullCheck(folders);
        return folders;
    }

    /**
     * Checks if the argument file already exists in the current location of the program.
     *
     * @param file File to check for existence
     * @return True if it exists and false otherwise
     */
    static boolean fileCopiedAlready(final File file) {
        return new File(currentLocation.getAbsolutePath() + fileSeparator + file.getName()).exists();
    }

    /**
     * Checks if an array of folders is not null. Used internally to check we're looping
     * through all files correctly. Additionally, it logs any errors found.
     *
     * @param folders Array of folders to check
     * @throws IOException             thrown if there are any errors logging
     * @throws UnexpectedPathException thrown the passed array is null, which happens when trying to
     *                                 extract the files inside something that isn't a folder
     */
    private static void fileListNullCheck(final File[] folders) throws IOException {
        if (folders == null) {
            UnexpectedPathException ex = new UnexpectedPathException("Location does not denote folder");
            Log.error(ex);
            throw ex;
        }
    }

    /**
     * Checks if a folder location is not null. Used internally to check we're looping
     * through all files correctly. Additionally, it logs any errors found.
     *
     * @param location File to check
     * @throws IOException          thrown if there are any errors logging
     * @throws NullPointerException thrown if the passed file is null
     */
    private static void fileNullCheck(final File location) throws IOException {
        if (location == null) {
            final NullPointerException ex = new NullPointerException("Storage location cannot be null");
            Log.error(ex);
            throw ex;
        }
    }

    /**
     * Returns a fixed filename to ensure duplicates are handled gracefully instead of
     * overwriting or skipping files. Filenames will get a number inside parenthesis
     * at the end of the name before the extension, or it will increase that number
     * if there is already a duplicate.
     *
     * @param toTest File to test for
     * @return New filename
     * @throws IOException          thrown if there is an error logging possible errors
     * @throws NullPointerException thrown if the passed file is null
     */
    public static String getDuplicateFilename(File toTest) throws IOException {
        if (toTest == null) {
            final NullPointerException filePassedWasNull = new NullPointerException("File passed was null");
            Log.error(filePassedWasNull);
            throw filePassedWasNull;
        }
        String filename = toTest.getName();
        while (fileCopiedAlready(new File(filename))) filename = transformName(filename); //
        return filename;
    }

    /**
     * Transforms a filename into a new name for the purposes of a duplicated filename issue.
     *
     * @param filename the filename to transform
     * @return the transformed new filename
     */
    public static String transformName(String filename) {
        if (filename == null) throw new NullPointerException("Passed null filename");
        var match = endsWithPattern(filename);
        final int periodAtEnd = filename.lastIndexOf('.');
        if (match.matched()) {
            final String pat = match.getMatch();
            int currentNumber = pat.charAt(1) - '0';
            String newPat = String.format("%c%d%c", pat.charAt(0), currentNumber+1, pat.charAt(2));
            filename = filename.substring(0, filename.lastIndexOf(pat)) + newPat + (periodAtEnd == -1 ? "" : filename.substring(periodAtEnd));
        } else {
            filename = periodAtEnd == -1 ? filename + "(1)" : filename.substring(0, periodAtEnd) + "(1)" + filename.substring(periodAtEnd);
        }
        return filename;
    }


    /**
     * Tests if a filename ends with (&lt;integer&gt;) or not. There are two scenarios where this is contemplated
     * as true: <br><br>
     * 1. The String has extension. Anything after the last period in the name will be considered the extension.
     * In this case, that String will be considered to end with that pattern if it contains the pattern before the period. <br> <br>
     * 2. The String has no extension, in which case, it will look at the leading n number of characters that match the pattern.
     * <br><br>
     *
     * @param toTest The String to test for.
     * @return A Match object containing information of a possible match and the String that was matched.
     */
    private static Match endsWithPattern(String toTest) {
        final int indexOfPeriod = toTest.lastIndexOf('.');
        toTest = indexOfPeriod == -1 ? toTest : toTest.substring(0, indexOfPeriod); // if it ends in period, we only care about matching before it
        matcher = pattern.matcher(toTest);
        boolean found = false;
        String lastMatch = null;
        while (matcher.find()) {
            found = true;
            lastMatch = matcher.group();
        }
        return new Match(found, lastMatch);
    }

    String oldAlgorithm() {
        String filename = null;
        while (true) {
            if (IOUtils.fileCopiedAlready(new File(filename))) {
                filename = filename;
                continue;
            }
            matcher = pattern.matcher(filename);
            if (matcher.find()) {
                String group = matcher.group(matcher.groupCount());
                int newNum = parseInt(group.substring(1, 2)) + 1;
                String newGroup = group.replaceAll("\\d", newNum + "");
                filename = filename.replace(group, newGroup);
                if (IOUtils.fileCopiedAlready(new File(filename))) continue;
                return filename;
            }
            break;
        }
        int lastIndexOfPeriod = filename.lastIndexOf(".");
        return lastIndexOfPeriod == -1 ? filename + "(1)" : filename.substring(0, lastIndexOfPeriod) + "(1)" + filename.substring(lastIndexOfPeriod);

    }
}
