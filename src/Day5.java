import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day5 {
    public static void main(String[] args) throws IOException {
        String[] input = InputUtil.readAsString("input5.txt").split("\n\n");
        List<Range> ranges = Arrays.stream(input[0].split("\n")).map(Range::new).toList();
        List<Long> values = Arrays.stream(input[1].split("\n")).map(Long::parseLong).toList();
        first(values, ranges);
        second(new ArrayList<>(ranges));
    }

    private static void first(List<Long> values, List<Range> ranges) {
        long count = values.stream().filter(v -> inAnyRange(ranges, v)).count();
        System.out.println(count);
    }

    private static void second(List<Range> ranges) {
        ranges.sort(Comparator.comparing(Range::start).thenComparing(Range::end));
        for (int i = 0; i < ranges.size() - 1; i++) {
            Range current = ranges.get(i);
            Range next = ranges.get(i + 1);
            if (current.inside(next.start())) {
                ranges.set(i, new Range(current.start(), Math.max(current.end(), next.end())));
                ranges.remove(i + 1);
                i--;
            }
        }

        long count = ranges.stream().mapToLong(Range::size).sum();
        System.out.println(count);
    }

    private static boolean inAnyRange(List<Range> ranges, long value) {
        return ranges.stream().anyMatch(range -> range.inside(value));
    }

    private record Range(long start,long end) {
        public Range(String s) {
            String[] sp = s.split("-");
            this(Long.parseLong(sp[0]), Long.parseLong(sp[1]));
        }

        public boolean inside(long value) {
            return start <= value && value <= end;
        }

        public long size() {
            return end - start + 1L;
        }
    }
}
