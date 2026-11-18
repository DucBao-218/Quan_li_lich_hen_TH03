package com.example.thuchanhapp3;

import android.database.SQLException;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Locale;

public class AddAppointmentActivity extends BaseActivity {
    private DatabaseHelper database;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        ((android.widget.TextView) findViewById(R.id.toolbar_title)).setText(R.string.add_appointment);
        findViewById(R.id.toolbar_back).setOnClickListener(view -> finish());
        database = new DatabaseHelper(this);
        EditText title = findViewById(R.id.appointment_title);
        EditText location = findViewById(R.id.appointment_location);
        DatePicker datePicker = findViewById(R.id.date_picker);
        TimePicker timePicker = findViewById(R.id.time_picker);
        Switch reminder = findViewById(R.id.reminder_switch);
        timePicker.setIs24HourView(true);
        if (savedInstanceState == null) {
            Calendar initial = Calendar.getInstance();
            initial.add(Calendar.HOUR_OF_DAY, 1);
            datePicker.updateDate(initial.get(Calendar.YEAR), initial.get(Calendar.MONTH), initial.get(Calendar.DAY_OF_MONTH));
            timePicker.setHour(initial.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(initial.get(Calendar.MINUTE));
        }
        findViewById(R.id.cancel_button).setOnClickListener(view -> finish());
        findViewById(R.id.save_button).setOnClickListener(view -> {
            // Commit text typed directly into the spinner before reading its value.
            datePicker.clearFocus();
            timePicker.clearFocus();
            String name = title.getText().toString().trim();
            String place = location.getText().toString().trim();
            if (name.isEmpty() || place.isEmpty()) {
                if (name.isEmpty()) title.setError(getString(R.string.required_title));
                if (place.isEmpty()) location.setError(getString(R.string.required_location));
                return;
            }
            Calendar selected = Calendar.getInstance();
            selected.clear();
            selected.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                    timePicker.getHour(), timePicker.getMinute(), 0);
            if (selected.getTimeInMillis() <= System.currentTimeMillis()) {
                Toast.makeText(this, R.string.future_required, Toast.LENGTH_LONG).show();
                return;
            }
            String date = String.format(Locale.ROOT, "%04d-%02d-%02d", datePicker.getYear(),
                    datePicker.getMonth() + 1, datePicker.getDayOfMonth());
            String time = String.format(Locale.ROOT, "%02d:%02d", timePicker.getHour(), timePicker.getMinute());
            try {
                database.insertAppointment(new Appointment(0, name, place, date, time, reminder.isChecked()));
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                finish();
            } catch (SQLException exception) {
                Toast.makeText(this, R.string.save_failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
