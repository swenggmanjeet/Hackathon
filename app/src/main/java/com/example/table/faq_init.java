package com.example.table;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class faq_init extends Activity {
    public boolean selected_title = false;
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent;
            if (selected_title){
                intent = new  Intent(getBaseContext(), faq_init.class);
                startActivity(intent);}
            else {
                intent = new  Intent(getBaseContext(), welcome_screen.class);
                startActivity(intent);
                finish();}
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_init);

        final Button Return = findViewById(R.id.q_a_Ret_btn);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_title) {
                    Intent intent = new Intent(getBaseContext(), faq_init.class);
                    startActivity(intent);
                }
                else{Intent intent = new Intent(getBaseContext(), welcome_screen.class);
                    startActivity(intent);}
            }
        });

        BufferedReader reader;
        StringBuilder faq_file = new StringBuilder();
        JSONObject faq = new JSONObject();
        int cnt = 0;
        try { reader = new BufferedReader(new InputStreamReader(getAssets().open("faq.json")));
            String mLine;
            while ((mLine = reader.readLine()) != null) { faq_file.append(mLine); }
            faq = new JSONObject(faq_file.toString());

            cnt = faq.getJSONArray("topics").length(); }
        catch (IOException | JSONException e) {e.printStackTrace();}
        int i;
        final TableLayout init_layout = findViewById(R.id.faq_init_layout);
        final TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        for (i=0;i<cnt;i++){
            try {StringBuilder current_title = new StringBuilder();
                current_title.append(Objects.requireNonNull(faq.getJSONArray("topics").getJSONObject(i).names()).toString());
                current_title.replace(0,2,"");
                current_title.replace(current_title.length()-2,current_title.length(),"");

                TableRow row = new TableRow(this);
                Button title_btn = new Button(this);
                title_btn.setText(current_title);
                title_btn.setId(i);
                final JSONObject finalFaq = faq;
                title_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selected_title = true;
                        init_layout.removeAllViews();

                        JSONObject micro_faq;
                        try {
                            micro_faq = finalFaq.getJSONArray("topics").getJSONObject(v.getId());
                            StringBuilder topic_name = new StringBuilder().append(Objects.requireNonNull(micro_faq.names()).toString());
                            topic_name.replace(0,2,"");
                            topic_name.replace(topic_name.length()-2,topic_name.length(),"");
                            JSONArray micro_faq_array = micro_faq.getJSONArray(topic_name.toString());

                            int micro_cnt = micro_faq_array.length();
                            int j;
                            for (j=0;j<micro_cnt;j++){
                                JSONObject current_qa = micro_faq_array.getJSONObject(j);
                                TableRow row_q = new TableRow(init_layout.getContext());
                                TableRow row_a = new TableRow(init_layout.getContext());
                                TextView text_q = new TextView(init_layout.getContext());
                                TextView text_a = new TextView(init_layout.getContext());

                                String string_q = current_qa.getString("q") + "\n";
                                text_q.setText(string_q);
                                text_q.setGravity(Gravity.CENTER);
                                text_q.setTextSize(20);
                                text_q.setTextColor(Color.BLUE);
                                text_q.setTypeface(null, Typeface.BOLD);

                                String string_a = current_qa.getString("a") + "\n\n";
                                text_a.setText(string_a);
                                text_a.setTextSize(18);
                                text_a.setTextColor(Color.BLACK);

                                text_q.setLayoutParams(new TableRow.LayoutParams(0 , TableLayout.LayoutParams.MATCH_PARENT, 1));
                                text_a.setLayoutParams(new TableRow.LayoutParams(0 , TableLayout.LayoutParams.MATCH_PARENT, 1));

                                row_q.addView(text_q);
                                row_a.addView(text_a);

                                init_layout.addView(row_q,layoutParams);
                                init_layout.addView(row_a,layoutParams);

                            }
                        }
                        catch (JSONException e) {
                            Toast my_toast = Toast.makeText(getApplication(),e.getMessage(),Toast.LENGTH_LONG);
                            my_toast.show();
                            e.printStackTrace();}
                    }
                });
                title_btn.setLayoutParams(new TableRow.LayoutParams(0 , TableLayout.LayoutParams.MATCH_PARENT, 1));
                row.addView(title_btn);
                init_layout.addView(row,layoutParams);
            }
            catch (Exception e) { e.printStackTrace(); }
        }
    }
}
