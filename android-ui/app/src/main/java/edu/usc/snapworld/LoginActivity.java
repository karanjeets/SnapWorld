package edu.usc.snapworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginClick(View v)
    {
        if(v.getId() == R.id.Login)
        {
            EditText b = (EditText) findViewById(R.id.password);
            String password = b.getText().toString();
            if(password.equals("snapworld")) {
                EditText a = (EditText) findViewById(R.id.username);
                String username = a.getText().toString();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("username", username);
                startActivity(i);
            }
            else
            {
                //Toa.makeText(getActivity(), "this is my Toast message!!! =)",
                //      Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Password Incorrect!!! ",Toast.LENGTH_LONG).show();
            }
        }
    }

}
