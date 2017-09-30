package com.example.jcowan_countbook;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jonah Cowan on 2017-09-14.
 */


//Class stores all information about a Counter which will be created by the user in mainActivity
public class Counter {
    private String name;
    private String comment;

    private int initialValue;
    private int currentValue;

    private Date lastEdit;

    //Counters require a name and initial value to be initialized
    //A comment can also be added
    public Counter(String name, String comment, int initialValue){
        this.name = name;
        this.comment = comment;
        this.initialValue = initialValue;
        this.currentValue = initialValue;
        this.lastEdit = new Date();
    }
    //If no comment is provided, the Counter will initialze comment with "No Comment"
    public Counter(String name, int initialValue){
        this.name = name;
        this.comment = "No Comment";
        this.initialValue = initialValue;
        this.currentValue = initialValue;
        this.lastEdit = new Date();
    }

    //getters and setters
    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getComment(){
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getInitialValue(){
        return initialValue;
    }
    public void setInitialValue(int initialValue) {
        this.initialValue = initialValue;
    }
    //Allows for easy printing of the inital value.
    public String getInitialValueAsString(){
        String valueAsString = Integer.toString(initialValue);
        return valueAsString;
    }

    //allows for easy printing of the current value
    public String getCurrentValueAsString(){
        String valueAsString = Integer.toString(currentValue);
        return valueAsString;
    }
    public int getCurrentValue() {
        return currentValue;
    }
    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    //allows for easy printing of the last edit date
    public String getLastEditAsString() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormatter.format(lastEdit);
        return formattedDate;
    }
    public void setLastEdit(Date lastEdit) {
        this.lastEdit = lastEdit;
    }
}
