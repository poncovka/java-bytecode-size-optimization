package jbyco.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Directories are searched recursively. Jar files
 * are extracted in a temporary directory and processed there.
 */
public class ExtractedFilesIterator extends CommonFilesIterator {

    /**
     * The working directory.
     */
    Path workingDirectory;

    /**
     * Instantiates a new bytecode files iterator.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public ExtractedFilesIterator() throws IOException {
        // nothing
    }

    /**
     * Instantiates a new extracted files iterator.
     *
     * @param path             the path
     * @param workingDirectory the working directory
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public ExtractedFilesIterator(Path path, Path workingDirectory) throws IOException {
        super();
        init(path, workingDirectory);
    }

    /**
     * Initializes the iterator.
     *
     * @param path             the path
     * @param workingDirectory the working directory
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void init(Path path, Path workingDirectory) throws IOException {

        // set temporary directory
        this.workingDirectory = workingDirectory;

        // init files iterator
        init(path);
    }

    /* (non-Javadoc)
     * @see jbyco.io.CommonFilesIterator#processFile(java.nio.file.Path, jbyco.io.CommonFile)
     */
    @Override
    protected CommonFile processFile(Path path, CommonFile parent) throws IOException {

        // get general file
        CommonFile file = super.processFile(path, parent);

        // is jar file?
        if (file.isFile() && file.isJar()) {

            // create temporary directory
            Path tmp = Files.createTempDirectory(workingDirectory, file.getName());

            // extract files to the directory
            JarExtractor.extract(path, tmp);

            // return extracted file
            return new CommonFile(tmp.toFile(), file.getRelativePath(), true);
        }

        return file;
    }
}
