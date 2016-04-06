package jbyco.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarExtractor {

	static public void extract(Path from, Path to) throws IOException {
		
		JarFile jar = new JarFile(from.toFile());
		Enumeration<JarEntry> entries = jar.entries();
		
		while (entries.hasMoreElements()) {
			
			JarEntry entry = entries.nextElement();
			Path path = to.resolve(entry.getName());
			File file = path.toFile();
			
			if (entry.isDirectory()) {
				file.mkdir();
				continue;
			}
			else {
				file.getParentFile().mkdirs();
			}
			
			InputStream in = jar.getInputStream(entry);
			Files.copy(in, path);
			in.close();
		}
		
		jar.close();
	}
}
