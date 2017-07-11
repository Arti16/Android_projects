package com.saxena.ayush.keepit;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Ayush on 5/26/2017.
 */

public class JsonFile {
    private String uid;
    private  Context context;
    JsonFile(String uid, Context context)
    {
        this.uid=uid;
        this.context=context;
    }
    JSONObject getJsonRead()
    {
        JSONObject jsonObject;
        try {
            FileInputStream file=context.openFileInput(uid+".json");
            int size=file.available();
            byte[] arr=new byte[size];
            file.read(arr);
            file.close();
            String json=new String(arr,"UTF-8");
            jsonObject=new JSONObject(json);
        } catch (Exception e) {
            jsonObject=null;
        }
        return jsonObject;
    }
    boolean writeJson(JSONObject jsonObject)
    {
        if(jsonObject==null)
            return true;
        String t=jsonObject.toString();
        byte[] arr=t.getBytes();
        try {
            FileOutputStream file=context.openFileOutput(uid+".json",Context.MODE_PRIVATE);
            file.write(arr);
            file.close();
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }
}
