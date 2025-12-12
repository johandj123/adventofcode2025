import lib.CharMatrix;
import lib.GraphUtil;
import lib.InputUtil;
import lib.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Day12 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsStringGroups("input12.txt");
        List<CharMatrix> presents = new ArrayList<>();
        for (int i = 0; i < input.size() - 1; i++) {
            String s = input.get(i);
            s = s.substring(s.indexOf('\n') + 1);
            presents.add(new CharMatrix(s));
        }
        List<Region> regions = Arrays.stream(input.getLast().split("\n")).map(Region::new).toList();
        List<Integer> presentSizes = presents.stream().map(Day12::countPositions).toList();
        List<Set<CharMatrix>> presentsAllOrientations = presents.stream().map(Day12::allOrientations).toList();

        int count = 0;
        for (Region region : regions) {
            if (region.size() < region.countPositionsNeeded(presentSizes)) {
                continue;
            }
            if (region.canPlacePresents(presentsAllOrientations)) {
                count++;
            }
        }
        System.out.println(count);
    }

    private static Set<CharMatrix> allOrientations(CharMatrix charMatrix) {
        return GraphUtil.reachable(charMatrix, c -> List.of(c, c.mirrorHorizontal(), c.mirrorVertical(), c.transpose()));
    }

    private static int countPositions(CharMatrix charMatrix) {
        return (int) charMatrix.stream().filter(cell -> cell.get() == '#').count();
    }

    static class Region {
        int width;
        int height;
        List<Integer> counts;

        public Region(String line) {
            List<Integer> ints = InputUtil.extractPositiveIntegers(line);
            width = ints.get(0);
            height = ints.get(1);
            counts = ints.subList(2, ints.size());
        }

        int size() {
            return width * height;
        }

        int countPositionsNeeded(List<Integer> presentSizes) {
            int result = 0;
            for (int i = 0; i < counts.size(); i++) {
                result += (counts.get(i) * presentSizes.get(i));
            }
            return result;
        }

        List<Set<CharMatrix>> presentsToBePlaced(List<Set<CharMatrix>> presentsAllOrientations) {
            List<Set<CharMatrix>> result = new ArrayList<>();
            for (int i = 0; i < counts.size(); i++) {
                for (int j = 0; j < counts.get(i); j++) {
                    result.add(presentsAllOrientations.get(i));
                }
            }
            return result;
        }

        @Override
        public String toString() {
            return "Region{" +
                    "width=" + width +
                    ", height=" + height +
                    ", counts=" + counts +
                    '}';
        }

        public boolean canPlacePresents(List<Set<CharMatrix>> presentsAllOrientations) {
            CharMatrix charMatrix = new CharMatrix(height, width, '.');
            List<Set<CharMatrix>> presentList = presentsToBePlaced(presentsAllOrientations);
            return canPlacePresents(charMatrix, presentList);
        }

        private boolean canPlacePresents(CharMatrix charMatrix, List<Set<CharMatrix>> presentsAllOrientations) {
            if (presentsAllOrientations.isEmpty()) {
                return true;
            }
            Set<CharMatrix> presentOrientations = presentsAllOrientations.getFirst();
            for (CharMatrix present : presentOrientations) {
                for (int y = 0; y < charMatrix.getHeight() - present.getHeight() + 1; y++) {
                    for (int x = 0; x < charMatrix.getWidth() - present.getWidth() + 1; x++) {
                        Position position = new Position(x, y);
                        if (canPlace(charMatrix, present, position)) {
                            place(charMatrix, present, position);
                            boolean result = canPlacePresents(charMatrix, presentsAllOrientations.subList(1, presentsAllOrientations.size()));
                            if (result) {
                                return true;
                            }
                            unplace(charMatrix, present, position);
                        }
                    }
                }
            }
            return false;
        }

        private boolean canPlace(CharMatrix charMatrix, CharMatrix present, Position position) {
            for (int y = 0; y < present.getHeight(); y++) {
                for (int x = 0; x < present.getWidth(); x++) {
                    if (present.get(x, y) == '#' && charMatrix.get(position.x() + x, position.y() + y) == '#') {
                        return false;
                    }
                }
            }
            return true;
        }

        private void place(CharMatrix charMatrix, CharMatrix present, Position position) {
            set(present, charMatrix, position, '#');
        }

        private void unplace(CharMatrix charMatrix, CharMatrix present, Position position) {
            set(present, charMatrix, position, '.');
        }

        private static void set(CharMatrix present, CharMatrix charMatrix, Position position, char c) {
            for (int y = 0; y < present.getHeight(); y++) {
                for (int x = 0; x < present.getWidth(); x++) {
                    if (present.get(x, y) == '#') {
                        charMatrix.set(position.x() + x, position.y() + y, c);
                    }
                }
            }
        }
    }
}
