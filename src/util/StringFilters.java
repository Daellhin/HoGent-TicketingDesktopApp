package util;

/**
 * Util class voor strings
 * 
 * @author lorin
 *
 */
public class StringFilters {

	public static String numeric(String value) {
		if (!value.matches("\\d*")) {
			return value.replaceAll("[^\\d]", "");
		}
		return value;
	}

	public static String maxLength(String value, int maxLength) {
		if (value.length() > maxLength) {
			return value.substring(0, Math.min(value.length(), maxLength));
		}
		return value;
	}

}
