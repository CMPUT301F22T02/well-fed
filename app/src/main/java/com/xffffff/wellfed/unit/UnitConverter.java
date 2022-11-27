package com.xffffff.wellfed.unit;

import android.content.Context;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.xffffff.wellfed.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UnitConverter is a class that converts units to other units
 * It is used to convert units in the Ingredients and Recipe ingredients
 */
public class UnitConverter {
    /**
     * Citation:
     * Charrondiere, U. R., Haytowitz, D., &amp; Stadlmayr, B. (2012).
     * FAO/INFOODS Density Database Version 2.0. Retrieved November 22, 2022,
     * from https://www.fao.org/fileadmin/templates/food_composition/documents/
     * density_DB_v2_0_final-1__1_.xlsx
     */

    /**
     * The ingredient density map is a map of ingredient names to their
     * densities in g/mL
     */
    HashMap<String, Double> ingredientDensityMap;
    /**
     * The units ArrayList is an ArrayList of all the units that are
     * supported by the app
     */
    ArrayList<String> units;

    /**
     * The UnitConverter constructor initializes the ingredientDensityMap and
     * units ArrayList
     *
     * @param context the context of the app
     */
    public UnitConverter(Context context) {
        InputStream is =
            context.getResources().openRawResource(R.raw.densities);
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        ingredientDensityMap = new Gson().fromJson(reader, HashMap.class);
    }

    /**
     * The convert method converts a quantity from one unit to another unit
     *
     * @param value      the quantity to convert
     * @param ingredient the ingredient to convert
     * @param from       the unit to convert from
     * @param to         the unit to convert to
     * @return the converted quantity
     */
    public Double convert(double value, String ingredient, Unit from, Unit to) {
        if (from == to || from instanceof CountUnit ||
            to instanceof CountUnit) {
            return value;
        }

        String foundMatch =
            "######";   // no key in our dictionary will match this
        if (ingredientDensityMap.get(ingredient) == null) {
            Pattern pattern =
                Pattern.compile(ingredient, Pattern.CASE_INSENSITIVE);
            for (String key : ingredientDensityMap.keySet()) {
                Matcher matcher = pattern.matcher(key);
                if (matcher.find()) {
                    foundMatch = key;
                    break;
                }
            }
        } else {
            foundMatch = ingredient;
        }

        if (from.getClass() != to.getClass()) {
            if (ingredientDensityMap.get(foundMatch) == null) {
                throw new IllegalArgumentException("Ingredient not found");
            }
            if (from instanceof VolumeUnit && to instanceof MassUnit) {
                Double volume = convert(value, foundMatch, from, build("mL"));
                value = volume * ingredientDensityMap.get(foundMatch);
            } else if (from instanceof MassUnit && to instanceof VolumeUnit) {
                Double mass = convert(value, foundMatch, from, build("g"));
                value = mass / ingredientDensityMap.get(foundMatch);
            } else {
                throw new IllegalArgumentException(
                    "Cannot convert " + ingredient + " from " +
                        from.getUnit() + " to " + to.getUnit());
            }
        } else {
            value *= from.getConversionFactor();
        }
        return value / to.getConversionFactor();
    }

    /**
     * The convert method converts a quantity from one unit to another unit
     *
     * @param value the quantity to convert
     * @param from  the unit to convert from
     * @param to    the unit to convert to
     * @return the converted quantity
     */
    public Double convert(double value, Unit from, Unit to) {
        if ((from instanceof MassUnit && to instanceof MassUnit) ||
            (from instanceof VolumeUnit && to instanceof VolumeUnit) ||
            (from instanceof CountUnit && to instanceof CountUnit)) {
            return value *
                (from.getConversionFactor() / to.getConversionFactor());
        } else {
            throw new IllegalArgumentException(
                "Cannot convert " + " from " + from.getUnit() + " to " +
                    to.getUnit());
        }
    }

    /**
     * The format method formats a quantity to a specified unit
     *
     * @param value      the quantity to format
     * @param ingredient the ingredient to format
     * @param from       the unit to format from
     * @param to         the unit to format to
     * @return the formatted quantity
     */
    public String format(double value, String ingredient, Unit from, Unit to) {
        return String.format("%.2f %s", convert(value, ingredient, from, to),
            to.getUnit());
    }

    /**
     * the scaleUnit method scales a unit to a specified unit
     *
     * @param value the quantity to scale
     * @param unit  the unit to scale
     * @return the scaled unit
     */
    public Pair<Double, Unit> scaleUnit(double value, Unit unit) {
        Unit bestUnit = unit;
        double bestValue = Double.MAX_VALUE;
        for (Unit u : getUnitsLike(unit)) {
            double scaledValue = convert(value, unit, u);
            if (scaledValue > 1 && scaledValue < bestValue) {
                bestUnit = u;
                bestValue = scaledValue;
            }
        }
        if (bestValue == Double.MAX_VALUE) {
            bestValue = value;
        }
        return new Pair<>(bestValue, bestUnit);
    }

    /**
     * build method builds a unit from a string
     *
     * @param unit the string to build the unit from
     * @return the built unit
     */
    public Unit build(String unit) {
        if (CountUnit.CONVERSION_FACTORS.containsKey(unit)) {
            return new CountUnit(unit);
        } else if (MassUnit.CONVERSION_FACTORS.containsKey(unit)) {
            return new MassUnit(unit);
        } else if (VolumeUnit.CONVERSION_FACTORS.containsKey(unit)) {
            return new VolumeUnit(unit);
        } else {
            throw new IllegalArgumentException("Unit not found");
        }
    }

    /**
     * getUnitsLike method gets all the units that are like the specified unit
     *
     * @param unit the unit to get units like
     * @return the units that are like the specified unit
     */
    public ArrayList<Unit> getUnitsLike(Unit unit) {
        ArrayList<Unit> units = new ArrayList<>();
        if (unit instanceof CountUnit) {
            for (String key : CountUnit.CONVERSION_FACTORS.keySet()) {
                Unit u = new CountUnit(key);
                if (u.getSystems().equals(unit.getSystems())) {
                    units.add(u);
                }
            }
        } else if (unit instanceof MassUnit) {
            for (String key : MassUnit.CONVERSION_FACTORS.keySet()) {
                Unit u = new MassUnit(key);
                if (u.getSystems().equals(unit.getSystems())) {
                    units.add(u);
                }
            }
        } else if (unit instanceof VolumeUnit) {
            for (String key : VolumeUnit.CONVERSION_FACTORS.keySet()) {
                Unit u = new VolumeUnit(key);
                if (u.getSystems().equals(unit.getSystems())) {
                    units.add(u);
                }
            }
        } else {
            throw new IllegalArgumentException("Unit not found");
        }
        return units;
    }

    /**
     * Gets all of the units being used, out of count/mass/volume
     *
     * @return an arraylist of all units
     */
    public ArrayList<String> getUnits() {
        if (this.units != null) {
            return this.units;
        }
        Set<String> keys = new HashSet<>();
        keys.addAll(CountUnit.CONVERSION_FACTORS.keySet());
        keys.addAll(MassUnit.CONVERSION_FACTORS.keySet());
        keys.addAll(VolumeUnit.CONVERSION_FACTORS.keySet());
        ArrayList<String> units = new ArrayList<>(keys);
        Collections.sort(units, String.CASE_INSENSITIVE_ORDER);
        this.units = units;
        return units;
    }
}
