package lib.simplex;

import lib.BigRational;

import java.util.*;

public class Tableau {
    final List<SortedMap<Integer, BigRational>> columns = new ArrayList<>();
    final int width;
    final int height;

    Tableau(int width, int height) {
        this.width = width;
        this.height = height;
        for (int i = 0; i < width; i++) {
            columns.add(new TreeMap<>());
        }
    }

    void emptyMinusOneColumn() {
        columns.get(width - 2).clear();
    }

    void minusOneColumn() {
        emptyMinusOneColumn();
        Map<Integer, BigRational> ht = columns.get(width - 2);
        for (int row = 0; row < height; row++) {
            ht.put(row, (row == 0) ? BigRational.ONE : BigRational.MINUS_ONE);
        }
    }

    BigRational get(int j,int i) {
        return columns.get(i).getOrDefault(j, BigRational.ZERO);
    }

    BigRational getTopRow(int i) {
        return get(0, i);
    }

    BigRational getTopRowOrDefault(int i) {
        return get(0, i);
    }

    void set(int j, int i, BigRational value) {
        if (value.isZero()) {
            columns.get(i).remove(j);
        } else {
            columns.get(i).put(j, value);
        }
    }

    void add(int j,int i,BigRational value) {
        set(j, i, get(j, i).add(value));
    }

    int findPivotColumn(boolean aux) {
        int limit = aux ? width - 1 : width - 2;
        for (int i = 0; i < limit; i++) {
            BigRational v = getTopRow(i);
            if (v.isNegative()) {
                return i;
            }
        }
        return -1;
    }

    int findPivotRow(int i) {
        SortedMap<Integer, BigRational> ht = columns.get(i);
        int j = -1;
        BigRational d = BigRational.ZERO;

        for (var el : ht.entrySet()) {
            int row = el.getKey();
            if (row == 0) {
                continue;
            }

            BigRational a = el.getValue();
            if (!a.isPositive()) {
                continue;
            }

            BigRational b = get(row, width - 1);
            BigRational ba = b.divide(a);

            if (j == -1 || ba.compareTo(d) < 0) {
                j = row;
                d = ba;
            }
        }

        return j;
    }

    int findSmallestB() {
        BigRational d = BigRational.ZERO;
        int j = -1;
        SortedMap<Integer, BigRational> ht = columns.get(width - 1);

        for (var el : ht.entrySet()) {
            if (j == -1 || el.getValue().compareTo(d) < 0) {
                j = el.getKey();
                d = el.getValue();
            }
        }

        return j;
    }

    void pivot(int j,int i) {
        // Pivot to (j,i)
        // Divide row j by the value at (j,i) ; skip column i since we set this column to the unit vector below
        BigRational d = get(j, i).reciprocal();
        for (int col = 0; col < width; col++) {
            if (col == i) {
                continue;
            }

            BigRational value = get(j, col).multiply(d);
            set(j, col, value);

            for (var elp : columns.get(i).entrySet()) {
                if (elp.getKey() == j) {
                    continue;
                }
                add(elp.getKey(), col, elp.getValue().multiply(value).negate());
            }
        }

        // Replace column i by the unit vector with a one at column j
        columns.get(i).clear();
        columns.get(i).put(j, BigRational.ONE);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sb.append(get(y, x)).append('\t');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
