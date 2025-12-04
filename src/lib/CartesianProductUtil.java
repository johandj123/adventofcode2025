package lib;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CartesianProductUtil {
    private CartesianProductUtil() {
    }

    public static <T> Iterable<List<T>> cartesianProduct(int power, List<T> list) {
        return new CartesianProduct<>(power, list);
    }

    public static <T> Stream<List<T>> cartesianProductStream(int power, List<T> list) {
        return StreamSupport.stream(cartesianProduct(power, list).spliterator(), false);
    }

    private static class CartesianProduct<T> implements Iterable<List<T>>
    {
        private final int power;
        private final List<T> list;

        public CartesianProduct(int power, List<T> list) {
            this.power = power;
            this.list = list;
        }

        @Override
        public Iterator<List<T>> iterator() {
            return new CartesianProductIterator<>(power, list);
        }
    }

    private static class CartesianProductIterator<T> implements Iterator<List<T>> {
        private final int power;
        private final List<T> list;
        private final int size;
        private int[] next;
        private int[] current;

        public CartesianProductIterator(int power, List<T> list) {
            this.power = power;
            this.list = list;
            size = list.size();
            next = current = new int[power];
        }

        @Override
        public boolean hasNext() {
            return (makeNext() != null);
        }

        @Override
        public List<T> next() {
            int[] r = makeNext();
            next = null;
            return (r == null) ? null : Arrays.stream(r).mapToObj(list::get).collect(Collectors.toList());
        }

        private int[] makeNext() {
            if (next != null)
                return next;
            if (current == null)
                return null;

            boolean ok = false;
            for (int i = power - 1; i >= 0; i--) {
                current[i]++;
                if (current[i] < size) {
                    ok = true;
                    break;
                } else {
                    current[i] = 0;
                }
            }
            if (!ok) {
                current = null;
            }
            next = current;
            return next;
        }
    }
}
