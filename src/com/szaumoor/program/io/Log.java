package com.szaumoor.program.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;

import static com.szaumoor.program.io.IOUtils.lineBreak;

/**
 * Utility class to generate logs from exceptions and save them in disk.
 */
public final class Log {
    private enum LogType {ERROR, DUPLICATES}

    private static final String ext = ".txt";

    /**
     * Constructs a time-based filename for any logging needs.
     *
     * @param type The type of the log, will determine the name partially.
     * @return The generated full name for the log file.
     */
    private static String generateLogFilename(final LogType type) {
        return LocalDateTime.now()
                .toString()
                .replaceAll("-", "_")
                .replaceAll(":", "_") + "_" + type.toString().toLowerCase();
    }

    /**
     * Method that takes an exception and writes the output into a text file at the
     * current location.
     *
     * @param ex Exception to be recorded in the log
     * @throws IOException thrown if there are errors saving the log into disk
     */
    public static void error(final Exception ex) throws IOException {
        final File filename = resolveFilename(LogType.ERROR);
        var writer = Files.newBufferedWriter(Path.of(filename.toURI()), StandardOpenOption.CREATE);
        ex.printStackTrace(new PrintWriter(writer));
        writer.flush();
        writer.close();
    }

    /**
     * Method that takes a List of paths and writes the output into a text file
     * at the current location.
     *
     * @param paths List of paths of the files duplicated
     * @throws IOException thrown if there are errors saving the log into disk
     */
    public static void duplicates(final List<String> paths) throws IOException {
        if (paths == null) throw new NullPointerException();
        if (paths.size() <= 1) return;
        final File logFile = resolveFilename(LogType.DUPLICATES);
        var writer = Files.newBufferedWriter(Path.of(logFile.toURI()), StandardOpenOption.CREATE);
        for (var path : paths) writer.write(path + lineBreak);
        writer.flush();
        writer.close();
    }

    /**
     * Makes sure that the filename does not clash with a present one if there is some weirdness
     * in the naming or for whatever reason the timestamp is identical to some other file.
     * <br>
     * If that is the case, the program will simply append "_copy" to the end of the filename.
     *
     * @return The resulting filename after comparison.
     */
    private static File resolveFilename(final LogType type) {
        String filename = generateLogFilename(type);
        File file = new File(filename + ext);
        return file.exists() ? new File(filename + "_copy" + ext) : file;
    }
}
