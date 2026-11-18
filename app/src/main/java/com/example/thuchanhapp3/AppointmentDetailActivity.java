package com.example.thuchanhapp3;

import android.app.AlertDialog;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class AppointmentDetailActivity extends BaseActivity {
    public static final String EXTRA_ID = "appointment_id";
    private DatabaseHelper database;
    private long appointmentId;
    private boolean restoringReminder;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        database = new DatabaseHelper(this);
        appointmentId = getIntent().getLongExtra(EXTRA_ID, -1);
        Appointment appointment = database.getAppointmentById(appointmentId);
        if (appointment == null) {
            Toast.makeText(this, R.string.not_found, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        ((TextView) findViewById(R.id.detail_title)).setText(appointment.getTitle());
        ((TextView) findViewById(R.id.detail_location)).setText(appointment.getLocation());
        ((TextView) findViewById(R.id.detail_datetime)).setText(appointment.getDisplayDateTime());
        Switch reminder = findViewById(R.id.detail_reminder);
        TextView status = findViewById(R.id.reminder_status);
        reminder.setChecked(appointment.isReminder());
        status.setText(appointment.isReminder() ? R.string.reminder_on : R.string.reminder_off);
        reminder.setOnCheckedChangeListener((button, checked) -> {
            if (restoringReminder) return;
            try {
                if (database.updateReminder(appointmentId, checked) != 1) {
                    Toast.makeText(this, R.string.not_found, Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                status.setText(checked ? R.string.reminder_on : R.string.reminder_off);
            } catch (SQLException exception) {
                Toast.makeText(this, R.string.save_failed, Toast.LENGTH_LONG).show();
                restoringReminder = true;
                button.setChecked(!checked);
                restoringReminder = false;
            }
        });
        findViewById(R.id.back_button).setOnClickListener(view -> finish());
        findViewById(R.id.delete_button).setOnClickListener(view -> new AlertDialog.Builder(this)
                .setTitle(R.string.delete_appointment).setMessage(R.string.delete_confirmation)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteAppointment()).show());
    }

    private void deleteAppointment() {
        try {
            int deleted = database.deleteAppointment(appointmentId);
            Toast.makeText(this, deleted == 1 ? R.string.deleted : R.string.not_found, Toast.LENGTH_SHORT).show();
            finish();
        } catch (SQLException exception) {
            Toast.makeText(this, R.string.delete_failed, Toast.LENGTH_LONG).show();
        }
    }

    @Override protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
