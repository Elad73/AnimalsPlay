package com.playz.moondragon.animalplay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playz.moondragon.animalplay.Model.PreferenceCategory;
import com.playz.moondragon.animalplay.Model.QuizData;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {


    public static final String GUESSES = "NumberOfGuesses";
    public static final String ANIMALS_TYPES = "AnimalsType";
    public static final String PLAY_BACKGROUND_COLOR = "PlayBackgroundColor";
    public static final String PLAY_FONT = "PlayFont";
    public static final String IMAGES_TYPES = "ImagesType";
    public static final String RESET_ROUND = "ResetRound";

    private boolean isSettingsChanged = false;

    static Typeface boyzRGrossNF;
    static Typeface chubbyDotty;
    static Typeface emilysCandyRegular;
    static Typeface loveLetters;

    private MainActivityFragment myAnimalQuizFragment;
    private FirebaseDatabase fbDB;
    private DatabaseReference dbRef;

    public static PreferenceScreen settings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("AnimalPlay", "MainActivity/onCreate: entered onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fbDB = FirebaseDatabase.getInstance();
        dbRef = fbDB.getReferenceFromUrl("https://animalplay-8de10.firebaseio.com/");

        boyzRGrossNF = Typeface.createFromAsset(getAssets(), "fonts/BoyzRGrossNF.ttf");
        chubbyDotty = Typeface.createFromAsset(getAssets(), "fonts/Chubby Dotty.ttf");
        emilysCandyRegular = Typeface.createFromAsset(getAssets(), "fonts/EmilysCandy-Regular.ttf");
        loveLetters = Typeface.createFromAsset(getAssets(), "fonts/Love Letters.ttf");

        //This is an attempt to load and build settings screen from database
        //loadSettings();
        //setSharedPreferencesDefaultValues();

        PreferenceManager.setDefaultValues(MainActivity.this, R.xml.preferences, false);
        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).registerOnSharedPreferenceChangeListener(settingsChangeListener);

        QuizData.getInstance().initializePlay(getAssets());

        myAnimalQuizFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.animalQuizFragment);
        myAnimalQuizFragment.initializeFragment(PreferenceManager.getDefaultSharedPreferences(MainActivity.this));

        isSettingsChanged = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("AnimalPlay", "MainActivity/onCreateOptionsMenu: entered onCreateOptionsMenu");

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d("AnimalPlay", "MainActivity/onOptionsItemSelected: entered onOptionsItemSelected");
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
                    myAnimalQuizFragment.resetRound(sharedPreferences);
                    break;
                case ANIMALS_TYPES:
                    Set<String> animalTypes = sharedPreferences.getStringSet(ANIMALS_TYPES, null);

                    if (animalTypes != null && animalTypes.size() > 0) {
                        myAnimalQuizFragment.resetRound(sharedPreferences);
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        animalTypes.add(getString(R.string.default_animal_type));
                        editor.putStringSet(ANIMALS_TYPES, animalTypes);
                        editor.apply();

                        Toast.makeText(MainActivity.this, R.string.default_animalType_toast_message, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PLAY_FONT:
                    myAnimalQuizFragment.modifyQuizFont(sharedPreferences);
                    break;
                case PLAY_BACKGROUND_COLOR:
                    myAnimalQuizFragment.modifyBackgroundColor(sharedPreferences);
                    break;
                case IMAGES_TYPES:
                    myAnimalQuizFragment.modifyImagesTypeToDisplay(sharedPreferences);
                    break;
            }

            Toast.makeText(MainActivity.this, R.string.setting_Changed_toast_message, Toast.LENGTH_SHORT).show();
        }
    };



   /* private void setSharedPreferencesDefaultValues() {
        Log.d("AnimalPlay", "MainActivity/setSharedPreferencesDefaultValues: entered setSharedPreferencesDefaultValues");
        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        SharedPreferences.Editor editor = sharedPreference.edit();


        editor.putString(GUESSES, "2");
        Set<String> animalTypes = new HashSet<>();
        for(CharSequence cs :  getResources().getTextArray(R.array.type_of_animal_values)) {
            if (!animalTypes.add(cs.toString())) {
                Log.e("AnimalPlay", "MainActivity/setSharedPreferencesDefaultValues: on animalTypes default value, found duplication -  " + cs.toString());
            }
        }
        editor.putStringSet(ANIMALS_TYPES, animalTypes);

        editor.putString(PLAY_BACKGROUND_COLOR, getResources().getString(R.string.default_color_black));
        editor.putString(PLAY_FONT, getResources().getString(R.string.default_font));
        editor.putString(IMAGES_TYPES, getResources().getString(R.string.default_image_type));
        editor.apply();
    }
    private void loadSettings() {

        Log.d("AnimalPlay", "MainActivity/loadSettings: entered loadSettings");
        DatabaseReference settings = fbDB.getReference("Settings");
        settings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Get all of the child elements of "Settings"
                loadSettingsElements((Map<String, Object>) dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void loadSettingsElements(Map<String, Object> settings) {
        Log.d("AnimalPlay", "MainActivity/loadSettingsElements(Map<String, Object> settingsElements: entered loadSettingsElements value-" + settings.toString());

        for(Map.Entry<String,Object> childEntry : settings.entrySet()) {
            Map child = (Map)childEntry.getValue();
            Preference settingsElement = loadSettingsElement(child, MainActivity.this);
            addElementToSettings(settingsElement);
        }
    }
    private void firstAttempt() {
        SharedPreferences sPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();

        Log.d("AnimalPlay", "MainActivity/loadSettings: entered onDataChange");
        //String defValue, entries, entryValues, Persistant, summary, title;
        //Preference pref = new Preference(MainActivity.this);
        PreferenceCategory prefCategory = new PreferenceCategory();
        Log.d("AnimalPlay", "MainActivity/loadSettings: dataSnapshot-" + dataSnapshot.getValue() );
        prefCategory = dataSnapshot.getValue(PreferenceCategory.class);
        //pref.setDefaultValue(dataSnapshot.getValue(Preference.class));
        Log.d("AnimalPlay", "MainActivity/loadSettings: pref-" + prefCategory);
        editor.putString(GUESSES, prefCategory.getTitle());
        editor.putString(GUESSES, prefCategory.getSummary());
        editor.putLong(GUESSES, prefCategory.getDefaultValue());
        editor.putString(GUESSES, prefCategory.getEntries());
        editor.putString(GUESSES, prefCategory.getEntryValues());
        editor.putBoolean(GUESSES, prefCategory.getPersistent());
        editor.apply();
        //editor.commit();

        Log.d("AnimalPlay", "MainActivity/loadSettings: editor committed");


        ListPreference lsPref = new ListPreference(MainActivity.this);
        lsPref.setKey(GUESSES);
        String[] entries = prefCategory.getEntries().split(",");
        lsPref.setEntries( entries);
        String[] entryValues = prefCategory.getEntryValues().split(",");
        lsPref.setEntryValues(entryValues);
        lsPref.setDefaultValue(prefCategory.getDefaultValue());
        lsPref.setPersistent(prefCategory.getPersistent());
        lsPref.setTitle(prefCategory.getTitle());
        lsPref.setSummary(prefCategory.getSummary());

        Log.d("AnimalPlay", "MainActivity/loadSettings:  lsPref.getValue()-" +  lsPref.getValue());
        //lsPref.getValue()

    }
    private void addElementToSettings(Preference settingsElement) {
        if (settings != null) {
            SharedPreferences sPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putLong(settingsElement.getKey(), settingsElement.getDefaultValue());


        }
    }
    private Preference loadSettingsElement(Map child, Context context) {
        Preference element = null;
        String[] entries, entryValues;

        switch( (String)child.get("elementType")) {
            case "MultiSelectListPreference":
                element = new MultiSelectListPreference(context);
                entries = ((String)child.get("entries")).split(",");
                ((MultiSelectListPreference)element).setEntries( entries);
                entryValues = ((String)child.get("entryValues")).split(",");
                ((MultiSelectListPreference)element).setEntryValues(entryValues);
                break;
            case "ListPreference":
                element = new ListPreference(context);
                entries = ((String)child.get("entries")).split(",");
                ((ListPreference)element).setEntries( entries);
                entryValues = ((String)child.get("entryValues")).split(",");
                ((ListPreference)element).setEntryValues(entryValues);
                break;
        }

        element.setPersistent(Boolean.parseBoolean((String)child.get("persistent")));
        element.setTitle((String)child.get("title"));
        element.setSummary((String)child.get("summary"));
        element.setDefaultValue(child.get("defaultValue"));
        return element;
    }
   */
}
