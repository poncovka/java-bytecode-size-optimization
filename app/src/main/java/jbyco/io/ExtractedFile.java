package jbyco.io;

import java.nio.file.Path;

public class ExtractedFile extends CommonFile {

	Path abstracted;
	
	public ExtractedFile(Path path, Path abstracted) {
		super(path);
		this.abstracted = abstracted;
	}

	public Path getAbstractedPath() {
		return abstracted;
	}
	
	@Override
	public String getName() {
		return abstracted.toFile().getName();
	}
	
	
}
