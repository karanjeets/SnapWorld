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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class LoginActivity extends AppCompatActivity {
    JSONObject jsondata=null;
    String username = "";
    Intent i;

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
                username = a.getText().toString();
                i = new Intent(LoginActivity.this, MainActivity.class);

                AsyncTaskParseJson json = new AsyncTaskParseJson(new AsyncTaskParseJson.AsyncResponse() {


                    @Override

                    public void processFinish(JSONObject output) {
                        try {

                            jsondata=output;
                           // System.out.println("From MainActivity");
                            //System.out.println(jsondata.getString("latitude"));
                            JSONArray jsonarray = jsondata.getJSONArray("Categories");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String name = jsonobject.getString("name");
                                String id = jsonobject.getString("id");
                                Constants.categoryMap.put(name,id);

                                //System.out.println(name);
                            }
                            i.putExtra("username", username);
                            //i.putExtra("categoryMap",categoryMap);
                            startActivity(i);


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                String url = "http://104.197.77.81:8080/snapworld/data/getcategory";


                json.yourJsonStringUrl=url;

                System.out.println(json.yourJsonStringUrl);
                //JSONArray dataJsonArr = null;

                json.execute();


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
