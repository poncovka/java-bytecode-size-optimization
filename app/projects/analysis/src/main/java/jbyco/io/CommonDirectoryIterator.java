package jbyco.io;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * Iterator over common files in a directory.
 */
public class CommonDirectoryIterator implements Iterator<Path> {
	
	/** The directory. */
	CommonFile file;
	
	/** The iterator. */
	Iterator<Path> iterator;
	
	/** The stream. */
	DirectoryStream<Path> stream;
	
	/**
	 * Instantiates a new common directory iterator.
	 *
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public CommonDirectoryIterator(CommonFile file) throws IOException {
		
		// init
		this.file = file;
		
		// create iterator over files
		this.stream = Files.newDirectoryStream(file.getPath());
		this.iterator = stream.iterator();
	}
	
	public CommonFile getFile() {
		return file;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Path next() {
		return iterator.next();
	}
	
	/**
	 * Close.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void close() throws IOException {
		this.stream.close();
	}
	
}
