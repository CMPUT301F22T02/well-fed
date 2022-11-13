package com.example.wellfed.common;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class UTCDateFormat extends SimpleDateFormat {
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    /**
     * Constructs a <code>SimpleDateFormat</code> using the default pattern and
     * date format symbols for the default
     * {@link Locale.Category#FORMAT FORMAT} locale.
     * <b>Note:</b> This constructor may not support all locales.
     * For full coverage, use the factory methods in the {@link DateFormat}
     * class.
     */
    public UTCDateFormat() {
        super();
        this.setTimeZone(UTC);
    }

    /**
     * Constructs a <code>SimpleDateFormat</code> using the given pattern and
     * the default date format symbols for the default
     * {@link Locale.Category#FORMAT FORMAT} locale.
     * <b>Note:</b> This constructor may not support all locales.
     * For full coverage, use the factory methods in the {@link DateFormat}
     * class.
     * <p>This is equivalent to calling
     * {@link #SimpleDateFormat(String, Locale)
     * SimpleDateFormat(pattern, Locale.getDefault(Locale.Category.FORMAT))}.
     *
     * @param pattern the pattern describing the date and time format
     * @throws NullPointerException     if the given pattern is null
     * @throws IllegalArgumentException if the given pattern is invalid
     * @see Locale#getDefault(Locale.Category)
     * @see Locale.Category#FORMAT
     */
    public UTCDateFormat(String pattern) {
        super(pattern);
        this.setTimeZone(UTC);
    }

    /**
     * Constructs a <code>SimpleDateFormat</code> using the given pattern and
     * the default date format symbols for the given locale.
     * <b>Note:</b> This constructor may not support all locales.
     * For full coverage, use the factory methods in the {@link DateFormat}
     * class.
     *
     * @param pattern the pattern describing the date and time format
     * @param locale  the locale whose date format symbols should be used
     * @throws NullPointerException     if the given pattern or locale is null
     * @throws IllegalArgumentException if the given pattern is invalid
     */
    public UTCDateFormat(String pattern, Locale locale) {
        super(pattern, locale);
        this.setTimeZone(UTC);
    }

    /**
     * Constructs a <code>SimpleDateFormat</code> using the given pattern and
     * date format symbols.
     *
     * @param pattern       the pattern describing the date and time format
     * @param formatSymbols the date format symbols to be used for formatting
     * @throws NullPointerException     if the given pattern or formatSymbols is null
     * @throws IllegalArgumentException if the given pattern is invalid
     */
    public UTCDateFormat(String pattern, DateFormatSymbols formatSymbols) {
        super(pattern, formatSymbols);
        this.setTimeZone(UTC);
    }
}
