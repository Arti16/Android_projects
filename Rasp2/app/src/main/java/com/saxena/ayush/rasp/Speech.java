package com.saxena.ayush.rasp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ayush on 4/5/2017.
 */

public class Speech {
    private Map<String,String > keywords,store;
    private String device,state,room,action,reply;
    private String[] results;
    public int ROOM=2,ACTION=0,DEVICE=1,STATE=3,REPLY=4;

    String correctionKeywords[]={"it","them","sorry","actually"};
    Speech()
    {
        keywords=new HashMap<>();
        store=new HashMap<>();
        ArrayList<String> temp=new ArrayList<String>();
        temp.add("lights");
        temp.add("light");
        temp.add("tubelight");
        temp.add("bulb");
        for(String x: temp)
            keywords.put(x,"lights");

        temp.clear();
        temp.add("fan");
        temp.add("pankha");
        temp.add("punkha");
        for(String x: temp)
            keywords.put(x,"fan");

        temp.clear();
        temp.add("television");
        temp.add("tv");
        temp.add("led tv");
        for(String x: temp)
            keywords.put(x,"television");
        temp.clear();
        temp.add("bathroom");
        temp.add("bath room");
        temp.add("bath rooms");
        temp.add("bathrooms");
        temp.add("toilet");
        temp.add("wash room");
        temp.add("washroom");
        for(String x: temp)
            keywords.put(x,"bathroom");

        temp.clear();
        temp.add("bedroom");
        temp.add("bedrooms");
        temp.add("bed room");
        for(String x: temp)
            keywords.put(x,"bedroom");

        temp.clear();
        temp.add("drawing room");
        temp.add("drawing rooms");
        temp.add("drawingroom");
        temp.add("drawingrooms");
        for(String x: temp)
            keywords.put(x,"drawingroom");

        temp.clear();
        temp.add("lobby");
        temp.add("lobbies");
        for(String x: temp)
            keywords.put(x,"lobby");

        temp.clear();
        temp.add("kitchen");
        temp.add("kitchens");
        temp.add("kitchen's");
        for(String x: temp)
            keywords.put(x,"kitchen");

        temp.clear();
        temp.add("dining room");
        temp.add("diningroom");
        for(String x: temp)
            keywords.put(x,"diningroom");

        temp.clear();
        temp.add("on");
        temp.add("onn");
        temp.add("open");
        temp.add("kholdo");
        temp.add("khol do");
        temp.add("khol");
        for(String x: temp)
            keywords.put(x,"1");

        temp.clear();
        temp.add("off");
        temp.add("of");
        temp.add("close");
        temp.add("band");
        temp.add("don't");
        temp.add("do not");
        for(String x: temp)
            keywords.put(x,"0");

        store.put("lights","device");
        store.put("fan","device");
        store.put("television","device");
        store.put("bathroom","room");
        store.put("bedroom","room");
        store.put("drawingroom","room");
        store.put("lobby","room");
        store.put("guestroom","room");
        store.put("diningroom","room");
        store.put("kitchen","room");
        store.put("0","state");
        store.put("1","state");
    }
    String[] init(String result)
    {
        results=new String[5];
        if(reply!=null && reply.equals("done"))
            device=room=state=null;
        action=null;
        reply=null;
        SetUp(result);
        if(reply==null)
            reply="Try again";
        results[0]=action;
        results[1]=device;
        results[2]=room;
        results[3]=state;
        results[4]=reply;
        return results;
    }
    String getAction()
    {
        if(action==null)
            action="garbage";
        return action;
    }
    String getReply()
    {
        return reply;
    }

    String getDevice() {
        return device;
    }

    String getState() {
        return state;
    }

    String getRoom() {
        return room;
    }

    private void SetUp(String result)
    {
        result=result.toLowerCase();
        String[] wordsSentence=result.split(" ");
        for (int i=0;i<wordsSentence.length;i++)
        {
            String word=wordsSentence[i];
            if(i!=0 && word.equalsIgnoreCase("room"))
                word=wordsSentence[i-1]+" room";
            String item=keywords.get(word);
            if(item==null && i!=0)
            {
                if(keywords.get(wordsSentence[i-1]+" "+word)!=null)
                    item=keywords.get(wordsSentence[i-1]+" "+word);
            }
            String type=store.get(item);

            if(type==null || item==null)
                continue;
            if(type.equals("device"))
                device=item;
            else if (type.equals("state"))
                state=item;
            else if(type.equals("room"))
                room=item;
        }
        action=findAction(device,room,state,result);
        if(action==null &&device==null)
        {
            reply="Which device?";
           // return "\n "+device+"  "+room+"  "+state;
        }
        else if(action==null && room==null)
        {
            reply="in which room?";
           // return "\n "+device+"  "+room+"  "+state;
        }
        else if(action==null && state==null)
        {
            reply="Should I turn it on or off?";
            //return "\n "+device+"  "+room+"  "+state;
        }
        if(action==null)
            action="incomplete";
    }
    String findAction(String device,String room,String state,String result)
    {
        List<String> wordsList= Arrays.asList(result.split(" "));
        for (String temp:correctionKeywords) {
            if(wordsList.contains(temp))
            {
                action="correct_previous";
                reply="done";
                return "correct_previous";
            }
        }
        if(device!=null && state!=null && room!=null)
        {
            if(!(result.contains("do not") || result.contains("don't") || result.contains(" no ")))
            {
                reply="done";
                return "turn_on";
                //device=state=room=null;
            }
        }
        return null;

    }

}
