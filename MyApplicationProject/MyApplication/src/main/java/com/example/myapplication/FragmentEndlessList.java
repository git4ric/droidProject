package com.example.myapplication;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.app.ListFragment;
import android.gesture.GestureUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class which holds an example of a endless list. It means a list will be
 * displayed and it will always have items to be displayed. <br>
 * New data is loaded asynchronously in order to provide a good user experience.
 */
public class FragmentEndlessList extends ListFragment {


    private static final String TAG_ROW_ID = "id";
    private static final String TAG_SUBJECT = "subject";
    private static final String TAG_PRICE = "price";
    private static final String TAG_TITLE = "title";

    private static final String ACTIVITY_TAG = "FragmentEndlessList: ";

    private static String SEARCH_STRING = "";

    private Activity activity;
    
    /**
     * Adapter for endless list.
     */
    private EndlessListAdapter arrayAdapter = null;

    /**
     * Variable which controls when new items start being fetched. For instance
     * you may want to start get element when the list have 5 elements left to
     * be displayed.
     */
    private int totalSizeToBe = 0;

    /**
     * Variable which controls when new items are being loaded. If this variable
     * is true, it means items are being loaded, otherwise it is set to false.
     */
    boolean isLoading = false;

    /**
     * The number of elements which are retrieved every time the service is
     * called for retrieving elements.
     */
    private static int LIST_SIZE;

    /**
     * The number of elements left in the list when the asynchronous service
     * will be called.
     */
    private static final int LOAD_AHEAD_SIZE = 0;

    /**
     * The number of items added to the <i>totalSizeToBe</i> field.
     */
    private static final int INCREMENT_TOTAL_MINIMUM_SIZE = 1;
    
    /**
     * Property to save the top most index of the list
     */
    private static final String PROP_TOP_ITEM = "top_list_item";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
    * List of items fetched from service
    */
    private List<CustomData> mListData;

    /**
     * Need to map each the thing which is unique to List Item ie rowId to item
     */
    public  Map<String,CustomData> ITEM_MAP = new HashMap<String, CustomData>();

    /**
     * Page number
     */
    private int mPageNo;

    private ListView mListView;

