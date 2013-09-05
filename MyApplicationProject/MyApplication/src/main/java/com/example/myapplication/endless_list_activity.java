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
import android.util.Log;
import android.view.Window;
import android.widget.Toast;


/**
 * Activity that calls an endless list using fragment 
 */
public class endless_list_activity extends Activity implements FragmentEndlessList.Callbacks{

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("DEBUG","********************endless_list_activity Activity created **********************");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activitylayout);
        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra("com.example.myapplication.homepage");

        setTitle("Search Results for " + message);

        //add fragment as an endless list (as a FragmentList)
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        FragmentEndlessList newFrag = new FragmentEndlessList();
        Bundle bundle=new Bundle();
        bundle.putString("Search_String",message);
        newFrag.setArguments(bundle);
        ft.add(R.id.layout1, newFrag);
        ft.commit();              
    }

    @Override
    public void onItemSelected(long id,String bookName, String bookPrice) {
        Log.e("Item Clicked: ",Long.toString(id));
        Intent detailIntent = new Intent(this, ItemDetailActivity.class);
        detailIntent.putExtra("BOOK_ROW_ID",id);
        detailIntent.putExtra("BOOK_NAME",bookName);
        detailIntent.putExtra("BOOK_PRICE",bookPrice);
        startActivity(detailIntent);

//        Intent detailIntent = new Intent(this, ItemDetailActivity.class);
//        detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
//        startActivity(detailIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}