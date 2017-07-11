package com.saxena.ayush.keepit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DisplayDetails extends AppCompatActivity {
    TextView title,email,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b=getIntent().getExtras();
        setContentView(R.layout.activity_display_details);
        title=(TextView)findViewById(R.id.showTitleName);
        email=(TextView)findViewById(R.id.showEmail);
        pass=(TextView)findViewById(R.id.showPassword);
        title.setText(b.getString("title"));
        email.setText(b.getString("email"));
        pass.setText(b.getString("password"));

    }
}
