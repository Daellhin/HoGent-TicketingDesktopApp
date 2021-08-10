package util;

import java.util.Map;
import java.util.stream.Stream;

public class Util {

	public static <K, V> Stream<K> keysOfValue(Map<K, V> map, V value) {
		return map.entrySet().stream().filter(entry -> value.equals(entry.getValue())).map(Map.Entry::getKey);
	}

	public static <K, V> K firstKeyOfValue(Map<K, V> map, V value) {
		return keysOfValue(map, value).findFirst().get();
	}

}
