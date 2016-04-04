package jbyco.io.files;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClassFile implements BytecodeFile {

	JarFile jar;
	JarEntry entry;
	
	public JarClassFile(JarFile jar, JarEntry entry) {
		this.jar = jar;
		this.entry = entry;
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return new BufferedInputStream(jar.getInputStream(entry));
	}

	@Override
	public void close() {
		// nothing
	}
	
	@Override
	public String toString() {
		return jar.getName() + "/" + entry.getName();
		
	}

	@Override
	public String getName() {
		return jar.getName() + File.separator + entry.getName();
	}

}
