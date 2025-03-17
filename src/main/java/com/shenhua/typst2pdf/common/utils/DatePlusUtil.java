package com.shenhua.typst2pdf.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类 基于 java8之后引入的新的日期时间API
 */
public class DatePlusUtil {
    public static final String DAY_START_ = " 00:00:00";
    public static final String DAY_END_ = " 23:59:59";

    /**
     * 标准的日期转换格式
     */
    public static final String STD_DAY_STR_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String STD_DAY_STR_FORMAT_SHORT = "yyyy-MM-dd";
    public static final String STD_DAY_STR_FORMAT_YEAR_MONTH = "yyyy-MM";

    /**
     * 标准时间转换类
     */
    public static final DateTimeFormatter STD_FORMAT = DateTimeFormatter.ofPattern(STD_DAY_STR_FORMAT);
    public static final DateTimeFormatter STD_FORMAT_SHORT = DateTimeFormatter.ofPattern(STD_DAY_STR_FORMAT_SHORT);
    /**
     * 这种格式format没问题，但是parse就需要使用YearMonth.parse
     */
    public static final DateTimeFormatter STR_FORMAT_YEAR_MONTH = DateTimeFormatter.ofPattern(STD_DAY_STR_FORMAT_YEAR_MONTH);


    /**
     * 获取当前日期时间
     *
     * @return 当前日期时间
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /**
     * 将 LocalDateTime 转换为指定格式字符串
     *
     * @param dateTime LocalDateTime 对象
     * @param pattern 日期时间格式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    /**
     * 将指定格式字符串解析为 LocalDateTime
     *
     * @param str 字符串日期时间
     * @param pattern 日期时间格式
     * @return LocalDateTime 对象
     */
    public static LocalDateTime parse(String str, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(str, formatter);
    }

