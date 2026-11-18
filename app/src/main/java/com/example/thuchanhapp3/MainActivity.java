package com.example.thuchanhapp3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private DatabaseHelper database;
    private ListView list;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new DatabaseHelper(this);
        list = findViewById(R.id.appointment_list);
        list.setEmptyView(findViewById(R.id.empty_list));
        findViewById(R.id.add_button).setOnClickListener(view ->
                startActivity(new Intent(this, AddAppointmentActivity.class)));
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, AppointmentDetailActivity.class);
            intent.putExtra(AppointmentDetailActivity.EXTRA_ID, id);
            startActivity(intent);
        });
    }

    @Override protected void onResume() {
        super.onResume();
        ArrayList<Appointment> appointments = database.getAllAppointments();
        list.setAdapter(new AppointmentAdapter(this, appointments));
        ((TextView) findViewById(R.id.total_count)).setText(String.valueOf(database.countAll()));
        ((TextView) findViewById(R.id.reminder_count)).setText(String.valueOf(database.countReminders()));
    }

    @Override protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
