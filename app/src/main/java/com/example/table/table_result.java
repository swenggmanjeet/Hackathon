package com.example.table;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;


public class table_result extends AppCompatActivity {
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
        inflater.inflate(R.menu.appbar_plus,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.plus_btn) {
            Intent intent = new Intent(getBaseContext(), table_content.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.delete_SaveFile_btn){
            File Dir = getFilesDir();
            File FILE = new File(Dir, "save_file.json");
            boolean IS_deleted = FILE.delete();
            if (IS_deleted){
                Intent intent = new Intent(getBaseContext(), table_result.class);
                startActivity(intent);}
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_result);
        table_parse_data();
    }

    @SuppressLint("SetTextI18n")
    public void table_parse_data() {

        try {
            FileInputStream profile_str_reader = openFileInput("save_file.json");
            int c;
            int i;
            int n;
            StringBuilder profile_str= new StringBuilder();
            while( (c = profile_str_reader.read()) != -1){
                profile_str.append((char) c); }
            profile_str_reader.close();
            JSONObject table_data_obj = new JSONObject(profile_str.toString());
            JSONArray data = ((JSONArray) table_data_obj.get("data"));

            String symptoms_am = null;
            String symptoms_pm = " Not filled yet";
            String date = null;
            String temperature_am = null;
            String temperature_pm = null;

            List<String> content_list = missed_content(data);

            int data_size = content_list.size();

            if (data_size%2==1){
                i = data_size-1;
                String content_list_per_input = content_list.get(i);
                String[] content_split = content_list_per_input.split("'");
                date = content_split[0];
                temperature_am = content_split[1];
                symptoms_am = content_split[2];
                data_size-=1;

                table_result_dynamic(date,temperature_am,temperature_pm,symptoms_am,symptoms_pm);
            }
            for (i=data_size-1;i>=0;i-=2){
                for (n=i-1;n<=i;n+=1){
                    if(n%2==0){
                        String content_list_per_input = content_list.get(n);
                        String[] content_split = content_list_per_input.split("'");
                        date = content_split[0];
                        temperature_am = content_split[1];
                        symptoms_am = content_split[2];
                    }
                    else{
                        String content_list_per_input = content_list.get(n);
                        String[] content_split = content_list_per_input.split("'");
                        date = content_split[0];
                        temperature_pm = content_split[1];
                        symptoms_pm = content_split[2];
                    }
                }
                table_result_dynamic(date,temperature_am,temperature_pm,symptoms_am,symptoms_pm);
            }
        }
        catch (Exception e) {
            File file_exists = getBaseContext().getFileStreamPath("save_file.json");
            if (file_exists.exists()){
                Toast error = Toast.makeText(table_result.this,"error while parsing data",Toast.LENGTH_SHORT);
                error.show();
            }
        }

    }

    public List<String> missed_content(JSONArray data) throws JSONException {
        List<String> content_string_list = new ArrayList<>();
        String content_string;
        int i;

        for (i=0;i<data.length();i+=1) {
            JSONObject table_data_obj = (JSONObject) data.get(i);
            if (table_data_obj.getString("AM_PM").equals(String.valueOf(false))){
                if (i==0){
                    content_string = select_content(data,i);
                    content_string_list.add(content_string);
                }
                else{
                    JSONObject table_data_obj_pre = (JSONObject) data.get(i-1);
                    if (table_data_obj.getString("data_time").equals(table_data_obj_pre.getString("data_time"))){
                        if (table_data_obj_pre.getString("AM_PM").equals(String.valueOf(false))){
                            content_string = select_content(data,i);
                            content_string_list.set(content_string_list.size()-1,content_string);
                        }
                        else{
                            content_string = select_content(data,i);
                            content_string_list.set(content_string_list.size()-2,content_string);
                        }
                    }
                    else{
                        if (table_data_obj_pre.getString("AM_PM").equals(String.valueOf(false))){
                            content_string = table_data_obj_pre.getString("data_time") + "'" + " " + "'" + " Forgot to fill in!";
                            content_string_list.add(content_string);
                            content_string = select_content(data,i);
                            content_string_list.add(content_string);
                        }
                        else{
                            content_string = select_content(data,i);
                            content_string_list.add(content_string);
                        }
                    }

                }
            }
            else{
                if (i==0){
                    content_string = table_data_obj.getString("data_time") + "'" + " " + "'" + " Forgot to fill in!";
                    content_string_list.add(content_string);
                    content_string = select_content(data,i);
                    content_string_list.add(content_string);
                }
                else{
                    JSONObject table_data_obj_pre = (JSONObject) data.get(i-1);
                    if (table_data_obj.getString("data_time").equals(table_data_obj_pre.getString("data_time"))){
                        if (table_data_obj_pre.getString("AM_PM").equals(String.valueOf(false))){
                            content_string = select_content(data,i);
                            content_string_list.add(content_string);
                        }
                        else{
                            content_string = select_content(data,i);
                            content_string_list.set(content_string_list.size()-1,content_string);
                        }
                    }
                    else{
                        if (table_data_obj_pre.getString("AM_PM").equals(String.valueOf(false))){
                            content_string = table_data_obj_pre.getString("data_time") + "'" + " " + "'" + " Forgot to fill in!";
                            content_string_list.add(content_string);
                            content_string = table_data_obj.getString("data_time") + "'" + " " + "'" + " Forgot to fill in!";
                            content_string_list.add(content_string);
                            content_string = select_content(data,i);
                            content_string_list.add(content_string);
                        }
                        else{
                            content_string = table_data_obj.getString("data_time") + "'" + " " + "'" + " Forgot to fill in!";
                            content_string_list.add(content_string);
                            content_string = select_content(data,i);
                            content_string_list.add(content_string);
                        }
                    }
                }
            }
        }
        List<String> content_string_final = new ArrayList<>();
        for (i=0;i<content_string_list.size();i+=1){
            String data_full = "Day "+(i/2+1)+ " - " + content_string_list.get(i);
            content_string_final.add(data_full);
        }

        return content_string_final;
    }

