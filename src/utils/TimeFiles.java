/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author carolyn.sher
 */
public class TimeFiles {
       public static Calendar stringToCalendar(String strDate) throws ParseException{
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
       Date date = sdf. parse(strDate);
       Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
   } 
}
