package com.example.table;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;

public class welcome_screen extends AppCompatActivity {
    boolean public_profile_created;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            finishAffinity();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.modify_profile:
                File dir = getFilesDir();
                File file = new File(dir, "profile.json");
                file.delete();
                Intent intent = new Intent(getBaseContext(), profile_creator.class);
                startActivity(intent);
                break;
            case R.id.delete_profile:
                if (public_profile_created) {
                    File Dir = getFilesDir();
                    File FILE = new File(Dir, "profile.json");
                    FILE.delete();
                    Intent intent_del = new Intent(getBaseContext(), welcome_screen.class);
                    startActivity(intent_del);
                    Toast delete_profile = Toast.makeText(welcome_screen.this, "Your profile is deleted!", Toast.LENGTH_SHORT);
                    delete_profile.show();
                }
                else{
                    Toast delete_profile = Toast.makeText(welcome_screen.this, "There is no profile to delete!", Toast.LENGTH_SHORT);
                    delete_profile.show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        boolean profile_created;
        String username = "";
        try {
            FileInputStream profile_str_reader = openFileInput("profile.json");
            int c;
            StringBuilder profile_str= new StringBuilder();
            while( (c = profile_str_reader.read()) != -1){
                profile_str.append((char) c); }
            profile_str_reader.close();
            profile_created = true;
            username = new JSONObject(profile_str.toString()).getString("username");}
        catch (Exception e){profile_created = false;}
        public_profile_created = profile_created;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_welcome_screen);
        final Button goto_triage = findViewById(R.id.button_triage);
        final Button goto_table = findViewById(R.id.button_table);
        final Button goto_faq = findViewById(R.id.button_faq);

        TextView welcome = findViewById(R.id.textView_welcome);
        if (profile_created){ welcome.setText("Welcome back, " + username + "!"); }
        else { welcome.setText("Welcome"); }

        final boolean finalProfile_created = profile_created;
        goto_triage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalProfile_created){
                    Intent intent = new  Intent(getBaseContext(), triage.class);
                    startActivity(intent);
                }
                else {
                    AlertDialog dialog = new AlertDialog.Builder(welcome_screen.this)
                            .setTitle("First, let's create your profile!")
                            .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getBaseContext(), profile_creator.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .create();
                    dialog.show();
                }
            }
        });
        goto_table.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(getBaseContext(), table_result.class);
                startActivity(intent);
            }
        });
        goto_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(getBaseContext(), faq_init.class);
                startActivity(intent);
            }
        });
    }
}