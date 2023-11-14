package service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String formatDateTime(Long instant){
        DateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return f.format(new Date(instant*1000));
    }

    public static String formatDateTime(Date date){
        DateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return f.format(date);
    }
}
