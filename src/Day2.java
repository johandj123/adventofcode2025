import lib.InputUtil;

import java.io.IOException;
import java.util.function.Predicate;

public class Day2 {
    public static void main(String[] args) throws IOException {
        String[] input = InputUtil.readAsString("input2.txt").split(",");
        count(input, Day2::isInvalidFirst);
        count(input, Day2::isInvalidSecond);
    }

    private static void count(String[] input, Predicate<Long> invalid) {
        long sum = 0;
        for (String line : input) {
            String[] range = line.split("-");
            long start = Long.parseLong(range[0]);
            long end = Long.parseLong(range[1]);
            for (long i = start; i <= end; i++) {
                if (invalid.test(i)) {
                    sum += i;
                }
            }
        }
        System.out.println(sum);
    }

    private static boolean isInvalidFirst(long i) {
        String s = Long.toString(i);
        return (s.length() % 2) == 0 && s.substring(0, s.length() / 2).equals(s.substring(s.length() / 2));
    }

    private static boolean isInvalidSecond(long i) {
        String s = Long.toString(i);
        int l = s.length();
        for (int p = 1; p < l; p++) {
            if ((l % p) == 0) {
                String part = s.substring(0, p);
                StringBuilder sb = new StringBuilder(part);
                while (sb.length() < s.length()) {
                    sb.append(part);
                }
                if (sb.toString().equals(s)) {
                    return true;
                }
            }
        }
        return false;
    }
}
