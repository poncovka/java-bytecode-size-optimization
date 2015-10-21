package jbyco;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Loader {

	protected String getExtension(String filename) {
		String extension = "";
		int i = filename.lastIndexOf(".");
		
		if (i >= 0) {
			extension = filename.substring(i + 1);
		}
		
		return extension;
	}
	
	void load(String filename) throws IOException {
		File file = new File(filename);
		
		// file is a directory
		if (file.isDirectory()) {
			
			System.out.println("Directory: " + file.getName());
			
			// walk the files in a directory
			DirectoryStream<Path> dir = Files.newDirectoryStream(file.toPath());
			for (Path path:dir) {
				// load new file
				this.load(path.toString());
			}
			
		}
		// file is a file
		else if (file.isFile()) {
			System.out.print("File: " + file.getName());
			
			String extension = this.getExtension(file.getName());
			switch(extension) {
				case "java"	: System.out.print(", java"); break;
				case "class": System.out.print(", class");break;
				case "jar"	: System.out.print(", jar");break;
				default		: break;
			}
			
			System.out.println();
		}
		// file does not exist
		else {
			System.out.println("File " + file.getName() + " does not exist.");
		}
	}
	
	

}
