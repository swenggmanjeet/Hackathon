package com.example.table;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class table_content extends AppCompatActivity {
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new  Intent(getBaseContext(), table_result.class);
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
            Intent intent = new  Intent(getBaseContext(), table_result.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_content);
        final Button save = findViewById(R.id.button_confirm_table);
        final EditText temperature = findViewById(R.id.text_temperature);
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean file_exists;
                File old_file_exists = getBaseContext().getFileStreamPath("save_file.json");
                file_exists = old_file_exists.exists();

                JSONObject save_file = new JSONObject();
                JSONObject old_file = new JSONObject();

                if (file_exists){
                    try {
                        FileInputStream str_reader = openFileInput("save_file.json");
                        int c;
                        StringBuilder save_file_str= new StringBuilder();
                        while( (c = str_reader.read()) != -1){
                            save_file_str.append((char) c); }
                        str_reader.close();
                        old_file = new JSONObject(save_file_str.toString());
                    }
                    catch (Exception e) {e.printStackTrace();}
                }

                // save date/time
                @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("MMM dd yyyy");
                String dateFormat = date.format(Calendar.getInstance().getTime());
                //Date savedTime = Calendar.getInstance().getTime();
                try { save_file.put("data_time",dateFormat); }
                catch (JSONException e) { e.printStackTrace(); }

                // toggle Am/Pm
                boolean toggle = ((ToggleButton) findViewById(R.id.togglebutton_AM_PM)).isChecked();
                try {save_file.put("AM_PM",toggle);}
                catch (JSONException e) {e.printStackTrace();}

                // Save temperature
                if (temperature.getText().toString().isEmpty()){
                    temperature.setError("Enter temperature!"); }
                else {
                    String temperature_string = temperature.getText().toString();
                    try {
                        save_file.put("temperature", temperature_string);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //save checkbox list
                    boolean cough = ((CheckBox) findViewById(R.id.checkBox_cough)).isChecked();
                    try {
                        save_file.put("cough", cough);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    boolean fatigue = ((CheckBox) findViewById(R.id.checkBox_fatigue)).isChecked();
                    try {
                        save_file.put("fatigue", fatigue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    boolean chestPain = ((CheckBox) findViewById(R.id.checkBox_chest_pain)).isChecked();
                    try {
                        save_file.put("chest_pain", chestPain);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    boolean breath = ((CheckBox) findViewById(R.id.checkBox_breath_difficulty)).isChecked();
                    try {
                        save_file.put("breath_difficulty", breath);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    boolean sore = ((CheckBox) findViewById(R.id.checkBox_sore_throat)).isChecked();
                    try {
                        save_file.put("sore_throat", sore);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    boolean headache = ((CheckBox) findViewById(R.id.checkBox_headache)).isChecked();
                    try {
                        save_file.put("headache", headache);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    boolean taste = ((CheckBox) findViewById(R.id.checkBox_taste)).isChecked();
                    try {
                        save_file.put("no_taste", taste);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    boolean barf = ((CheckBox) findViewById(R.id.checkBox_vomit)).isChecked();
                    try {
                        save_file.put("barf", barf);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    boolean muscle = ((CheckBox) findViewById(R.id.checkBox_muscle)).isChecked();
                    try {
                        save_file.put("muscle", muscle);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    boolean confusion = ((CheckBox) findViewById(R.id.checkBox_confusion)).isChecked();
                    try {
                        save_file.put("confusion", confusion);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    boolean lesion = ((CheckBox) findViewById(R.id.checkBox_lesion)).isChecked();
                    try {
                        save_file.put("skin_lesion", lesion);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = openFileOutput("save_file.json", Context.MODE_PRIVATE);
                    } catch (FileNotFoundException except) {
                        except.printStackTrace();
                    }
                    try {
                        JSONObject new_file = new JSONObject();
                        JSONArray my_data = new JSONArray();
                        if (file_exists) {
                            my_data = ((JSONArray) old_file.get("data"));
                        }
                        my_data.put(save_file);
                        new_file.put("data", my_data);

                        assert fileOutputStream != null;
                        fileOutputStream.write(new_file.toString().getBytes());
                        fileOutputStream.close();
                        Toast myToast = Toast.makeText(getApplication(), "Saved!", Toast.LENGTH_LONG);
                        myToast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast myToast = Toast.makeText(getApplication(), "ERROR!", Toast.LENGTH_SHORT);
                        myToast.show();
                        File Dir = getFilesDir();
                        File FILE = new File(Dir, "save_file.json");
                        FILE.delete();
                    }
                    Intent my_intent = new  Intent(getBaseContext(), table_result.class);
                    startActivity(my_intent);
                }

            }
        });
    }
}
