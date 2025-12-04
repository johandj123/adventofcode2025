package lib;

import java.util.Arrays;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class RegionGrid<T> {
    private final int[] h;
    private final int[] v;
    private final Object[][] content;

    public RegionGrid(Collection<Rectangle> rectangles, Supplier<T> initialContent) {
        SortedSet<Integer> hset = new TreeSet<>();
        SortedSet<Integer> vset = new TreeSet<>();
        for (Rectangle rectangle : rectangles) {
            hset.add(rectangle.x1);
            vset.add(rectangle.y1);
            hset.add(rectangle.x2);
            vset.add(rectangle.y2);
        }
        h = hset.stream().mapToInt(x -> x).toArray();
        v = vset.stream().mapToInt(x -> x).toArray();
        content = new Object[v.length - 1][h.length - 1];
        for (int y = 0; y < content.length; y++) {
            for (int x = 0; x < content[0].length; x++) {
                content[y][x] = initialContent.get();
            }
        }
    }

    public void apply(Rectangle rectangle, BiConsumer<Rectangle, T> consumer) {
        int x1 = Arrays.binarySearch(h, rectangle.x1);
        int y1 = Arrays.binarySearch(v, rectangle.y1);
        int x2 = Arrays.binarySearch(h, rectangle.x2);
        int y2 = Arrays.binarySearch(v, rectangle.y2);
        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++) {
                consumer.accept(new Rectangle(h[x], v[y], h[x + 1], v[y + 1]), (T) content[y][x]);
            }
        }
    }

    public <R> R aggregate(R initial, AggregateFunction<R, T> aggregateFunction) {
        R result = initial;
        for (int y = 0; y < content.length; y++) {
            for (int x = 0; x < content[0].length; x++) {
                result = aggregateFunction.apply(new Rectangle(h[x], v[y], h[x + 1], v[y + 1]), (T) content[y][x], result);
            }
        }
        return result;
    }

    public interface AggregateFunction<R,T> {
        R apply(Rectangle rectangle, T content, R current);
    }

    /**
     * Rectangle from (x1,y1) inclusive to (x2,y2) exclusive.
     */
    public record Rectangle(int x1, int y1, int x2, int y2) {
        public long area() {
            return ((long)(x2 - x1)) * ((long)(y2 - y1));
        }
    }
}
