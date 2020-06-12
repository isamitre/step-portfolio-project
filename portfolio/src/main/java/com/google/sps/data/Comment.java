package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.time.format.FormatStyle;

// Class representing a Comment
public class Comment {
  private float id;
  private String author;
  private String commentText;
  private String date;
  private float sentiment;

  public Comment(float id, String author, String commentText, float sentiment){
    this.id = id;
    this.author = author;
    this.commentText = commentText;
    date = getCurrentDate();
    this.sentiment = sentiment;
  }
  public Comment(float id, String author, String commentText, float sentiment, String date){
    this.id = id;
    this.author = author;
    this.commentText = commentText;
    this.date = date;
    this.sentiment = sentiment;
  }
  public void setDateToCurrentTime(){
    date = getCurrentDate();
  }
  public String getDate() {
    return date;
  }

  public static String getCurrentDate() {
    DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);  
    LocalDateTime now = LocalDateTime.now();  
    String output = dtf.format(now);
    return output;
  }
}
