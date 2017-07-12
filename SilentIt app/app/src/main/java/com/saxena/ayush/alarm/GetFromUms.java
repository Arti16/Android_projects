package com.saxena.ayush.alarm;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Ayush on 1/30/2017.
 */

public class GetFromUms extends AppCompatActivity {
    File file;
    //private Button button;
    private WebView webView;
    ListView lv;
    String reg;
    ArrayAdapter<String> arr;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.getfromums_act);
        isStoragePermissionGranted();

        file=Environment.getExternalStorageDirectory();
        //Get webview
        webView = (WebView) findViewById(R.id.webview);
        //startWebView("https://ums.lpu.in/lpuums/Reports/frmStudentTimeTable.aspx/");
        Context context=GetFromUms.this;
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.inputtext, null);

        AlertDialog.Builder adb= new AlertDialog.Builder(
                context);
        adb.setTitle("Enter Your Registration Number");
        // set prompts.xml to alertdialog builder
        adb.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.reginput);
        adb.setCancelable(false);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String text=userInput.getText().toString().trim();
                if(text==null || text.isEmpty())
                {
                    Toast.makeText(GetFromUms.this, "Enter The Registration Number", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                    finish();

                }
                else
                {
                    reg=text;
                    startWebView("https://ums.lpu.in/lpuums/");
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog aa=adb.create();
        aa.show();

        //startWebView("https://ums.lpu.in/lpuums/default.aspx");
        /*webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                startWebView("https://ums.lpu.in/lpuums");

            }

        });*/


        webView.setDownloadListener(new DownloadListener()
        {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                final File destinationDir = new File (Environment.getExternalStorageDirectory(),"Silent It");
                if (!destinationDir.exists()) {
                    destinationDir.mkdir();}
                request.setMimeType(mimeType);
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file...");
                String name=URLUtil.guessFileName(url, contentDisposition, mimeType);
                request.setTitle(name);
                final File destinationFile = new File (destinationDir,name);
                if(destinationFile.exists())
                {
                    if(destinationFile.delete())
                        Toast.makeText(GetFromUms.this, "Previous File Deleted", Toast.LENGTH_SHORT).show();;
                }
                BroadcastReceiver onComplete=new BroadcastReceiver() {
                    public void onReceive(Context ctxt, Intent intent) {
                        // your code
                        setIt(destinationFile);
                        progress.dismiss();
                        //progress.setMessage("Setting everything...");
                        //progress.show();
                    }
                };
                request.setDestinationUri(Uri.fromFile(destinationFile));
                request.allowScanningByMediaScanner();
                getApplicationContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

                //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //request.setDestinationInExternalPublicDir("Silent It",name);
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                progress=new ProgressDialog(GetFromUms.this);
                progress.setMessage("Downloading");
                progress.setCancelable(false);
                progress.show();
                //setIt(destinationFile);
                //Toast.makeText(getApplicationContext(), "Downloading File",Toast.LENGTH_LONG).show();
            }});

    }



    boolean errorRec=true;
    ProgressDialog progressDialog;
    ProgressDialog progress;
    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {
                        //If you will not use this method url links are opeen in new brower not in webview
           /* public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }*/

            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
                if (progressDialog == null  && done)
                {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(GetFromUms.this);
                    progressDialog.setMessage("Loading...\nPlease Wait or Check your internet connection");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            }
            /*public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Alert(view,error.toString());
                view.loadUrl("https://ums.lpu.in/lpuums");
                errorRec=true;
            }*/
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                errorRec=true;
                startWebView("https://ums.lpu.in/lpuums/");

                if (progressDialog!=null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                //Alert(view,description);
               // webView.loadUrl("https://ums.lpu.in/lpuums");

            }

            boolean done=true;
            int i=0;
            public void onPageFinished(WebView view, String url) {
                if(errorRec)
                {
                    errorRec=false;
                    if (progressDialog!=null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                   // Toast.makeText(GetFromUms.this, "newww", Toast.LENGTH_SHORT).show();
                    startWebView("https://ums.lpu.in/lpuums/Reports/frmStudentTimeTable.aspx/");
                }
                else {
                    try {
                        if (progressDialog!=null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                            //Alert();
                        /*.loadUrl("javascript:(function(){"+
                                "l=document.getElementById('mA');"+
                                "e=document.createEvent('HTMLEvents');"+
                                "e.initEvent('click',true,true);"+
                                "l.dispatchEvent(e);"+
                                "})()");*/

                            //view.loadUrl("javascript:(function(){document.getElementById('TextBox1').value='11407635';})()");

                            //view.loadUrl("javascript:($find('ReportViewerabcd').exportReport('XML');)()");

                            if (done) {
                                //Toast.makeText(GetFromUms.this, "done", Toast.LENGTH_SHORT).show();
                                view.loadUrl("javascript:(function(){document.getElementById('txtRegistrationNumber').value='"+reg+"';})()");
                                view.loadUrl("javascript:(function(){document.getElementById('btnGetTimeTable').click();})()");
                                done = false;
                                Alert(view, "Press Ok when you see time table loaded..");
                            }

                        }

                    } catch (Exception exception) {
                        exception.printStackTrace();
                        System.out.print("hel");
                    }
                }
            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);

        // Other webview options

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);


        /*
         String summary = "<html><body>You scored <b>192</b> points.</body></html>";
         webview.loadData(summary, "text/html", null);
         */

        //Load url in webview
        //webView.loadUrl("https://ums.lpu.in/lpuums");
        webView.loadUrl(url);

    }

    // Open previous opened link from history on webview when back button pressed

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        super.onBackPressed();
    }
    void Alert(final WebView vv,final String s)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Instructions");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(s);
        //alertDialogBuilder.setMessage("Enter your Reg No.->'view time table'\n\n->Click save icon\n\n->'Export XML' \n\n Note:Zoom out and then click export");
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        vv.loadUrl("javascript:($find('ReportViewerabcd').exportReport('XML'))()");
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    void setIt(File file)
    {
        webView.setVisibility(View.GONE);
        try {
            JSONObject jsonObj = getJson(file);

            // Getting JSON Array node
            JSONObject report = jsonObj.getJSONObject("Report");
            JSONObject tablix1=report.getJSONObject("Tablix1");
            JSONObject details_collection=tablix1.getJSONObject("Details_Collection");
            JSONArray details=details_collection.getJSONArray("Details");
            SqlDatabase db=new SqlDatabase(getApplicationContext());
            db.open();
            db.trunc();
            db.closeTable();
            // looping through All Contacts
            for (int i = 0; i < details.length(); i++) {
                int times[]=new int[8];
                String classes[]=new String[8];
                int temp=0;
                JSONObject d= details.getJSONObject(i);
                String time=(d.getString("Timing"));
                if(time.contains("PM"))
                    temp=12;
                time=time.substring(0,2);
                times[0]=temp+Integer.parseInt(time);
                classes[0]=""+times[0];
                if(d.has("Mon"))
                {
                    classes[1]=d.getString("Mon");
                    times[1]=1;
                }
                if(d.has("Tue"))
                {
                    classes[2]=d.getString("Tue");
                    times[2]=1;
                }
                if(d.has("Wed"))
                {
                    classes[3]=d.getString("Wed");
                    times[3]=1;
                }
                if(d.has("Thu"))
                {
                    classes[4]=d.getString("Thu");
                    times[4]=1;
                }
                if(d.has("Fri"))
                {
                    classes[5]=d.getString("Fri");
                    times[5]=1;
                }
                if(d.has("Sat"))
                {
                    classes[6]=d.getString("Sat");
                    times[6]=1;
                    if(classes[6].contains("Project Work"))
                        times[6]=0;
                }
                new Transfer2().execute(classes);
                new Transfer().execute(times);
                // Phone node is JSON Object

                // tmp hash map for single contact
            }
            progress.dismiss();
            Toast.makeText(GetFromUms.this, "All Set", Toast.LENGTH_LONG).show();
            finish();
        }
        catch (Exception e){}
    }
    JSONObject getJson(File file) throws IOException {
        System.out.println("hello");
        FileInputStream fis = null;
        final File destinationDir = new File (Environment.getExternalStorageDirectory(),"Silent It");
        File destinationFile = new File (destinationDir,"rptTimeTableStudent.xml");
        try {
            fis = new FileInputStream(destinationFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonObj = null;
            jsonObj = XML.toJSONObject(sb.toString());
            return jsonObj;
        }
        catch (Exception e){
            Log.d("JSON",e.toString());
            return null;}
        finally {
            if (fis!=null)
            {
                fis.close();
            }
        }
    }
    class Transfer extends AsyncTask<int[],Void,String> {

        @Override
        protected String doInBackground(int[]... strings) {

            SqlDatabase d=new SqlDatabase(GetFromUms.this);
            d.open();
            d.createEntry(strings[0]);
            d.closeTable();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    class Transfer2 extends AsyncTask<String[],Void,String> {

        @Override
        protected String doInBackground(String[]... strings) {

            SqlDatabase d=new SqlDatabase(GetFromUms.this);
            d.open();
            d.createEntry(strings[0]);
            d.closeTable();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("err","Permission is granted");
                return true;
            } else {

                Log.v("err","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("err","Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("err","Permission: "+permissions[0]+ "was "+grantResults[0]);
            Toast.makeText(GetFromUms.this,"Permission: "+permissions[0]+ "was "+grantResults[0], Toast.LENGTH_SHORT).show();
            //resume tasks needing this permission
        }
    }
}
