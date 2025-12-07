import lib.CharMatrix;
import lib.CounterLong;
import lib.InputUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Day7 {
    public static void main(String[] args) throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input7.txt"));
        first(charMatrix);
        second(charMatrix);
    }

    private static void first(CharMatrix charMatrix) {
        CharMatrix.Cell start = charMatrix.find('S').orElseThrow();
        int y = start.getY();
        Set<Integer> xset = Set.of(start.getX());
        int splits = 0;
        for (; y < charMatrix.getHeight() - 1; y++) {
            Set<Integer> nextxset = new HashSet<>();
            for (int x : xset) {
                if (charMatrix.get(x, y + 1) == '^') {
                    splits++;
                    nextxset.add(x - 1);
                    nextxset.add(x + 1);
                } else {
                    nextxset.add(x);
                }
            }
            xset = nextxset;
        }
        System.out.println(splits);
    }

    private static void second(CharMatrix charMatrix) {
        CharMatrix.Cell start = charMatrix.find('S').orElseThrow();
        int y = start.getY();
        CounterLong<Integer> xcounter = new CounterLong<>();
        xcounter.add(start.getX(), 1);
        for (; y < charMatrix.getHeight() - 1; y++) {
            CounterLong<Integer> nextxcounter = new CounterLong<>();
            for (var entry : xcounter.entrySet()) {
                int x = entry.getKey();
                long count = entry.getValue();
                if (charMatrix.get(x, y + 1) == '^') {
                    nextxcounter.add(x - 1, count);
                    nextxcounter.add(x + 1, count);
                } else {
                    nextxcounter.add(x, count);
                }
            }
            xcounter = nextxcounter;
        }
        System.out.println(xcounter.values().stream().mapToLong(x -> x).sum());
    }
}
