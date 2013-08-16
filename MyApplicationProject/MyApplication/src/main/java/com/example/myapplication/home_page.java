package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.HorizontalListView;
/**
 * Created by user on 11/08/13.
 */
public class home_page extends Activity implements AdapterView.OnItemSelectedListener {

    private HorizontalListView mHlvCustomList_Recently_Uploaded;
    private HorizontalListView mHlvCustomList1_Last_Viewed;

    private Handler mHandler = new Handler();

    private CustomData[] mCustomData = new CustomData[] {
            new CustomData("A brief History Of Time","$99","img1"),
            new CustomData("Maths for Dummies","$199","img2"),
            new CustomData("Fifty Shades of Grey","$20","img3"),
            new CustomData("Whoops, that was not supposed to be there","FREE","img4"),
            new CustomData("Chicken Soup for Naughty souls","$49.99","img5")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_page);
        Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Make the Horizontal scrollable list
        mHlvCustomList_Recently_Uploaded = (HorizontalListView) findViewById(R.id.hlvCustomList);
        mHlvCustomList1_Last_Viewed = (HorizontalListView) findViewById(R.id.hlvCustomList2);

        setupCustomLists();

//        mHandler.postDelayed(new Runnable() {
//            public void run() {
//                doStuff();
//            }
//        }, 5000);
    }

    private void setupCustomLists() {
        // Make an array adapter using the built in android layout to render a list of strings
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, mCustomData);
        CustomArrayAdapter adapter1 = new CustomArrayAdapter(this, mCustomData);

        // Assign adapter to HorizontalListView
        mHlvCustomList_Recently_Uploaded.setAdapter(adapter);
        mHlvCustomList1_Last_Viewed.setAdapter(adapter1);
        // mHlvCustomListWithDividerAndFadingEdge.setAdapter(adapter);
    }

//    private void doStuff() {
//        Intent intent = new Intent(home_page.this, endless_list_activity.class);
//        home_page.this.startActivity(intent);
//    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        //Toast.makeText(getApplicationContext(),"Selected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

};


