package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.time.format.FormatStyle;

// Class representing a Comment
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
  public void setDateToCurrentTime(){
    date = getCurrentDate();
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
