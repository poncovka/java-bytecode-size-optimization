package jbyco.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BytecodeFilesIterator extends FilesIterator {

	public BytecodeFilesIterator(Path path) throws IOException {
		super(path);
		
		if (!checkFile(next)) {
			next = findNextFile();
		}
	}
	
	public boolean checkFile(CommonFile file) throws IOException {
		return file.isClassFile();
	}
	
	@Override
	protected CommonFile findNextFile() throws IOException {
		
		CommonFile file;
		
		while ((file = super.findNextFile()) != null) {
			if (checkFile(file)) {
				return file;
			}
		}
		
		return null;
	}
	
	@Override
	protected CommonFile processFile(Path path, Path parentAbstractPath) throws IOException {
		
		// get general file
		CommonFile file = super.processFile(path, parentAbstractPath);
		String name = file.getName();
		
		// is jar file?
		if(file.isFile() && file.isJar()) {
					
			// create temporary file
			Path tmp = Files.createTempDirectory(name);
						
			// extract files
			JarExtractor.extract(path, tmp);
				
			// return recreated file
			return new CommonFile(tmp, file.getAbstractPath());				
		}
		
		return file;
	}
	
}
