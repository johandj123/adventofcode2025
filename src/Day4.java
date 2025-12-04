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
            for (int y = 0; y < charMatrix.getHeight(); y++) {
                for (int x = 0; x < charMatrix.getWidth(); x++) {
                    CharMatrix.Position position = charMatrix.new Position(x, y);
                    if (position.get() == 'x') {
                        position.set('.');
                    }
                }
            }
        }
        System.out.println(count);
    }

    private static int markWithX(CharMatrix charMatrix) {
        int count = 0;
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                CharMatrix.Position position = charMatrix.new Position(x, y);
                if (position.get() != '.') {
                    int rolls = 0;
                    for (var p : position.getNeighboursIncludingDiagonal()) {
                        if (p.getUnbounded() != '.') {
                            rolls++;
                        }
                    }
                    if (rolls < 4) {
                        count++;
                        position.set('x');
                    }
                }
            }
        }
        return count;
    }
}
