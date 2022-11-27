package com.xffffff.wellfed;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.xffffff.wellfed.unit.UnitConverter;

/**
 * Tests unit conversion.
 *
 * Intended device: Pixel 6 Pro API 22
 */
public class UnitConverterTest {
    private static final String TAG = "UnitConverterTest";

    /**
     * Tests converting kg to cup
     */
    @org.junit.Test public void testConvert() {
        Context context =
                InstrumentationRegistry.getInstrumentation().getTargetContext();
        UnitConverter unitConverter = new UnitConverter(context);
        Double result =
                unitConverter.convert(2, "Sugar", unitConverter.build("kg"),
                        unitConverter.build("metric cup"));
        unitConverter.convert(1, "Salt", unitConverter.build("kg"),
                unitConverter.build("tbsp"));
        assertEquals((Double) 8.421052631578949, result);
    }
}
