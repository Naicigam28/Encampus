package com.viper.team.encampus;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements View.OnClickListener {

    String strEmail, password, username;
    EditText email, pass;
    ProgressBar bar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.editText);
        pass = (EditText) findViewById(R.id.editText2);
        findViewById(R.id.signUp).setOnClickListener(this);
        findViewById(R.id.button11).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        bar=findViewById(R.id.progressBar2);


    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    protected void login() {
        bar.setVisibility(View.VISIBLE);
        if(strEmail.isEmpty())
        {
            email.setError("Please enter Your Email");
            bar.setVisibility(View.GONE);
            return;
        }
        else if(password.isEmpty())
        {
            pass.setError("Please enter Your Password");
            bar.setVisibility(View.GONE);
            return;
        }
        else{
        mAuth.signInWithEmailAndPassword(strEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    finish();
                    username = mAuth.getCurrentUser().getDisplayName();
                    TextView msg = (TextView) findViewById(R.id.welMsg);
                    Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    MessageNotificationService service=new MessageNotificationService(getApplicationContext());
                    startService(new Intent(getApplicationContext(),service.getClass()));
                    bar.setVisibility(View.GONE);
                    startActivity(intent);
                    // msg.setText("Welcome test");
                } else {
                    bar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });}


    }


    boolean ValidateUserInput(TextView view) {
        Pattern specialChars = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        if (specialChars.matcher(view.getText()).find()) {
            view.setBackgroundColor(Color.RED);
            return false;
        } else return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUp:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.button7:
                startActivity(new Intent(this,PassReset.class));
                break;
            case R.id.button11:
                strEmail = email.getText().toString().trim();
                password = pass.getText().toString().trim();
                login();
                break;
        }
    }
}
