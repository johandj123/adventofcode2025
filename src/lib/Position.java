package lib;

public record Position(int x, int y) {
    public Position add(int dx,int dy) {
        return new Position(x + dx, y + dy);
    }

    public Position add(Direction delta) {
        return new Position(x + delta.x(), y + delta.y());
    }
}
