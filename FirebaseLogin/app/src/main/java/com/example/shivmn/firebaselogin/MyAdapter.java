package com.example.shivmn.firebaselogin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ayush on 11/7/17.
 */

public class MyAdapter extends BaseAdapter {
    ArrayList<messageModel> list;
    Context context;
    LayoutInflater layoutInflater;
    Activity activity;
    MyAdapter(ArrayList<messageModel> list, Context context,Activity activity)
    {
        this.context=context;
        this.list=list;
        this.activity=activity;
        layoutInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        messageModel obj=(messageModel) getItem(position);
        View view=convertView;
        if(convertView==null)
            view=layoutInflater.inflate(R.layout.messagelayout,null);

        TextView muser,mtime,mtext;
        muser=(TextView) view.findViewById(R.id.message_user);
        mtext=(TextView) view.findViewById(R.id.message_text);
        mtime=(TextView) view.findViewById(R.id.message_time);
        muser.setText(obj.getMessageUser());
        mtime.setText(obj.getMessageTime()+"");
        mtext.setText(obj.getMessageText());
        return view;
    }
}
