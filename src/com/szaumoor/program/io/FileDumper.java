package com.szaumoor.program.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles all the copying and file looping.
 */
public enum FileDumper {
    ;
    public static final File currentLocation = new File(System.getProperty("user.dir"));

    /**
     * Copies files recursively, effectively grabbing every file in every subfolder that exists
     * from the current location.
     *
     * @param location The location from which we want to copy files from sub-folders.
     * @throws IOException thrown if there is a copy or log error.
     */
    private static void recursiveFileDump(final File location, final List<String> listOfDuplicates) throws IOException {
        final File[] files = copyFromLocation(location, listOfDuplicates);
        for (var folder : files) {
            if (folder.isDirectory()) {
                recursiveFileDump(folder, listOfDuplicates);
            }
        }
    }

    /**
     * Copies all the files in the sub-folders into the current location in a recursive or non-recursive way.
     *
     * @param location  The location from which this will be done.
     * @param recursive Whether it will do this process recursively or not.
     * @throws IOException thrown if there is a copy or log error
     */
    public static boolean dumpFromFolders(final File location, boolean recursive) throws IOException {
        var listOfDuplicates = new ArrayList<String>(50);
        listOfDuplicates.add("The following %d files already exist here and were copied with a different name: %n");
        if (!recursive) copyFromLocation(location, listOfDuplicates);
        else recursiveFileDump(location, listOfDuplicates);
        if (listOfDuplicates.size() > 1) {
            String updatedFirstLine = String.format(listOfDuplicates.get(0), listOfDuplicates.size());
            listOfDuplicates.set(0, updatedFirstLine);
            Log.duplicates(listOfDuplicates);
            return true;
        }
        return false;
    }

    /**
     * Copies all the files in the associated location into the current directory of the program.
     *
     * @param location Location to search in.
     * @return Array of files that are contained in the current location.
     * @throws IOException thrown if there is any sort of IO error while trying to copy files.
     */
    private static File[] copyFromLocation(final File location, final List<String> listOfDuplicates) throws IOException {
        final File[] folders = IOUtils.getFiles(location);
        for (File f : folders) {
            if (f.isDirectory()) {
                File[] files = IOUtils.getFiles(f);
                for (File inside : files) {
                    if (!inside.isDirectory()) {
                        if (!IOUtils.fileCopiedAlready(inside)) {
                            copyFile(inside, inside.getName());
                            System.out.println(inside.getName() + " copied!");
                        } else {
                            copyFile(inside, IOUtils.getDuplicateFilename(inside));
                            System.out.println(inside.getName() + " copied with a different name, as it already exists.");
                            listOfDuplicates.add(inside.getAbsolutePath());
                        }
                    }
                }
            }
        }
        return folders;
    }

    /**
     * Copies a single file into the current location with the passed file and filename.
     *
     * @param file       File to be copied to the current location
     * @param nameOfCopy The name the copied file should have (minus extension)
     * @throws IOException                thrown if there is an error when copying anything
     * @throws FileAlreadyExistsException thrown if in the process of copying the file it attempts to copy it despite
     *                                    having a file with the same name in the current location. This should only
     *                                    happen if there is a logic error in the code.
     */
    private static void copyFile(File file, String nameOfCopy) throws IOException {
        try {
            Files.copy(Path.of(file.toURI()),
                    Path.of(currentLocation.toURI()).resolve(nameOfCopy));
        } catch (FileAlreadyExistsException ex) {
            throw new RuntimeException("Program found a name clash. It shouldn't have", ex);
        }
    }
}
