package com.xffffff.wellfed.unit;

import android.util.Pair;

import com.xffffff.wellfed.ingredient.Ingredient;

public class UnitHelper {

    private UnitConverter unitConverter;

    public UnitHelper(UnitConverter unitConverter) {
        this.unitConverter = unitConverter;
    }

    public Pair<Double, String> convertToSmallest(String unitString, Double value) {
        Unit unit = unitConverter.build(unitString);
        if (unit instanceof MassUnit) {
            Double smallest = unitConverter.convert(value, unit, unitConverter.build(MassUnit.smallestMetricUnit));
            return new Pair<>(smallest, MassUnit.smallestMetricUnit);
        } else if (unit instanceof VolumeUnit) {
            Double smallest = unitConverter.convert(value, unit, unitConverter.build(VolumeUnit.smallestMetricUnit));
            return new Pair<>(smallest, VolumeUnit.smallestMetricUnit);
        } else if (unit instanceof CountUnit) {
            return new Pair<>(value, CountUnit.smallestMetricUnit);
        } else {
            throw new IllegalArgumentException("No Unit " + unitString + " exists");
        }
    }

    // return the converted ingredient

    /**
     * Converts ingredients to mass if possible
     *
     * @param ingredient assumes that ingredient has been converted using
     *                   {@link UnitHelper#convertToSmallest(String, Double)}
     * @return
     */
    public Pair<Double, String> availableUnits(Ingredient ingredient) {
        Unit unit = unitConverter.build(ingredient.getUnit());
        if (unit instanceof VolumeUnit) {
            try {
                Double amount = unitConverter.convert(ingredient.getAmount(), ingredient.getDescription(),
                        unitConverter.build(VolumeUnit.smallestMetricUnit), unitConverter.build(MassUnit.smallestMetricUnit));
                return new Pair<>(amount, MassUnit.smallestMetricUnit);
            } catch (IllegalArgumentException e) {
                return new Pair<>(ingredient.getAmount(), ingredient.getUnit());
            }
        }
        return new Pair<>(ingredient.getAmount(), ingredient.getUnit());

    }

}
