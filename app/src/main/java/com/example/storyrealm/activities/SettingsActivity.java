package com.example.storyrealm.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.example.storyrealm.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private SwitchMaterial themeSwitch;
    private SeekBar fontSizeSeekBar;
    private TextView fontSizeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        themeSwitch = findViewById(R.id.theme_switch);
        fontSizeSeekBar = findViewById(R.id.font_size_seekbar);
        fontSizeLabel = findViewById(R.id.font_size_label);

        // Load saved preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNightMode = sharedPreferences.getBoolean("night_mode", false);
        int fontSize = sharedPreferences.getInt("font_size", 16);

        // Apply saved theme
        AppCompatDelegate.setDefaultNightMode(isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        themeSwitch.setChecked(isNightMode);

        // Apply saved font size
        fontSizeLabel.setTextSize(fontSize);
        fontSizeSeekBar.setProgress(fontSize);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save theme preference
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("night_mode", isChecked);
            editor.apply();

            // Apply theme change
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Save font size preference
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("font_size", progress);
                editor.apply();

                // Apply font size change
                fontSizeLabel.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
