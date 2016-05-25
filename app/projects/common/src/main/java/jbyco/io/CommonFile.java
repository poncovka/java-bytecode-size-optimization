package jbyco.io;

import jbyco.lib.Utils;

import java.io.*;
import java.nio.file.Path;

/**
 * A representation of the file.
 */
public class CommonFile {

    /**
     * The existing file.
     */
    final File file;

    /**
     * The relative file. The path may contain extracted folders.
     **/
    final File relativeFile;

    /**
     * Was the file extracted?
     **/
    final boolean extracted;

    /**
     * Instantiates a new common file.
     *
     * @param file     the path to the existing file
     * @param relative the relative path to the file
     * @param extracted was the file extracted?
     */
    public CommonFile(File file, Path relative, boolean extracted) {
        this.file = file;
        this.relativeFile = relative.toFile();
        this.extracted = extracted;
    }

    public CommonFile(File file, Path relative) {
        this(file, relative, false);
    }

    public boolean isClassFile() {
        return Utils.endsWithClass(getName());
    }

    public boolean isJar() {
        return Utils.endsWithJar(getName());
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean isFile() {
        return file.isFile();
    }

    public boolean isExtracted() {
        return extracted;
    }

    public String getName() {
        return relativeFile.getName();
    }

    public Path getPath() {
        return file.toPath();
    }

    public Path getRelativePath() {
        return relativeFile.toPath();
    }

    public File toFile() {
        return file;
    }

    public File toRelativeFile() {
        return relativeFile;
    }

    public InputStream getInputStream() throws IOException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    public OutputStream getOutputStream() throws IOException {
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    public Path resolveRelativePath(Path absolute) {
        return absolute.resolve(getRelativePath());
    }

    public CommonFile copy(File file) {
        return new CommonFile(file, getRelativePath(), isExtracted());
    }

    public boolean createDirectories() {
        return file.mkdirs();
    }

    public boolean delete() {
        return file.delete();
    }

}
