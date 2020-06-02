package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;

// Class representing a comment
public class Comment {
    private String name;
    private String comment;

    public Comment(){
        name="";
        comment="";
    }
    public Comment(String n, String c){
        name = n;
        comment = c;
    }

    public void setName(String n){
        name = n;
    }
    public void setComment(String c){
        comment = c;
    }
    public String getName(){
        return name;
    }
    public String getComment(){
        return comment;
    }
}