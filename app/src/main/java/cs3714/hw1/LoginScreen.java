package cs3714.hw1;


import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class LoginScreen extends Activity implements View.OnClickListener {


    private EditText password, username;
    private Button login;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        editor = getPreferences(Context.MODE_PRIVATE).edit();
        login = (Button)findViewById(R.id.button);
        login.setOnClickListener(this);

        password = (EditText)findViewById(R.id.password);
        username = (EditText)findViewById(R.id.username);
    }

    @Override
    public void onClick(View v) {



        if(password.getText().toString().equals("123") &&
                username.getText().toString().equals("user")) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("loggedin",true);
            this.startActivity(intent);
            finish();

        }
    }
}
