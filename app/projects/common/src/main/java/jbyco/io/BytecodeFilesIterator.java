package jbyco.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * An iterator over all class files on a given path.
 */
public class BytecodeFilesIterator implements Iterator<CommonFile>, Iterable<CommonFile> {

    ExtractedFilesIterator i;
    CommonFile next;

    public BytecodeFilesIterator(Path path, Path workingDirectory) throws IOException {
        this.i = new ExtractedFilesIterator(path, workingDirectory);
    }

    public boolean isBytecodeFile(CommonFile file) {
        return file.isFile() && file.isClassFile();
    }

    @Override
    public boolean hasNext() {

        // find the next class file
        while (i.hasNext()) {

            next = i.next();

            if (isBytecodeFile(next)) {
                return true;
            }
        }

        // not found
        next = null;
        return false;
    }

    @Override
    public CommonFile next() {
        return next;
    }

    @Override
    public Iterator<CommonFile> iterator() {
        return this;
    }
}
