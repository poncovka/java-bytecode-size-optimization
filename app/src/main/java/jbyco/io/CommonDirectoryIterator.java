package jbyco.io;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public class CommonDirectoryIterator implements Iterator<Path> {
	
	CommonFile file;
	Iterator<Path> iterator;
	DirectoryStream<Path> stream;
	
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

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Path next() {
		return iterator.next();
	}
	
	public void close() throws IOException {
		this.stream.close();
	}
	
}
