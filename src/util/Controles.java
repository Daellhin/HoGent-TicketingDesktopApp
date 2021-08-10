package util;

/**
 * Util class voor controles
 * 
 * @author lorin
 *
 */
public final class Controles {

	// -- strings --
	public static void stringNotEmpty(String value, String message) throws IllegalArgumentException {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void stringBetween(String value, int minLength, int maxLength, String message)
			throws IllegalArgumentException {
		if (value.length() < minLength || value.length() > maxLength) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void stringMinimum(String value, int minLength, String message) throws IllegalArgumentException {
		stringNotEmpty(value, message);
		stringBetween(value, minLength, Integer.MAX_VALUE, message);
	}

	public static void stringOnlyLetters(String value, String message) throws IllegalArgumentException {
		stringNotEmpty(value, message);
		if (!value.matches("[a-zA-Z]*")) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void stringOnlyNumbers(String value, String message) throws IllegalArgumentException {
		stringNotEmpty(value, message);
		if (!value.matches("^[0-9]+$")) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void stringAlphaNumeric(String value, String message) throws IllegalArgumentException {
		if (!value.matches("[a-zA-Z0-9]*")) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void stringAlphaNumericAllowsNull(String value, String message) throws IllegalArgumentException {
		if (value != null) {
			stringAlphaNumeric(value, message);
		}
	}

	public static void stringContainsLetters(String value, String message) throws IllegalArgumentException {
		stringNotEmpty(value, message);
		if (!value.matches(".*[a-zA-Z]*")) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void stringTelephoneNumber(String value, String message) throws IllegalArgumentException {
		stringNotEmpty(value, message);
		if (!value.matches("\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|\r\n"
				+ "2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|\r\n"
				+ "4[987654310]|3[9643210]|2[70]|7|1)\\d{7,14}$")) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void stringEmail(String value, String message) throws IllegalArgumentException {
		stringNotEmpty(value, message);
		if (!value.matches(
				"^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Source: https://stackoverflow.com/a/25677072/8807613
	 */
	public static void stringCity(String value, String message) throws IllegalArgumentException {
		stringNotEmpty(value, message);
		if (!value.matches("^([a-zA-Z\u0080-\u024F]+(?:. |-| |'))*[a-zA-Z\u0080-\u024F]*$")) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void stringBigDecimal(String value, String message) throws IllegalArgumentException {
		stringNotEmpty(value, message);
		if (!value.matches("^(\\d*\\.)?\\d+$")) {
			throw new IllegalArgumentException(message);
		}
	}

}
