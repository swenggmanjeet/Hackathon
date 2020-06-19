package com.example.table;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class profile_creator extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.close_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.close_btn) {
            Intent intent = new  Intent(getBaseContext(), welcome_screen.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_creator);

        final Button save = findViewById(R.id.button_confirm_table);
        final EditText username = findViewById(R.id.username);
        final EditText age = findViewById(R.id.age);

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                JSONObject profile = new JSONObject();
                if (username.getText().toString().isEmpty())
                    username.setError("Please enter your name!");
                else if (age.getText().toString().isEmpty())
                    age.setError("Please enter your age!");
                else {
                    String username_string = username.getText().toString();
                    try { profile.put("username", username_string); }
                    catch (JSONException e) { e.printStackTrace(); }

                    String age_string = age.getText().toString();
                    try {profile.put("age",age_string);}
                    catch (JSONException e) { e.printStackTrace(); }

                    boolean pregnancy = ((CheckBox) findViewById(R.id.checkBox_pregnancy)).isChecked();
                    try {profile.put("pregnancy",pregnancy);}
                    catch (JSONException e) { e.printStackTrace(); }

                    boolean lung_disease = ((CheckBox) findViewById(R.id.checkBox_lung_disease)).isChecked();
                    try {profile.put("lung_disease",lung_disease);}
                    catch (JSONException e) { e.printStackTrace(); }

                    boolean hearth_disease = ((CheckBox) findViewById(R.id.checkBox_hearth_disease)).isChecked();
                    try {profile.put("hearth_disease",hearth_disease);}
                    catch (JSONException e) { e.printStackTrace(); }

                    boolean immune_syst = ((CheckBox) findViewById(R.id.checkBox_immune_syst)).isChecked();
                    try {profile.put("immune_syst",immune_syst);}
                    catch (JSONException e) { e.printStackTrace(); }

                    boolean obesity = ((CheckBox) findViewById(R.id.checkBox_obesity)).isChecked();
                    try {profile.put("obesity",obesity);}
                    catch (JSONException e) { e.printStackTrace(); }

                    boolean hiv = ((CheckBox) findViewById(R.id.checkBox_hiv)).isChecked();
                    try {profile.put("hiv",hiv);}
                    catch (JSONException e) { e.printStackTrace(); }

                    boolean diabetes = ((CheckBox) findViewById(R.id.checkBox_diabetes)).isChecked();
                    try {profile.put("diabetes",diabetes);}
                    catch (JSONException e) { e.printStackTrace(); }

                    boolean liver_disease = ((CheckBox) findViewById(R.id.checkBox_liver_disease)).isChecked();
                    try {profile.put("liver_disease",liver_disease);}
                    catch (JSONException e) { e.printStackTrace(); }

                    boolean live_alone = ((ToggleButton) findViewById(R.id.live_alone_btn)).isChecked();
                    try {profile.put("live_alone",live_alone);}
                    catch (JSONException e) { e.printStackTrace(); }

                    boolean food_access = ((ToggleButton) findViewById(R.id.food_res_btn)).isChecked();
                    try {profile.put("food_access",food_access);}
                    catch (JSONException e) { e.printStackTrace(); }

                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = openFileOutput("profile.json", Context.MODE_PRIVATE);
                    } catch (FileNotFoundException except) {
                        except.printStackTrace();
                    }
                    try {
                        assert fileOutputStream != null;
                        fileOutputStream.write(profile.toString().getBytes());
                        fileOutputStream.close();
                        Toast myToast = Toast.makeText(getApplication(), "Saved!", Toast.LENGTH_SHORT);
                        myToast.show();
                        Intent intent = new  Intent(getBaseContext(), triage.class);
                        startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast myToast = Toast.makeText(getApplication(), "ERROR!", Toast.LENGTH_SHORT);
                        myToast.show();
                    }
                }
            }
        });

    }
}