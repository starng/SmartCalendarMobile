package edu.sjsu.cmpe281.smartcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private CalendarListAdapter mAdapter;

    public static final int ADD_NEW_CALENDAR_EVENT = 11;
    public static final String EVENT_NAME = "eventName";
    public static final String EVENT_DATE = "eventDate";
    public static final String EVENT_TIME = "eventTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        mAdapter = new CalendarListAdapter(createList(0));
        recList.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(MainActivity.this, AddCalendarEventActivity.class);
                startActivityForResult(intent, ADD_NEW_CALENDAR_EVENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == ADD_NEW_CALENDAR_EVENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                CalendarEvent ci = new CalendarEvent();
                ci.name = data.getStringExtra(EVENT_NAME);
                ci.date = data.getStringExtra(EVENT_DATE);
                ci.time = data.getStringExtra(EVENT_TIME);
                mAdapter.addItem(ci);
            }
        }
    }

    private List<CalendarEvent> createList(int size) {

        List<CalendarEvent> result = new ArrayList<CalendarEvent>();
        for (int i=1; i <= size; i++) {
            CalendarEvent ci = new CalendarEvent();
            ci.name = CalendarEvent.NAME_PREFIX + i;
            ci.date = CalendarEvent.SURNAME_PREFIX + i;
            ci.time = CalendarEvent.EMAIL_PREFIX + i;

            result.add(ci);

        }

        return result;
    }
}