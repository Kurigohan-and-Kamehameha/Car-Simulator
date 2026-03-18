package org.example.cargameFx.enums;

public enum Direction {

    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public double vx(double speed) { return dx * speed; }
    public double vy(double speed) { return dy * speed; }

}