    public String select_content(JSONArray data,int i) throws JSONException {

        JSONObject table_data_obj = (JSONObject) data.get(i);

        StringBuilder symptoms = new StringBuilder();

        String date = table_data_obj.getString("data_time");
        String temperature = table_data_obj.getString("temperature");

        symptoms.setLength(0);

        if (table_data_obj.getString("cough").equals(String.valueOf(true)))
            symptoms.append(" Cough").append("\n");
        if (table_data_obj.getString("fatigue").equals(String.valueOf(true)))
            symptoms.append(" Fatigue").append("\n");
        if (table_data_obj.getString("chest_pain").equals(String.valueOf(true)))
            symptoms.append(" Chest pain").append("\n");
        if (table_data_obj.getString("breath_difficulty").equals(String.valueOf(true)))
            symptoms.append(" Breath difficulty").append("\n");
        if (table_data_obj.getString("sore_throat").equals(String.valueOf(true)))
            symptoms.append(" Sore throat").append("\n");
        if (table_data_obj.getString("headache").equals(String.valueOf(true)))
            symptoms.append(" Headache").append("\n");
        if (table_data_obj.getString("no_taste").equals(String.valueOf(true)))
            symptoms.append(" Loss of taste smell").append("\n");
        if (table_data_obj.getString("barf").equals(String.valueOf(true)))
            symptoms.append(" Nausea/diarrhea/vomit").append("\n");
        if (table_data_obj.getString("muscle").equals(String.valueOf(true)))
            symptoms.append(" Muscle pain/weakness").append("\n");
        if (table_data_obj.getString("confusion").equals(String.valueOf(true)))
            symptoms.append(" Confusion/dizziness").append("\n");
        if (table_data_obj.getString("skin_lesion").equals(String.valueOf(true)))
            symptoms.append(" Rash/hives skin lesions").append("\n");

        if (symptoms.length() == 0)
            symptoms.append(" None");
        else
            symptoms.setLength(symptoms.length() - 1);

        return (date + "'" + temperature + "'" + symptoms);
    }

