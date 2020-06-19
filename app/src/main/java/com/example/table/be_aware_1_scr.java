package com.example.table;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class be_aware_1_scr extends Activity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_be_aware_1);

        final Button Return = findViewById(R.id.BAware1Ret_btn);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(getBaseContext(), welcome_screen.class);
                startActivity(intent);
            }
        });
    }
}
