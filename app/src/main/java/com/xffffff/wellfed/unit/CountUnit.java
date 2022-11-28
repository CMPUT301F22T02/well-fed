package com.xffffff.wellfed.unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * CountUnit is a class that contains all the count units that are supported by
 * the app
 */
public class CountUnit extends Unit {
    /**
     * The smallest unit for count
     */
    public static final String smallestMetricUnit = "count";
    /**
     * The conversion factors for the count units
     */
    public static final HashMap<String, Double> CONVERSION_FACTORS =
        new HashMap<>(Map.of("count", 1d));
    /**
     * The unit systems that the count units are in
     */
    public static final HashMap<String, Set<String>> UNIT_SYSTEMS =
        new HashMap<>(Map.of("count", Set.of("metric", "imperial")));

    /**
     * Constructor for the CountUnit class
     *
     * @param unit the unit of count
     */
    public CountUnit(String unit) {
        super(unit);
        conversionFactor = CONVERSION_FACTORS.get(unit);
        systems = UNIT_SYSTEMS.get(unit);
    }
}
