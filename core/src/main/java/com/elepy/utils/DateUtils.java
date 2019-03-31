package com.elepy.utils;

import com.elepy.exceptions.ElepyConfigException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {

    private static final Map<String, String> DATE_FORMATS = new HashMap<>();

    static {
        DATE_FORMATS.put("^\\d{8}$", "yyyyMMdd");
        DATE_FORMATS.put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
        DATE_FORMATS.put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
        DATE_FORMATS.put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
        DATE_FORMATS.put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
        DATE_FORMATS.put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
        DATE_FORMATS.put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
        DATE_FORMATS.put("^\\d{12}$", "yyyyMMddHHmm");
        DATE_FORMATS.put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
        DATE_FORMATS.put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
        DATE_FORMATS.put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
        DATE_FORMATS.put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
        DATE_FORMATS.put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
        DATE_FORMATS.put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
        DATE_FORMATS.put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
        DATE_FORMATS.put("^\\d{14}$", "yyyyMMddHHmmss");
        DATE_FORMATS.put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
        DATE_FORMATS.put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
        DATE_FORMATS.put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
        DATE_FORMATS.put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
        DATE_FORMATS.put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
        DATE_FORMATS.put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
        DATE_FORMATS.put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
    }

    private DateUtils() {
    }

    public static Date guessDate(String string) {
        String dateFormat = guessDateFormat(string);

        try {
            if (dateFormat == null) {
                return new Date(Long.parseLong(string));
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
                return simpleDateFormat.parse(string);
            }
        } catch (NumberFormatException | ParseException e) {

            throw new ElepyConfigException(String.format("Can't parse the date '%s'.", string));

        }


    }

    private static String guessDateFormat(String dateString) {
        for (Map.Entry<String, String> regexp : DATE_FORMATS.entrySet()) {
            if (dateString.toLowerCase().matches(regexp.getKey())) {
                return DATE_FORMATS.get(regexp.getValue());
            }
        }
        return null;
    }
}