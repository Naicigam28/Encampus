package com.viper.team.encampus;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewEventsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);
        ArrayList<String> events=getIntent().getExtras().getStringArrayList("Events");
        LinearLayout scroll=findViewById(R.id.EventsLin);
        scroll.removeAllViews();

        for (int i=0;i<events.size();i++)
        {
            TextView view1 =new TextView(getApplicationContext());
            view1.setText(events.get(i));
            view1.setBackgroundColor(Color.GRAY);
            scroll.addView(view1);
        }

    }
}
