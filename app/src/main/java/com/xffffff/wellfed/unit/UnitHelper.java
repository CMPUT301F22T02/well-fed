package com.xffffff.wellfed.unit;

import android.util.Pair;

import com.xffffff.wellfed.ingredient.Ingredient;

/**
 * UnitHelper is a helper class that contains methods for converting units
 */
public class UnitHelper {

    /**
     * UnitConverter objects are used to convert units
     */
    private UnitConverter unitConverter;

    /**
     * Constructor for the UnitHelper class that initializes the
     * UnitConverter object with the given unit
     *
     * @param unitConverter the UnitConverter object to use
     */
    public UnitHelper(UnitConverter unitConverter) {
        this.unitConverter = unitConverter;
    }

    /**
     * Convert the given unit to the smallest unit in the unit system
     *
     * @param unitString the unit to convert
     * @param value      the value of the unit
     * @return the converted unit
     */
    public Pair<Double, String> convertToSmallest(String unitString,
                                                  Double value) {
        Unit unit = unitConverter.build(unitString);
        if (unit instanceof MassUnit) {
            Double smallest = unitConverter.convert(value, unit,
                unitConverter.build(MassUnit.smallestMetricUnit));
            return new Pair<>(smallest, MassUnit.smallestMetricUnit);
        } else if (unit instanceof VolumeUnit) {
            Double smallest = unitConverter.convert(value, unit,
                unitConverter.build(VolumeUnit.smallestMetricUnit));
            return new Pair<>(smallest, VolumeUnit.smallestMetricUnit);
        } else if (unit instanceof CountUnit) {
            return new Pair<>(value, CountUnit.smallestMetricUnit);
        } else {
            throw new IllegalArgumentException(
                "No Unit " + unitString + " exists");
        }
    }


    /**
     * Converts ingredients to mass if possible
     *
     * @param ingredient assumes that ingredient has been converted using
     *                   {@link UnitHelper#convertToSmallest(String, Double)}
     * @return the converted ingredient
     */
    public Pair<Double, String> availableUnits(Ingredient ingredient) {
        Unit unit = unitConverter.build(ingredient.getUnit());
        if (unit instanceof VolumeUnit) {
            try {
                Double amount = unitConverter.convert(ingredient.getAmount(),
                    ingredient.getDescription(),
                    unitConverter.build(VolumeUnit.smallestMetricUnit),
                    unitConverter.build(MassUnit.smallestMetricUnit));
                return new Pair<>(amount, MassUnit.smallestMetricUnit);
            } catch (IllegalArgumentException e) {
                return new Pair<>(ingredient.getAmount(), ingredient.getUnit());
            }
        }
        return new Pair<>(ingredient.getAmount(), ingredient.getUnit());
    }
}
