/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.utilities;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * @author martin
 */
public class StringUtils {

	private static int defaultAutoLineLength = 40;

	static boolean contains(char c, String what) {
		return what.indexOf(c) >= 0;
	}

	static int skip_char(StringBuilder s, String what, int pos) {
		while (pos <= (s.length() - 1)) {
			char c;

			c = s.charAt(pos);

			if (contains(c, what)) {
				pos++;
				continue;
			}

			break;
		}

		return pos;
	}

	static int skip_char_reverse(StringBuilder s, String what, int pos) {
		while (pos > 0) {
			char c;

			c = s.charAt(pos);

			if (contains(c, what)) {
				pos--;
				continue;
			}

			break;
		}

		return pos;
	}

	public static int skip_spaces_reverse(StringBuilder s, int pos) {
		return skip_char_reverse(s, " \t\n\r", pos);
	}

	public static int skip_spaces(StringBuilder s, int pos) {
		return skip_char(s, " \t\n\r", pos);
	}

	public static boolean is_space(char c) {
		if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
			return true;
		}

		return false;
	}

	public static List<String> split_str(StringBuilder s, String c) {

		List<String> res = new ArrayList<String>();

		int start = 0;
		do {
			int pos = s.indexOf(c, start);

			if (pos < 0) {
				res.add(s.substring(start));
				break;
			}

			res.add(s.substring(start, pos));

			start = pos + 1;

		} while (start > 0);

		return res;
	}

	public static String strip(StringBuilder s, String what) {
		int start = skip_char(s, what, 0);
		int end = skip_char_reverse(s, what, s.length() - 1);

		if (start > end)
			return s.substring(start);

		return s.substring(start, end + 1);
	}

	public static String strip_post(StringBuilder s, String what) {
		int end = skip_char_reverse(s, what, s.length() - 1);

		return s.substring(0, end + 1);
	}

	public static String strip_post(String str, String what) {

		if (str == null) {
			throw new NullPointerException();
		}

		StringBuilder s = new StringBuilder();
		s.append(str);
		return strip_post(s, what);
	}

	public static String strip(String s, String what) {
		return strip(new StringBuilder(s), what);
	}

	public static void set_defaultAutoLineLenght(int length) {
		defaultAutoLineLength = length;
	}

	public static int get_defaultAutoLineLenght() {
		return defaultAutoLineLength;
	}

	public static String autoLineBreak(String what) {
		return autoLineBreak(what, defaultAutoLineLength);
	}

	public static String autoLineBreak(StringBuilder what, int length) {
		return autoLineBreak(what.toString(), length);
	}

	public static String autoLineBreak(StringBuilder what) {
		return autoLineBreak(what.toString(), defaultAutoLineLength);
	}

	public static String autoLineBreak(String what, int length) {
		/**
		 * Diese Funktion löscht noch überschüssige Leerzeichen weg.
		 */

		String res[] = autoLineBreak_int(what, length).split("\n");

		StringBuilder stripped_string = new StringBuilder();

		for (String line : res) {
			if (stripped_string.length() > 0)
				stripped_string.append('\n');

			stripped_string.append(line.trim());
		}

		return stripped_string.toString();
	}

	private static String autoLineBreak_int(String what, int length) {

		final char[] myPreferedSigns = { ';', '.', ',', '!', '?', '>', '-' };
		final char[] mySpaceSigns = { ' ', '\t' };

		final int searchWindowLengthPreferedSigns = 20;
		final int searchWindowLengthSpaceSigns = 50;

		if (length < searchWindowLengthPreferedSigns / 2
				|| length >= what.length()
				|| searchWindowLengthPreferedSigns / 2 >= what.length()) {
			// Doesn't make sense
			return what;
		}

		char[] in = what.toCharArray();
		StringBuilder str = new StringBuilder();

		for (int walker = 1; walker < in.length; walker++) {

			if (in.length <= searchWindowLengthPreferedSigns / 2) {
				break;
			}

			if (walker % length == 0) {

				// System.out.println("Want to break at: " +
				// in[walker - 2] + in[walker - 1] + ">" +
				// in[walker] + "<" + in[walker + 1] + in[walker + 2]);

				// try to find a sign in search window
				boolean found = false;
				for (int signidx = 0; signidx < myPreferedSigns.length; signidx++) {

					// try with preferred signs
					for (int index = 1; index <= (searchWindowLengthPreferedSigns / 2); index++) {

						if ((walker + index + 1) >= in.length
								|| (walker - index) <= 0) {
							break; // not enough left
						}

						if (in[walker + index] == myPreferedSigns[signidx]) {
							str.append(new String(in, 0, walker + index + 1));
							str.append("\n");

							walker += 1; // jump over break sign

							// spaces überspringen
							for (int i = 0; i < mySpaceSigns.length; i++) {
								if (in[walker + 1] == mySpaceSigns[i]) {
									walker++;
								}
							}

							String rest = new String(in, walker + index,
									in.length - (walker + index));
							in = rest.toCharArray();
							walker = 0;
							found = true;
							break;
						} else if (in[walker - index] == myPreferedSigns[signidx]) {
							str.append(new String(in, 0, walker - index + 1));
							str.append("\n");

							walker += 1; // jump over break sign

							for (int i = 0; i < mySpaceSigns.length; i++) {
								if (in[walker + 1] == mySpaceSigns[i]) {
									walker++;
								}
							}

							String rest = new String(in, walker - index,
									in.length - (walker - index));
							in = rest.toCharArray();

							walker = 0;
							found = true;
							break;
						}
					}

					if (found) {
						break;
					}
				}

				for (int signidx = 0; signidx < mySpaceSigns.length; signidx++) {
					// try with blanks
					for (int index = 1; index <= (searchWindowLengthSpaceSigns / 2); index++) {

						if ((walker + index + 1) >= in.length
								|| (walker - index) <= 0) {
							break; // not enough left
						}

						if (in[walker + index] == mySpaceSigns[signidx]) {

							str.append(new String(in, 0, walker + index));
							str.append("\n");
							walker++;
							String rest = new String(in, walker + index,
									in.length - (walker + index));
							in = rest.toCharArray();
							walker = 0;
							found = true;
							break;
						} else if (in[walker - index] == mySpaceSigns[signidx]) {

							str.append(new String(in, 0, walker - index + 1));
							str.append("\n");
							walker++;
							String rest = new String(in, walker - index,
									in.length - (walker - index));
							in = rest.toCharArray();
							walker = 0;
							found = true;
							break;
						}

					}

					if (found) {
						break;
					}

				}

			}

		}
		str.append(in); // rest
		return str.toString();
	}

	/**
	 * converts a double into a string, by removing useless zeros from the end
	 * of the string eg:
	 * <ul>
	 * <li>12.2340000 => 12.234</li>
	 * <li>0.03 => 0.03</li>
	 * <li>12.000 => 12</li>
	 * </ul>
	 * 
	 * @param d
	 * @param rouding
	 *            precision
	 * @return
	 */
	public static String formatDouble(double d, int rounding) {
		return formatDouble(Rounding.rndDouble(d, rounding));
	}

	/**
	 * converts a double into a string, by removing useless zeros from the end
	 * of the string eg:
	 * <ul>
	 * <li>12.2340000 => 12.234</li>
	 * <li>0.03 => 0.03</li>
	 * <li>12.000 => 12</li>
	 * </ul>
	 * 
	 * @param d
	 *            the number
	 * @return
	 */
	public static String formatDouble(double d) {
		String s = String.format("%f", d);
		s = strip_post(s, "0");
		s = strip_post(s, ".");
		s = strip_post(s, ",");

		return s;
	}

	/**
	 * Finds out, if the given string has the meaning of 'Yes'
	 * 
	 * @param maybe_a_yes_value
	 * @return true, false
	 */
	public static boolean isYes(String maybe_a_yes_value) {
		if (maybe_a_yes_value == null) {
			return false;

		}
		if (maybe_a_yes_value.equalsIgnoreCase("ja")) {
			return true;

		}
		if (maybe_a_yes_value.equalsIgnoreCase("yes")) {
			return true;

		}
		if (maybe_a_yes_value.equalsIgnoreCase("true")) {
			return true;

		}
		if (maybe_a_yes_value.equalsIgnoreCase("1")) {
			return true;

		}
		if (maybe_a_yes_value.equalsIgnoreCase("x")) {
			return true;

		}
		if (maybe_a_yes_value.equalsIgnoreCase("+")) {
			return true;

		}
		return false;
	}

	/**
	 * Converts the complete Backtrace of an Exception into a String
	 * 
	 * @param ex
	 * @return Backtrace of the Exception
	 * @deprecated Use {@link Logger#error(Object, Throwable)} instead
	 */
	@Deprecated
	public static String exceptionToString(Exception ex) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		PrintStream s = new PrintStream(bos);
		ex.printStackTrace(s);
		s.flush();

		return bos.toString();
	}

	/**
	 * The method cuts off the requested number of lines from source string.<br>
	 * Processed are the indicaters is <b>\n<\b>
	 * 
	 * @param sourceString
	 *            The string that shall be processed.
	 * @param lines
	 *            That number of lines those shall be skipped.
	 * @return The rest of string afer the line cut off.
	 */
	public static String skipLeadingLines(String sourceString, int lines) {

		if (lines <= 0)
			return sourceString;

		String truncatedString;
		int lbCounter = 0;
		int idx = 0;

		char[] arr = sourceString.toCharArray();

		for (idx = 0; idx < arr.length; idx++) {
			if (lbCounter == lines) {
				break;
			}
			if (arr[idx] == '\n') {
				lbCounter++;
			}
		}

		truncatedString = String.valueOf(arr, idx, (arr.length - idx));
		return truncatedString;
	}

	/**
	 * Converts a byte array into its corresponding ASCII string
	 * 
	 * @param data
	 *            Input data
	 * @return the converted ASCII string
	 */
	public static String byteArrayToString(byte[] data) {

		if (data == null) {
			return "";
		}
		StringBuilder str = new StringBuilder();
		for (int index = 0; index < data.length; index++) {
			str.append((char) (data[index]));
		}
		return str.toString();
	}

	/**
	 * Adds Line numbers as prefix to the text
	 * 
	 * @param text
	 * @return Line numbered String
	 */
	public static String addLineNumbers(String text) {
		final String lines[] = text.split("\n");

		final String max_num = String.valueOf(lines.length);

		final String format = "%0" + max_num.length() + "d: ";

		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < lines.length; i++) {
			sb.append(String.format(format, i + 1));
			sb.append(lines[i]);
			sb.append('\n');
		}

		return sb.toString();
	}
}
