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

    public Comment(){
        name="";
        comment="";
        date="";
    }
    public Comment(String n, String c){
        name = n;
        comment = c;
        date = getCurrentDate();
    }

    public void setName(String n){
        name = n;
    }
    public void setComment(String c){
        comment = c;
    }
    public void setDate(){
      date = getCurrentDate();
    }
    public String getName(){
        return name;
    }
    public String getComment(){
        return comment;
    }
    public String getDate() {
        return date;
    }

    private String getCurrentDate() {
      DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);  
      LocalDateTime now = LocalDateTime.now();  
      String output = dtf.format(now);
      return output;
    }
}