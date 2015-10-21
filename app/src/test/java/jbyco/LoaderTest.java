package jbyco;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class LoaderTest extends jbyco.Test{
	
	@Test
	public void shouldLoadJavaFile() throws IOException {
		String path = this.getPath("jbycotest/Test1.java");
		Loader loader = new Loader();
		loader.load(path);
	}
	
	@Test
	public void shouldGetExtension(){
		Loader loader = new Loader();
		String extension = loader.getExtension("Test.java");
		assertEquals(extension, "java");
	}

	@Test
	public void shouldGetLastExtension(){
		Loader loader = new Loader();
		String extension = loader.getExtension("Test.tar.gz");
		assertEquals(extension, "gz");
	}
	
	@Test
	public void shouldNotGetExtension(){
		Loader loader = new Loader();
		String extension = loader.getExtension("Test");
		assertEquals(extension, "");
	}

	
}
