package com.playz.moondragon.animalplay;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;

import java.util.Collections;
import java.util.Set;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsActivityFragment extends PreferenceFragment {

    private MainActivityFragment myAnimalQuizFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("AnimalPlay", "SettingsActivityFragment/onCreate: entered onCreate");
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        //this.setPreferenceScreen(createPreferenceHierarchy());

    }

    /*
    public PreferenceScreen createPreferenceHierarchy() {

        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(getActivity());

        // "Reset Round" category created programmatically
        PreferenceCategory catResetRound = new PreferenceCategory(getActivity());
        catResetRound.setTitle("Reset Round Preferences");
        root.addPreference(catResetRound);

        ListPreference lsNumOfGuesses = new ListPreference(getActivity());
        lsNumOfGuesses.setKey(GUESSES);
        lsNumOfGuesses.setTitle( getResources().getString(R.string.number_of_guesses_title));
        lsNumOfGuesses.setSummary(getResources().getString(R.string.number_of_guesses_description));
        lsNumOfGuesses.setDialogTitle(getResources().getString(R.string.number_of_guesses_dialog_title));
        lsNumOfGuesses.setEntries(getResources().getTextArray(R.array.number_of_guesses_list));
        lsNumOfGuesses.setEntryValues(getResources().getTextArray(R.array.number_of_guesses_list));
        lsNumOfGuesses.setPersistent(Boolean.TRUE);
        lsNumOfGuesses.setDefaultValue("2");

        catResetRound.addPreference(lsNumOfGuesses);

        MultiSelectListPreference msLsAnimalTypes = new MultiSelectListPreference(getActivity());
        msLsAnimalTypes.setKey(ANIMALS_TYPES);
        msLsAnimalTypes.setTitle(getResources().getString(R.string.animal_types_title));
        msLsAnimalTypes.setSummary(getResources().getString(R.string.animal_types_description));
        msLsAnimalTypes.setEntries(getResources().getTextArray(R.array.type_of_animals));
        msLsAnimalTypes.setEntryValues(getResources().getTextArray(R.array.type_of_animal_values));
        msLsAnimalTypes.setPersistent(Boolean.TRUE);
        msLsAnimalTypes.setDefaultValue(getResources().getTextArray(R.array.type_of_animal_values));

        catResetRound.addPreference(msLsAnimalTypes);

        Preference prefReset = new Preference(getActivity());
        prefReset.setKey(RESET_ROUND);
        prefReset.setTitle(getResources().getString(R.string.reset_round_title));
        prefReset.setSummary(getResources().getString(R.string.reset_round_description));
        prefReset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //myAnimalQuizFragment = (MainActivityFragment) getFragmentManager().getFragment().findFragmentById(R.id.animalQuizFragment);
                MainActivityFragment maFrag = (MainActivityFragment) ((MainActivity) getActivity()).getSupportFragmentManager().findFragmentById(R.id.animalQuizFragment);
                maFrag.resetRound();
                return true;
            }
        });

        catResetRound.addPreference(prefReset);


        // "None Reset Round" category created programmatically
        PreferenceCategory catNonResetRound = new PreferenceCategory(getActivity());
        catNonResetRound.setTitle("None Reset Round Preferences");
        root.addPreference(catNonResetRound);

        ListPreference lsColors = new ListPreference(getActivity());
        lsColors.setKey(PLAY_BACKGROUND_COLOR);
        lsColors.setTitle(getResources().getString(R.string.choose_background_color));
        lsColors.setSummary(getResources().getString(R.string.choose_color_description));
        lsColors.setEntries(getResources().getTextArray(R.array.colors));
        lsColors.setEntryValues(getResources().getTextArray(R.array.colors));
        lsColors.setPersistent(Boolean.TRUE);
        lsColors.setDefaultValue(getResources().getString(R.string.default_color_black));

        catNonResetRound.addPreference(lsColors);


        ListPreference lsFonts = new ListPreference(getActivity());
        lsFonts.setKey(PLAY_FONT);
        lsFonts.setTitle(getResources().getString(R.string.choose_font));
        lsFonts.setSummary(getResources().getString(R.string.choose_font_description));
        lsFonts.setEntries(getResources().getTextArray(R.array.fonts));
        lsFonts.setEntryValues(getResources().getTextArray(R.array.fonts_values));
        lsFonts.setPersistent(Boolean.TRUE);
        lsFonts.setDefaultValue(getResources().getString(R.string.default_font));

        catNonResetRound.addPreference(lsFonts);


        ListPreference lsImageType = new ListPreference(getActivity());
        lsImageType.setKey(IMAGES_TYPES);
        lsImageType.setTitle(getResources().getString(R.string.choose_image_type));
        lsImageType.setSummary(getResources().getString(R.string.choose_image_type_description));
        lsImageType.setEntries(getResources().getTextArray(R.array.type_of_images));
        lsImageType.setEntryValues(getResources().getTextArray(R.array.type_of_images_values));
        lsImageType.setPersistent(Boolean.TRUE);
        lsImageType.setDefaultValue(getResources().getString(R.string.default_image_type));

        catNonResetRound.addPreference(lsImageType);

        return root;
    }
    */

}

