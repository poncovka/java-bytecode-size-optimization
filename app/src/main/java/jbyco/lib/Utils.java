package jbyco.lib;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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
	
}
