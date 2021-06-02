package com.example.greatexitproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.greatexitproject.scrolling.MultiScrollView;
import com.example.greatexitproject.settings.SettingsActivity;
import com.example.greatexitproject.settings.initOptions;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener, initOptions, ColorPickerDialogListener, SharedPreferences.OnSharedPreferenceChangeListener {

    final static int ID_FOR_CHANGE_MAIN_COLOR = 1;
    final static int ID_FOR_CHANGE_BACKGROUND_COLOR = 2;

    private ScrollView scrollY;
    private HorizontalScrollView scrollX;
    public static Button[][] buttons;
    private int WIDTH, HEIGHT;
    private int mainButtonOnField;
    int rememberedButtonId;
    private final int defaultBackgroundColor = Color.WHITE;
    private int backgroundColor;
    private final int defaultColor = Color.BLACK;
    private int mainColor;
    private GestureDetector gestureDetector;
    private ImageButton chooseColor;
    private ImageButton chooseBackgroundColor;
    private ImageButton settings;
    Intent toSettings;
    private SharedPreferences preferences;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isShowBar = preferences.getBoolean("switch_bar", false);
        if (isShowBar) {
            setTheme(R.style.AppThemeLightBar);
        } else {
            setTheme(R.style.AppThemeLightNoBar);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.field);
        initUI();



        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                scrollY.smoothScrollBy(0, (int) distanceY);
                scrollX.smoothScrollBy((int) distanceX, 0);
                return true;
            }
        });

        scrollY.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });


        toSettings = new Intent(this, SettingsActivity.class);


        try {
            WIDTH = Integer.parseInt(getIntent().getStringExtra("width")); // take input width from first activity
            HEIGHT = Integer.parseInt(getIntent().getStringExtra("height")); // take input height from first activity
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        initOptions();
        makeCells();

        chooseColor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createColorPickerDialog(ID_FOR_CHANGE_MAIN_COLOR);
            }
        });

        chooseBackgroundColor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createColorPickerDialog(ID_FOR_CHANGE_BACKGROUND_COLOR);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(toSettings);
            }
        });

    }


    private void makeCells() { // void to make a field with buttons
        buttons = new Button[WIDTH][HEIGHT];
        GridLayout fieldLayout = findViewById(R.id.fieldLayout);
        fieldLayout.setRowCount(HEIGHT);
        fieldLayout.setColumnCount(WIDTH);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rememberedButtonId = mainButtonOnField; // remembering button id
                buttons[i][j] = (Button) inflater.inflate(mainButtonOnField, fieldLayout, false);
                fieldLayout.addView(buttons[i][j]);
                buttons[i][j].setOnTouchListener(this);
                buttons[i][j].setTag(0); // 0 - button is pressed, 1 - button is not pressed
            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) { // touch handler
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (v.getTag().equals(0)) {
                    v.setBackgroundColor(mainColor);
                    v.setTag(1);
                } else if (v.getTag().equals(1)) {
                    v.setBackgroundColor(backgroundColor);
                    v.setTag(0);
                }
                break;
        }
        return false;
    }


    private void changeAllButtonsColor(Button[][] buttons, int color) { // void to paint all buttons on the field
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                buttons[i][j].setBackgroundColor(color);
            }
        }
    }

    private void createColorPickerDialog(int id) {
        ColorPickerDialog.newBuilder()
                .setColor(Color.RED)
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.CIRCLE)
                .setDialogId(id)
                .show(this);
    }

    @Override
    public void initOptions() {
        TextView mainColor = findViewById(R.id.main_color_text);
        TextView backgroundColor = findViewById(R.id.background_color_text);
        TextView settings = findViewById(R.id.settings_text);

        String text_size_option = preferences.getString("text_size", "Средний");
        switch (text_size_option) {
            case "Большой":
                mainColor.setTextSize(17);
                backgroundColor.setTextSize(17);
                settings.setTextSize(17);
                break;
            case "Средний":
                mainColor.setTextSize(14);
                backgroundColor.setTextSize(14);
                settings.setTextSize(14);
                break;
            case "Маленький":
                mainColor.setTextSize(11);
                backgroundColor.setTextSize(11);
                settings.setTextSize(11);
                break;
        }

        String buttonsOnFieldSize = preferences.getString("button_size", "Средний");
        switch (buttonsOnFieldSize) {
            case "Большой":
                mainButtonOnField = R.layout.large_size_button;
                break;
            case "Средний":
                mainButtonOnField = R.layout.middle_size_button;
                break;
            case "Маленький":
                mainButtonOnField = R.layout.small_size_button;
                break;
        }


    }

    private void initUI() {
        scrollY = (MultiScrollView) findViewById(R.id.scrollY);
        scrollX = (HorizontalScrollView) findViewById(R.id.scrollX);
        chooseColor = findViewById(R.id.main_color_button);
        chooseBackgroundColor = findViewById(R.id.background_color_button);
        settings = findViewById(R.id.settings_button);
        backgroundColor = defaultBackgroundColor;
        mainColor = defaultColor;
        preferences.registerOnSharedPreferenceChangeListener(this);

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
    public void onColorSelected(int dialogId, int color) {
        switch (dialogId) {
            case 1:
                mainColor = color;
                break;
            case 2:
                changeAllButtonsColor(buttons, color);
                backgroundColor = color;
                break;
        }
    }



    @Override
    public void onDialogDismissed(int dialogId) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (rememberedButtonId != mainButtonOnField) {

            recreate();
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("language")) {
            String lang = sharedPreferences.getString("language", "Русский");
            if (lang.equals("English")) {
                setAppLocale("en");
            } else {
                setAppLocale("ru");
            }
            recreate();
        } else if (key.equals("switch_bar")) {
            boolean showActionBar = sharedPreferences.getBoolean("switch_bar", false);
                if (showActionBar) {
                    setTheme(R.style.AppThemeLightBar);
                } else {
                    setTheme(R.style.AppThemeLightNoBar);
                }
                recreate();
            } else if (key.equals("button_size")) {

            String buttonsOnFieldSize = preferences.getString("button_size", "Средний");
            switch (buttonsOnFieldSize) {
                case "Большой":
                    mainButtonOnField = R.layout.large_size_button;
                    break;
                case "Средний":
                    mainButtonOnField = R.layout.middle_size_button;
                    break;
                case "Маленький":
                    mainButtonOnField = R.layout.small_size_button;
                    break;
            }
        } else if (key.equals("text_size")) {
            TextView mainColor = findViewById(R.id.main_color_text);
            TextView backgroundColor = findViewById(R.id.background_color_text);
            TextView settings = findViewById(R.id.settings_text);

            String text_size_option = preferences.getString("text_size", "Средний");
            switch (text_size_option) {
                case "Большой":
                    mainColor.setTextSize(17);
                    backgroundColor.setTextSize(17);
                    settings.setTextSize(17);
                    break;
                case "Средний":
                    mainColor.setTextSize(14);
                    backgroundColor.setTextSize(14);
                    settings.setTextSize(14);
                    break;
                case "Маленький":
                    mainColor.setTextSize(11);
                    backgroundColor.setTextSize(11);
                    settings.setTextSize(11);
                    break;
            }
        }

        }
    }

