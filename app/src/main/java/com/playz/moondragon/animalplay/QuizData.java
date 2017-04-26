package com.playz.moondragon.animalplay;

import android.content.res.AssetManager;
import android.util.Log;

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

    private final short NUMBER_OF_ANIMALS_INCLUDED_IN_QUIZ = 10;

    HashMap<String, List<Animal>> _hmAllAnimals = null;
    List<Animal> _lsAllAnimalsInRound;
    List<Animal> _lsAnimalsToGuessInRound;
    List<String>  _lsCurrentWrongGuesses;
    Animal        _currentRightAnswer;
    short         _numberOfGuessesInRound;
    int           _numberOfGuessesRowsInRound;
    //List<AnimalType> _lsAnimalTypesInRound;
    short            _numberOfAttemptsInRound;
    short            _defaultTextSize;
    short _numberOfSuccessfulAnswers;
    Set<String> _setAnimalTypesInRound = null;

    public  QuizData() {
        _numberOfAttemptsInRound = 0;
        _defaultTextSize = 24;

        _hmAllAnimals = new HashMap<>();
        _lsAllAnimalsInRound = new ArrayList<>();
        _lsAnimalsToGuessInRound = new ArrayList<>();
        _lsCurrentWrongGuesses = new ArrayList<>();
    }

    public List<String> getCurrentWrongGuesses() {
        return _lsCurrentWrongGuesses;
    }

    public List<Animal> getAnimalsInRound() {
        return _lsAnimalsToGuessInRound;
    }

    public short getDefaultTextSize() {
        return _defaultTextSize;
    }

    public short getNumberOfSuccessfulAnswers() {
        return _numberOfSuccessfulAnswers;
    }

    public short getNumberOfAnimalsIncludedInRound() {
        return NUMBER_OF_ANIMALS_INCLUDED_IN_QUIZ;
    }

    public Animal getCurrentRightAnswer() {
        return _currentRightAnswer;
    }

    public short getNumberOfGuessesInRound() {
        return _numberOfGuessesInRound;
    }

    public int getNumberOfGuessesRowsInRound() {
        return _numberOfGuessesRowsInRound;
    }

    /**
     * Initializing the Hashmap of all Animals in the project.
     * Passing through the assets root, getting the animal assets folders and extracting the image files and mp3 files.
     * Instantiating the Animal object.
     * Creating the "Types" Lists and populating them with Animals.
     * The result is _hmAllAnimals populated with "types" lists and "Animal" objects inside.
     * After populating once, there is no need to do it again.
     * @param assets
     */
    public void initializeData(AssetManager assets, Set<String> animalTypes ) {

        loadAnimalTypesInRound(animalTypes);

        try {
            if (assets != null && _hmAllAnimals.size() == 0) {

                String[] allAssets = assets.list(""); //getting the root folder (assets)
                for (String folder : allAssets) {
                    if (folder.indexOf("_Animals") > 0) {
                        String type = folder.substring(0, folder.indexOf('_'));//getting the "Tame" from "Tame_Animals-Sheep"
                       //                          _setAnimalTypesInRound.add(type);
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
                                String assetName = assetInType.substring(assetInType.indexOf('-') + 1, assetInType.indexOf('.')).replace('_', ' ');
                                String assetsPath = folder + '/' + assetInType;
                                Animal animal = isContainAnimalInAllAnimalsRound(lsAllAnimalsType, assetName);
                                if (animal == null) {
                                    animal = new Animal(assetName, type);
                                    lsAllAnimalsType.add(animal);
                                }

                                switch (assetType) {
                                    case "png":
                                        animal.setImagePath(assetsPath);
                                        break;
                                    case "mp3":
                                        animal.setSoundPath(assetsPath);
                                        break;
                                }

                            }
                        }
                    }
                }

        }
        catch (IOException ioEx) {
            Log.e("QuizLog", "Inside QuizData:initializeData", ioEx);
        }
    }

    private Animal isContainAnimalInAllAnimalsRound(List<Animal> lsAnimals, String animalName) {

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

    public void loadNumberOfGuesses(short numberOfGuesses) {
        _numberOfGuessesInRound = numberOfGuesses;
        _numberOfGuessesRowsInRound = _numberOfGuessesInRound/2;
    }

    public void loadAnimalTypesInRound(Set<String> setAnimalTypes) {
        if (_setAnimalTypesInRound != null) {
            _setAnimalTypesInRound.clear();
        }
        _setAnimalTypesInRound = setAnimalTypes;
    }

    public Set<String> getAnimalTypesInRound() {
        return _setAnimalTypesInRound;
    }

    public void incrementRoundAttempts() {
        _numberOfAttemptsInRound++;
    }

    public void incrementSuccessfullAttempts() {
        _numberOfSuccessfulAnswers++;
    }

    public Animal getNextAnimalInRound() {
        _lsCurrentWrongGuesses.clear();
        _currentRightAnswer = _lsAllAnimalsInRound.remove(0);

        for(short i=0; i<getNumberOfGuessesInRound(); i++) {
            _lsCurrentWrongGuesses.add(_lsAllAnimalsInRound.get(i).getName());
        }

        _lsAllAnimalsInRound.add(_currentRightAnswer); //adding back the removed animal to the end of the list

        return getCurrentRightAnswer();
    }

    public void prepareNewRound() {

        //populating _lsAllAnimalsInRound List
        _lsAllAnimalsInRound.clear();

        for (String type : _setAnimalTypesInRound) {
            List<Animal> lsAnimalsType = _hmAllAnimals.get(type);
            for (Animal animal : lsAnimalsType) {
                _lsAllAnimalsInRound.add(animal);
            }
        }
        Collections.shuffle(_lsAllAnimalsInRound);

        //populating _lsAnimalsToGuessInRound List
        _lsAnimalsToGuessInRound.clear();
        SecureRandom secureRandomNumber = new SecureRandom();

        for (short i=0; i < NUMBER_OF_ANIMALS_INCLUDED_IN_QUIZ; i++) {
            int randomIndex = secureRandomNumber.nextInt(_lsAllAnimalsInRound.size());
            Animal animal = _lsAllAnimalsInRound.get(randomIndex);
            _lsAnimalsToGuessInRound.add(animal);
        }
        Collections.shuffle(_lsAnimalsToGuessInRound);
    }
}



