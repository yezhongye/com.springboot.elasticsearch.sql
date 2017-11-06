package com.ye.es.my.util;

import com.ye.es.my.myenum.YMDEnumBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zjx on 2017/10/20 0020.
 */
public class MyDateUtils {

    /**
     * 日期转String
     *
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    /**
     * 根据传入的年月日参数返回date对应的年月日
     * @param date
     * @param ymd
     * @return
     */
    public static String getDateByYMD(Date date,String ymd){
        SimpleDateFormat df = null;
        if(YMDEnumBean.YEAR.getName().equals(ymd)){

            df = new SimpleDateFormat("yyyy");
        }
        if(YMDEnumBean.MONTH.getName().equals(ymd)){
            df = new SimpleDateFormat("MM");
        }
        if(YMDEnumBean.DAY.getName().equals(ymd)){
            df = new SimpleDateFormat("dd");
        }
        if(YMDEnumBean.HOUR.getName().equals(ymd)){
            df = new SimpleDateFormat("HH");
        }
        if(YMDEnumBean.MINUTE.getName().equals(ymd)){
            df = new SimpleDateFormat("mm");
        }
        if(YMDEnumBean.SECOND.getName().equals(ymd)){
            df = new SimpleDateFormat("ss");
        }
        return df.format(date);
    }
}
