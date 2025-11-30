package lib;

import java.util.Collection;
import java.util.Map;

public class CollectionUtil {
    private CollectionUtil() {
    }

    public static <T> Counter<T> calculateHistogram(Collection<T> input) {
        Counter<T> histogram = new Counter<>();
        for (T value : input) {
            histogram.inc(value);
        }
        return histogram;
    }

    public static <T> T leastCommonValue(Collection<T> input) {
        Counter<T> histogram = calculateHistogram(input);
        return histogram.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow();
    }

    public static <T> T mostCommonValue(Collection<T> input) {
        Counter<T> histogram = calculateHistogram(input);
        return histogram.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow();
    }
}
