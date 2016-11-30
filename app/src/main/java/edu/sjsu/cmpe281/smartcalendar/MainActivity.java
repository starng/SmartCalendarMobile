package edu.sjsu.cmpe281.smartcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private CalendarListAdapter mAdapter;
    private RecyclerView recList;
    private RequestQueue queue;

    public static final int ADD_NEW_CALENDAR_EVENT = 11;
    public static final String ADD_EVENT_JSON = "addEventJson";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        queue = Volley.newRequestQueue(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(MainActivity.this, AddCalendarEventActivity.class);
                startActivityForResult(intent, ADD_NEW_CALENDAR_EVENT);
            }
        });

        getEvents();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == ADD_NEW_CALENDAR_EVENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String jsonStr = data.getStringExtra(ADD_EVENT_JSON);
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    CalendarEvent event = new CalendarEvent();
                    event.name = jsonObject.getString("eventName");
                    event.startTime = jsonObject.getString("startTime");
                    event.endTime = jsonObject.getString("endTime");
                    mAdapter.addItem(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postRequest(getString(R.string.postEventUrl), jsonStr);
            }
        }
    }

    private List<CalendarEvent> createList(int size) {
        List<CalendarEvent> result = new ArrayList<CalendarEvent>();
        for (int i=1; i <= size; i++) {
            CalendarEvent ci = new CalendarEvent();
            ci.name = CalendarEvent.NAME_PREFIX + i;
            Random r = new Random();
            ci.startTime = "2016-10-" + (r.nextInt(32 - 10) + 10) + " 17:20:00.0000";
            ci.endTime = "2016-11-" + (r.nextInt(32 - 10) + 10) + " 17:20:00.0000";
            result.add(ci);
        }

        return result;
    }

    //REST CAll to get events
    private void getEvents() {
        String url = getString(R.string.getEventsUrl);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        //events[]
                        //eventName: "Bing's Event"
                        //startTime: "asdasda"
                        //endTime: "asdad"

                        try {

                            ArrayList<CalendarEvent> eventList = new ArrayList<>();

                            //TODO: FOR LOOP START
                            while(!response.getString("eventName").equals(null)) {
                                CalendarEvent event = new CalendarEvent();

                                event.name = response.getString("eventName");
                                event.startTime = DateUtil.FormatDateView(response.getString("startTime"));
                                event.endTime = DateUtil.FormatDateView(response.getString("endTime"));
                                eventList.add(event);
                            }
                            //FOR LOOP END

                            mAdapter = new CalendarListAdapter(eventList);
                            recList.setAdapter(mAdapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );

        queue.add(getRequest);
    }

    private void postRequest(final String url, final String requestBody) {
        try {
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.getMessage());
                        }
                    }
            ) {
                @Override
                public String getBodyContentType() {
                   return String.format("application/json; charset=utf-8");
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                requestBody, "utf-8");
                        return null;
                    }
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {

        }
    }
}