package util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit util for {@link DurationParser}.
 */
public class DurationParserTest {
	/**
	 * Used for readability in other constants.
	 */
	private static final int SECONDS = 1;

	/**
	 * Number of seconds in a minute.
	 */
	private static final int MINUTES = 60 * SECONDS;

	/**
	 * Number of seconds in an hour.
	 */
	private static final int HOURS = 60 * MINUTES;

	/**
	 * Number of seconds in a day.
	 */
	private static final int DAYS = 24 * HOURS;

	/**
	 * The exception rule.
	 */
	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	/**
	 * Test a variety of standard integer formats return the correct time.
	 *
	 * @throws ParseException an unexpected exception
	 */
	@Test
	public void testDurationParsingWithIntegerFormats() throws ParseException {
		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("2d"), 2 * DAYS);
		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("2d 4h"), 2 * DAYS + 4 * HOURS);
		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("2d 4h 30m"), 2 * DAYS + 4 * HOURS + 30 * MINUTES);
		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("2d 4h 30m 20s"), 2 * DAYS + 4 * HOURS + 30 * MINUTES + 20 * SECONDS);

		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("4h"), 4 * HOURS);
		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("4h 30m"), 4 * HOURS + 30 * MINUTES);
		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("4h 30m 20s"), 4 * HOURS + 30 * MINUTES + 20 * SECONDS);

		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("30m"), 30 * MINUTES);
		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("30m 20s"), 30 * MINUTES + 20 * SECONDS);

		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("4h 20s"), 4 * HOURS + 20 * SECONDS);
		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("30m"), 30 * MINUTES);
		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("20s"), 20 * SECONDS);
	}

	/**
	 * Test a variety of standard decimal formats return the correct time.
	 *
	 * @throws ParseException an unexpected exception
	 */
	@Test
	public void testDurationParsingWithDecimalFormats() throws ParseException {
		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("2.5d"), Math.round(2.5 * DAYS));
		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("2.5d 4.3h"), Math.round(2.5 * DAYS) + Math.round(4.3 * HOURS));
		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("2.5d 4.3h 0.8m"), Math.round(2.5 * DAYS) + Math.round(4.3 * HOURS) + Math.round(0.8 * MINUTES));
		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("2.5d 4.3h 0.8m 0.5s"), Math.round(2.5 * DAYS) + Math.round(4.3 * HOURS) + Math.round(0.8 * MINUTES) + Math.round(0.5 * SECONDS));
	}

	/**
	 * Test a format with mixed whitespace parses successfully.
	 *
	 * @throws ParseException an unexpected exception
	 */
	@Test
	public void testDurationParsingWithMixedWhitespace() throws ParseException {
		assertEquals("Unexpected duration.", DurationParser.parseToSeconds("  2.5d   4.3h8m  "), Math.round(2.5 * DAYS) + Math.round(4.3 * HOURS) + 8 * MINUTES);
	}

	/**
	 * Test that a duration with a missing suffix throws a {@link ParseException}.
	 */
	@Test
	public void testDurationParsingWithMissingSuffix() {
		try {
			DurationParser.parseToSeconds("5");
			fail("Expected a ParseException to be thrown.");
		} catch (final ParseException e) {
			assertEquals("Unexpected ParseException error offset.", 1, e.getErrorOffset());
		}
	}

	/**
	 * Test that a duration with a missing suffix in the middle of the string throws a {@link ParseException}.
	 */
	@Test
	public void testDurationParsingWithPartiallyCorrectString() {
		try {
			DurationParser.parseToSeconds("4.5d 8. 2m");
			fail("Expected a ParseException to be thrown.");
		} catch (final ParseException e) {
			assertEquals("Unexpected ParseException error offset.", 7, e.getErrorOffset());
		}
	}

	/**
	 * Test that a duration with a non-existent suffix throws a {@link ParseException}.
	 */
	@Test
	public void testDurationParsingWithNonExistentSuffix() {
		try {
			DurationParser.parseToSeconds("2m 4z 6h");
			fail("Expected a ParseException to be thrown.");
		} catch (final ParseException e) {
			assertEquals("Unexpected ParseException error offset.", 4, e.getErrorOffset());
			assertEquals("Unexpected ParseException message.", "Invalid suffix 'z'.", e.getLocalizedMessage());
		}
	}

	/**
	 * Test that a duration with an empty string throws a {@link ParseException}.
	 *
	 * @throws ParseException an expected exception
	 */
	@Test
	public void testDurationParsingWithEmptyString() throws ParseException {
		expectedException.expect(ParseException.class);
		expectedException.expectMessage("Unable to parse number.");
		DurationParser.parseToSeconds("");
	}
}
