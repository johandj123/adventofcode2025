import lib.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Day9 {
    public static void main(String[] args) throws IOException {
        List<Position> positions = InputUtil.readAsLines("input9.txt").stream().map(Day9::positionFromLine).toList();
        first(positions);
        second(positions);
    }

    private static void first(List<Position> positions) {
        long maxarea = CartesianProductUtil.cartesianProductStream(2, positions).mapToLong(Day9::rectArea).max().orElseThrow();
        System.out.println(maxarea);
    }

    private static void second(List<Position> positions) {
        var xmap = toIndexMap(positions, Position::x);
        var ymap = toIndexMap(positions, Position::y);
        CharMatrix charMatrix = new CharMatrix(ymap.size(), xmap.size(), '.');
        for (int i = 0; i < positions.size(); i++) {
            Position a = positions.get(i);
            Position b = positions.get((i + 1) % positions.size());
            if (a.x() == b.x()) {
                int x = xmap.get(a.x());
                int y1 = Math.min(ymap.get(a.y()), ymap.get(b.y()));
                int y2 = Math.max(ymap.get(a.y()), ymap.get(b.y()));
                for (int y = y1; y <= y2; y++) {
                    charMatrix.set(x, y, '#');
                }
            } else if (a.y() == b.y()) {
                int y = ymap.get(a.y());
                int x1 = Math.min(xmap.get(a.x()), xmap.get(b.x()));
                int x2 = Math.max(xmap.get(a.x()), xmap.get(b.x()));
                for (int x = x1; x <= x2; x++) {
                    charMatrix.set(x, y, '#');
                }
            } else {
                throw new IllegalArgumentException();
            }
        }
        GraphUtil.visitReachable(charMatrix.new Cell(charMatrix.getWidth() / 2, charMatrix.getHeight() / 2 + 1),
                cell -> cell.getNeighbours().stream().filter(c -> c.get() == '.').toList(),
                cell -> cell.set('#'));

        long maxarea = 0L;
        for (Position a : positions) {
            inner:
            for (Position b : positions) {
                int ax = Math.min(xmap.get(a.x()), xmap.get(b.x()));
                int ay = Math.min(ymap.get(a.y()), ymap.get(b.y()));
                int bx = Math.max(xmap.get(a.x()), xmap.get(b.x()));
                int by = Math.max(ymap.get(a.y()), ymap.get(b.y()));
                for (int y = ay; y <= by; y++) {
                    for (int x = ax; x <= bx; x++) {
                        if (charMatrix.get(x, y) == '.') {
                            continue inner;
                        }
                    }
                }
                maxarea = Math.max(maxarea, rectArea(List.of(a, b)));
            }
        }
        System.out.println(maxarea);
    }

    private static Map<Integer,Integer> toIndexMap(List<Position> positions, Function<Position,Integer> function) {
        List<Integer> list = positions.stream().map(function).distinct().sorted().toList();
        Map<Integer,Integer> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i), i);
        }
        return map;
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
