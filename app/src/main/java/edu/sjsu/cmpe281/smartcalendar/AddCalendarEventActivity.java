package edu.sjsu.cmpe281.smartcalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddCalendarEventActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText nameEditText;
    private TextView dateText;
    private TextView timeText;

    private Button dateTextBtn;
    private Button timeTextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        nameEditText = (EditText) findViewById(R.id.input_name);
        dateText = (TextView) findViewById(R.id.input_date);
        timeText = (TextView) findViewById(R.id.input_time);
        dateTextBtn = (Button) findViewById(R.id.date_button);
        timeTextBtn = (Button) findViewById(R.id.time_button);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Add New Event");

        dateTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        timeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            boolean isError = false;

            if (nameEditText.getText().equals("")) {
                nameEditText.setError("Please enter a name for your event");
                isError = true;
            }
            if (dateText.getText().equals(getString(R.string.emptyDate))) {
                dateText.setError("Please set a date");
                isError = true;
            }
            if (timeText.getText().equals(getString(R.string.emptyTime))) {
                timeText.setError("Please set a time");
                isError = true;
            }

            if (!isError) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.EVENT_NAME, nameEditText.getText().toString());
                intent.putExtra(MainActivity.EVENT_DATE, dateText.getText());
                intent.putExtra(MainActivity.EVENT_TIME, timeText.getText());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView dateText = (TextView) getActivity().findViewById(R.id.input_date);
            dateText.setText(month + "/" + day + "/" + year);
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            TextView timeText = (TextView) getActivity().findViewById(R.id.input_time);
            timeText.setText(hourOfDay + ":" + minute);
        }
    }
}
