package com.example.wellfed.unit;

import android.content.Context;

import com.example.wellfed.R;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

//TODO: cite https://en.wikibooks.org/wiki/Cookbook:Units_of_measurement
//TODO: cite http://conversion.org/
public class UnitConverter {
    /**
     * Citation:
     * Charrondiere, U. R., Haytowitz, D., &amp; Stadlmayr, B. (2012).
     * FAO/INFOODS Density Database Version 2.0. Retrieved November 22, 2022,
     * from https://www.fao.org/fileadmin/templates/food_composition/documents/
     * density_DB_v2_0_final-1__1_.xlsx
     */
    HashMap<String, Double> ingredientDensityMap;


    public UnitConverter(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.densities);
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        ingredientDensityMap = new Gson().fromJson(reader, HashMap.class);
    }

    public Double convert(double value, String ingredient, Unit from, Unit to) {
        if (from == to || from instanceof CountUnit ||
                to instanceof CountUnit) {
            return value;
        }

        if (from.getClass() != to.getClass()) {
            if (ingredientDensityMap.get(ingredient) == null) {
                throw new IllegalArgumentException("Ingredient not found");
            }
            if (from instanceof VolumeUnit && to instanceof MassUnit) {
                Double volume =
                        convert(value, ingredient, from, build("mL"));
                value = volume * ingredientDensityMap.get(ingredient);
            } else if (from instanceof MassUnit && to instanceof VolumeUnit) {
                Double mass =
                        convert(value, ingredient, from, build("g"));
                value = mass / ingredientDensityMap.get(ingredient);
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

    public Double convert(double value, Unit from, Unit to) {
        return convert(value, null, from, to);
    }

    public String format(double value, String ingredient, Unit from, Unit to) {
        return String.format("%.2f %s", convert(value, ingredient, from, to),
                to.getUnit());
    }

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
}
