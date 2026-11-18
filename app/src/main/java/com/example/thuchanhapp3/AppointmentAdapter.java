package com.example.thuchanhapp3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class AppointmentAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<Appointment> appointments;

    public AppointmentAdapter(Context context, List<Appointment> appointments) {
        inflater = LayoutInflater.from(context);
        this.appointments = appointments;
    }

    @Override public int getCount() { return appointments.size(); }
    @Override public Appointment getItem(int position) { return appointments.get(position); }
    @Override public long getItemId(int position) { return getItem(position).getId(); }
    @Override public boolean hasStableIds() { return true; }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView == null ? inflater.inflate(R.layout.item_appointment, parent, false) : convertView;
        Appointment appointment = getItem(position);
        ((TextView) row.findViewById(R.id.row_title)).setText(appointment.getTitle());
        ((TextView) row.findViewById(R.id.row_location)).setText(appointment.getLocation());
        ((TextView) row.findViewById(R.id.row_datetime)).setText(appointment.getDisplayDateTime());
        TextView reminder = row.findViewById(R.id.row_reminder);
        reminder.setText(appointment.isReminder() ? R.string.has_reminder : R.string.no_reminder);
        reminder.setTextColor(row.getContext().getColor(appointment.isReminder() ? R.color.reminder_text : R.color.text_secondary));
        reminder.setBackgroundResource(appointment.isReminder() ? R.drawable.reminder_on_badge : R.drawable.reminder_off_badge);
        return row;
    }
}
