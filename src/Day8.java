import lib.Graph;
import lib.GraphUtil;
import lib.InputUtil;

import java.io.IOException;
import java.util.*;

public class Day8 {
    static void main() throws IOException {
        List<Box> boxes = InputUtil.readAsLines("input8.txt").stream().map(Box::new).toList();
        List<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < boxes.size(); i++) {
            for (int j = i + 1; j < boxes.size(); j++) {
                pairs.add(new Pair(boxes.get(i), boxes.get(j)));
            }
        }
        pairs.sort(Comparator.comparing(Pair::getDistance));
        first(boxes, pairs);
        second(boxes, pairs);
    }

    private static void first(List<Box> boxes, List<Pair> pairs) {
        Graph<Box> graph = new Graph<>();
        boxes.forEach(graph::addNode);
        for (int i = 0; i < 1000; i++) {
            Pair pair = pairs.get(i);
            graph.addLink(pair.a, pair.b);
            graph.addLink(pair.b, pair.a);
        }
        List<Set<Box>> components = graph.components();
        List<Integer> sizes = new ArrayList<>(components.stream().map(Set::size).sorted().toList());
        Collections.reverse(sizes);
        System.out.println(sizes.stream().mapToLong(x -> x).limit(3).reduce(1L, (a, b) -> a * b));
    }

    private static void second(List<Box> boxes, List<Pair> pairs) {
        Graph<Box> graph = new Graph<>();
        boxes.forEach(graph::addNode);
        for (Pair pair : pairs) {
            graph.addLink(pair.a, pair.b);
            graph.addLink(pair.b, pair.a);
            List<Set<Box>> components = graph.components();
            if (components.size() == 1) {
                System.out.println((long)pair.a.x * (long)pair.b.x);
                break;
            }
        }
    }

    static class Pair {
        final Box a;
        final Box b;
        final double distance;

        public Pair(Box a, Box b) {
            this.a = a;
            this.b = b;
            this.distance = a.distance(b);
        }

        public double getDistance() {
            return distance;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "a=" + a +
                    ", b=" + b +
                    ", distance=" + distance +
                    '}';
        }
    }

    record Box(int x,int y,int z) {
        Box(String line) {
            String[] sp = line.split(",");
            this(Integer.parseInt(sp[0]), Integer.parseInt(sp[1]), Integer.parseInt(sp[2]));
        }

        double distance(Box other) {
            double dx = x - other.x;
            double dy = y - other.y;
            double dz = z - other.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }
}
