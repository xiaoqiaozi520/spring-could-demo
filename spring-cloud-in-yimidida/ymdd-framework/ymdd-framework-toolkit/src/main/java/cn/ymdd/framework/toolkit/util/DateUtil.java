package cn.ymdd.framework.toolkit.util;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public abstract class DateUtil {
    public static String format(Date date, String format) {
        return null == date ? format : new SimpleDateFormat(format).format(date);
    }

    public static String format(LocalDate date, String format) {
        return null == date ? format : date.format(DateTimeFormatter.ofPattern(format));
    }

    public static String format(LocalDateTime date, String format) {
        return null == date ? format : date.format(DateTimeFormatter.ofPattern(format));
    }

    public static String format(long unixTime, String format) {
        return format(toLocalDateTime(unixTime), format);
    }

    public static Date toDate(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (Exception e) {
            throw new RuntimeException("Date error, sDate: " + date + " format: " + format, e);
        }

    }

    public static Date toDate(long unixTime) {
        return Date.from(Instant.ofEpochSecond(unixTime));
    }

    public static LocalDate toLocalDate(String date, String format) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
    }

    public static LocalDate toLocalDate(long unixTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(String date, String format) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(format));
    }

    public static LocalDateTime toLocalDateTime(long unixTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneId.systemDefault());
    }

    public static LocalDate dateToLocalDate(long date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime timeToLocalDateTime(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    public static String now(String format) {
        return format(now(), format);
    }

    public static Long second() {
        return Long.valueOf(Instant.now().getEpochSecond());
    }

    public static Long second(LocalDate date) {
        return Long.valueOf(null == date ? 0L : date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond());
    }

    public static Long second(LocalDateTime dateTime) {
        return Long.valueOf(null == dateTime ? 0L : dateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond());
    }

    public static Long time() {
        return Long.valueOf(System.currentTimeMillis());
    }

    public static Long time(LocalDate date) {
        return Long.valueOf(null == date ? 0L : date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    public static Long time(LocalDateTime dateTime) {
        return Long.valueOf(null == dateTime ? 0L : dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    public static long getDayStart(long unixTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(toDate(unixTime));
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.toInstant().getEpochSecond();
    }

    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, days);
        return calendar.getTime();
    }

    public static int getDayOfWeek() {
        return LocalDate.now().getDayOfWeek().getValue();
    }

    public static String subMailTime(long dateTime) {
        LocalDateTime time = timeToLocalDateTime(dateTime);
        return time.getMonth().getValue() + "月" + time.getDayOfMonth() + "日 " + time.getHour() + ":" + time.getMinute();
    }
}