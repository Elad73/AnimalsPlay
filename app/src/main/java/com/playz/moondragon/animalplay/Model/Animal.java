package com.playz.moondragon.animalplay.Model;

/**
 * Created by Elad on 24/04/2017.
 */

public class Animal {

    private String _name;
    private String[] _imagesPath = null;
    short _numberOfImagesToHold = 2;
    private String _soundPath;
    protected String _type;
    private  String _objectKey;


    public Animal(String animalName, String animalType) {
        _name = animalName;
        _type = animalType;
    }


    public String getImagePath(short imageIndex) { return _imagesPath[imageIndex]; }

    public String getType() { return _type; }

    public String getName() { return _name; }

    public String getSoundPath() {
        return _soundPath;
    }

    public void setImagePath(String imagePath, short imageIndex) {
        if (_imagesPath != null) {
            if (imageIndex < _numberOfImagesToHold) {
                _imagesPath[imageIndex] = imagePath;
            }
        }
        else {
            _imagesPath = new String[_numberOfImagesToHold];
            _imagesPath[imageIndex] = imagePath;
        }
    }

    public void setSoundPath(String _soundPath) {
        this._soundPath = _soundPath;
    }

    public void setName(String _name) {
        this._name = _name;
    }
}

