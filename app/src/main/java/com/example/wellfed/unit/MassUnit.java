package com.example.wellfed.unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MassUnit extends Unit {

    public final static String smallestMetricUnit = "g";
    public final static String smallestImperialUnit = "oz";

    public final static HashMap<String, Double> CONVERSION_FACTORS =
            new HashMap<>(
            Map.of(
                    "g", 1d,
                    "kg", 1000d,
                    "oz", 28.349523125,
                    "lb", 453.59237
            )
    );
    public static final HashMap<String, Set<String>> UNIT_SYSTEMS =
            new HashMap<>(
            Map.of(
                    "g", Set.of("metric"),
                    "kg", Set.of("metric"),
                    "oz", Set.of("imperial"),
                    "lb", Set.of("imperial")
            )
    );
    public MassUnit(String unit) {
        super(unit);
        if (!CONVERSION_FACTORS.containsKey(unit)) {
            throw new IllegalArgumentException("Invalid unit: " + unit);
        }
        conversionFactor = CONVERSION_FACTORS.get(unit);
        systems = UNIT_SYSTEMS.get(unit);
    }
}
