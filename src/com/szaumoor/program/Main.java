package com.szaumoor.program;

import com.szaumoor.program.io.FileDumper;
import com.szaumoor.program.view.Dialog;
import com.szaumoor.program.io.Log;
import com.szaumoor.program.view.DialogType;

import java.io.IOException;

import static com.szaumoor.program.io.FileDumper.currentLocation;

/**
 * Main class.
 * Copies all the files in the sub-folders, recursively or not. Needs to have either "true" or "false"
 * in the program arguments in order to decide to do the process recursively or not, respectively.
 */
public final class Main {
    public static void main(String[] appArgs) throws Exception {
        try {
            final boolean duplicationFound = FileDumper.dumpFromFolders(currentLocation, Boolean.parseBoolean(appArgs[0]));
            if (duplicationFound)
                Dialog.show("Process finished but duplicates were found and copied with a different name. Check logs.", DialogType.WARNING);
            else Dialog.show("Process finished!", DialogType.INFO);
        } catch (Exception e) {
            e.printStackTrace(); // for direct viewing if executed from a terminal
            Dialog.show("Something went wrong", DialogType.ERROR);
            try {
                Log.error(e);
            } catch (IOException ex) {
                Dialog.show("There was an error while trying to log the errors found", DialogType.ERROR);
            }
            System.exit(-1);
        }
        System.exit(0);
    }
}