/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.myapplication;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Activity that calls an endless list using fragment 
 */
public class endless_list_activity extends Activity implements FragmentEndlessList.Callbacks{

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitylayout);
        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra("com.example.myapplication.homepage");

        setTitle("Search Results for " + message);


        //add fragment as an endless list (as a FragmentList)
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        FragmentEndlessList newFrag = new FragmentEndlessList();
        ft.add(R.id.layout1, newFrag);
        ft.commit();              
    }

    @Override
    public void onItemSelected(int id) {
        Intent detailIntent = new Intent(this, ItemDetailActivity.class);
        detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
        startActivity(detailIntent);
    }
}