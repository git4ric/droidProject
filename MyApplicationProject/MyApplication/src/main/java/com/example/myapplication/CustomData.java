package com.example.myapplication;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;

/** This is just a simple class for holding data that is used to render our custom view */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CustomData extends Fragment{

    private String bookName;
    private String bookPrice;
    private String imgName;

    public CustomData(String name, String price,String picName) {
        bookPrice = price;
        bookName = name;
        imgName = picName;
    }

    /**
     * @return the text
     */
    public String getBookName() {
        return bookName;
    }

    /**
     * @return the text
     */
    public String getBookPrice() {
        return bookPrice;
    }

    /**
     * @return the text
     */
    public String getImgName() {
        return imgName;
    }

}
