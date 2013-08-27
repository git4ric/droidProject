package com.example.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 27/07/13.
 */
public class ListContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<CustomData> ITEMS = new ArrayList<CustomData>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String,CustomData> ITEM_MAP = new HashMap<String, CustomData>();


    public void addItem(CustomData item) {
        ITEMS.add(item);
        ITEM_MAP.put(Long.toString(item.mBookRowId), item);
    }
}
