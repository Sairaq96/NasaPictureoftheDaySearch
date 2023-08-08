package com.example.nasapictureofthedaysearch;

import static android.icu.text.DateFormat.getDateInstance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextSelection;
import android.widget.DatePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;
import java.text.CollationElementIterator;
import java.text.StringCharacterIterator;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private String mCurrentDateString;
    private String mCurrentDateFormatInput;
    private String TAG = "MainActivity";
    private View mTextViewChoosDate;
    private View mButtonDarkScreen;
    private String NIGHT_MODE;
    private boolean isNightModeOn;
    private View mTextViewDescription;
    private int TextViewChoosDate;
    private View mButtonviewButton;
    private View mImageView;
    private View mImageViewFavoritesList;
    private View mtextViewTitle;
    private View mtextViewImageDate;
     private View mButtonDateButton;
    private CollationElementIterator mTextViewChooseDate;
    private View iImageViewAddFavorites;
       private String SHARED_PREFS;
    private Object mUrlRequestforJsonFavorites;
    private Object mUrlRequestForJson;
    private MenuBuilder mFavoriteList;
    private String URL_JSON;

    private Object PreConfig;
    private String mUrlRequestForJsonLastActive;
    private Object mUrlRequestDefaultKey = "https://api.nasa.gov/planetary/apod?api_key=8bp9gx6OeCr43AH0MJcxEIoJ97p4fSzdEFqBalfL";
    private CollationElementIterator mTextViewDatePicker;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonDarkScreen = findViewById(R.id.darkScreen);
        SharedPreferences prefDarkScreen = getSharedPreferences(NIGHT_MODE, MODE_PRIVATE);
        SharedPreferences.Editor editorDarkScreen = prefDarkScreen.edit();
        isNightModeOn = prefDarkScreen.getBoolean(NIGHT_MODE, false);

        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode((AppCompatDelegate.MODE_NIGHT_NO));
        }
        mButtonDarkScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNightModeOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editorDarkScreen.putBoolean(NIGHT_MODE, false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editorDarkScreen.putBoolean(NIGHT_MODE, true);
                }
                editorDarkScreen.apply();
            }
        });

        mTextViewDescription = findViewById(R.id.TextViewDescription);
        mTextViewChoosDate = findViewById(TextViewChoosDate);
        mButtonviewButton = findViewById(R.id.viewButton);
        mImageView = findViewById(R.id.imageViewPicture);

        mImageViewFavoritesList = findViewById(R.id.imageViewFavoriteList);
        mImageViewFavoritesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mtextViewTitle = findViewById(R.id.textViewTitle);
        mtextViewImageDate = findViewById(R.id.textViewImageDate);

        mButtonviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });

        mButtonDateButton = findViewById(R.id.dateButton);
        mButtonDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        iImageViewAddFavorites = findViewById(R.id.addFavorites);
        iImageViewAddFavorites.setOnClickListener(new View.OnClickListener() {


            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) { saveDataIntoFavoriteList(); }

            });

        loadDataIntoFavoriteList();
        mTextViewDatePicker.setText(getString(R.string.select_date_to_get_the_picture_of_the_day));
        }

        @SuppressLint("RestrictedApi")
        private void saveDataIntoFavoritesList() {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            mUrlRequestforJsonFavorites = mUrlRequestForJson;
            mFavoriteList.add((CharSequence) mUrlRequestforJsonFavorites);

            setFavoriteList(mUrlRequestforJsonFavorites);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(URL_JSON, (String) mUrlRequestForJson);
            Log.d(TAG, "mUrlRequestForJsonFavorites" + mUrlRequestforJsonFavorites);
            editor.apply();
            Toast.makeText(MainActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();

        }

        private void loadDataIntoFavoritesList() {

            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            mUrlRequestforJsonFavorites = sharedPreferences.getString(URL_JSON, "true");
            Log.d(TAG, "mUrlRequestForJsonFavorites: " + mUrlRequestforJsonFavorites);

        }

    private void setFavoriteList(Object mUrlRequestforJsonFavorites) {
    }

    private void loadDataIntoFavoriteList() {
    }

    private void saveDataIntoFavoriteList() {
    }

    private void jsonParse() {
        Log.d(TAG, "mUrlRequestForJsonLastActive: " + mUrlRequestForJsonLastActive);

        if (mCurrentDateFormatInput != null) {

            mUrlRequestForJson = mUrlRequestDefaultKey + "&" + "date" + "=" + mCurrentDateFormatInput;
            mUrlRequestForJsonLastActive = mUrlRequestDefaultKey + "&" + "date" + "=" + mCurrentDateFormatInput;
        } else {
            if (mUrlRequestForJsonLastActive.equals("") || mUrlRequestForJsonLastActive == null) {
                mUrlRequestForJson = mUrlRequestDefaultKey;
            } else {
                mUrlRequestForJson = mUrlRequestForJsonLastActive;
            }
        }

    }

    /**
     * @param view       the picker associated with the dialog
     * @param year       the selected year
     * @param month      the selected month (0-11 for compatibility with
     *                   {@link Calendar#MONTH})
     * @param dayOfMonth the selected day of the month (1-31, depending on
     *                   month)
     */
    @SuppressLint("NewApi")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Log.d(TAG, "onDateSet: " + year + ":" + (month + 1) + ":" + dayOfMonth);
        mCurrentDateString = getDateInstance().format(c.getTime());
        Log.d(TAG, "currentDateString: " + mCurrentDateString);

        String monthString = ((month + 1) / 10 <= 1) ? Integer.toString( month + 1) : "0" + (month + 1);
        String dayOfMonthString = ((dayOfMonth / 10 >= 1) ? Integer.toString(dayOfMonth) : "0" + (dayOfMonth));
        mCurrentDateFormatInput = year + "-" + (monthString) + "-" + (dayOfMonthString);
        Log.d(TAG, "currentDateFormatInput: " + mCurrentDateFormatInput);

        mTextViewChooseDate.setText(mCurrentDateString);

    }

    private class TextViewChooseDate {
    }

}