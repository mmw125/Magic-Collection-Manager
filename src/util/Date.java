package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Date {
	int day, month, year;
	
	public Date(int day, int month, int year){
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	public static Date getCurrentDate(){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		java.util.Date date = new java.util.Date();
		return parseDate(dateFormat.format(date));
	}
	
	public static Date parseDate(String s){
		String[] split = s.split("/");
//		return new Date(1,1,1);
		int day = 0, month = 0, year = 0;
		try{
			day = Integer.parseInt(split[1]);
			month = Integer.parseInt(split[0]);
			year = Integer.parseInt(split[2]);
		}catch(Exception e){
			e.printStackTrace();
		}
		return new Date(month, day, year);
	}
	
	public int getDay(){ return day; }
	public int getMonth(){ return month; }
	public int getYear(){ return year; }
	public void setDay(int day){ this.day = day; }
	public void setMonth(int month){ this.month = month; }
	public void setYear(int year){ this.year = year; }
	public String toString(){ return month+"/"+day+"/"+year; }
}
