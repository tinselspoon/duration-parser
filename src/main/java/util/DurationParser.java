package util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;

/**
 * Converts duration strings containing a mixture of time units with suffixes, e.g. "2h 30m", into a total number of seconds.
 */
public class DurationParser {

	/**
	 * Private constructor.
	 */
	private DurationParser() {
	}

	/**
	 * Used for readability in other constants.
	 */
	private static final int seconds = 1;

	/**
	 * Number of seconds in a minute.
	 */
	private static final int minutes = 60 * seconds;

	/**
	 * Number of seconds in an hour.
	 */
	private static final int hours = 60 * minutes;

	/**
	 * Number of seconds in a day.
	 */
	private static final int days = 24 * hours;

	/**
	 * Parses the specified duration string to a total number of seconds.
	 * The string must be numbers followed by a suffix, separated by zero or more whitespace characters, e.g. "2h 30m".
	 * Allowed suffixes are 'd', 'h', 'm' and 's'.
	 * Decimals are supported, but the final result is rounded to the nearest second.
	 *
	 * @param duration a duration string to convert
	 * @return the total number of seconds represented by the duration string
	 * @throws ParseException if the input string could not be successfully parsed
	 */
	public static long parseToSeconds(String duration) throws ParseException {

		long total = 0;
		NumberFormat nf = NumberFormat.getInstance();
		ParsePosition parsePosition = new ParsePosition(0);

		// Consume any starting whitespace of the string
		consumeWhitespace(duration, parsePosition);

		do {
			// Parse the number
			Number numberOb = nf.parse(duration, parsePosition);
			if (numberOb == null)
				throw new ParseException("Unable to parse number.", parsePosition.getIndex());
			double number = numberOb.doubleValue();

			// Extract the suffix
			if (duration.length() <= parsePosition.getIndex())
				throw new ParseException("Number '" + number + "' must be followed by a suffix.", parsePosition.getIndex());
			char suffix = duration.charAt(parsePosition.getIndex());

			// Apply the appropriate multiplication
			switch (suffix) {
				case 'd':
					total += Math.round(number * days);
					break;
				case 'h':
					total += Math.round(number * hours);
					break;
				case 'm':
					total += Math.round(number * minutes);
					break;
				case 's':
					total += Math.round(number * seconds);
					break;
				default:
					throw new ParseException("Invalid suffix '" + suffix + "'.", parsePosition.getIndex());
			}

			// Advance and consume whitespace
			parsePosition.setIndex(parsePosition.getIndex() + 1);
			consumeWhitespace(duration, parsePosition);

		} while (parsePosition.getIndex() < duration.length());

		return total;
	}

	/**
	 * Advance the parsePosition object to consume all whitespace characters from the current position.
	 *
	 * @param s             the string to inspect
	 * @param parsePosition the current parse position identifying the index from which to start consuming
	 */
	private static void consumeWhitespace(String s, ParsePosition parsePosition) {
		while (parsePosition.getIndex() < s.length() && Character.isWhitespace(s.charAt(parsePosition.getIndex())))
			parsePosition.setIndex(parsePosition.getIndex() + 1);
	}
}
