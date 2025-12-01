import lib.InputUtil;

import java.io.IOException;
import java.util.List;

public class Day1 {
    static void main() throws IOException {
        List<String> input = InputUtil.readAsLines("input1.txt");
        first(input);
        second(input);
    }

    private static void first(List<String> input) {
        int dial = 50;
        int count = 0;
        for (String line : input) {
            int delta = Integer.parseInt(line.replace('L', '-').replace("R", ""));
            dial = Math.floorMod(dial + delta, 100);
            if (dial == 0) {
                count++;
            }
        }
        System.out.println(count);
    }

    private static void second(List<String> input) {
        int dial = 50;
        int count = 0;
        for (String line : input) {
            int delta = Integer.parseInt(line.replace('L', '-').replace("R", ""));
            if (delta < 0) {
                for (int i = 0; i < -delta; i++) {
                    dial = Math.floorMod(dial - 1, 100);
                    if (dial == 0) {
                        count++;
                    }
                }
            } else {
                for (int i = 0; i < delta; i++) {
                    dial = Math.floorMod(dial + 1, 100);
                    if (dial == 0) {
                        count++;
                    }
                }
            }
        }
        System.out.println(count);
    }
}
