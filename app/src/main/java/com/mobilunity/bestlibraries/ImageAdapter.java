package com.mobilunity.bestlibraries;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<String> imageUrls;

    public ImageAdapter(Context context) {
        this.context = context;
        this.imageUrls = new ArrayList<>();
    }

    public ImageAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls == null ? 0 : imageUrls.size();
    }

    @Override
    public Object getItem(int i) {
        return imageUrls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setData(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        notifyDataSetChanged();
    }

    public void addItem(String imageUrl) {
        if (this.imageUrls == null) this.imageUrls = new ArrayList<>();
        if (this.imageUrls.contains(imageUrl)) return;
        this.imageUrls.add(imageUrl);
        notifyDataSetChanged();
    }

    public void deleteItem(String imageUrl) {
        if (imageUrl == null || this.imageUrls == null) return;
        this.imageUrls.remove(imageUrl);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) view;
        }

        Glide.with(context).load(imageUrls.get(i)).into(imageView);

        return imageView;
    }
}
