package at.redeye.FrameWork.utilities;

import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class StringUtils {

	private static int defaultAutoLineLength = 40;

	private static boolean contains(char c, String what) {
		return what.indexOf(c) >= 0;
	}

	private static int skip_char(String s, String what) {
		for (int pos = 0; pos < s.length(); ++pos) {

			char c = s.charAt(pos);

			if (!contains(c, what)) {
				return pos;
			}
		}

		return s.length();
	}

	private static int skip_char_reverse(String s, String what, int pos) {
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

	public static String strip(String s, String what) {
		int start = skip_char(s, what);
		int end = skip_char_reverse(s, what, s.length() - 1);

		if (start > end)
			return s.substring(start);

		return s.substring(start, end + 1);
	}

	public static String strip_post(String s, String what) {
		int end = skip_char_reverse(s, what, s.length() - 1);

		return s.substring(0, end + 1);
	}

	public static void set_defaultAutoLineLenght(int length) {
		defaultAutoLineLength = length;
	}

	public static String autoLineBreak(String what) {
		return autoLineBreak(what, defaultAutoLineLength);
	}

	public static String autoLineBreak(String what, int length) {
		String[] res = autoLineBreak_int(what, length).split("\n");
		return Arrays.stream(res)
				.map(String::trim)
				.collect(joining("\n"));
	}

	private static String autoLineBreak_int(String what, int length) {

		final char[] myPreferedSigns = { ';', '.', ',', '!', '?', '>', '-' };
		final char[] mySpaceSigns = { ' ', '\t' };

		final int searchWindowLengthPreferedSigns = 10;
		final int searchWindowLengthSpaceSigns = 50;

		if (searchWindowLengthPreferedSigns > length || length >= what.length()) {
			// Doesn't make sense
			return what;
		}

		char[] in = what.toCharArray();
		StringBuilder str = new StringBuilder();

		for (int walker = 1; walker < in.length; walker++) {
			if (walker % length == 0) {
				// try to find a sign in a search window
				boolean found = false;
				for (char myPreferedSign : myPreferedSigns) {
					// try with preferred signs
					for (int index = 1; index <= searchWindowLengthPreferedSigns; index++) {

						if ((walker + index + 1) >= in.length
								|| (walker - index) <= 0) {
							break; // not enough lefts
						}

						if (in[walker + index] == myPreferedSign) {
							str.append(new String(in, 0, walker + index + 1));
							str.append("\n");

							walker += 1; // jump over break sign

							// spaces überspringen
							for (char mySpaceSign : mySpaceSigns) {
								if (in[walker + 1] == mySpaceSign) {
									walker++;
								}
							}

							String rest = new String(in, walker + index,
									in.length - (walker + index));
							in = rest.toCharArray();
							walker = 0;
							found = true;
							break;
						}
						if (in[walker - index] == myPreferedSign) {
							str.append(new String(in, 0, walker - index + 1));
							str.append("\n");

							walker += 1; // jump over break sign

							for (char mySpaceSign : mySpaceSigns) {
								if (in[walker + 1] == mySpaceSign) {
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

				for (char mySpaceSign : mySpaceSigns) {
					// try with blanks
					for (int index = 1; index <= (searchWindowLengthSpaceSigns / 2); index++) {

						if ((walker + index + 1) >= in.length
								|| (walker - index) <= 0) {
							break; // not enough lefts
						}

						if (in[walker + index] == mySpaceSign) {

							str.append(new String(in, 0, walker + index));
							str.append("\n");
							walker++;
							String rest = new String(in, walker + index,
									in.length - (walker + index));
							in = rest.toCharArray();
							walker = 0;
							found = true;
							break;
						}
						if (in[walker - index] == mySpaceSign) {

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
	 * Finds out if the given string has the meaning of 'Yes'
	 *
	 * @return true, false
	 */
	public static boolean isYes(String maybe_a_yes_value) {
		return Stream.of("ja", "yes", "true", "1", "x", "+")
				.anyMatch(anotherString -> anotherString.equalsIgnoreCase(maybe_a_yes_value));
	}

	/**
	 * Adds Line numbers as prefix to the text
	 *
	 * @return Line numbered String
	 */
	public static String addLineNumbers(String text) {
		final String[] lines = text.split("\n");

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

    public static String limitLength(String data, int max) {
        return data.length() > max ? data.substring(0, max) + "…" : data;
    }
}
