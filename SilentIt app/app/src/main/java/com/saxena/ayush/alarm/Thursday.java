package com.saxena.ayush.alarm;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

/**
 * Created by Ayush on 1/27/2017.
 */

public class Thursday extends Fragment implements CompoundButton.OnCheckedChangeListener {
    View view;
    SqlDatabase db;
    FragmentActivity act;
    private final String DAY="thursday";
    public Thursday() {
        // Required empty public constructor
    }
    CheckBox c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.thursday, container, false);
        c1=(CheckBox)view.findViewById(R.id.check1);
        c2=(CheckBox)view.findViewById(R.id.check2);
        c3=(CheckBox)view.findViewById(R.id.check3);
        c4=(CheckBox)view.findViewById(R.id.check4);
        c5=(CheckBox)view.findViewById(R.id.check5);
        c6=(CheckBox)view.findViewById(R.id.check6);
        c7=(CheckBox)view.findViewById(R.id.check7);
        c8=(CheckBox)view.findViewById(R.id.check8);
        c9=(CheckBox)view.findViewById(R.id.check9);
        c10=(CheckBox)view.findViewById(R.id.check10);
        c11=(CheckBox)view.findViewById(R.id.check11);
        c12=(CheckBox)view.findViewById(R.id.check12);
        act=getActivity();
        db=new SqlDatabase(getActivity());
        db.open();
        Cursor c=db.getCursor(DAY);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            CheckBox ch;
            boolean b= c.getInt(c.getColumnIndex(DAY)) == 1;
            int time=c.getInt(c.getColumnIndex(SqlDatabase.TIME));
            if(time!=19)
            {
                ch=getCheckBox(time);
                ch.setChecked(b);
            }
            c.moveToNext();
        }
        c.close();
        db.closeTable();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        c1.setOnCheckedChangeListener(this);
        c2.setOnCheckedChangeListener(this);
        c3.setOnCheckedChangeListener(this);
        c4.setOnCheckedChangeListener(this);
        c5.setOnCheckedChangeListener(this);
        c6.setOnCheckedChangeListener(this);
        c7.setOnCheckedChangeListener(this);
        c8.setOnCheckedChangeListener(this);
        c9.setOnCheckedChangeListener(this);
        c10.setOnCheckedChangeListener(this);
        c11.setOnCheckedChangeListener(this);
        c12.setOnCheckedChangeListener(this);
    }

    public CheckBox getCheckBox(int time)
    {
        int r;
        switch (time)
        {
            case 7:
                r=R.id.check1;
                break;
            case 8:
                r=R.id.check2;
                break;
            case 9:
                r=R.id.check3;
                break;
            case 10:
                r=R.id.check4;
                break;
            case 11:
                r=R.id.check5;
                break;
            case 12:
                r=R.id.check6;
                break;
            case 13:
                r=R.id.check7;
                break;
            case 14:
                r=R.id.check8;
                break;
            case 15:
                r=R.id.check9;
                break;
            case 16:
                r=R.id.check10;
                break;
            case 17:
                r=R.id.check11;
                break;
            default:
                r=R.id.check12;
                break;

        }
        return (CheckBox)view.findViewById(r);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    String toText(int t)
    {
        return ""+t;
    }
    @Override
    public void onCheckedChanged(CompoundButton btn, boolean b) {
        String value=""+(b?1:0);
        switch (btn.getId())
        {
            case R.id.check1:
                new Transfer().execute(toText(7),value);
                break;
            case R.id.check2:
                new Transfer().execute(toText(8),value);
                break;
            case R.id.check3:
                new Transfer().execute(toText(9),value);
                break;
            case R.id.check4:
                new Transfer().execute(toText(10),value);
                break;
            case R.id.check5:
                new Transfer().execute(toText(11),value);
                break;
            case R.id.check6:
                new Transfer().execute(toText(12),value);
                break;
            case R.id.check7:
                new Transfer().execute(toText(13),value);
                break;
            case R.id.check8:
                new Transfer().execute(toText(14),value);
                break;
            case R.id.check9:
                new Transfer().execute(toText(15),value);
                break;
            case R.id.check10:
                new Transfer().execute(toText(16),value);
                break;
            case R.id.check11:
                new Transfer().execute(toText(17),value);
                break;
            case R.id.check12:
                new Transfer().execute(toText(18),value);
                break;
        }
    }

    class Transfer extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            int time= Integer.parseInt(strings[0]);
            int value=Integer.parseInt(strings[1]);
            SqlDatabase d=new SqlDatabase(act);
            d.open();
            d.updateEntry(time,DAY,value);
            d.closeTable();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
