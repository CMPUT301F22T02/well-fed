/*
 * DateUtil
 *
 * Version: v1.1.0
 *
 * Date: 2022-11-22
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

/**
 * The DateUtil class contains utility methods for dates.
 */
public class DateUtil {
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
     * Constructs a DateUtil object
     */
    public DateUtil() {
        this.hashFormat = getSimpleDateFormat("yyyy-MM-dd");
        this.dayNameInWeekFormat =  getSimpleDateFormat("E");

    }

    /**
     * Returns true if two dates are equal
     *
     * @param date1 the first date
     * @param date2 the second date
     * @return true if two dates are equal
     */
    public boolean equals(Date date1, Date date2) {
        if (date1 == date2) {
            return true;
        }
        return Objects.equals(hashCode(date1), hashCode(date2));
    }

    /**
     * Returns the hash code of the Date object
     *
     * @return the hash code of the Date object
     */
    private int hashCode(Date date) {
        return Objects.hash(this.hashFormat.format(date));
    }

    /**
     * Returns the day number of the week.
     *
     * @return the day number of the week
     */
    private int getDayNumberOfWeek(Date date) {
        String dayNameInWeek = dayNameInWeekFormat.format(date);
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
    public Date getFirstDayOfWeek(Date date) {
        int dayNumberOfWeek = getDayNumberOfWeek(date);
        return new Date(date.getTime() - dayNumberOfWeek * DAY);
    }

    /**
     * Returns the date of the last day of the week.
     *
     * @return the date of the last day of the week
     */
    public Date getLastDayOfWeek(Date date) {
        int dayNumberOfWeek = getDayNumberOfWeek(date);
        return new Date(date.getTime() + (6 - dayNumberOfWeek) * DAY);
    }

    /**
     * Returns a simple date format object with the specified pattern
     *
     * @param pattern the pattern {@link SimpleDateFormat}
     * @return a simple date format object with the specified pattern
     */
    private SimpleDateFormat getSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern, Locale.US);
    }

    /**
     * Returns the date as a string in the format specified by the pattern
     *
     * @param pattern the pattern {@link SimpleDateFormat}
     * @return the date as a string in the format specified by the pattern
     */
    public String format(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }
}
