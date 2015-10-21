package jbyco;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

public abstract class Test {
	
	public String getPath(String filename) {
		ClassLoader classLoader = getClass().getClassLoader();
		
		URL url = classLoader.getResource(filename);
		assertNotNull("The resource does not exist.",url);
		
		String path = url.getPath();
		assertNotNull("The path of the resource was not found.", path);
		
		return path;
	}
	
}
