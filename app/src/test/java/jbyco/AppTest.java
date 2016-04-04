package jbyco;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;


public class AppTest extends jbyco.Test{
	
	
	@Test
	public void shouldGetPath() {
		String path = this.getPath("jbycotest/Test1.java");
		assertNotNull(path);
	}

	@Test(expected=AssertionError.class)
	public void shouldNotGetPath() {
		this.getPath("doesnotexist.java");		
	}
	
	@Test
	public void shouldAcceptParams1() {
		String path = this.getPath("jbycotest/Test1.java");
		String[] args = {path};
		//App.main(args);		
	}
	

	
}
