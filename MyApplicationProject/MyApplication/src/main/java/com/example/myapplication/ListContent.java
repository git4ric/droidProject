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
    public static List<ListElement> ITEMS = new ArrayList<ListElement>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, ListElement> ITEM_MAP = new HashMap<String, ListElement>();


    public void addItem(ListElement item) {
        ITEMS.add(item);
        ITEM_MAP.put(Integer.toString(item.id), item);
    }
}
