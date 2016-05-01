package jbyco.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import jbyco.lib.Utils;

/**
 * A class for counting class files in a directory and subdirectories.
 */
public class BytecodeFilesCounter {

    /** The count of class files. */
    int counter = 0;

    public int getCount() {
        return counter;
    }

    /**
     * Search all paths and count the class files.
     *
     * @param paths the paths
     * @return the number of class files
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public int countAll(Collection<Path> paths) throws IOException {

        for (Path path:paths) {
            countFiles(path);
        }

        return counter;
    }

    /**
     * Search the path and count the class files.
     *
     * @param path the path
     * @return the number of class files
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public int countFiles(Path path) throws IOException {

        for (CommonFile file : new CommonFilesIterator(path)) {

            if (file.isClassFile()) {
                counter++;
            }
            else if (file.isJar()) {
                countFilesInJar(file.getPath());
            }
        }

        return counter;
    }

    /**
     * Count files in a jar file.
     *
     * @param path the path to a jar file
     * @return the number of class files
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public int countFilesInJar(Path path) throws IOException {

        JarFile jar = new JarFile(path.toFile());
        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {

            JarEntry entry = entries.nextElement();
            String name = entry.getName();

            if (!entry.isDirectory()) {

                if (Utils.endsWithClass(name)) {
                    counter++;
                }
                else if (Utils.endsWithJar(name)) {
                    countFilesInJar(jar.getInputStream(entry));
                }
            }
        }

        jar.close();
        return counter;
    }

    /**
     * Count files in a jar file.
     *
     * @param in the input stream of a jar entry
     * @return the number of class files
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public int countFilesInJar(InputStream in) throws IOException {

        JarInputStream jar = new JarInputStream(in);
        JarEntry entry;

        while ((entry = jar.getNextJarEntry()) != null) {

            if (!entry.isDirectory()) {

                String name = entry.getName();
                if (Utils.endsWithClass(name)) {
                    counter++;
                }
                else {
                    countFilesInJar(jar);
                }
            }
        }

        return counter;
    }
}
