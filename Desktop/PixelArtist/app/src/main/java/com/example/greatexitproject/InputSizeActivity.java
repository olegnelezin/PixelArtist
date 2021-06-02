package com.example.greatexitproject;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.greatexitproject.settings.initOptions;

import java.util.Locale;

public class InputSizeActivity extends Activity implements initOptions, SharedPreferences.OnSharedPreferenceChangeListener {
    EditText editWidth;
    EditText editHeight;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

        setContentView(R.layout.input_size);
        initOptions();
        i = new Intent(this, MainActivity.class);
    }

    public void setSize(View v) {
        editWidth = findViewById(R.id.edit_width);
        editHeight = findViewById(R.id.edit_height);

        String width = editWidth.getText().toString();
        String height = editHeight.getText().toString();

        i.putExtra("width", width);
        i.putExtra("height", height);
        startActivity(i);
    }


    @Override
    public void initOptions() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView inputInfoForUser = findViewById(R.id.text_for_user);
        TextView startButton = findViewById(R.id.start_button);
        TextView editTextWidth = findViewById(R.id.edit_width);
        TextView editTextHeight = findViewById(R.id.edit_height);
        String option = preferences.getString("text_size", "Средний");
        switch (option) {
            case "Большой":
                editTextWidth.setTextSize(28);
                editTextHeight.setTextSize(28);
                inputInfoForUser.setTextSize(30);
                startButton.setTextSize(27);
                break;
            case "Средний":
                editTextWidth.setTextSize(18);
                editTextHeight.setTextSize(18);
                inputInfoForUser.setTextSize(23);
                startButton.setTextSize(20);
                break;
            case "Маленький":
                editTextWidth.setTextSize(14);
                editTextHeight.setTextSize(14);
                inputInfoForUser.setTextSize(15);
                startButton.setTextSize(13);
                break;
        }
    }

    @Override
    public void setAppLocale(String localeCode) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initOptions();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("language")) {
            String lang = sharedPreferences.getString("language", "English");
            if (lang.equals("English")) {
                setAppLocale("en");
            } else {
                setAppLocale("ru");
            }
            recreate();

        }
    }
}
