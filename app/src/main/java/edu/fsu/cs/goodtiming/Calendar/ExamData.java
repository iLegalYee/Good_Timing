package edu.fsu.cs.goodtiming.Calendar;

public class ExamData {
    String name;
    String date;
    String message;
    int id;
    String calendar;

    ExamData(String name,
             String date,
             String message,
             int id,
             String calendar)
    {
        this.name = name;
        this.date = date;
        this.message = message;
        this.id = id;
        this.calendar = calendar;
    }
}