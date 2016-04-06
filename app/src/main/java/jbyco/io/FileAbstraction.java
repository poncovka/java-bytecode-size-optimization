package jbyco.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public class FileAbstraction {
	
	// path to the existing file
	Path realPath;
	
	// abstract path to the file  
	Path abstractPath;
	
	// file
	java.io.File file;
	
	public FileAbstraction(Path realPath, Path abstractPath) {
		this.abstractPath = abstractPath;
		this.realPath = realPath;
		this.file = realPath.toFile();
	}
	
	public String getName() {
		return abstractPath.getFileName().toString();
	}
	
	public Path getRealPath() {
		return realPath;
	}
	
	public Path getAbstractPath() {
		return abstractPath;
	}
	
	public void copyToPath(Path newRealPath) {
		// TODO
	}
	
	public InputStream getInputStream() throws IOException {
		return new BufferedInputStream(new FileInputStream(file));
	}
	
	public OutputStream getOutputStream() throws IOException {
		return new BufferedOutputStream(new FileOutputStream(file));
	}
	
	public boolean isClassFile() {
		return getName().endsWith(".class");
	}
	
	public boolean isJar() {
		return getName().endsWith(".jar");
	}
	
	public boolean isDirectory() {
		return file.isDirectory();
	}

	public boolean isFile() {
		return file.isFile();
	}
	
}
