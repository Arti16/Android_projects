package com.saxena.ayush.tictactoe;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Ayush Saxena on 1/14/2017.
 */

public class Game extends AppCompatActivity {
    Socket s;
    ServerSocket ss;
    TextView tvChance;
    boolean cancel=true,start=false;
    String msg="Waiting for Client";
    AlertDialog.Builder ad;
    AlertDialog alert;
    TextView tv;
    DataInputStream dis;
    DataOutputStream dos;
    int chance=0,turn=0,opponent;
    int array[][]=new int[3][3];
    ImageButton i00,i01,i02,i10,i11,i12,i20,i21,i22;
    int symbol,Realplayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        i00=(ImageButton)findViewById(R.id.image00);
        i01=(ImageButton)findViewById(R.id.image01);
        i02=(ImageButton)findViewById(R.id.image02);
        i10=(ImageButton)findViewById(R.id.image10);
        i11=(ImageButton)findViewById(R.id.image11);
        i12=(ImageButton)findViewById(R.id.image12);
        i20=(ImageButton)findViewById(R.id.image20);
        i21=(ImageButton)findViewById(R.id.image21);
        i22=(ImageButton)findViewById(R.id.image22);
        tvChance=(TextView)findViewById(R.id.tvchance);
        i00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(0,0,Realplayer);

            }
        });
        i01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(0,1,Realplayer);
            }
        });
        i02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(0,2,Realplayer);
            }
        });
        i10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(1,0,Realplayer);
            }
        });
        i11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(1,1,Realplayer);
            }
        });
        i12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(1,2,Realplayer);
            }
        });
        i20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(2,0,Realplayer);
            }
        });
        i21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(2,1,Realplayer);
            }
        });
        i22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(2,2,Realplayer);
            }
        });

        String type=getIntent().getStringExtra("type");
        if(type.equals("host"))
        {
            symbol=R.mipmap.cross;
            opponent=2;
            Realplayer=1;
            ad=new AlertDialog.Builder(Game.this);
            ad.setTitle("Please Wait");
            ad.setMessage("Waiting for Clients");
            ad.setCancelable(false);
            ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancel=false;
                    dialog.cancel();
                    try{
                        if(s!=null)
                            s.close();
                        ss.close();
                        finish();
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.toString());
                    }
                }
            });
            alert=ad.create();
            alert.show();

            new AsyncTask<Void, Void, Exception>() {
                @Override
                protected Exception doInBackground(Void... params) {
                    try{
                        ss=new ServerSocket(2001);
                        //Toast.makeText(Game.this, "Waiting", Toast.LENGTH_SHORT).show();
                        s=ss.accept();
                        //Toast.makeText(Game.this, "Connected", Toast.LENGTH_SHORT).show();

                        cancel=false;

                    }
                    catch (Exception e) {
                        System.out.println(e.toString());
                        return e;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Exception result) {
                    if(result!=null)
                        Toast.makeText(Game.this,result.toString(), Toast.LENGTH_SHORT).show();
                    else
                    {
                        start=true;
                        Toast.makeText(Game.this, "Connected", Toast.LENGTH_SHORT).show();
                        alert.cancel();
                        setup();
                        AlertDialog.Builder add=new AlertDialog.Builder(Game.this);
                        add.setMessage("All Set");
                        add.setTitle("Press Start to start the game..");
                        add.setPositiveButton("Start", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                send("$start$1");

                                if(!start)
                                    Toast.makeText(Game.this, "Waiting For the opponent to be ready", Toast.LENGTH_SHORT).show();
                            }
                        });
                        AlertDialog aa=add.create();
                        aa.show();
                    }
                }
            }.execute();
        }
        else
        {
            symbol=R.mipmap.o;
            opponent=1;
            Realplayer=2;
            Context context=Game.this;
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.imagealert, null);

            AlertDialog.Builder adb= new AlertDialog.Builder(
                    context);
            adb.setTitle("Input Ip Address");
            // set prompts.xml to alertdialog builder
            adb.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.ipinput);
            adb.setCancelable(false);
            adb.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    final String ip=userInput.getText().toString().trim();
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... params) {
                            try{
                                if(!ip.isEmpty())
                                s=new Socket(ip,2001);
                                if(ip.isEmpty())
                                {
                                    finish();
                                    return "dd";
                                }
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.toString());
                                return e.toString();
                            }
                            return "ok";
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            if(!result.equals("ok"))
                                finish();
                            else
                            {
                                setup();
                                Toast.makeText(Game.this, "Connected", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder add=new AlertDialog.Builder(Game.this);
                                add.setMessage("All Set");
                                add.setTitle("Press Start to start the game..");
                                add.setPositiveButton("Start", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        send("$start$1");
                                        tvChance.setText("Friend's Turn");
                                        Toast.makeText(Game.this, "Its Your Opponents Turn", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                AlertDialog aa=add.create();
                                aa.show();
                            }
                        }
                    }.execute();
                }
            });
            AlertDialog aa=adb.create();
            aa.show();
        }
    }
    void setup()
    {
        try{
            dis=new DataInputStream(s.getInputStream());
            dos=new DataOutputStream(s.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                        new AsyncTask<Void, String, String>() {
                        @Override
                        protected String doInBackground(Void... params) {
                            while(true)
                            {
                                try{
                                    String t= dis.readUTF();
                                    publishProgress(t);
                                }
                                catch (Exception e)
                                {
                                    System.out.println("error -"+e.toString());
                                    try {
                                        s.close();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                    finish();
                                    break;

                                }
                            }
                            return null;
                        }

                            @Override
                            protected void onProgressUpdate(String... values) {
                                super.onProgressUpdate(values);
                                rec(values[0]);
                            }

                            @Override
                        protected void onPostExecute(String result) {
                            rec(result);
                        }
                    }.execute();
                    }
            }).start();
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    void send(String text)
    {
        try{
            dos.writeUTF(text);
        }
        catch(Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    void rec(String text)
    {
        if (text!=null && text.contains("$start$"))
        {
            start = true;
            reset();
            if(text.charAt(text.length()-1)-48==Realplayer)
            {
                tvChance.setText("Your Turn");
                chance=Realplayer;
            }
            else
            {
                tvChance.setText("Friend's Turn");
                chance=0;
            }
            Toast.makeText(this, "Friend is ready", Toast.LENGTH_SHORT).show();
        }
        if (text!=null && text.contains("$index$")) {
            int i = text.charAt(text.length() - 2) - 48;
            int j = text.charAt(text.length() - 1) - 48;
            array[i][j] = opponent;
            int oppSymbol;
            if (opponent == 1)
                oppSymbol = R.mipmap.cross;
            else
                oppSymbol = R.mipmap.o;
            if (i == 0 && j == 0)
            {
                i00.setImageResource(oppSymbol);
                i00.setClickable(false);
            }
            if (i == 0 && j == 1)
            {
                i01.setImageResource(oppSymbol);
                i01.setClickable(false);
            }
            if (i == 0 && j == 2)
            {
                i02.setClickable(false);
                i02.setImageResource(oppSymbol);
            }
            if (i == 1 && j == 0)
            {
                i10.setClickable(false);
                i10.setImageResource(oppSymbol);
            }
            if (i == 1 && j == 1)
            {
                i11.setClickable(false);
                i11.setImageResource(oppSymbol);
            }
            if (i == 1 && j == 2)
            {
                i12.setClickable(false);
                i12.setImageResource(oppSymbol);
            }
            if (i == 2 && j == 0)
            {
                i20.setClickable(false);
                i20.setImageResource(oppSymbol);
            }
            if (i == 2 && j == 1)
            {
                i21.setClickable(false);
                i21.setImageResource(oppSymbol);
            }
            if (i == 2 && j == 2)
            {
                i22.setClickable(false);
                i22.setImageResource(oppSymbol);
            }
            check(i, j, opponent);
            chance = 3 - opponent;
            tvChance.setText("Your Turn");
        }
    }
    void play(int i,int j,int p)
    {
        int oppSymbol;
        System.out.print("ready to play");
            if (chance!=0 && start) {
                array[i][j]=p;
                check(i,j,p);
                send("$index$"+i+j);
                chance=0;
                tvChance.setText("Friend's Turn");
                if (p==1)
                    oppSymbol = R.mipmap.cross;
                else
                    oppSymbol = R.mipmap.o;
                if (i == 0 && j == 0)
                {
                    i00.setImageResource(oppSymbol);
                    i00.setClickable(false);
                }
                if (i == 0 && j == 1)
                {
                    i01.setImageResource(oppSymbol);
                    i01.setClickable(false);
                }
                if (i == 0 && j == 2)
                {
                    i02.setClickable(false);
                    i02.setImageResource(oppSymbol);
                }
                if (i == 1 && j == 0)
                {
                    i10.setClickable(false);
                    i10.setImageResource(oppSymbol);
                }
                if (i == 1 && j == 1)
                {
                    i11.setClickable(false);
                    i11.setImageResource(oppSymbol);
                }
                if (i == 1 && j == 2)
                {
                    i12.setClickable(false);
                    i12.setImageResource(oppSymbol);
                }
                if (i == 2 && j == 0)
                {
                    i20.setClickable(false);
                    i20.setImageResource(oppSymbol);
                }
                if (i == 2 && j == 1)
                {
                    i21.setClickable(false);
                    i21.setImageResource(oppSymbol);
                }
                if (i == 2 && j == 2)
                {
                    i22.setClickable(false);
                    i22.setImageResource(oppSymbol);
                }
            }
        }
    void reset()
    {
        turn=0;
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                array[i][j]=0;
        int oppSymbol=0;
        i00.setImageResource(0);
        i00.setClickable(true);

        i01.setImageResource(oppSymbol);
        i01.setClickable(true);

        i02.setClickable(true);
        i02.setImageResource(oppSymbol);

        i10.setClickable(true);
        i10.setImageResource(oppSymbol);

        i11.setClickable(true);
        i11.setImageResource(oppSymbol);

        i12.setClickable(true);
        i12.setImageResource(oppSymbol);

        i20.setClickable(true);
        i20.setImageResource(oppSymbol);

        i21.setClickable(true);
        i21.setImageResource(oppSymbol);

        i22.setClickable(true);
        i22.setImageResource(oppSymbol);
    }
    void check(int i,int j,int player)
    {
        turn++;
        boolean win=false;
        if(i==0 &&j==0)
            if((array[0][0]==player &&(array[0][1])==player&&(array[0][2])==player )||(array[0][0]==player &&(array[1][0])==player&&(array[2][0])==player )||(array[0][0]==player &&(array[1][1])==player&&(array[2][2])==player ))
            {
                Toast.makeText(this, "Player "+player+" win", Toast.LENGTH_SHORT).show();
                win=true;
            }
        if(i==0 &&j==1)
            if((array[0][0]==player &&(array[0][1])==player&&(array[0][2])==player )||(array[0][1]==player &&(array[1][1])==player&&(array[2][1])==player ))
            {
                Toast.makeText(this, "Player "+player+" win", Toast.LENGTH_SHORT).show();
                win=true;
            }
        if(i==0 &&j==2)
            if((array[0][0]==player &&(array[0][1])==player&&(array[0][2])==player )||(array[2][0]==player &&(array[1][1])==player&&(array[0][2])==player )||(array[0][2]==player &&(array[1][2])==player&&(array[2][2])==player ))
            {
                Toast.makeText(this, "Player "+player+" win", Toast.LENGTH_SHORT).show();
                win=true;
            }
        if(i==1 &&j==0)
            if((array[1][0]==player &&(array[1][1])==player&&(array[1][2])==player )||(array[0][0]==player &&(array[1][0])==player&&(array[2][0])==player ))
            {
                Toast.makeText(this, "Player "+player+" win", Toast.LENGTH_SHORT).show();
                win=true;
            }
        if(i==1 &&j==1)
            if((array[1][0]==player &&(array[1][1])==player&&(array[1][2])==player )||(array[0][1]==player &&(array[1][1])==player&&(array[2][1])==player )||(array[0][0]==player &&(array[1][1])==player&&(array[2][2])==player )||(array[0][2]==player &&(array[1][1])==player&&(array[2][0])==player ))
            {
                Toast.makeText(this, "Player "+player+" win", Toast.LENGTH_SHORT).show();
                win=true;
            }
        if(i==1 &&j==2)
            if((array[0][2]==player &&(array[1][2])==player&&(array[2][2])==player )||(array[1][0]==player &&(array[1][1])==player&&(array[1][2])==player ))
            {
                Toast.makeText(this, "Player "+player+" win", Toast.LENGTH_SHORT).show();
                win=true;
            }
        if(i==2 &&j==0)
            if((array[0][0]==player &&(array[1][0])==player&&(array[2][0])==player )||(array[2][0]==player &&(array[2][1])==player&&(array[2][2])==player )||(array[2][0]==player &&(array[1][1])==player&&(array[0][2])==player ))
            {
                Toast.makeText(this, "Player "+player+" win", Toast.LENGTH_SHORT).show();
                win=true;
            }
        if(i==2 &&j==1)
            if((array[0][1]==player &&(array[1][1])==player&&(array[2][1])==player )||(array[2][0]==player &&(array[2][1])==player&&(array[2][2])==player ))
            {
                Toast.makeText(this, "Player "+player+" win", Toast.LENGTH_SHORT).show();
                win=true;
            }
        if(i==2 &&j==2)
            if((array[2][0]==player &&(array[2][1])==player&&(array[2][2])==player )||(array[0][2]==player &&(array[1][2])==player&&(array[2][2])==player )||(array[0][0]==player &&(array[1][1])==player&&(array[2][2])==player ))
            {
                Toast.makeText(this, "Player "+player+" win", Toast.LENGTH_SHORT).show();
                win=true;
            }
        if(win)
        {
            final int winner;
            AlertDialog.Builder ad=new AlertDialog.Builder(Game.this);
            if(Realplayer==player)
            {
                ad.setTitle("Congratulation");
                ad.setMessage("You won");
                winner=Realplayer;

            }
            else
            {
                ad.setTitle("Sorry");
                ad.setMessage("You Lost");

                winner=player;
            }
            ad.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    send("$start$"+winner);
                }
            });
            ad.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    send("$exit$");
                    try{
                        if(s!=null)
                            s.close();
                        finish();
                    }
                    catch (Exception e){

                    }
                }
            });
            ad.create().show();
        }
        else if(turn==9){
            AlertDialog.Builder ad=new AlertDialog.Builder(Game.this);
            ad.setTitle("Its a draw");
            ad.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    send("$start$"+1);
                }
            });
            ad.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    send("$exit$");
                    try{
                        if(s!=null)
                            s.close();
                        finish();
                    }
                    catch (Exception e){

                    }
                }
            });
            ad.create().show();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(s!=null) {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(s!=null)
        {
            try{
                s.close();
            }
            catch (Exception e){}
        }
    }
}
