package com.example.wellfed.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class UTCDate extends Date {
    private static final long DAY = 86400000;
    private final SimpleDateFormat hashFormat;
    private final SimpleDateFormat dayNameInWeekFormat;
    private final TimeZone utc;
    private HashMap<String, Integer> dayNameInWeekDayNumberOfWeekMap;

    public UTCDate() {
        this(System.currentTimeMillis());
    }

    public UTCDate(long date) {
        super(date);
        utc = TimeZone.getTimeZone("UTC");
        hashFormat = this.getSimpleDateFormat("yyyy-MM-dd");
        dayNameInWeekFormat = this.getSimpleDateFormat("E");
        dayNameInWeekDayNumberOfWeekMap = new HashMap<>();
        dayNameInWeekDayNumberOfWeekMap.put("Sun", 0);
        dayNameInWeekDayNumberOfWeekMap.put("Mon", 1);
        dayNameInWeekDayNumberOfWeekMap.put("Tue", 2);
        dayNameInWeekDayNumberOfWeekMap.put("Wed", 3);
        dayNameInWeekDayNumberOfWeekMap.put("Thu", 4);
        dayNameInWeekDayNumberOfWeekMap.put("Fri", 5);
        dayNameInWeekDayNumberOfWeekMap.put("Sat", 6);
    }

    public static UTCDate from(Date date) {
        return new UTCDate(date.getTime());
    }

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

    @Override public int hashCode() {
        return Objects.hash(this.hashFormat.format(this));
    }

    public UTCDate getFirstDayOfWeek() {
        String dayNameInWeek = this.dayNameInWeekFormat.format(this);
        int dayNumberOfWeek =
                this.dayNameInWeekDayNumberOfWeekMap.get(dayNameInWeek);
        return new UTCDate(this.getTime() - dayNumberOfWeek * DAY);
    }

    public UTCDate getLastDayOfWeek() {
        String dayNameInWeek = this.dayNameInWeekFormat.format(this);
        int dayNumberOfWeek =
                this.dayNameInWeekDayNumberOfWeekMap.get(dayNameInWeek);
        return new UTCDate(this.getTime() + (6 - dayNumberOfWeek) * DAY);
    }

    private SimpleDateFormat getSimpleDateFormat(String pattern) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(pattern, Locale.US);
        simpleDateFormat.setTimeZone(this.utc);
        return simpleDateFormat;
    }

    public String format(String pattern) {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(pattern);
        return simpleDateFormat.format(this);
    }
}
