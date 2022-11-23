package com.xffffff.wellfed.unit;

import java.util.Set;

public abstract class Unit {
    private final String unit;
    protected double conversionFactor;
    protected Set<String> systems;

    public Unit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public Set<String> getSystems() {
        return systems;
    }
}
