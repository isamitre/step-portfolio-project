package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.time.format.FormatStyle;

// Class representing a comment
public class Comment {
    private String name;
    private String comment;
    private String date;

    public Comment(String n, String c){
        name = n;
        comment = c;
        date = getCurrentDate();
    }
    public Comment(String n, String d, String c){
        name = n;
        comment = c;
        date = d;
    }

    private String getCurrentDate() {
      DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);  
      LocalDateTime now = LocalDateTime.now();  
      String output = dtf.format(now);
      return output;
    }

    public String getDate(){
        return date;
    }
}