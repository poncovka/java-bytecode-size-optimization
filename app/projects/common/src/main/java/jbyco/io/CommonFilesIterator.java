package jbyco.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * An iterator over all files in a directory and subdirectories.
 */
public class CommonFilesIterator implements Iterator<CommonFile>, Iterable<CommonFile> {

    /**
     * The next file to return.
     */
    CommonFile next;

    /**
     * The stack of directory iterators.
     */
    Deque<CommonDirectoryIterator> stack;

    /**
     * Instantiates a new common files iterator.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public CommonFilesIterator() throws IOException {
        // nothing
    }

    /**
     * Instantiates a new common files iterator.
     *
     * @param path the path
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public CommonFilesIterator(Path path) throws IOException {
        init(path);
    }

    /**
     * Initializes the iterator.
     *
     * @param path the path
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected void init(Path path) throws IOException {

        // check the path
        if (!path.toFile().exists()) {
            throw new FileNotFoundException(path.toString());
        }

        // init
        this.stack = new ArrayDeque<>();

        // find next general file
        this.next = processFile(path, null);
        updateStack(this.next);
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return next != null;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    @Override
    public CommonFile next() {

        CommonFile file = next;

        try {
            next = findNextFile();
        } catch (IOException e) {
            e.printStackTrace();
            next = null;
        }

        return file;
    }


    /**
     * Gets the relative path to the file.
     *
     * @param parent the parent file
     * @param name   the name of file
     * @param isFile is it name of the file?
     * @return the relative path to the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected Path getRelativePath(CommonFile parent, String name, boolean isFile) throws IOException {

        if (parent != null) {
            return parent.getRelativePath().resolve(name);
        } else if (isFile) {
            return Paths.get(name);
        } else {
            return Paths.get("");
        }
    }

    /**
     * Process the file.
     *
     * @param path   the path to the file
     * @param parent the parent of the file
     * @return the common file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected CommonFile processFile(Path path, CommonFile parent) throws IOException {

        File file = path.toFile();
        Path relative = getRelativePath(parent, file.getName(), file.isFile());

        return new CommonFile(file, relative);
    }

    /**
     * Update the stack.
     *
     * @param file the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected void updateStack(CommonFile file) throws IOException {

        // push directory on the stack
        if (file != null && file.isDirectory()) {
            stack.push(new CommonDirectoryIterator(file));
        }
    }

    /**
     * Find the next file.
     *
     * @return the common file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected CommonFile findNextFile() throws IOException {

        CommonFile file = null;


        // get file in directories
        while (file == null && !stack.isEmpty()) {

            // get directory
            CommonDirectoryIterator dir = stack.peek();

            // get file
            if (dir.hasNext()) {
                file = processFile(dir.next(), dir.getFile());
                updateStack(file);
            }
            // close and remove directory
            else {
                dir.close();
                stack.pop();
            }
        }

        // return new file
        return file;
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<CommonFile> iterator() {
        return this;
    }
}