    private boolean mStopLoading;
    /**
    *
    */
    private static RequestQueue mReqQueue;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(long id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {

        @Override
        public void onItemSelected(long id) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.e(ACTIVITY_TAG,"========= ----  onCreate ---- =========");
        mListData = new ArrayList<CustomData>();
        activity = getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(ACTIVITY_TAG,"========= ----  onActivityCreated ---- =========");
        Bundle bundle = this.getArguments();
        if(bundle != null){
            SEARCH_STRING = bundle.getString("Search_String");
        }

        // Populate list
        arrayAdapter = new EndlessListAdapter(getActivity(),R.layout.list_row,mListData);
        mReqQueue = Volley.newRequestQueue(this.getActivity());
        activity.setProgressBarIndeterminateVisibility(true);

        setListAdapter(arrayAdapter);
        // fetch items from service

        retrieveItems(0,SEARCH_STRING,mPageNo);

        mListView = getListView();
        mListView.setOnScrollListener(new EndlessListScrollListener());


        if (savedInstanceState != null) {
            // Restore last state for top list position
            int listTopPosition = savedInstanceState.getInt(PROP_TOP_ITEM, 0);

            // load elements enough to get to the top of the list
           // downloadAction = new DownloadItems();
            if (listTopPosition > LIST_SIZE) {
              //do something
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.e(ACTIVITY_TAG,"========= ----  onPause ---- =========");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e(ACTIVITY_TAG,"========= ----  onResume ---- =========");
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.e(ACTIVITY_TAG,"========= ----  onStart ---- =========");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mListData.clear();
        mStopLoading = false;
        mReqQueue.cancelAll(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.e(ACTIVITY_TAG, "========= ----  onStop ---- =========");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.e(ACTIVITY_TAG,"========= ----  onAttach ---- =========");
        LIST_SIZE = 0;
        mPageNo = 0;
        mStopLoading = true;
        SEARCH_STRING = "";
        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(ACTIVITY_TAG,"========= ----  onDetach ---- =========");
        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(mListData.get(position).getBookRowID());
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        Log.e(ACTIVITY_TAG,"========= ----  onSaveInstanceState ---- =========");
        int listPosition = mListView.getFirstVisiblePosition();
        if (listPosition > 0) {
            state.putInt(PROP_TOP_ITEM, listPosition);
        }
    }

    /**
     * This method represents a service which takes a long time to be executed.
     * To simulate it, it is inserted a lag of 1 second. <br>
     * This method basically creates a <i>cache</i> number of
     * {@link CustomData} and returns it. It creates {@link CustomData}s with
     * text higher than <i>itemNumber</i>.
     * 
     * @param itemNumber
     *            Basic number to create other elements.
     *
     * 
     * @return Returns the created list of {@link CustomData}s.
     */
    private void retrieveItems(final Integer itemNumber, String search, int pageNo) {
        isLoading = true;
        try {
            String url = "http://krhbooks.com/api/1.0/data/searchQuery?_nkw=" + search + "&_pg=" + Integer.toString(pageNo);
            Log.e("Searching URL: ","" + url);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                 @Override
                public void onResponse(JSONObject response) {
                     Log.e("JSON RESPONSE from URL: ",response.toString());
                     mStopLoading = false;
                     parseJSONResponse(itemNumber, response);
                 }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("JSON Response Error","" + error.getMessage());
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8" );
                        Log.e("responseBody: ","" + responseBody);
                        if(responseBody.equals("{}")){
                            Log.e("responseBody: ","HERE HERE");
                            mStopLoading = true;
                            isLoading = false;
                            mReqQueue.cancelAll(this);
                        }
                        //Handle a malformed json response
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(2000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,2.0f));
            mReqQueue.add(jsObjRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJSONResponse(int itemNumber, JSONObject response){
        List<CustomData> queriedData = new ArrayList<CustomData>();
        try{
            JSONArray arr = response.getJSONArray("books");
            for(int i=0;i<arr.length();i++){
                CustomData data = new CustomData(0,0,"","","");
                JSONObject objectData = arr.getJSONObject(i);
                data.setmBookRowId(Long.valueOf(objectData.getString(TAG_ROW_ID)));
                data.setmBookPrice(objectData.getString(TAG_PRICE));
                data.setmBookSubject(objectData.getString(TAG_SUBJECT));
                data.setmBookName(objectData.getString(TAG_TITLE));
                data.setId(itemNumber + i);
                queriedData.add(i,data);
                ITEM_MAP.put(Long.toString(data.mBookRowId),data);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        mListData.addAll(queriedData);
        LIST_SIZE = LIST_SIZE + mListData.size();
        activity.setProgressBarIndeterminateVisibility(false);
        arrayAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    /**
     * Listener which handles the endless list. It is responsible for
     * determining when the long service will be called asynchronously.
     */
    class EndlessListScrollListener implements OnScrollListener {

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {

            // load more elements if there are LOAD_AHEAD_SIZE left in the list
            // to be displayed
            boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount - LOAD_AHEAD_SIZE;

            /*
             * Add one more condition: only get more results in case the list
             * achieves a minimum size. This is necessary in order to avoid that
             * this method is called each time the condition above is reached
             * and the scroll is pressed.
             *
             * if (!mStopLoading && loadMore && totalSizeToBe <= totalItemCount) {
             */
            if (!mStopLoading && loadMore) {
                totalSizeToBe += INCREMENT_TOTAL_MINIMUM_SIZE;
                mPageNo = mPageNo + 1;
                retrieveItems(LIST_SIZE + 1,SEARCH_STRING,mPageNo);
                arrayAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // do nothing
        }
    }
    
    /**
     * Adapter which handles the list be be displayed.
     */
    class EndlessListAdapter extends ArrayAdapter<CustomData> {

        private final Activity context;
        private List<CustomData> items;
        private final int rowViewId;

        /**
         * Instantiate the Adapter for an Endless List Activity.
         * 
         * @param context
         *            {@link Activity} which holds the endless list.
         * @param rowviewId
         *            Identifier of the View which holds each row of the List.
         * @param items
         *            Initial set of items which are added to list being
         *            displayed.
         */
        public EndlessListAdapter(Activity context, int rowviewId,
                List<CustomData> items) {
            super(context, rowviewId, items);
            this.context = context;
            this.items = items;
            this.rowViewId = rowviewId;
        }

        /**
         * Check whether a {@link ListItem} is already in this adapter.
         * 
         * @param item
         *            Item to be verified whether it is in the adapter.
         * 
         * @return Returns <code>true</code> in case the {@link CustomData} is
         *         in the adapter, <code>false</code> otherwise.
         */
        public boolean contains(CustomData item) {
            return items.contains(item);
        }

        /**
         * Get a {@link CustomData} at a certain position.
         * 
         * @param index
         *            Position where the {@link CustomData} is retrieved.
         * 
         * @return Returns the {@link CustomData} give a certain position.
         */
        public CustomData getItemAt(int index) {
            return items.get(index);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            TextView textView_title;
            TextView textView_price;
            TextView textView_subject;


            View rowView = convertView;

            if(position <= LIST_SIZE){
                /*
             * We inflate the row using the determined layout. Also, we fill the
             * necessary data in the text and image views.
             */
                LayoutInflater inflater = context.getLayoutInflater();
                rowView = inflater.inflate(rowViewId,null, true);
                textView_title = (TextView) rowView.findViewById(R.id.title);
                textView_price = (TextView) rowView.findViewById(R.id.price);
                textView_subject = (TextView) rowView.findViewById(R.id.author);
                imageView = (ImageView) rowView.findViewById(R.id.list_image);

                textView_title.setText(getItem(position).getBookName());
                textView_price.setText(getItem(position).getBookPrice());
                textView_subject.setText(getItem(position).getmBookSubject());
                textView_subject.setVisibility(View.VISIBLE);
                textView_price.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);

                /*
                 * If we reached the last position of the list and the loading
                 * operation is still being performed, set the loading message
                 * instead the normal value.
                 *
                 * Moreover, we modify the layout in order to center the loading
                 * message.
                 */
                if (isLoading && position == items.size() - 1) {
                    textView_title.setText(R.string.loading_message);
                    textView_subject.setVisibility(View.INVISIBLE);
                    textView_price.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);

                    // wrap content of the text view in order to center it
                    RelativeLayout.LayoutParams layoutParameters = (RelativeLayout.LayoutParams) textView_price
                            .getLayoutParams();
                    layoutParameters.width = RelativeLayout.LayoutParams.WRAP_CONTENT;

                    // set image to the center, the text field will go along
                    imageView
                            .setImageResource(android.R.drawable.progress_indeterminate_horizontal);
                    RelativeLayout linearLayout = (RelativeLayout) rowView
                            .findViewById(R.id.layout_list_row);
                    linearLayout.setGravity(Gravity.CENTER);
                }
            }



            return rowView;
        }
    }
}
