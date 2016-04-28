package jbyco.lib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * A library of useful functions.
 */
public class Utils {
	
	/**
	 * Convert division of two integers to the string.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the string
	 */
	public static String intDivToString(int x, int y) {
		return (y == 0) ? "0" : Integer.toString(x/y);
	}
	
	/**
	 * Convert division of two doubles to the string.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the string
	 */
	public static String doubleDivToString(int x, int y) {
		
		// set format
		DecimalFormat format = new DecimalFormat("#.###");
		
		// set format symbols
		format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
		
		// return string
		return (y == 0) ? "0" : format.format(x * 1.0 / y); 
	}
	
	/**
	 * Convert an array to the string.
	 *
	 * @param <T> the generic type
	 * @param array the array
	 * @param delimiter the delimiter
	 * @return the string
	 */
	public static <T> String arrayToString(T[] array, String delimiter) {
		
		StringBuilder builder = new StringBuilder();
		
		// first item
		if (0 < array.length) {
			builder.append(array[0]);
		}
		
		// other items separated by delimiter
		for (int i = 1; i < array.length; i++) {
			builder.append(delimiter);
			builder.append(array[i]);
		}
				
		return builder.toString();
	}
	
	/**
	 * Gets the escaped string.
	 *
	 * @param string the string
	 * @param quoted the string appended to the begin and end of the escaped string
	 * @return the escaped string
	 */
	public static String getEscapedString(String string, String quoted) {
		return quoted + StringEscapeUtils.escapeJava(string) + quoted;
	}
	
	/**
	 * Ends with class.
	 *
	 * @param s the string
	 * @return true, if successful
	 */
	public static boolean endsWithClass(String s) {
		return s.endsWith(".class");
	}
	
	/**
	 * Ends with jar.
	 *
	 * @param s the string
	 * @return true, if successful
	 */
	public static boolean endsWithJar(String s) {
		return s.endsWith(".jar");
	}
	
	/**
	 * To byte array.
	 *
	 * @param in the input
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] toByteArray(InputStream in) throws IOException {
		
		// set the length of the buffer
		final int lenght = 1024;
		
		// init
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[lenght];
		int n = 0;
		
		// read input stream
		while ((n = in.read(buffer, 0, lenght)) != -1) {
			out.write(buffer, 0, n);
		}
		
		// get byte array
		out.flush();
		return out.toByteArray();		
	}
	
	public static boolean isMathInteger(double x) {
		return (x % 1) == 0;
	}
}
