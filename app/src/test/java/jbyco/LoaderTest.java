package jbyco;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class LoaderTest extends jbyco.Test{
	
	@Test
	public void shouldLoadJavaFile() throws IOException {
		String path = this.getPath("jbycotest/Test1.java");
		InputLoader loader = new InputLoader();
		loader.load(path);
	}
	
	@Test
	public void shouldGetExtension(){
		InputLoader loader = new InputLoader();
		String extension = loader.getExtension("Test.java");
		assertEquals(extension, "java");
	}

	@Test
	public void shouldGetLastExtension(){
		InputLoader loader = new InputLoader();
		String extension = loader.getExtension("Test.tar.gz");
		assertEquals(extension, "gz");
	}
	
	@Test
	public void shouldNotGetExtension(){
		InputLoader loader = new InputLoader();
		String extension = loader.getExtension("Test");
		assertEquals(extension, "");
	}

	
}
