package jbyco.io.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class ClassFile implements BytecodeFile {

	Path path;
	
	public ClassFile(Path path) {
		this.path = path;
	}
	
	@Override
	public InputStream getInputStream() throws FileNotFoundException {
		return new FileInputStream(path.toFile());
	}

	@Override
	public void close() throws IOException {
		// nothing
	}

	@Override
	public String toString() {
		return path.toString();	
	}
	
}
