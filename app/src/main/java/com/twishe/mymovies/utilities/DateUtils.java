package com.twishe.mymovies.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String convert_date(String mdate){
        if(mdate.equals("0000-00-00")){
            return "N/A";
        }
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat format2 = new SimpleDateFormat("dd MMMM, yyyy",Locale.ENGLISH);
            Date date = (Date) format1.parse(mdate);
            return (format2.format(date));
        }
        catch (Exception e){
            e.printStackTrace();
            return mdate;
        }

    }
}
