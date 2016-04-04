package jbyco.io.files;

import java.io.IOException;
import java.io.InputStream;

public interface BytecodeFile extends AbstractFile {
	
	public InputStream getInputStream() throws IOException;
	public void close() throws IOException;
	public String getName();

}
