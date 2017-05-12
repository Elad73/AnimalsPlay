package com.playz.moondragon.animalplay.Model;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import com.playz.moondragon.animalplay.MainActivity;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Elad on 24/04/2017.
 */

public class QuizData {

    private final short NUMBER_OF_ANIMALS_INCLUDED_IN_ROUND = 10;

    private static QuizData _instance = null;

    private HashMap<String, List<Animal>> _hmAllAnimals = null;
    private List<Animal> _lsAllAnimalsInRound = null;
    private List<Animal> _lsAnimalsToGuessInRound = null;
    private List<String> _lsCurrentButtonsToGuess = null;
    private Animal _animalToGuessInCurrentTurn = null;
    private Set<String> _setAnimalTypesInRound = null;
    private short _numberOfButtonsInRound = 0;
    private int _numberOfButtonRowsInRound = 0;
    private short _numberOfAttemptsInRound = 0;
    private short _numberOfSuccessfulAnswers = 0;
    private short _imagesTypeToDisplayInTurn = 0;
    private short _defaultTextSize;

    private  QuizData() {

        _hmAllAnimals = new HashMap<>();
        _lsAllAnimalsInRound = new ArrayList<>();
        _lsAnimalsToGuessInRound = new ArrayList<>();
        _lsCurrentButtonsToGuess = new ArrayList<>();

         _numberOfButtonsInRound = 2;
         _numberOfButtonRowsInRound = _numberOfButtonsInRound/2;
        _numberOfAttemptsInRound = 0;
        _numberOfSuccessfulAnswers = 0;
        _defaultTextSize = 24;
    }

    //region Getters
    public static QuizData getInstance() {
        if (_instance == null) {
            _instance = new QuizData();
        }
        return _instance;
    }

    public short getNumberOfAnimalsIncludedInRound() {
        return NUMBER_OF_ANIMALS_INCLUDED_IN_ROUND;
    }

    public List<String> getButtonsToGuessInTurn() {
        return _lsCurrentButtonsToGuess;
    }

    public List<Animal> getAnimalsToGuessInRound() { return _lsAnimalsToGuessInRound; }

    public Animal getAnimalToGuessInCurrentTurn() { return _animalToGuessInCurrentTurn; }

    public Set<String> getAnimalTypesInRound() {
        return _setAnimalTypesInRound;
    }

    public short getNumberOfButtonsInRound() {
        return _numberOfButtonsInRound;
    }

    public int getNumberOfButtonRowsInRound() {
        return _numberOfButtonRowsInRound;
    }

    public short getNumberOfAttemptsInRound() { return _numberOfAttemptsInRound; }

    public short getNumberOfSuccessfulAnswers() {
        return _numberOfSuccessfulAnswers;
    }

    public short getDefaultTextSize() {
        return _defaultTextSize;
    }

    public short getTypeOfImageToDisplayInTurn() {
        return _imagesTypeToDisplayInTurn;
    }
    //endregion


    /**
     * Initializing the Hashmap of all Animals in the play.
     * Initializing the default animalstypes in the round.
     * @param assets
     */
    public void initializePlay(AssetManager assets) {

        Log.d("AnimalPlay", "QuizData/initializePlay: Entering initializePlay");
        loadAllAnimalsInPlay(assets);
    }

    /**
     * Initializing the Hashmap of all Animals in the play.
     * Passing through the assets root, getting the animal assets folders and extracting the image files and mp3 files.
     * Instantiating the Animal object.
     * Creating the "Types" Lists and populating them with Animals.
     * The result is _hmAllAnimals populated with "types" lists and "Animal" objects inside.
     * After populating once, there is no need to do it again.
     * @param assets
     */
    private void loadAllAnimalsInPlay(AssetManager assets) {

        Log.d("AnimalPlay", "QuizData/loadAllAnimalsInPlay: Entering loadAllAnimalsInPlay");
        try {
            if (assets != null && _hmAllAnimals.size() == 0) {

                String[] allAssets = assets.list(""); //getting the root folder (assets)
                for (String folder : allAssets) {
                    if (folder.indexOf("_Animals") > 0) {
                        String type = folder.substring(0, folder.indexOf('_'));//getting the "Tame" from "Tame_Animals-Sheep"
                        List<Animal> lsAllAnimalsType;
                        if (_hmAllAnimals.containsKey(type)) {
                            lsAllAnimalsType = _hmAllAnimals.get(type);
                        }
                        else {
                            lsAllAnimalsType = new ArrayList<>();
                            _hmAllAnimals.put(type, lsAllAnimalsType);
                        }

                        String[] allAssetsInType = assets.list(folder); //getting all of the assets in the folder
                        for (String assetInType : allAssetsInType) {
                            String assetType = assetInType.substring(assetInType.indexOf('.') + 1);
                            String assetName = assetInType.substring(assetInType.indexOf('-') + 1, assetInType.indexOf('_'));
                            assetName =  assetName.replace('@', '\n');
                            String assetsPath = folder + '/' + assetInType;
                            Animal animal = isContainAnimalInList(lsAllAnimalsType, assetName);
                            if (animal == null) {
                                animal = new Animal(assetName, type);
                                lsAllAnimalsType.add(animal);
                                //Log.i("AnimalPlay", "QuizData/loadAllAnimalsInPlay: new animal added " + animal.getName());
                            }

                            switch (assetType) {
                                case "png":
                                case "jpg":
                                    String index = assetInType.substring(assetInType.indexOf('_') + 1, assetInType.indexOf('.'));
                                    if (index != "") {
                                        Short imageIndex = Short.parseShort(index);
                                        animal.setImagePath(assetsPath, imageIndex);
                                    }
                                    break;
                                case "mp3":
                                case "wav":
                                    animal.setSoundPath(assetsPath);
                                    break;
                            }
                        }
                    }
                }
            }
            Log.d("AnimalPlay", "QuizData/loadAllAnimalsInPlay: _hmAllAnimals initialized with size " + _hmAllAnimals.size());
        } catch (IOException ioEx) {
            Log.e("AnimalPlay", "Inside QuizData:initializePlay", ioEx);
        }
    }

