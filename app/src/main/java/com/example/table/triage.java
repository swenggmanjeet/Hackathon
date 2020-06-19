package com.example.table;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;

public class triage extends AppCompatActivity {
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new  Intent(getBaseContext(), welcome_screen.class);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

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
        setContentView(R.layout.triage);

        final Button finish = findViewById(R.id.finish_btn);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 0;

                if (((CheckBox) findViewById(R.id.fever_check)).isChecked()) count+= 1;
                if (((CheckBox) findViewById(R.id.shortness_check)).isChecked()) count+= 1;
                if (((CheckBox) findViewById(R.id.chest_pain_check)).isChecked()) count+= 1;
                if (((CheckBox) findViewById(R.id.blue_lips_check)).isChecked()) count+= 1;
                if (((CheckBox) findViewById(R.id.breathing_check)).isChecked()) count+= 1;
                if (((CheckBox) findViewById(R.id.confusion_check)).isChecked()) count+= 1;
                boolean infectedP = ((ToggleButton) findViewById(R.id.person_infected_toggle)).isChecked();
                boolean infectedC = ((ToggleButton) findViewById(R.id.country_infected_toggle)).isChecked();

                if (count <= 0 && !infectedP && !infectedC){
                    Intent intent = new  Intent(getBaseContext(), you_fine_scr.class);
                    startActivity(intent);
                }
                else if(count <= 0 && (infectedP | infectedC)){
                    Intent intent = new  Intent(getBaseContext(), be_aware_1_scr.class);
                    startActivity(intent);
                }
                else{
                    JSONObject profile = new JSONObject();
                    try {
                        FileInputStream profile_str_reader = openFileInput("profile.json");
                        int c;
                        StringBuilder profile_str= new StringBuilder();
                        while( (c = profile_str_reader.read()) != -1){
                            profile_str.append((char) c); }
                        profile_str_reader.close();
                        profile = new JSONObject(profile_str.toString());}
                    catch (Exception e){e.printStackTrace();}

                    int age, risk_group=0,res_acc=0;
                    try {
                        age = Integer.parseInt(profile.getString("age"));
                        if (profile.getString("pregnancy").equals(String.valueOf(true))) risk_group += 1;
                        if (profile.getString("lung_disease").equals(String.valueOf(true))) risk_group += 1;
                        if (profile.getString("hearth_disease").equals(String.valueOf(true))) risk_group += 1;
                        if (profile.getString("immune_syst").equals(String.valueOf(true))) risk_group += 1;
                        if (profile.getString("obesity").equals(String.valueOf(true))) risk_group += 1;
                        if (profile.getString("hiv").equals(String.valueOf(true))) risk_group += 1;
                        if (profile.getString("diabetes").equals(String.valueOf(true))) risk_group += 1;
                        if (profile.getString("liver_disease").equals(String.valueOf(true))) risk_group += 1;

                    if (age < 60 && risk_group <= 2){
                        if (profile.getString("live_alone").equals(String.valueOf(true))) res_acc += 1;
                        if (profile.getString("food_access").equals(String.valueOf(true))) res_acc += 1;

                        if (count <= 1 && res_acc == 2){
                            Intent intent = new  Intent(getBaseContext(), be_aware_2_scr.class);
                            startActivity(intent);
                        }
                        else{
                            Intent intent = new  Intent(getBaseContext(), call_doc_scr.class);
                            startActivity(intent);
                        }
                    }
                    else{
                        Intent intent = new  Intent(getBaseContext(), call_doc_scr.class);
                        startActivity(intent);
                    }}
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}