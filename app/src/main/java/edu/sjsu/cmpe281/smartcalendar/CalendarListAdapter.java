package edu.sjsu.cmpe281.smartcalendar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CalendarListAdapter extends RecyclerView.Adapter <CalendarListAdapter.CalendarViewHolder>{
    private List<CalendarEvent> entryList;

    public CalendarListAdapter(List<CalendarEvent> entryList) {
        this.entryList = entryList;
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_layout, parent, false);

        return new CalendarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CalendarViewHolder holder, int position) {
        CalendarEvent entry = entryList.get(position);
        holder.vName.setText(entry.name);
        holder.vDate.setText(entry.date);
        holder.vTime.setText(entry.time);
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vDate;
        protected TextView vTime;

        public CalendarViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.eventName);
            vDate = (TextView) v.findViewById(R.id.eventDate);
            vTime = (TextView) v.findViewById(R.id.eventTime);
        }
    }

    public void addItem(CalendarEvent event) {
        entryList.add(event);
        notifyDataSetChanged();
    }
}
