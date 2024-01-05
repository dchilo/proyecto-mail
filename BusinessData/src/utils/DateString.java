/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author ronal
 */
public class DateString {
    
    public static Calendar StringToDate(String date){
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd");
        try{
            Calendar dt = Calendar.getInstance();
            dt.setTime(format.parse(date));
            return dt;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String DateToString(Calendar date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try{
            String dt = format.format(date);
            return dt;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static Calendar StringToDatetime(String date){
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        try{
            Calendar dt = Calendar.getInstance();
            dt.setTime(format.parse(date));
            return dt;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String DatetimeToString(Calendar date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            String dt = format.format(date);
            return dt;
        }catch (Exception e){
        }
        return null;
    }
   
    public static Date StringToDateSQL(String date)throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd");
        Date dt = new Date(format.parse(date).getTime());
        return dt;
    }
 
    public static String DateSQLToString(Date date)throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dt = format.format(date);
        return dt;
    }
    
    public static Timestamp StringToDatetimeSQL(String date)throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Timestamp ts = new Timestamp(format.parse(date).getTime());
        System.out.println(ts);
        return ts;
    }

    public static String timestampToStringWithoutTimeZone(Timestamp timestamp) {
        // Crear un objeto SimpleDateFormat con el formato deseado
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // Formatear el Timestamp a una cadena sin la parte de la zona horaria
        return format.format(timestamp);
    }
    
    public static String DatetimeSQLToString(Timestamp date)throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String dt = format.format(date);
        return dt;
    }
}
