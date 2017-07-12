package com.saxena.ayush.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String day;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private Toolbar toolbar;
    FloatingActionButton btn;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //fetching day---------------------------------------------------------------DAy
        Calendar cal = Calendar.getInstance();
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                day = "monday";
                break;
            case Calendar.TUESDAY:
                day = "tuesday";
                break;
            case Calendar.WEDNESDAY:
                day = "wednesday";
                break;
            case Calendar.THURSDAY:
                day = "thursday";
                break;
            case Calendar.FRIDAY:
                day = "friday";
                break;
            case Calendar.SATURDAY:
                day = "saturday";
                break;
            case Calendar.SUNDAY:
                day = "sunday";
                break;
        }
        //-----------------------------------------------------------------------------DAY
        setContentView(R.layout.activity_main);
        //Toast.makeText(this, ""+Environment.getExternalStorageDirectory().toString(), Toast.LENGTH_LONG).show();
        btn=(FloatingActionButton)findViewById(R.id.floatingbtn);
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(MainActivity.this, "Press To Set Everything", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SetIt.class);
                startService(i);
                Toast.makeText(MainActivity.this, "All Settings have been saved.\nYou can close the app now", Toast.LENGTH_LONG).show();
            }
        });
        /*btn = (Button) findViewById(R.id.button);
        btn.setText("Set For Today");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Context context = getApplicationContext();
        final AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent set = new Intent(context, SetAlarm.class);
        PendingIntent SetPending = PendingIntent.getBroadcast(context, 100, set, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis() + 5 * 60 * 1000, AlarmManager.INTERVAL_DAY, SetPending);
        tempCursor.close();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();*/
        setAlarm();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.getfromums) {
            Intent i=new Intent(this,GetFromUms.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Monday(), "Monday");
        adapter.addFragment(new Tuesday(), "Tuesday");
        adapter.addFragment(new Wednesday(), "Wednesday");
        adapter.addFragment(new Thursday(), "Thursday");
        adapter.addFragment(new Friday(), "Friday");
        adapter.addFragment(new Saturday(), "Saturday");
        adapter.addFragment(new Sunday(), "Sunday");
        viewPager.setAdapter(adapter);
    }

    int tm = 101;

    private void generateNotification(Context context, String Text) {
        String message = Text;
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ringing);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle("Title").setContentText(message)
                .setSmallIcon(R.drawable.ringing)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        Notification notification = builder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(tm++, notification);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    void setAlarm()
    {
        Context context = getApplicationContext();
        final AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent set = new Intent(context, SetAlarm.class);
        PendingIntent SetPending = PendingIntent.getBroadcast(context, 100, set, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,30);
        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis()+120*1000, AlarmManager.INTERVAL_DAY, SetPending);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // callSetit();
        setAlarm();
    }

    void callSetit()
    {
        Intent i = new Intent(getApplicationContext(), SetIt.class);
        startService(i);
    }
}
