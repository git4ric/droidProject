package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private static final String TAG = "ItemDetailFragment";

    private long BOOK_ROW_ID;

    /**
     * The dummy content this fragment is presenting.
     */
    //private DummyContent.DummyItem mItem;

    private CustomData mListElement;

    private static RequestQueue mFullReqQueue;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        BOOK_ROW_ID = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the info on which book was selected
        if(getArguments().containsKey("BOOK_ROW_ID")){
            BOOK_ROW_ID = this.getArguments().getLong("BOOK_ROW_ID");
            //Toast.makeText(getActivity(), String.valueOf(bookRowID), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mFullReqQueue = Volley.newRequestQueue(this.getActivity());
        fetchItemDetail(Long.toString(BOOK_ROW_ID));
    }

    private void fetchItemDetail(String bookRowID) {
        try{
            String url = "http://krhbooks.com/api/1.0/data/requestedResult?_rowId=" + bookRowID;
            Log.e(TAG + "Searching URL: ","" + url);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(TAG + "JSON RESPONSE from URL: ",response.toString());
                    parseJSONResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG + "JSON Response Error","" + error.getMessage());
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8" );
                        Log.e(TAG + "responseBody: ","" + responseBody);
                        if(responseBody.equals("{}")){
                            Log.e(TAG + "responseBody: ","HERE HERE");
                            mFullReqQueue.cancelAll(this);
                        }
                        //Handle a malformed json response
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(2000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,2.0f));
            mFullReqQueue.add(jsObjRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void parseJSONResponse(JSONObject response){
      
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        return rootView;
    }
}
