package jbyco;

import java.io.IOException;

public class App {

	public static void main(String[] args) throws IOException {
		System.out.println("Arguments: ");
		for(String arg:args) {
			System.out.println(arg);
		}
		System.out.println();
		
		
		if (args.length > 0) {
			String filename = args[0];
			Loader loader = new Loader();
			loader.load(filename);
		}

	}

}
