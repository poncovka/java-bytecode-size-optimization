package jbyco.lib;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.apache.commons.lang3.StringEscapeUtils;

public class Utils {
	
	public static String intDivToString(int x, int y) {
		return (y == 0) ? "0" : Integer.toString(x/y);
	}
	
	public static String doubleDivToString(int x, int y) {
		
		// set format
		DecimalFormat format = new DecimalFormat("#.###");
		
		// set format symbols
		format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
		
		// return string
		return (y == 0) ? "0" : format.format(x * 1.0 / y); 
	}
	
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
	
	public static String getEscapedString(String string, String quoted) {
		return quoted + StringEscapeUtils.escapeJava(string) + quoted;
	}
}
