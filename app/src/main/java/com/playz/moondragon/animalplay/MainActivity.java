package com.playz.moondragon.animalplay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {


    public static final String GUESSES = "settings_numberOfGuesses";
    public static final String ANIMALS_TYPES = "settings_animalsType";
    public static final String QUIZ_BACKGROUND_COLOR = "settings_quizBackgroundColor";
    public static final String QUIZ_FONT = "settings_quizFont";
    public static final String IMAGES_TYPES = "settings_imagesType";
    public static final String RESET_ROUND = "settings_reset_round";


    private boolean isSettingsChanged = false;

    static Typeface boyzRGrossNF;
    static Typeface chubbyDotty;
    static Typeface emilysCandyRegular;
    static Typeface loveLetters;

    MainActivityFragment myAnimalQuizFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boyzRGrossNF = Typeface.createFromAsset(getAssets(), "fonts/BoyzRGrossNF.ttf");
        chubbyDotty = Typeface.createFromAsset(getAssets(), "fonts/Chubby Dotty.ttf");
        emilysCandyRegular = Typeface.createFromAsset(getAssets(), "fonts/EmilysCandy-Regular.ttf");
        loveLetters = Typeface.createFromAsset(getAssets(), "fonts/Love Letters.ttf");

        PreferenceManager.setDefaultValues(MainActivity.this, R.xml.quiz_prefereces, false);

        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).registerOnSharedPreferenceChangeListener(settingsChangeListener);

        QuizData.getInstance().initializePlay(getAssets());

        myAnimalQuizFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.animalQuizFragment);
        myAnimalQuizFragment.initializeFragment(PreferenceManager.getDefaultSharedPreferences(MainActivity.this));

        isSettingsChanged = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Intent preferenceIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(preferenceIntent);
        return super.onOptionsItemSelected(item);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener settingsChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            isSettingsChanged = true;

            switch (key) {
                case GUESSES:
                    myAnimalQuizFragment.resetRound();
                    break;
                case ANIMALS_TYPES:
                    Set<String> animalTypes = sharedPreferences.getStringSet(ANIMALS_TYPES, null);

                    if (animalTypes != null && animalTypes.size() > 0) {
                        myAnimalQuizFragment.resetRound();
                    }else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        animalTypes.add(getString(R.string.default_animal_type));
                        editor.putStringSet(ANIMALS_TYPES, animalTypes);
                        editor.apply();

                        Toast.makeText(MainActivity.this, R.string.default_animalType_toast_message, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case QUIZ_FONT:
                    myAnimalQuizFragment.modifyQuizFont(sharedPreferences);
                    break;
                case QUIZ_BACKGROUND_COLOR:
                    myAnimalQuizFragment.modifyBackgroundColor(sharedPreferences);
                    break;
                case IMAGES_TYPES:
                    myAnimalQuizFragment.modifyImagesTypeToDisplay(sharedPreferences);
                    break;
                case RESET_ROUND:
                    myAnimalQuizFragment.resetRound();
                    break;
            }

            Toast.makeText(MainActivity.this, R.string.setting_Changed_toast_message, Toast.LENGTH_SHORT).show();
        }
    };
}
