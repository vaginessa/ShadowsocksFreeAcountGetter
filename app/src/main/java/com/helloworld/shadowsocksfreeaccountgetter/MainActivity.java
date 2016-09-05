package com.helloworld.shadowsocksfreeaccountgetter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;


public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("settings", MODE_WORLD_READABLE);
        Config.CHECKED = sharedPreferences.getBoolean("checked", true);
        Switch switcher = (Switch) findViewById(R.id.switcher);
        switcher.setChecked(Config.CHECKED);
        switcher.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Config.CHECKED = b;
        sharedPreferences.edit().putBoolean("checked", b).apply();
    }
}
