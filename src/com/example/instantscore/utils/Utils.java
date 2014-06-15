package com.example.instantscore.utils;

public class Utils {

	public static String formatDate(String date) {
		if(date!=null && !date.equals("null") && !date.isEmpty()){
			String[] arr = date.split(" ");
			return arr[0].substring(0, 3) + " " + arr[1];
		}
		return "";
	}

}
