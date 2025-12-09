import lib.CartesianProductUtil;
import lib.InputUtil;
import lib.Position;

import java.io.IOException;
import java.util.List;

public class Day9 {
    public static void main(String[] args) throws IOException {
        List<Position> positions = InputUtil.readAsLines("input9.txt").stream().map(Day9::positionFromLine).toList();
        long first = CartesianProductUtil.cartesianProductStream(2, positions).mapToLong(Day9::rectArea).max().orElseThrow();
        System.out.println(first);
    }

    private static Position positionFromLine(String s) {
        String[] sp = s.split(",");
        return new Position(Integer.parseInt(sp[0]), Integer.parseInt(sp[1]));
    }

    private static long rectArea(List<Position> positions) {
        Position a = positions.get(0);
        Position b = positions.get(1);
        long w = 1 + Math.abs(a.x() - b.x());
        long h = 1 + Math.abs(a.y() - b.y());
        return w * h;
    }
}
