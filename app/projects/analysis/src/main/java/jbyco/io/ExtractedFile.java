package jbyco.io;

import java.nio.file.Path;

/**
 * The representation of the extracted file.
 */
public class ExtractedFile extends CommonFile {

    /** The abstracted path to the file. */
    Path abstracted;

    /**
     * Instantiates a new extracted file.
     *
     * @param path the path
     * @param abstracted the abstracted
     */
    public ExtractedFile(Path path, Path abstracted) {
        super(path);
        this.abstracted = abstracted;
    }

    public Path getAbstractedPath() {
        return abstracted;
    }

    @Override
    public String getName() {
        return abstracted.toFile().getName();
    }


}
