package com.xffffff.wellfed.unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VolumeUnit extends Unit {

    public static final String smallestMetricUnit = "mL";
    public static final String smallestImperialUnit = "oz";

    public static final HashMap<String, Double> CONVERSION_FACTORS =
            new HashMap<>(
                    Map.of("mL", 1d, "tsp", 5d, "tbsp", 15d, "metric cup", 250d,
                            "L", 1000d, "oz", 29.5735295625, "cup", 236.5882365,
                            "pint", 473.176473, "quart", 946.352946, "gal",
                            3785.411784));
    public static final HashMap<String, Set<String>> UNIT_SYSTEMS =
            new HashMap<>(Map.of("mL", Set.of("metric"), "L", Set.of("metric"),
                    "metric cup", Set.of("metric"), "tsp",
                    Set.of("metric", "imperial"), "tbsp",
                    Set.of("metric", "imperial"), "oz", Set.of("imperial"),
                    "cup", Set.of("imperial"), "pint", Set.of("imperial"),
                    "quart", Set.of("imperial"), "gal", Set.of("imperial")));

    public VolumeUnit(String unit) throws IllegalArgumentException {
        super(unit);
        if (!CONVERSION_FACTORS.containsKey(unit)) {
            throw new IllegalArgumentException("Invalid unit: " + unit);
        }
        conversionFactor = CONVERSION_FACTORS.get(unit);
        systems = UNIT_SYSTEMS.get(unit);
    }
}
