package com.xffffff.wellfed.unit;

import java.util.Set;

/**
 * Unit is an abstract class that contains the conversion factor and unit
 */
public abstract class Unit {
    /**
     * The unit string for the unit
     */
    private final String unit;
    /**
     * The conversion factor for the unit
     */
    protected double conversionFactor;
    /**
     * The unit systems that the unit is in
     */
    protected Set<String> systems;

    /**
     * The constructor for the Unit class that initializes the unit
     *
     * @param unit the unit string
     */
    public Unit(String unit) {
        this.unit = unit;
    }

    /**
     * The getter for the unit string
     *
     * @return the unit string
     */
    public String getUnit() {
        return unit;
    }

    /**
     * The getter for the conversion factor
     *
     * @return the conversion factor
     */
    public double getConversionFactor() {
        return conversionFactor;
    }

    /**
     * The getter for the unit systems
     *
     * @return the unit systems
     */
    public Set<String> getSystems() {
        return systems;
    }
}
