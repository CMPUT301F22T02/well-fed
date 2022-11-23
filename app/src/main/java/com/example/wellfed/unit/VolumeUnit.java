package com.example.wellfed.unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VolumeUnit extends Unit {
    public static final HashMap<String, Double> CONVERSION_FACTORS =
            new HashMap<>(
            Map.of(
                    "mL", 1d,
                    "tsp", 5d,
                    "tbsp", 15d,
                    "metric cup", 250d,
                    "L", 1000d,
                    "US fl oz", 29.5735295625,
                    "US fl cup", 236.5882365,
                    "US fl pint", 473.176473,
                    "US fl quart", 946.352946,
                    "gal (US)", 3785.411784
            )
    );
    public static final HashMap<String, Set<String>> UNIT_SYSTEMS =
            new HashMap<>(
            Map.of(
                    "mL", Set.of("metric"),
                    "L", Set.of("metric"),
                    "metric cup", Set.of("metric"),
                    "tsp", Set.of("metric", "imperial"),
                    "tbsp", Set.of("metric", "imperial"),
                    "US fl oz", Set.of("imperial"),
                    "US fl cup", Set.of("imperial"),
                    "US fl pint", Set.of("imperial"),
                    "US fl quart", Set.of("imperial"),
                    "gal (US)", Set.of("imperial")
            )
    );
    public VolumeUnit(String unit) throws IllegalArgumentException {
        super(unit);
        if (!CONVERSION_FACTORS.containsKey(unit)) {
            throw new IllegalArgumentException("Invalid unit: " + unit);
        }
        conversionFactor = CONVERSION_FACTORS.get(unit);
        systems = UNIT_SYSTEMS.get(unit);
    }
}
