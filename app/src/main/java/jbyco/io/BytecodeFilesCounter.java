package jbyco.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import jbyco.lib.Utils;

public class BytecodeFilesCounter {

	int counter = 0;
	
	public int getCount() {
		return counter;
	}
	
	public int countAll(Collection<Path> paths) {
		
		for (Path path:paths) {
			countFiles(path);
		}
		
		return counter;
	}
	
	public int countFiles(Path path) {
		
		for (CommonFile file : new FilesIterator(path)) {
			
			if (file.isClassFile()) {
				counter++;
			}
			else if (file.isJar()) {
				countFilesInJar(file.getPath());
			}
		}
		
		return counter;
	}

	public int countFilesInJar(Path path) {

		try {
			
			JarFile jar = new JarFile(path.toFile());
			Enumeration<JarEntry> entries = jar.entries();
			
			while (entries.hasMoreElements()) {
				
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				
				if (!entry.isDirectory()) {
					
					if (Utils.endsWithClass(name)) {
						counter++;
					}
					else if (Utils.endsWithJar(name)) {
						countFilesInJar(jar.getInputStream(entry));
					}
				}
			}
			
			jar.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return counter;
	}
	
	public int countFilesInJar(InputStream in) {
		
		try {
			
			JarInputStream jar = new JarInputStream(in);
			JarEntry entry;
			
			while ((entry = jar.getNextJarEntry()) != null) {
				
				if (!entry.isDirectory()) {
					
					String name = entry.getName();
					if (Utils.endsWithClass(name)) {
						counter++;
					}
					else {
						countFilesInJar(jar);
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return counter;
	}
}
