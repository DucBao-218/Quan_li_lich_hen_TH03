package com.example.thuchanhapp3;

/** Một bản ghi lấy từ bảng appointments. */
public class Appointment {
    private final long id;
    private final String title, location, date, time;
    private final boolean reminder;

    public Appointment(long id, String title, String location, String date, String time, boolean reminder) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.date = date;
        this.time = time;
        this.reminder = reminder;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public boolean isReminder() { return reminder; }

    public String getDisplayDateTime() {
        String[] parts = date.split("-");
        return parts[2] + "/" + parts[1] + "/" + parts[0] + " · " + time;
    }
}
