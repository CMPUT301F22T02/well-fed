package com.xffffff.wellfed.unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CountUnit extends Unit {
    public static final String smallestMetricUnit = "count";
    public static final String smallestImperialUnit = "count";
    public static final HashMap<String, Double> CONVERSION_FACTORS =
            new HashMap<>(Map.of("count", 1d));
    public static final HashMap<String, Set<String>> UNIT_SYSTEMS =
            new HashMap<>(Map.of("count", Set.of("metric", "imperial")));

    public CountUnit(String unit) {
        super(unit);
        conversionFactor = CONVERSION_FACTORS.get(unit);
        systems = UNIT_SYSTEMS.get(unit);
    }
}
