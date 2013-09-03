package com.example.myapplication;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;

/** This is just a simple class for holding data that is used to render our custom view */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CustomData{

    private String mBookName;
    private String mBookPrice;
    private String mImgName;
    private String mBookSubject;
    private int mId;
    long mBookRowId; // rowID is the id associated with each book given by web service

    public CustomData(int id,long rowId, String name, String price,String picName) {
        mBookPrice = price;
        mBookName = name;
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

    public void setId(int id){
        mId = id;
    }

    /**
    *@return the bookRowId associated with book
    */
    public long getBookRowID(){
        return mBookRowId;
    }

    public void setmBookRowId(long id){
        mBookRowId = id;
    }

    /**
     * @return the text
     */
    public String getBookName() {
        return mBookName;
    }

    public void setmBookName(String name){
        mBookName = name;
    }


    public void setmBookPrice(String price){
        mBookPrice = price;
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


    public String getmBookSubject() {
        return mBookSubject;
    }

    public void setmBookSubject(String mBookSubject) {
        this.mBookSubject = mBookSubject;
    }
}
