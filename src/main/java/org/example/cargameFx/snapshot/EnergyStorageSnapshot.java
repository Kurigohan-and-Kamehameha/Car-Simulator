package org.example.cargameFx.snapshot;

public record EnergyStorageSnapshot(double power, double capacity) {

    public EnergyStorageSnapshot {
        if (capacity < 0) throw new IllegalArgumentException("capacity must be >= 0");
        power = Math.max(0, Math.min(capacity, power));
    }

    public double getPercentage() {
        return power / capacity;
    }

    public double getPercentage100() {
        double percent = (power / capacity) * 100.0;
        return Math.round(percent * 100.0) / 100.0;
    }
}
