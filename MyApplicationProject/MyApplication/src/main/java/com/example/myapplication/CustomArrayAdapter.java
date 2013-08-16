package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import junit.framework.Assert;

/** An array adapter that knows how to render views when given CustomData classes */
public class CustomArrayAdapter extends ArrayAdapter<CustomData> {
    private LayoutInflater mInflater;

    public CustomArrayAdapter(Context context, CustomData[] values) {
        super(context, R.layout.custom_data, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = mInflater.inflate(R.layout.custom_data, parent, false);

            // Create and save off the holder in the tag so we get quick access to inner fields
            // This must be done for performance reasons
            holder = new Holder();
            holder.bookName = (TextView) convertView.findViewById(R.id.book_name);
            holder.bookPrice = (TextView) convertView.findViewById(R.id.book_price);
            holder.bookImage = (ImageView) convertView.findViewById(R.id.book_image);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        // Populate the text
        holder.bookName.setText(getItem(position).getBookName());
        holder.bookPrice.setText(getItem(position).getBookPrice());
        holder.bookImage.setImageResource(getDrawable(getContext(),getItem(position).getImgName()));

        // Set the color
        //convertView.setBackgroundColor(getItem(position).getBackgroundColor());

        return convertView;
    }

    public static int getDrawable(Context context, String name)
    {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);

        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public TextView bookName;
        public TextView bookPrice;
        public ImageView bookImage;
    }
}
