/*
 * UTCDate
 *
 * Version: v1.0.0
 *
 * Date: 2022-11-03
 *
 * Copyright notice:
 * This file is part of well-fed.
 *
 * well-fed is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * well-fed is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with well-fed. If not, see <https://www.gnu.org/licenses/>.
 */

package com.example.wellfed.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * The UTCDate class provides extends the Date class with methods for managing
 * dates in UTC time.
 */
public class UTCDate extends Date {
    /**
     * Holds the number of milliseconds in a day
     */
    private static final long DAY = 86400000;
    /**
     * Holds the hash date format
     */
    private final SimpleDateFormat hashFormat;
    /**
     * Holds the day name in week date format
     */
    private final SimpleDateFormat dayNameInWeekFormat;
    /**
     * Holds the UTC time zone
     */
    private final TimeZone utc;

    /**
     * Constructs a UTCDate object with the current date and time.
     */
    public UTCDate() {
        this(System.currentTimeMillis());
    }

    /**
     * Constructs a UTCDate object with the specified date
     *
     * @param date the date in milliseconds since epoch
     */
    public UTCDate(long date) {
        super(date);
        utc = TimeZone.getTimeZone("UTC");
        hashFormat = this.getSimpleDateFormat("yyyy-MM-dd");
        dayNameInWeekFormat = this.getSimpleDateFormat("E");
    }

    /**
     * Constructs a UTCDate object from a Date object
     *
     * @param date the date object
     */
    public static UTCDate from(Date date) {
        return new UTCDate(date.getTime());
    }

    /**
     * Returns true if the object is a UTCDate object with the same date
     *
     * @param o the object to compare
     * @return true if the object is a UTCDate object with the same date
     */
    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UTCDate)) {
            return false;
        }
        UTCDate utcDate = (UTCDate) o;
        return Objects.equals(this.hashCode(), utcDate.hashCode());
    }

    /**
     * Returns the hash code of the UTCDate object
     *
     * @return the hash code of the UTCDate object
     */
    @Override public int hashCode() {
        return Objects.hash(this.hashFormat.format(this));
    }

    /**
     * Returns the day number of the week.
     *
     * @return the day number of the week
     */
    private int getDayNumberOfWeek() {
        String dayNameInWeek = this.dayNameInWeekFormat.format(this);
        switch (dayNameInWeek) {
            case "Sun":
                return 0;
            case "Mon":
                return 1;
            case "Tue":
                return 2;
            case "Wed":
                return 3;
            case "Thu":
                return 4;
            case "Fri":
                return 5;
            case "Sat":
                return 6;
            default:
                throw new IllegalStateException();
        }
    }

    /*
     * Returns the date of the first day of the week.
     *
     * @return the date of the first day of the week
     */
    public UTCDate getFirstDayOfWeek() {
        int dayNumberOfWeek = this.getDayNumberOfWeek();
        return new UTCDate(this.getTime() - dayNumberOfWeek * DAY);
    }

    /**
     * Returns the date of the last day of the week.
     *
     * @return the date of the last day of the week
     */
    public UTCDate getLastDayOfWeek() {
        int dayNumberOfWeek = this.getDayNumberOfWeek();
        return new UTCDate(this.getTime() + (6 - dayNumberOfWeek) * DAY);
    }

    /**
     * Returns a simple date format object with the specified pattern
     *
     * @param pattern the pattern {@link SimpleDateFormat}
     * @return a simple date format object with the specified pattern
     */
    private SimpleDateFormat getSimpleDateFormat(String pattern) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(pattern, Locale.US);
        simpleDateFormat.setTimeZone(this.utc);
        return simpleDateFormat;
    }

    /**
     * Returns the date as a string in the format specified by the pattern
     *
     * @param pattern the pattern {@link SimpleDateFormat}
     * @return the date as a string in the format specified by the pattern
     */
    public String format(String pattern) {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(pattern);
        return simpleDateFormat.format(this);
    }
}
