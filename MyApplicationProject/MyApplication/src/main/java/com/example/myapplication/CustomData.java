package com.example.myapplication;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;

/** This is just a simple class for holding data that is used to render our custom view */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CustomData{

    private String mBookNAme;
    private String mBookPrice;
    private String mImgName;
    int mId;
    long mBookRowId; // rowID is the id associated with each book given by web service

    public CustomData(int id,long rowId, String name, String price,String picName) {
        mBookPrice = price;
        mBookNAme = name;
        mImgName = picName;
        mBookRowId = rowId;
        mId = id;
    }

    /**
    *@return id associated with book
    */
    public int getId(){
        return mId;
    }

    /**
    *@return the bookRowId associated with book
    */
    public long getBookRowID(){
        return mBookRowId;
    }

    /**
     * @return the text
     */
    public String getBookName() {
        return mBookNAme;
    }

    /**
     * @return the text
     */
    public String getBookPrice() {
        return mBookPrice;
    }

    /**
     * @return the text
     */
    public String getImgName() {
        return mImgName;
    }


}
