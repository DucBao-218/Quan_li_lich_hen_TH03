package com.example.thuchanhapp3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "appointments.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, password TEXT NOT NULL)");
        db.execSQL("CREATE TABLE appointments (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title TEXT NOT NULL, location TEXT NOT NULL, date TEXT NOT NULL, "
                + "time TEXT NOT NULL, reminder INTEGER NOT NULL)");
        ContentValues user = new ContentValues();
        user.put("username", "admin");
        user.put("password", "123456");
        db.insertOrThrow("users", null, user);
        seed(db, "Họp nhóm đồ án", "Phòng B203", "2026-10-25", "09:00", true);
        seed(db, "Khám răng", "Nha khoa Minh Tâm", "2026-10-26", "14:30", true);
        seed(db, "Đi cà phê với bạn", "Highland Coffee", "2026-10-27", "19:00", false);
    }

    private void seed(SQLiteDatabase db, String title, String location, String date, String time, boolean reminder) {
        db.insertOrThrow("appointments", null, values(new Appointment(0, title, location, date, time, reminder)));
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Phiên bản 1. Khi nâng phiên bản, bổ sung migration để giữ dữ liệu.
    }

    public boolean checkLogin(String username, String password) {
        try (Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT id FROM users WHERE username = ? AND password = ? LIMIT 1",
                new String[]{username, password})) {
            return cursor.moveToFirst();
        }
    }

    public long insertAppointment(Appointment appointment) {
        return getWritableDatabase().insertOrThrow("appointments", null, values(appointment));
    }

    private ContentValues values(Appointment appointment) {
        ContentValues values = new ContentValues();
        values.put("title", appointment.getTitle());
        values.put("location", appointment.getLocation());
        values.put("date", appointment.getDate());
        values.put("time", appointment.getTime());
        values.put("reminder", appointment.isReminder() ? 1 : 0);
        return values;
    }

    public ArrayList<Appointment> getAllAppointments() {
        ArrayList<Appointment> results = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM appointments ORDER BY date ASC, time ASC, id ASC", null)) {
            while (cursor.moveToNext()) results.add(readAppointment(cursor));
        }
        return results;
    }

    public Appointment getAppointmentById(long id) {
        try (Cursor cursor = getReadableDatabase().query("appointments", null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null)) {
            return cursor.moveToFirst() ? readAppointment(cursor) : null;
        }
    }

    private Appointment readAppointment(Cursor cursor) {
        return new Appointment(cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("title")),
                cursor.getString(cursor.getColumnIndexOrThrow("location")),
                cursor.getString(cursor.getColumnIndexOrThrow("date")),
                cursor.getString(cursor.getColumnIndexOrThrow("time")),
                cursor.getInt(cursor.getColumnIndexOrThrow("reminder")) == 1);
    }

    public int updateReminder(long id, boolean reminder) {
        ContentValues values = new ContentValues();
        values.put("reminder", reminder ? 1 : 0);
        return getWritableDatabase().update("appointments", values, "id = ?", new String[]{String.valueOf(id)});
    }

    public int deleteAppointment(long id) {
        return getWritableDatabase().delete("appointments", "id = ?", new String[]{String.valueOf(id)});
    }

    public int countAll() { return count("SELECT COUNT(*) FROM appointments"); }
    public int countReminders() { return count("SELECT COUNT(*) FROM appointments WHERE reminder = 1"); }

    private int count(String sql) {
        try (Cursor cursor = getReadableDatabase().rawQuery(sql, null)) {
            return cursor.moveToFirst() ? cursor.getInt(0) : 0;
        }
    }
}