    /**
     * 获取指定日期的结束时间（23:59:59）
     * 相当于2023-03-22变成2023-03-22 23:59:59
     *
     * @param date 指定日期
     * @return 指定日期的结束时间
     */
    public static LocalDateTime getEndOfDay(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MAX);
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param start 开始日期
     * @param end 结束日期
     * @return 相差的天数
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        return Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param start 开始日期
     * @param end 结束日期
     * @return 相差的天数
     */
    public static long daysBetween(Date start, Date end) {
        return Duration.between(date2LocalDate(start).atStartOfDay(), date2LocalDate(end).atStartOfDay()).toDays();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param start 开始日期
     * @param end 结束日期
     * @return 相差的天数
     */
    public static long daysBetween(String start, String end) {
        return Duration.between(dateStr2LocalDate(start).atStartOfDay(), dateStr2LocalDate(end).atStartOfDay()).toDays();
    }

    /**
     * 获取当前时间N天前的日期时间
     *
     * @param days
     * @return
     */
    public static LocalDateTime minusDays(Integer days) {
        LocalDateTime now = LocalDateTime.now();
        return now.minusDays(days);
    }

    /**
     * 获取当前时间N天后的日期时间
     *
     * @param days
     * @return
     */
    public static LocalDateTime afterDays(Integer days) {
        LocalDateTime now = LocalDateTime.now();
        return now.plusDays(days);
    }

    /**
     * 获取当前时间N天后的日期时间
     *
     * @param localDate
     * @param days
     * @return
     */
    public static String afterDays(String localDate, Integer days) {
        return dateStr2LocalDate(localDate).plusDays(days).format(STD_FORMAT_SHORT);
    }

    /**
     * 获取特定时间N天后的日期
     *
     * @param localDateTime
     * @param days
     * @return
     */
    public static LocalDateTime afterDays(LocalDateTime localDateTime, Integer days) {
        return localDateTime.plusDays(days);
    }

    /**
     * 获取特定时间N天后的日期
     *
     * @param localDate
     * @param days
     * @return
     */
    public static LocalDate afterDays(LocalDate localDate, Integer days) {
        return localDate.plusDays(days);
    }

    /**
     * 获取特定时间N月后的日期
     *
     * @param localDate
     * @param months
     * @return
     */
    public static LocalDate afterMonths(LocalDate localDate, Integer months) {
        return localDate.plusMonths(months);
    }

    /**
     * 获取特定时间N月后的日期
     * @param localDate yyyy-MM-dd
     * @param months
     * @return
     */
    public static String afterMonths(String localDate, Integer months) {
        return dateStr2LocalDate(localDate).plusMonths(months).format(STD_FORMAT_SHORT);
    }

    /**
     * 获取特定时间N天前的日期
     *
     * @param localDateTime
     * @param days
     * @return
     */
    public static LocalDateTime beforeDays(LocalDateTime localDateTime, Integer days) {
        return localDateTime.minusDays(days);
    }

    /**
     * 获取当前时间N天前的日期时间
     *
     * @param days
     * @return
     */
    public static LocalDateTime beforeDays(Integer days) {
        LocalDateTime now = LocalDateTime.now();
        return now.minusDays(days);
    }

    /**
     * 获取特定时间N天前的日期
     *
     * @param localDateTime
     * @param days
     * @return
     */
    public static String beforeDaysStr(LocalDateTime localDateTime, Integer days) {
        return localDateTime.minusDays(days).format(STD_FORMAT_SHORT);
    }

    /**
     * 标准的日期转换成字符串
     *
     * @param localDateTime
     * @return
     */
    public static String stdLocalDateTime2String(LocalDateTime localDateTime) {
        // Java8之后日期转字符串，使用DateTimeFormatter, 线程安全，可用于多线程场景
        // DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return STD_FORMAT_SHORT.format(localDateTime);
    }

    /**
     * 标准的日期转换成字符串
     *
     * @param localDateTime
     * @return
     */
    public static String stdLocalDateTime2String(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.format(localDateTime);
    }

    /**
     * 标准的日期转换成字符串
     *
     * @param localDate
     * @return
     */
    public static String stdLocalDate2String(LocalDate localDate) {
        // Java8之后日期转字符串，使用DateTimeFormatter, 线程安全，可用于多线程场景
        // DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return STD_FORMAT_SHORT.format(localDate);
    }

    /**
     * 标准的日期转换成字符串
     *
     * @param localDate
     * @return
     */
    public static String stdLocalDate2String(LocalDate localDate, DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.format(localDate);
    }

    /**
     * 获得当前日期
     * @return
     */
    public static String getCurrentDay() {
        return STD_FORMAT_SHORT.format(now());
    }

    /**
     * 获取当前的前一天
     *
     * @return
     */
    public static String getPreviousDay() {
        LocalDate today = LocalDate.now();
        LocalDate previousDay = today.minusDays(1);
        return previousDay.format(STD_FORMAT_SHORT);
    }

    /**
     * 获取当前的后一天
     *
     * @return
     */
    public static String getAfterDay() {
        LocalDate today = LocalDate.now();
        LocalDate previousDay = today.plusDays(1);
        return previousDay.format(STD_FORMAT_SHORT);
    }

    /**
     * 字符串转换成日期时期时间
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime dateStr2LocalDateTime(String dateStr) {
        // Java8之后字符串转日期，使用DateTimeFormatter, 线程安全，可用于多线程场景
        // DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateStr, STD_FORMAT);
        // or
        // LocalDate of = LocalDate.of(2023, 3, 20);
    }

    /**
     * 字符串转换成日期时间
     *
     * @param dateStr
     * @return
     */
    public static LocalDate dateStr2LocalDate(String dateStr) {
        return LocalDate.parse(dateStr, STD_FORMAT_SHORT);
    }

    /**
     * LocalDate转化Date
     * @param localDate
     * @return
     */
    public static Date localDate2Date(LocalDate localDate){
        // (1)LocalDate转化Date， java.time.LocalDate.atStartOfDay()方法将此日期与午夜时间组合在一起，以便在此日期开始创建LocalDateTime
        // ZoneId.systemDefault()返回系统默认时区
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
        // date 即是转换结果，  转成string查看下
        // System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(date));
    }

    /**
     * Date转换为LocalDate
     * @param date
     * @return
     */
    public static LocalDate date2LocalDate(Date date){
        // (2)Date转换为LocalDate
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // localDate即是转换结果，  转成string查看下
        // System.out.println(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }


    public static int getYear(LocalDateTime localDateTime) {
        return localDateTime.getYear();
    }

    public static int getMonth(LocalDateTime localDateTime) {
        return localDateTime.getMonthValue();
    }

    public static int getDay(LocalDateTime localDateTime) {
        return localDateTime.getDayOfMonth();
    }

    public static int getYear() {
        return LocalDateTime.now().getYear();
    }

    public static int getMonth() {
        return LocalDateTime.now().getMonthValue();
    }

    public static String getMonthString() {
        int month = LocalDateTime.now().getMonthValue();
        if (month < 10) {
            return "0" + month;
        } else {
            return String.valueOf(month);
        }
    }

    /**
     * @return 2024-01 获取当前年月
     */
    public static String getYearMonth() {
        return LocalDateTime.now().format(STR_FORMAT_YEAR_MONTH);
    }

    public static int getDay() {
        return LocalDateTime.now().getDayOfMonth();
    }


    /**
     * 在区间内获取reviewDays的各个节点
     *
     * @param start "2022-01-01"
     * @param end "2022-01-10"
     * @param reviewDays 7
     * @return 2022-01-01  2022-01-08
     */
    public static List<String> getMilestoneInSection(String start, String end, Integer reviewDays) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(STD_DAY_STR_FORMAT_SHORT);
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);

        List<String> milestones = new ArrayList<>();
        while (!startDate.isAfter(endDate)) {
            startDate = startDate.plusDays(reviewDays);
            milestones.add(startDate.toString());
        }
        return milestones;
    }

