import lib.InputUtil;

import java.io.IOException;
import java.util.List;

public class Day3 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input3.txt");
        int first = input.stream().map(Day3::joltage2).reduce(0, Integer::sum);
        System.out.println(first);
        long second = input.stream().map(Day3::joltage12).reduce(0L, Long::sum);
        System.out.println(second);
    }

    private static int joltage2(String s) {
        int max = 0;
        for (int i = 0; i < s.length() - 1; i++) {
            int d1 = 10 * (s.charAt(i) - '0');
            for (int j = i + 1; j < s.length(); j++) {
                int d2 = s.charAt(j) - '0';
                int d = d1 + d2;
                max = Math.max(max, d);
            }
        }
        return max;
    }

    private static long joltage12(String s) {
        StringBuilder sb = new StringBuilder();
        int lastIndex = -1;
        for (int ii = 0; ii < 12; ii++) {
            int max = -1;
            int maxIndex = -1;
            for (int i = lastIndex + 1; i < s.length() - (11 - ii); i++) {
                int d = s.charAt(i) - '0';
                if (d > max) {
                    max = d;
                    maxIndex = i;
                }
            }
            sb.append((char)(max + '0'));
            lastIndex = maxIndex;
        }
        return Long.parseLong(sb.toString());
    }
}
