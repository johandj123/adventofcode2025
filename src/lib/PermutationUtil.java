package lib;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PermutationUtil {
    private PermutationUtil() {
    }

    public static <T> Iterable<List<T>> permutations(List<T> list) {
        return new Permutations<>(list);
    }

    public static <T> Stream<List<T>> permutationsStream(List<T> list) {
        return StreamSupport.stream(permutations(list).spliterator(), false);
    }

    private static class Permutations<T> implements Iterable<List<T>> {
        private final List<T> list;

        public Permutations(List<T> list) {
            this.list = list;
        }

        @Override
        public Iterator<List<T>> iterator() {
            return new PermutationIterator<>(list);
        }
    }

    // Based on https://en.wikipedia.org/wiki/Steinhaus%E2%80%93Johnson%E2%80%93Trotter_algorithm#Even.27s_speedup
    private static class PermutationIterator<T> implements Iterator<List<T>> {
        private final List<T> list;
        private int[] next;

        private final int n;
        private int[] perm;
        private int[] dirs;

        private PermutationIterator(List<T> list) {
            this.list = list;
            n = list.size();
            if (n <= 0) {
                perm = (dirs = null);
            } else {
                perm = new int[n];
                dirs = new int[n];
                for (int i = 0; i < n; i++) {
                    perm[i] = i;
                    dirs[i] = -1;
                }
                dirs[0] = 0;
            }

            next = perm;
        }

        @Override
        public List<T> next() {
            int[] r = makeNext();
            next = null;
            return (r == null) ? null : Arrays.stream(r).mapToObj(list::get).collect(Collectors.toList());
        }

        @Override
        public boolean hasNext() {
            return (makeNext() != null);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private int[] makeNext() {
            if (next != null)
                return next;
            if (perm == null)
                return null;

            // find the largest element with != 0 direction
            int i = -1, e = -1;
            for (int j = 0; j < n; j++)
                if ((dirs[j] != 0) && (perm[j] > e)) {
                    e = perm[j];
                    i = j;
                }

            if (i == -1) // no such element -> no more premutations
                return (next = (perm = (dirs = null))); // no more permutations

            // swap with the element in its direction
            int k = i + dirs[i];
            swap(i, k, dirs);
            swap(i, k, perm);
            // if it's at the start/end or the next element in the direction
            // is greater, reset its direction.
            if ((k == 0) || (k == n - 1) || (perm[k + dirs[k]] > e))
                dirs[k] = 0;

            // set directions to all greater elements
            for (int j = 0; j < n; j++)
                if (perm[j] > e)
                    dirs[j] = (j < k) ? +1 : -1;

            return (next = perm);
        }

        protected static void swap(int i, int j, int[] arr) {
            int v = arr[i];
            arr[i] = arr[j];
            arr[j] = v;
        }
    }
}