    /**
     * 专为review区间做的
     * @param start
     * @param end
     * @param reviewDays
     * @return
     */
    public static Boolean checkCurrentNowInThisMilestones(String start, String end, Integer reviewDays) {
        List<String> milestones = getMilestoneInSection(start, end, reviewDays);
        String current = STD_FORMAT_SHORT.format(LocalDate.now());
        return milestones.contains(current);
    }

    /**
     * 检测当前的时间是否是设定时间的前 7 天
     *
     * @param dateStr 设定的时间字符串，格式为 yyyy-MM-dd
     * @return 如果当前时间在设定时间的前 7 天 这点，则返回 true；否则返回 false。
     */
    public static boolean isInLastTimePoint(String dateStr, Integer timePoint) {
        // 将设定的时间字符串解析为 LocalDate 对象
        LocalDate targetDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(STD_DAY_STR_FORMAT_SHORT));

        // 获取当前时间，并转换为 LocalDate 对象
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        LocalDate today = now.toLocalDate();

        // 计算相差天数，并判断是否等于 7
        long days = targetDate.toEpochDay() - today.toEpochDay();
        return days == timePoint;
    }

    /**
     * 计算两个时间间隔的天数
     * @param date1
     * @param date2
     * @return
     */
    public static Integer getDaysBetween(String date1, String date2) {
        try {
            LocalDate localDate1 = LocalDate.parse (date1, DateTimeFormatter.ofPattern(STD_DAY_STR_FORMAT_SHORT));
            LocalDate localDate2 = LocalDate.parse (date2, DateTimeFormatter.ofPattern(STD_DAY_STR_FORMAT_SHORT));
            return (int)Math.abs (ChronoUnit.DAYS.between (localDate1, localDate2));
        } catch (Exception e) {
            // 避免其报错
            return 0;
        }
    }

    /**
     * 当前月份是否在6月之前，不包含6月
     * @return
     */
    public static boolean beforeJune() {
        return getMonth() < 6;
    }

    /**
     * year是否是当前年份
     * @param year
     * @return
     */
    public static boolean isCurrentYear(String year) {
        return String.valueOf(getYear()).equals(year);
    }

    /**
     * 获取当前月份的前X月 是 何年和月
     * 如当前2024-01 那么 X=6 月之前是2023-07
     * @param X
     * @return
     */
    public static String getPreviousXMonth(Integer X) {
        LocalDateTime now = LocalDateTime.now();
        // x月之前
        LocalDateTime xMonthsAgo = now.minusMonths(X);
        return xMonthsAgo.format(STR_FORMAT_YEAR_MONTH);
    }

    /**
     * 传入两个yyyy-MM的年月，获得其中所有的月份, 左右包含
     * @param start yyyy-MM
     * @param end yyyy-MM
     * @return
     */
    public static List<String> getMonthsBetween(String start, String end) {
        YearMonth startDate = YearMonth.parse(start, STR_FORMAT_YEAR_MONTH);
        YearMonth endDate = YearMonth.parse(end, STR_FORMAT_YEAR_MONTH);

        List<String> months = new ArrayList<>();
        while (!startDate.isAfter(endDate)) {
            months.add(startDate.format(STR_FORMAT_YEAR_MONTH));
            startDate = startDate.plusMonths(1);
        }
        return months;
    }

    /**
     * 传入两个yyyy-MM的年月，获得其中所有的月份, 左闭右开
     * @param start yyyy-MM
     * @param end yyyy-MM
     * @return
     */
    public static List<String> getMonthsBetweenNoIncludeEnd(String start, String end) {
        YearMonth startDate = YearMonth.parse(start, STR_FORMAT_YEAR_MONTH);
        YearMonth endDate = YearMonth.parse(end, STR_FORMAT_YEAR_MONTH);

        List<String> months = new ArrayList<>();
        while (startDate.isBefore(endDate)) {
            months.add(startDate.format(STR_FORMAT_YEAR_MONTH));
            startDate = startDate.plusMonths(1);
        }
        return months;
    }

    /**
     * 传入两个yyyy-MM-dd的日期，获得其中所有的日期, 左右包含
     * @param start yyyy-MM-dd
     * @param end yyyy-MM-dd
     * @return
     */
    public static List<String> getDatesBetween(String start, String end) {
        LocalDate startDate = LocalDate.parse(start, STD_FORMAT_SHORT);
        LocalDate endDate = LocalDate.parse(end, STD_FORMAT_SHORT);

        List<String> dates = new ArrayList<>();
        while (!startDate.isAfter(endDate)) {
            dates.add(startDate.format(STD_FORMAT_SHORT));
            startDate = startDate.plusDays(1);
        }
        return dates;
    }

    /**
     * 传入两个yyyy-MM-dd的日期，获得其中所有的日期, 左右包含
     * @param startDate yyyy-MM-dd
     * @param endDate yyyy-MM-dd
     * @return
     */
    public static List<String> getDatesBetween(LocalDate startDate, LocalDate endDate) {

        List<String> dates = new ArrayList<>();
        while (!startDate.isAfter(endDate)) {
            dates.add(startDate.format(STD_FORMAT_SHORT));
            startDate = startDate.plusDays(1);
        }
        return dates;
//        return startDate.datesUntil(endDate.plusDays(1))
//                .map(LocalDate::toString)
//                .collect(Collectors.toList());
    }

    /**
     * 校验当前时间是否在两个区间之内
     * 左右包含
     * @param startTime yyyy-MM-dd
     * @param endTime yyyy-MM-dd
     * @return
     */
    public static boolean checkCurrentNowWithinTwoIntervals(String startTime, String endTime) {
        LocalDate start = LocalDate.parse(startTime, STD_FORMAT_SHORT);
        LocalDate end = LocalDate.parse(endTime, STD_FORMAT_SHORT);
        LocalDate now = LocalDate.now();
        return (now.isAfter(start) || now.equals(start)) && (now.isBefore(end) || now.equals(end));
    }

    /**
     * 传入年月，输出年月日
     * 日是当前年月的最后一天
     * @param yearMonth yyyy-MM
     * @return
     */
    public static String getLastDayOfMonth(String yearMonth) {
        YearMonth ym = YearMonth.parse(yearMonth);
        LocalDate lastDay = ym.atEndOfMonth();
        return lastDay.toString();
    }

    /**
     * A 比 B 更晚吗
     * A = B 也视为 A 比 B 更晚
     *
     *
     * @param yyyyMMddA yyyy-MM-dd
     * @param yyyyMMddB yyyy-MM-dd
     * @return
     */
    public static boolean isALaterThanB(String yyyyMMddA, String yyyyMMddB) {
        LocalDate dateA = LocalDate.parse(yyyyMMddA, STD_FORMAT_SHORT);
        LocalDate dateB = LocalDate.parse(yyyyMMddB, STD_FORMAT_SHORT);

        return dateA.isAfter(dateB) || dateA.equals(dateB);
    }

    /**
     * A 比 B 更晚吗
     * A > B 才视为 A 比 B 更晚
     *
     *
     * @param yyyyMMddA yyyy-MM-dd
     * @param yyyyMMddB yyyy-MM-dd
     * @return
     */
    public static boolean isALaterThanBIgnoreEqual(String yyyyMMddA, String yyyyMMddB) {
        LocalDate dateA = LocalDate.parse(yyyyMMddA, STD_FORMAT_SHORT);
        LocalDate dateB = LocalDate.parse(yyyyMMddB, STD_FORMAT_SHORT);

        return dateA.isAfter(dateB);
    }

    /**
     * 判断date是否能够成功转换为一个可用的日期
     * @param dateStr
     * @param sdf
     * @return
     */
    public static boolean checkIt(String dateStr, DateTimeFormatter sdf) {
        try {
            sdf.parse(dateStr);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 传入字符类型日期，在这个日期上减去hours得到的日期
     * @param dateStr yyyy-MM-dd
     * @param hours
     * @return
     */
    public static String subtractHours(String dateStr, int hours) {
        LocalDateTime dateTime = LocalDateTime.parse(dateStr + DAY_START_, STD_FORMAT);
        LocalDateTime result = dateTime.minusHours(hours);
        return result.format(STD_FORMAT_SHORT);
    }

    /**
     * 获取一个yyyy-MM-dd格式集合中最小的一个日期字符串
     * @param dateStrList
     * @return
     */
    public static String getMinDate(List<String> dateStrList) {
        LocalDate minDate = null;
        String minDateStr = null;

        for (String dateStr : dateStrList) {
            LocalDate currentDate = LocalDate.parse(dateStr, STD_FORMAT_SHORT);
            if (minDate == null || currentDate.isBefore(minDate)) {
                minDate = currentDate;
                minDateStr = dateStr;
            }
        }

        return minDateStr;
    }

    /**
     * 获取一个yyyy-MM-dd格式集合中最大的一个日期字符串
     * @param dateStrList
     * @return
     */
    public static String getMaxDate(List<String> dateStrList) {
        LocalDate maxDate = null;
        String maxDateStr = null;

        for (String dateStr : dateStrList) {
            LocalDate currentDate = LocalDate.parse(dateStr, STD_FORMAT_SHORT);
            if (maxDate == null || currentDate.isAfter(maxDate)) {
                maxDate = currentDate;
                maxDateStr = dateStr;
            }
        }

        return maxDateStr;
    }

    /**
     * 获取dateStr往后推迟一天的日期
     * @param dateStr yyyy-MM-dd 字符串
     * @return
     */
    public static String getNextDate(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, STD_FORMAT_SHORT);
        LocalDate nextDate = date.plusDays(1);
        return nextDate.format(STD_FORMAT_SHORT);
    }

    /**
     * 传入两组startDate、endDate（yyyy-MM-dd），判断这两组的交集有多少天
     * @param startDate1
     * @param endDate1
     * @param startDate2
     * @param endDate2
     * @return
     */
    public static int intersectionDays(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        LocalDate startIntersection = startDate1.isAfter(startDate2) ? startDate1 : startDate2;
        LocalDate endIntersection = endDate1.isBefore(endDate2)? endDate1 : endDate2;

        if (startIntersection.isAfter(endIntersection)) {
            return 0;
        }

        return (int) startIntersection.datesUntil(endIntersection.plusDays(1)).count();
    }

    /**
     * 传入两组startDate、endDate（yyyy-MM-dd），判断这两组的交集有多少天
     * @param startDate1Str （yyyy-MM-dd）
     * @param endDate1Str （yyyy-MM-dd）
     * @param startDate2Str （yyyy-MM-dd）
     * @param endDate2Str （yyyy-MM-dd）
     * @return
     */
    public static int intersectionDays(String startDate1Str, String endDate1Str, String startDate2Str, String endDate2Str) {
        LocalDate startDate1 = LocalDate.parse(startDate1Str, STD_FORMAT_SHORT);
        LocalDate endDate1 = LocalDate.parse(endDate1Str, STD_FORMAT_SHORT);
        LocalDate startDate2 = LocalDate.parse(startDate2Str, STD_FORMAT_SHORT);
        LocalDate endDate2 = LocalDate.parse(endDate2Str, STD_FORMAT_SHORT);
        LocalDate startIntersection = startDate1.isAfter(startDate2) ? startDate1 : startDate2;
        LocalDate endIntersection = endDate1.isBefore(endDate2)? endDate1 : endDate2;

        if (startIntersection.isAfter(endIntersection)) {
            return 0;
        }

        return (int) startIntersection.datesUntil(endIntersection.plusDays(1)).count();
    }

    /**
     * 传入两个yyyy-MM-dd，然后比对当天日期，判断当天执行到的百分比，如今天是2024-08-19
     * startTime = 2024-08-18
     * endTime = 2024-08-20
     * 那么planProgress = 50%
     *
     * 如果在此区间前，为0
     * 如果在此区间后，为100
     * @param startDate
     * @param endDate
     * @return 返回整数
     */
    public static Integer calculateProgress(String startDate, String endDate) {
        if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
            return null;
        }

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        LocalDate today = LocalDate.now();

        if (today.isBefore(start)) {
            return 0;
        }
        if (today.isAfter(end)) {
            return 100;
        }

        long totalDays = end.toEpochDay() - start.toEpochDay() + 1;
        long passedDays = today.toEpochDay() - start.toEpochDay();

        double progress = (double) passedDays / totalDays * 100;
        return (int) Math.round(progress);
    }

    public static Date getFirstWeekBeginDate() {
        Date yearFirstDay = getYearFirstDay();
        Date beginDate = yearFirstDay;
        while (true) {
            int weekOfYear = getWeekOfYear(beginDate);
            if (weekOfYear == 1) {
                break;
            } else {
                beginDate = DateUtils.addDays(beginDate, 1);
            }
        }
        return beginDate;
    }

    public static Date getLastWeekEndDate() {
        Date yearEndDay = DateUtils.addDays(DateUtils.addYears(getYearFirstDay(), 1), -1);
        Date endDate = yearEndDay;
        while (true) {
            int weekOfYear = getWeekOfYear(endDate);
            if (weekOfYear != 1) {
                break;
            } else {
                endDate = DateUtils.addDays(endDate, -1);
            }
        }
        return endDate;
    }

    public static Date getYearFirstDay() {
        Calendar currentInstance = Calendar.getInstance();
        Calendar result = Calendar.getInstance();
        result.setTimeInMillis(0);
        result.set(currentInstance.get(Calendar.YEAR), 0, 1, 0, 0, 0);
        return result.getTime();
    }

    public static Date getYearLastDay() {
        Date yearFirstDay = getYearFirstDay();
        return DateUtils.addDays(DateUtils.addYears(yearFirstDay, 1), -1);
    }

    public static int getDayOfWeek(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.DAY_OF_WEEK);
    }

    public static int getWeekOfYear(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getYear(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.YEAR);
    }

    public static int getDayOfYear(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.DAY_OF_YEAR);
    }

    public static int getDayOfMonth(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.DAY_OF_MONTH);
    }

    public static Date getTodayWithZero() {
        Calendar currentInstance = Calendar.getInstance();
        Calendar result = Calendar.getInstance();
        result.setTimeInMillis(0);
        result.set(currentInstance.get(Calendar.YEAR), currentInstance.get(Calendar.MONTH), currentInstance.get(Calendar.DATE), 0, 0, 0);
        return result.getTime();
    }

    public static Date getPreviousWeekMonDay() {
        Date todayWithZero = DatePlusUtil.getTodayWithZero();
        int dayOfWeek = DatePlusUtil.getDayOfWeek(todayWithZero);
        Date date;
        if (dayOfWeek == 1) {
            date = DateUtils.addDays(todayWithZero, -13);
        } else {
            date = DateUtils.addDays(todayWithZero, -7 - dayOfWeek + 2);
        }
        return date;
    }

     public static void main(String[] args) {
//         List<String> dateStrList = Lists.newArrayList("2024-07-10", "2024-07-25", "2024-07-25", "", null);
//
//         // System.out.println(getMinDate(dateStrList));
//         System.out.println(getMaxDate(dateStrList.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList())));
//         System.out.println(intersectionDays("2024-07-31", "2024-08-02", "2024-08-03", "2024-08-07"));
         System.out.println(daysBetween("2024-08-08", "2024-08-04"));
         System.out.println(currentClockIsBetween("22:10", "23:59"));
     }

    /**
     * 当前时钟是否处于两个时间区间内
     * 如11:00处于10:30~12:00
     * @param startClock 10:30 格式为 "HH:mm"
     * @param endClock 12:00 格式为 "HH:mm"
     * @return
     *
     * ex: currentClockIsBetween("22:30", "23:59")
     */
    public static boolean currentClockIsBetween(String startClock, String endClock) {
        LocalTime currentTime = LocalTime.now();
        LocalTime startTime = LocalTime.parse(startClock);
        LocalTime endTime = LocalTime.parse(endClock);

        // 判断当前时间是否大于等于起始时间且小于等于结束时间
        // 需要考虑跨天的情况，比如起始时间是23:00，结束时间是01:00
        if (startTime.isBefore(endTime)) {
            return currentTime.isAfter(startTime) && currentTime.isBefore(endTime);
        } else {
            // 跨天的情况，当前时间大于等于起始时间或者小于等于结束时间则在区间内
            return currentTime.isAfter(startTime) || currentTime.isBefore(endTime);
        }
    }
}