    @SuppressLint("SetTextI18n")
    public void table_result_dynamic(String date, String temperature_am, String temperature_pm, String symptoms_am, String symptoms_pm){
        TableLayout table_dynamic_result = findViewById(R.id.TableLayout_result);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

        TableRow row = new TableRow(this);
        row.setBackgroundResource(R.drawable.row_border_top);
        TextView day = new TextView(this);
        day.setText(date);
        day.setTextSize(20);
        day.setTypeface(null, Typeface.BOLD);
        day.setTextColor(Color.BLACK);
        day.setGravity(Gravity.CENTER);
        day.setLayoutParams(new TableRow.LayoutParams(0 , TableLayout.LayoutParams.WRAP_CONTENT, 1));
        row.addView(day);
        table_dynamic_result.addView(row, layoutParams);

        TableRow row1 = new TableRow(this);
        row1.setBackgroundResource(R.drawable.row_border_middle);
        TextView am = new TextView(this);
        am.setText("AM");
        am.setTextColor(Color.BLACK);
        am.setGravity(Gravity.CENTER);
        am.setLayoutParams(new TableRow.LayoutParams(0 , TableLayout.LayoutParams.WRAP_CONTENT, (float) .5));
        TextView pm = new TextView(this);
        pm.setText("PM");
        pm.setTextColor(Color.BLACK);
        pm.setGravity(Gravity.CENTER);
        pm.setLayoutParams(new TableRow.LayoutParams(0 , TableLayout.LayoutParams.WRAP_CONTENT, (float) .5));
        row1.addView(am);
        row1.addView(pm);
        table_dynamic_result.addView(row1, layoutParams);

        TableRow row2 = new TableRow(this);
        row2.setBackgroundResource(R.drawable.row_border_middle);
        TextView temp_text = new TextView(this);
        temp_text.setText(" Temperature:");
        temp_text.setTextColor(Color.BLACK);
        temp_text.setLayoutParams(new TableRow.LayoutParams(0 , TableLayout.LayoutParams.WRAP_CONTENT, (float) .25));
        TextView temp_valor_am = new TextView(this);
        temp_valor_am.setText(temperature_am);
        temp_valor_am.setTextColor(Color.BLACK);
        temp_valor_am.setLayoutParams(new TableRow.LayoutParams(0 , TableLayout.LayoutParams.WRAP_CONTENT, (float) .25));
        TextView temp_text2 = new TextView(this);
        temp_text2.setText(" Temperature:");
        temp_text2.setTextColor(Color.BLACK);
        temp_text2.setLayoutParams(new TableRow.LayoutParams(0 , TableLayout.LayoutParams.WRAP_CONTENT, (float) .25));
        TextView temp_valor_pm = new TextView(this);
        temp_valor_pm.setText(temperature_pm);
        temp_valor_pm.setTextColor(Color.BLACK);
        temp_valor_pm.setLayoutParams(new TableRow.LayoutParams(0 , TableLayout.LayoutParams.WRAP_CONTENT, (float) .25));
        row2.addView(temp_text);
        row2.addView(temp_valor_am);
        row2.addView(temp_text2);
        row2.addView(temp_valor_pm);
        table_dynamic_result.addView(row2, layoutParams);

        TableRow row3 = new TableRow(this);
        row3.setBackgroundResource(R.drawable.row_border_middle_borderless);
        TextView symptoms_text = new TextView(this);
        symptoms_text.setText(" Symptoms:");
        symptoms_text.setTextColor(Color.BLACK);
        symptoms_text.setLayoutParams(new TableRow.LayoutParams(0 , TableLayout.LayoutParams.WRAP_CONTENT, (float) .5));
        TextView symptoms_text2 = new TextView(this);
        symptoms_text2.setText(" Symptoms:");
        symptoms_text2.setTextColor(Color.BLACK);
        symptoms_text2.setLayoutParams(new TableRow.LayoutParams(0 , TableLayout.LayoutParams.WRAP_CONTENT, (float) .5));
        row3.addView(symptoms_text);
        row3.addView(symptoms_text2);
        table_dynamic_result.addView(row3, layoutParams);

        TableRow row4 = new TableRow(this);
        row4.setBackgroundResource(R.drawable.row_border_bottom);
        TextView symptoms_content_am = new TextView(this);
        symptoms_content_am.setText(symptoms_am);
        symptoms_content_am.setTextColor(Color.BLACK);
        symptoms_content_am.setLayoutParams(new TableRow.LayoutParams(0 , TableLayout.LayoutParams.WRAP_CONTENT, (float) .5));
        TextView symptoms_content_pm = new TextView(this);
        symptoms_content_pm.setText(symptoms_pm);
        symptoms_content_pm.setTextColor(Color.BLACK);
        symptoms_content_pm.setLayoutParams(new TableRow.LayoutParams(0 , TableLayout.LayoutParams.WRAP_CONTENT, (float) .5));
        row4.addView(symptoms_content_am);
        row4.addView(symptoms_content_pm);
        table_dynamic_result.addView(row4, layoutParams);

        TableRow row5 = new TableRow(this);
        TextView null_text = new TextView(this);
        null_text.setText("");
        null_text.setTextSize(6);
        null_text.setTextColor(Color.BLACK);
        null_text.setLayoutParams(new TableRow.LayoutParams(0 , TableLayout.LayoutParams.WRAP_CONTENT, (float) 1));
        row5.addView(null_text);
        table_dynamic_result.addView(row5, layoutParams);
    }

}

