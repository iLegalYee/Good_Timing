package edu.fsu.cs.goodtiming.Calendar;

// Holds the data for each event in the showday fragment
// Accessed by the RecyclerAdapter
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