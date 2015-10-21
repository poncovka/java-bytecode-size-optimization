package jbycotest;

public class Test2 {
	
	int method1(int x, int y) {
		
		int result = 0;
		
		for (int i = 0; i < 10 ; i++) {
			result = (x * y) - i;			
		}
		
		return result;
	}

}
