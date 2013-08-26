package com.example.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

    /**
     * The dummy content this fragment is presenting.
     */
    //private DummyContent.DummyItem mItem;

    private ListElement mListElement;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            mListElement = ListContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }

        // Get the info on which book was selected 
        if(getArguments().containsKey("BOOK_ROW_ID")){
            long bookRowID = this.getArguments().getLong("BOOK_ROW_ID");
            Toast.makeText(getActivity(), String.valueOf(bookRowID), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        return rootView;
    }
}
