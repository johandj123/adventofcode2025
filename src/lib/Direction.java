package lib;

import java.util.Comparator;
import java.util.List;

public record Direction(int x, int y) implements Comparable<Direction> {
    public static final Direction NORTH = new Direction(0, -1);
    public static final Direction SOUTH = new Direction(0, 1);
    public static final Direction WEST = new Direction(-1, 0);
    public static final Direction EAST = new Direction(1, 0);

    public static final Direction UP = NORTH;
    public static final Direction DOWN = SOUTH;
    public static final Direction LEFT = WEST;
    public static final Direction RIGHT = EAST;

    public static final List<Direction> DIRECTIONS = List.of(NORTH, SOUTH, WEST, EAST);

    public static final List<Direction> DIRECTIONS_WITH_DIAGONAL = List.of(NORTH, SOUTH, WEST, EAST, new Direction(1, 1), new Direction(-1, 1), new Direction(1, -1), new Direction(-1, -1));

    private static final Comparator<Direction> COMPARATOR = Comparator.comparing(Direction::y).thenComparing(Direction::x);

    public Direction turnLeft() {
        return new Direction(y, -x);
    }

    public Direction turnRight() {
        return new Direction(-y, x);
    }

    @Override
    public int compareTo(Direction o) {
        return COMPARATOR.compare(this, o);
    }
}
