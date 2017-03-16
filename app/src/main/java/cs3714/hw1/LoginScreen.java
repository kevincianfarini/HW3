package cs3714.hw1;


import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cs3714.hw1.interfaces.LogInScreenInteraction;
import cs3714.hw1.network.UserLoginTask;


public class LoginScreen extends Activity implements View.OnClickListener, LogInScreenInteraction {


    private EditText password, username;
    private Button login;
    SharedPreferences.Editor editor;
    private boolean busyNetworking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        editor = getPreferences(Context.MODE_PRIVATE).edit();
        login = (Button)findViewById(R.id.button);
        login.setOnClickListener(this);

        password = (EditText) findViewById(R.id.password);
        username = (EditText) findViewById(R.id.username);
        busyNetworking = false;
    }

    @Override
    public void onClick(View v) {
//        if(password.getText().toString().equals("123") &&
//                username.getText().toString().equals("user")) {
//
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.putExtra("loggedin",true);
//            this.startActivity(intent);
//            finish();
//
//        }
        if (!busyNetworking) {
            UserLoginTask loginTask = new UserLoginTask(username.getText().toString(),
                    password.getText().toString(), getApplicationContext());
            loginTask.execute();
        }
    }

    @Override
    public void LoginStatus(String status) {
        
    }

    @Override
    public void NetworkingFlagUpdate(Boolean busyNetworking) {

    }
}
