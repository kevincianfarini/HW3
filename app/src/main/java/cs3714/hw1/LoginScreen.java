package cs3714.hw1;


import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cs3714.hw1.Constants.Constants;
import cs3714.hw1.interfaces.LogInScreenInteraction;
import cs3714.hw1.network.UserLoginTask;


public class LoginScreen extends Activity implements View.OnClickListener, LogInScreenInteraction {


    private EditText password, username;
    private Button login;
    SharedPreferences.Editor editor;
    private boolean busyNetworking = false;
    SharedPreferences prefs;
    UserLoginTask loginTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();
        login = (Button)findViewById(R.id.button);
        login.setOnClickListener(this);

        password = (EditText) findViewById(R.id.password);
        username = (EditText) findViewById(R.id.username);
    }

    @Override
    public void onClick(View v) {
        if (!busyNetworking) {
            loginTask = new UserLoginTask(username.getText().toString(),
                    password.getText().toString(), this);
            loginTask.execute();
        }
    }

    @Override
    public void LoginStatus(String status) {
        editor.putString("status", status).commit();
        if (status.equals(Constants.STATUS_LOGGED_IN)) {
            this.startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void NetworkingFlagUpdate(Boolean busyNetworking) {
        this.busyNetworking = busyNetworking;
    }
}
