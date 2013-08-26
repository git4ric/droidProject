package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SearchView;
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
    private int LOGIN_REQUEST_CODE = 1;
    private boolean mUserLoggedIn;
    private TextView mWelcomeText;

    // Need to fetch data
    private CustomData[] mCustomData = new CustomData[] {
            new CustomData(12,"A Game of Thrones","$99","img1"),
            new CustomData(13,"A Clash of Kings","$99","img1"),
            new CustomData(14,"A Storm of Swords","$199","img2"),
            new CustomData(15,"A Feast for Crows","$199","img2"),
            new CustomData(16,"A Dance With Dragons","$199","img2"),
            new CustomData(17,"Fifty Shades of Grey","$20","img3"),
            new CustomData(18,"Whoops, that was not supposed to be there","FREE","img4"),
            new CustomData(19,"Chicken Soup for Naughty souls","$49.99","img5")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserLoggedIn = false;
        // Set the page content
        setContentView(R.layout.home_page);

        //Set the category dropdown
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
        mWelcomeText = (TextView)findViewById(R.id.WelcomeText);
        setupCustomLists();

        // Setup SearchView Widget
        setUpSearchView();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOGIN_REQUEST_CODE) {

            if(resultCode == RESULT_OK){
                String userName = data.getStringExtra("MyApplication.username");
                mWelcomeText.setText("Welcome, " + userName);
                mUserLoggedIn = true;
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem login = menu.findItem(R.id.Login);
        MenuItem logout = menu.findItem(R.id.Logout);
        if(mUserLoggedIn){
            logout.setVisible(true);
            login.setVisible(false);
        }
        else{
            logout.setVisible(false);
            login.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.Login:
                Intent i = new Intent(getApplicationContext(), login_page.class);
                startActivityForResult(i,LOGIN_REQUEST_CODE);
                return true;

            case R.id.Logout:
                // Session class instance
                SessionManager session = new SessionManager(getApplicationContext());
                session.logoutUser();
                mUserLoggedIn = false;
                mWelcomeText.setText("Welcome, Guest");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Setup the SearchView widget to search
     */
    private void setUpSearchView(){
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener( ) {
            @Override
            public boolean   onQueryTextChange( String newText ) {
                // your text view here
                return true;
            }

            @Override
            public boolean   onQueryTextSubmit(String query) {
                //Toast.makeText(getApplicationContext(),query, Toast.LENGTH_LONG).show();
                searchView.setQuery("",false);
                //Prepare to fetch data for the Query and display it as a list
                startListActivity(query);

                return true;
            }
        });
    }

    /**
     * Starts the Endless List Activity when a user searched for something
     */
    private void startListActivity(String queryStr){
        Intent intent = new Intent(this, endless_list_activity.class);
        intent.putExtra("com.example.myapplication.homepage",queryStr);
        startActivity(intent);
    }

    /**
     * Setup the Custom Scrollable HorizontalList
     */
    private void setupCustomLists() {
        // Make an array adapter using the built in android layout to render a list of strings
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, mCustomData);
        CustomArrayAdapter adapter1 = new CustomArrayAdapter(this, mCustomData);

        // Assign adapter to HorizontalListView
        mHlvCustomList_Recently_Uploaded.setAdapter(adapter);
        mHlvCustomList1_Last_Viewed.setAdapter(adapter1);
    }

    // Search By Category selection
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


