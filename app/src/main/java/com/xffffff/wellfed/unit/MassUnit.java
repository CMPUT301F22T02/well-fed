package com.xffffff.wellfed.unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * MassUnit is a class that contains all the mass units that are supported by
 * the app
 */
public class MassUnit extends Unit {

    /**
     * The smallest unit for metric mass
     */
    public final static String smallestMetricUnit = "g";
    /**
     * The smallest unit for imperial mass
     */
    public final static String smallestImperialUnit = "oz";

    /**
     * The conversion factors for the mass units
     */
    public final static HashMap<String, Double> CONVERSION_FACTORS =
        new HashMap<>(Map.of("g", 1d, "kg", 1000d, "oz", 28.349523125, "lb",
            453.59237));
    /**
     * The unit systems that the mass units are in
     */
    public static final HashMap<String, Set<String>> UNIT_SYSTEMS =
        new HashMap<>(
            Map.of("g", Set.of("metric"), "kg", Set.of("metric"), "oz",
                Set.of("imperial"), "lb", Set.of("imperial")));

    /**
     * Constructor for the MassUnit class
     *
     * @param unit the unit of mass
     */
    public MassUnit(String unit) {
        super(unit);
        if (!CONVERSION_FACTORS.containsKey(unit)) {
            throw new IllegalArgumentException("Invalid unit: " + unit);
        }
        conversionFactor = CONVERSION_FACTORS.get(unit);
        systems = UNIT_SYSTEMS.get(unit);
    }
}
