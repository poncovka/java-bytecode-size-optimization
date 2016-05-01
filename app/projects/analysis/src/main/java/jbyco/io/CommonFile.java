package jbyco.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import jbyco.lib.Utils;

/**
 * A representation of the file.
 */
public class CommonFile {

    /** The existing file. */
    final File file;

    /**
     * Instantiates a new common file.
     *
     * @param file the file
     */
    public CommonFile(File file) {
        this.file = file;
    }

    /**
     * Instantiates a new common file.
     *
     * @param path the path
     */
    public CommonFile(Path path) {
        this.file = path.toFile();
    }

    public String getName() {
        return file.getName();
    }

    public Path getPath() {
        return file.toPath();
    }

    /**
     * To simple file.
     *
     * @return the file
     */
    public File toSimpleFile() {
        return file;
    }

    public InputStream getInputStream() throws IOException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    public OutputStream getOutputStream() throws IOException {
        return new BufferedOutputStream(new FileOutputStream(file));
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

    /**
     * Delete.
     *
     * @return true, if successful
     */
    public boolean delete() {
        return file.delete();
    }

}
