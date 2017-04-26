package com.playz.moondragon.animalplay;

/**
 * Created by Elad on 24/04/2017.
 */

public class TameAnimal extends Animal {

    public TameAnimal (String imagePath, String soundPath) {
        super(imagePath, soundPath);
        _type = "Tame_Animal";
    }
}
