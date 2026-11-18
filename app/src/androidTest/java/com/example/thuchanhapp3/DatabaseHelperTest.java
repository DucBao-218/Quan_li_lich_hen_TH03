package com.example.thuchanhapp3;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import java.io.File;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {
    private Context context;
    private DatabaseHelper database;
    private File testFile;

    @Before public void setUp() {
        Context target = InstrumentationRegistry.getInstrumentation().getTargetContext();
        testFile = new File(target.getCacheDir(), "appointments-test-" + System.nanoTime() + ".db");
        context = new ContextWrapper(target) {
            @Override public Context getApplicationContext() { return this; }
            @Override public File getDatabasePath(String name) { return testFile; }
            @Override public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                    SQLiteDatabase.CursorFactory factory) {
                return SQLiteDatabase.openOrCreateDatabase(testFile, factory);
            }
            @Override public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                    SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler handler) {
                return SQLiteDatabase.openOrCreateDatabase(testFile.getPath(), factory, handler);
            }
        };
        database = new DatabaseHelper(context);
    }

    @After public void tearDown() {
        database.close();
        SQLiteDatabase.deleteDatabase(testFile);
    }

    @Test public void seedLoginAndParameterizedQuery() {
        assertTrue(database.checkLogin("admin", "123456"));
        assertFalse(database.checkLogin("admin", "wrong"));
        assertFalse(database.checkLogin("admin' OR 1=1 --", "anything"));
        assertEquals(3, database.countAll());
        assertEquals(2, database.countReminders());
        List<Appointment> rows = database.getAllAppointments();
        assertEquals("Họp nhóm đồ án", rows.get(0).getTitle());
        assertEquals("25/10/2026 · 09:00", rows.get(0).getDisplayDateTime());
    }

    @Test public void crudSortingStatisticsAndPersistence() {
        long id = database.insertAppointment(new Appointment(0, "Lịch mới", "Địa điểm", "2026-10-26", "08:00", false));
        assertEquals(id, database.getAllAppointments().get(1).getId());
        assertEquals(4, database.countAll());
        assertEquals(1, database.updateReminder(id, true));
        assertEquals(3, database.countReminders());
        database.close();
        database = new DatabaseHelper(context);
        assertTrue(database.getAppointmentById(id).isReminder());
        assertEquals(1, database.deleteAppointment(id));
        assertNull(database.getAppointmentById(id));
        for (Appointment appointment : database.getAllAppointments()) database.deleteAppointment(appointment.getId());
        database.close();
        database = new DatabaseHelper(context);
        assertEquals(0, database.countAll());
        assertEquals(0, database.countReminders());
        assertTrue(database.getAllAppointments().isEmpty());
        assertTrue(database.checkLogin("admin", "123456"));
    }
}
