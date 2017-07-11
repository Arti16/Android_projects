package com.saxena.ayush.rasp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Ayush on 4/8/2017.
 */

public class CustomArrayAdapter extends BaseAdapter {
    Context context;
    List<String> data;
    List<Integer> dataState;
    CustomArrayAdapter(Activity a, List<String> d,List<Integer> ds)
    {
        context=a;
    }
    {

    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
