package com.lions.torque.caring.servicecar;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lions.torque.caring.R;
import com.lions.torque.caring.sessions_manager.SessionManager;
import com.squareup.picasso.Picasso;

public class Profile_Page extends AppCompatActivity {


    ImageView dp, back;
    SessionManager sessionManager;
    TextView name, email, ph_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManager = new SessionManager(getApplicationContext());
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"OpenSans.ttf");

        name = (TextView)findViewById(R.id.profile_name);
        email = (TextView)findViewById(R.id.profile_email);
        dp = (ImageView)findViewById(R.id.profile_pic);
        name.setTypeface(typeface);
        email.setTypeface(typeface);
        back = (ImageView)findViewById(R.id.profile_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Picasso.with(this)
                .load(sessionManager.getUserDetails().get("dp"))
                .resize(400,400)                        // optional
                .into(dp);

        name.setText(sessionManager.getUserDetails().get("name"));
        email.setText(sessionManager.getUserDetails().get("email"));



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
