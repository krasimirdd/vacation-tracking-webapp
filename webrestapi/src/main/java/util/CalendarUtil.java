package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.time.temporal.ChronoUnit.DAYS;


public class CalendarUtil {

    public static boolean checkIfChronological(LocalDate fromDate, LocalDate toDate) {
        return toDate.isAfter(fromDate.minusDays(1)) && fromDate.isAfter(LocalDate.now());
    }

    public static int getBusinessDaysCount(LocalDate fromDate, LocalDate toDate) {

        long daysCount = DAYS.between(fromDate, toDate);
        return getBusinessDaysCount(fromDate, daysCount);
    }

    public static int getBusinessDaysCount(LocalDate fromDate, long daysCount) {
        int businessDays = 0;

        LocalDate caurDate = fromDate;
        for (int i = 0; i <= daysCount; i++) {
            if (isWeekDay(caurDate) && !isFederalHoliday(caurDate))
                businessDays++;
            caurDate = caurDate.plusDays(1); // add one day
        }
        return businessDays;
    }

    private static boolean isWeekDay(LocalDate caurDate) {
        String dayOfWeek = caurDate.getDayOfWeek().name();
        return !dayOfWeek.equalsIgnoreCase("saturday") && !dayOfWeek.equalsIgnoreCase("sunday");
    }

    private static boolean isFederalHoliday(LocalDate caurDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        int year = LocalDate.now().getYear();
        List<LocalDate> federalHolidays = Arrays.asList(
                LocalDate.from(formatter.parse(year + "/01/01")),
                LocalDate.from(formatter.parse(year + "/03/03")),
                LocalDate.from(formatter.parse(year + "/05/01")),
                LocalDate.from(formatter.parse(year + "/05/06")),
                LocalDate.from(formatter.parse(year + "/05/24")),
                LocalDate.from(formatter.parse(year + "/09/06")),
                LocalDate.from(formatter.parse(year + "/09/22")),
                LocalDate.from(formatter.parse(year + "/12/24")),
                LocalDate.from(formatter.parse(year + "/12/25")),
                LocalDate.from(formatter.parse(year + "/12/26")),
                //easter
                LocalDate.from(formatter.parse("2020/04/19")),
                LocalDate.from(formatter.parse("2021/05/02"))
        );
        for (LocalDate holiday : federalHolidays) {
            if (holiday.equals(caurDate))
                return true;
        }
        return false;
    }

    public static LocalDate convertToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/mm/dd");
        String dateSlash = date.replaceAll("/", "-");
        LocalDate parse = LocalDate.parse(dateSlash);
        return parse;

    }

    public static List<LocalDate> getListOfDaysBetween(
            LocalDate startDate, LocalDate endDate) {

        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        List<LocalDate> list = new ArrayList<>();
        long limit = numOfDaysBetween+1;
        for (int i = 0; ; i = i + 1) {
            if (limit-- == 0) break;
            LocalDate localDate = startDate.plusDays(i);
            list.add(localDate);
        }
        return list;
    }
}