    /**
     * Do it once in a turn
     * @param sharedPreferences
     */
    public void initializeRound(SharedPreferences sharedPreferences) {

        Log.d("AnimalPlay", "QuizData/initializeRound: Entering initializeRound");
        loadAnimalTypesInRound(sharedPreferences.getStringSet(MainActivity.ANIMALS_TYPES, null));
        loadNumberOfGuesses(Short.parseShort(sharedPreferences.getString(MainActivity.GUESSES, null)));
        setTypeOfImagesToDisplayInTurn(Short.parseShort(sharedPreferences.getString(MainActivity.IMAGES_TYPES, null)));


        loadAllAnimalsInRound();
        loadAnimalsToGuessInRound();
    }

    private void  loadAllAnimalsInRound() {

        Log.d("AnimalPlay", "QuizData/loadAllAnimalsInRound: Entering loadAllAnimalsInRound");
        _lsAllAnimalsInRound.clear();

        for (String type : _setAnimalTypesInRound) {
            List<Animal> lsAnimalsType = _hmAllAnimals.get(type);
            for (Animal animal : lsAnimalsType) {
                _lsAllAnimalsInRound.add(animal);
                Log.d("AnimalPlay", "QuizData/loadAllAnimalsInRound: new animal path " + animal.getImagePath(_imagesTypeToDisplayInTurn));
            }
        }
        Collections.shuffle(_lsAllAnimalsInRound);
        Log.d("AnimalPlay", "QuizData/loadAllAnimalsInRound: _lsAllAnimalsInRound initialized with size " + _lsAllAnimalsInRound.size());
    }

    private void loadAnimalTypesInRound(Set<String> setAnimalTypes) {

        Log.d("AnimalPlay", "QuizData/loadAnimalTypesInRound: Entering loadAnimalTypesInRound");
        _setAnimalTypesInRound = setAnimalTypes;
    }

    private void loadNumberOfGuesses(short numberOfGuesses) {
        Log.d("AnimalPlay", "QuizData/loadNumberOfGuesses: Entering loadNumberOfGuesses");
        _numberOfButtonsInRound = numberOfGuesses;
        _numberOfButtonRowsInRound = _numberOfButtonsInRound /2;
    }

    public void setTypeOfImagesToDisplayInTurn(short imagesType) { _imagesTypeToDisplayInTurn = imagesType; }

    private void loadAnimalsToGuessInRound() {

        Log.d("AnimalPlay", "QuizData/loadAnimalsToGuessInRound: Entering loadAnimalsToGuessInRound");
        _lsAnimalsToGuessInRound.clear();
        SecureRandom secureRandomNumber = new SecureRandom();

        for (short i = 0; i < NUMBER_OF_ANIMALS_INCLUDED_IN_ROUND; ) {
            int randomIndex = secureRandomNumber.nextInt(_lsAllAnimalsInRound.size());
            Animal animal = _lsAllAnimalsInRound.get(randomIndex);
            if(isContainAnimalInList(_lsAnimalsToGuessInRound, animal.getName()) == null) {
                _lsAnimalsToGuessInRound.add(animal);
                Log.d("AnimalPlay", "QuizData/loadAnimalsToGuessInRound: new animal path " + animal.getImagePath(_imagesTypeToDisplayInTurn));
                i++;
            }
        }
        Collections.shuffle(_lsAnimalsToGuessInRound);
        _numberOfAttemptsInRound = 0;
        _numberOfSuccessfulAnswers = 0;
        Log.d("AnimalPlay", "QuizData/loadAnimalsToGuessInRound: _lsAnimalsToGuessInRound initialized with size " + _lsAnimalsToGuessInRound.size());
    }

    public Animal getNextAnimalInTurn() {

        _lsCurrentButtonsToGuess.clear();
        _animalToGuessInCurrentTurn = _lsAllAnimalsInRound.remove(0);

        for(short i = 0; i< getNumberOfButtonsInRound(); i++) {
            _lsCurrentButtonsToGuess.add(_lsAllAnimalsInRound.get(i).getName());
        }

        _lsAllAnimalsInRound.add(_animalToGuessInCurrentTurn); //adding back the removed animal to the end of the list

        return getAnimalToGuessInCurrentTurn();
    }

    private Animal isContainAnimalInList(List<Animal> lsAnimals, String animalName) {

        Animal animal = null;
        if (lsAnimals.size() > 0) {
            for (Animal animalInRound : lsAnimals) {
                if (animalInRound.getName().equals(animalName)) {
                    animal = animalInRound;
                    break;
                }
            }
        }
        return animal;
    }



    public void incrementRoundAttempts() {
        _numberOfAttemptsInRound++;
    }

    public void incrementSuccessfulAnswers() {
        _numberOfSuccessfulAnswers++;
    }
}



