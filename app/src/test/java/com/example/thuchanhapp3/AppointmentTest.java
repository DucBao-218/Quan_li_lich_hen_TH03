package com.example.thuchanhapp3;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppointmentTest {
    @Test public void displayDateTimeUsesVietnameseDateOrder() {
        Appointment appointment = new Appointment(1, "Họp", "B203", "2026-10-25", "09:00", true);
        assertEquals("25/10/2026 · 09:00", appointment.getDisplayDateTime());
        assertTrue(appointment.isReminder());
    }
}
