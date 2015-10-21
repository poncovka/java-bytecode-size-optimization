package jbycotest;

public class Test1 {
	int a = 100;
	int b = 100;
	
	int method1(int x, int y) {
		return x + y;
	}

	int method2(int x, int y) {
		return this.a + this.b;
	}

	String method3(int x, int y) {
		String a = "Hello";
		String b = "world!";
		String c = a + " " + b;
		return c;
		
	}
	
}
