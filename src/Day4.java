import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;

public class Day4 {
    static void main() throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input4.txt"), '.');
        first(charMatrix);
        second(charMatrix);
    }

    private static void first(CharMatrix charMatrix) {
        int count = markWithX(charMatrix);
        System.out.println(count);
    }

    private static void second(CharMatrix charMatrix) {
        int count = 0;
        while (true) {
            int delta = markWithX(charMatrix);
            if (delta == 0) {
                break;
            }
            count += delta;
            for (var cell : charMatrix) {
                if (cell.get() == 'x') {
                    cell.set('.');
                }
            }
        }
        System.out.println(count);
    }

    private static int markWithX(CharMatrix charMatrix) {
        int count = 0;
        for (var cell : charMatrix) {
            if (cell.get() != '.') {
                int rolls = 0;
                for (var p : cell.getNeighboursIncludingDiagonal()) {
                    if (p.getUnbounded() != '.') {
                        rolls++;
                    }
                }
                if (rolls < 4) {
                    count++;
                    cell.set('x');
                }
            }
        }
        return count;
    }
}
